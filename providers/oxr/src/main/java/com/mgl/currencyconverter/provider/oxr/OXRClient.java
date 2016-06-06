package com.mgl.currencyconverter.provider.oxr;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE;
import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;
import static java.util.concurrent.TimeUnit.SECONDS;

import java.net.URI;

import com.mgl.currencyconverter.provider.api.XChangeClient;
import com.mgl.currencyconverter.provider.api.model.XChangeCurrency;
import com.mgl.currencyconverter.provider.api.model.XChangeRate;

import java.util.Set;
import java.util.stream.Collectors;

import javax.json.JsonObject;
import javax.json.JsonString;
import javax.json.JsonValue;
import javax.json.JsonValue.ValueType;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import com.mgl.currencyconverter.provider.oxr.model.OXRCurrency;
import lombok.extern.slf4j.Slf4j;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.time.Instant;
import java.util.Map;
import java.util.function.Function;

import javax.json.JsonNumber;

import com.mgl.currencyconverter.provider.api.model.XChangeDetailedCurrency;
import com.mgl.currencyconverter.provider.oxr.model.OXRRate;

@Slf4j
public class OXRClient implements XChangeClient {

    private static final int JUST_ONE_POOLED_CONN_PER_ROUTE = 1;

    static final OXRCurrency US_DOLLAR = new OXRCurrency("USD", "United States Dollar");

    private final URI baseUri;
    private final String appId;

    private final ResteasyClient client;
    
    private Set<XChangeDetailedCurrency> detailedCurrencies;
    private Map<String, XChangeDetailedCurrency> detailedCurrenciesMap;

    public OXRClient(OXRClientConfiguration configuration) {
        checkNotNull(configuration, "configuration");
        baseUri = configuration.getBaseUri();
        appId = configuration.getAppId();
        client = new ResteasyClientBuilder()
                    .connectionCheckoutTimeout(configuration.getTimeout(), MILLISECONDS)
                    .socketTimeout(configuration.getTimeout(), MILLISECONDS)
                    .establishConnectionTimeout(configuration.getTimeout(), MILLISECONDS)
                    .connectionPoolSize(configuration.getConnectionPoolSize())
                    .connectionTTL(configuration.getConnectionTtl(), MILLISECONDS)
                    .maxPooledPerRoute(JUST_ONE_POOLED_CONN_PER_ROUTE)
                    .asyncExecutor(configuration.getAsyncExecutor(), false)
                .build();
    }

    private UriBuilder baseUriBuilder() {
        return UriBuilder.fromUri(baseUri).queryParam("app_id", appId);
    }

    private OXRCurrency currencyFromJson(String key, JsonValue jsonValue) {
        checkArgument(
            jsonValue.getValueType() == ValueType.STRING,
            "Unexpected return value type: %s", jsonValue.getValueType());
        JsonString jsonString = (JsonString) jsonValue;
        return new OXRCurrency(key, jsonString.getString());
    }

    private OXRRate rateFromJson(
            Instant ts, OXRCurrency baseCurrency,
            String key, JsonValue jsonValue) {
        checkArgument(
            jsonValue.getValueType() == ValueType.NUMBER,
            "Unexpected return value type: %s", jsonValue.getValueType());
        XChangeDetailedCurrency ratedCurrency = detailedCurrenciesMap.get(key);
        checkNotNull(ratedCurrency, "Unexpected currency rate for %s", key);
        JsonNumber jsonNumber = (JsonNumber) jsonValue;
        return new OXRRate(baseCurrency, (OXRCurrency) ratedCurrency, ts, jsonNumber.doubleValue());
    }

    private Set<XChangeDetailedCurrency> loadDetailedCurrencies(boolean ensureInitialised)
    throws IOException {
        if (ensureInitialised) {
            ensureInitialised();
        }
        UriBuilder uriBuilder = baseUriBuilder().path("currencies.json");
        WebTarget target = client.target(uriBuilder);
        Response response = target.request(APPLICATION_JSON_TYPE).get();
        try {
            if (response.getStatus() != HttpURLConnection.HTTP_OK) {
                throw new IOException("Unexpected status code: " + response.getStatus());
            }
            JsonObject jsonCurrencies = response.readEntity(JsonObject.class);
            return jsonCurrencies.entrySet().stream()
                    .map(entry -> currencyFromJson(entry.getKey(), entry.getValue()))
                    .collect(Collectors.toSet());
        } finally {
            response.close();
        }
    }

    @Override
    // TODO: Improve error checking and diagnostics for status and when parsing JSON
    public Set<XChangeDetailedCurrency> loadDetailedCurrencies() throws IOException {
        return loadDetailedCurrencies(true);
    }

    @Override
    // TODO: Improve error checking and diagnostics for status and when parsing JSON
    public Set<XChangeRate> loadRates(/* Set<XChangeCurrency> currencies */) throws IOException {
        ensureInitialised();
        // API subscriptors feature only
        // String symbols = currencies.stream()
        //         .map(XChangeCurrency::getCode)
        //         .collect(Collectors.joining(","));
        UriBuilder uriBuilder = baseUriBuilder().path("latest.json")/* .queryParam("symbols", symbols)*/;
        WebTarget target = client.target(uriBuilder);
        Response response = target.request(APPLICATION_JSON_TYPE).get();
        try {
            if (response.getStatus() != HttpURLConnection.HTTP_OK) {
                throw new IOException("Unexpected status code: " + response.getStatus());
            }
            JsonObject jsonRatesResult = response.readEntity(JsonObject.class);
            Instant ts = Instant.ofEpochMilli(SECONDS.toMillis(jsonRatesResult.getInt("timestamp")));
            String base = jsonRatesResult.getString("base");
            checkState(US_DOLLAR.getCode().equals(base), "Unsupported base: %s", base);
            JsonObject jsonRates = jsonRatesResult.getJsonObject("rates");
            return jsonRates.entrySet().stream()
                    .map(entry -> rateFromJson(ts, US_DOLLAR, entry.getKey(), entry.getValue()))
                    .collect(Collectors.toSet());
        } finally {
            response.close();
        }
    }

    private void ensureInitialised() {
        checkState(detailedCurrencies != null, "Detailed currencies not initialised");
        checkState(detailedCurrenciesMap != null, "Detailed currencies map not initialised");
    }

    @Override
    public void init() throws IOException {
        detailedCurrencies = loadDetailedCurrencies(false);
        detailedCurrenciesMap = detailedCurrencies.stream()
                .collect(Collectors.toMap(XChangeCurrency::getCode, Function.identity()));
    }

    @Override
    public void dispose() {
        log.debug("Disposing client");
        client.close();
        detailedCurrenciesMap = null;
        detailedCurrencies = null;
    }

}
