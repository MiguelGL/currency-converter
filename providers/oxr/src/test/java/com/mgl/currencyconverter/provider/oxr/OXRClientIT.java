package com.mgl.currencyconverter.provider.oxr;

import static com.mgl.currencyconverter.provider.oxr.OXRClient.US_DOLLAR;
import static org.hamcrest.core.IsCollectionContaining.hasItem;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.Executors;

import com.mgl.currencyconverter.provider.api.model.XChangeDetailedCurrency;
import com.mgl.currencyconverter.provider.api.model.XChangeRate;
import com.mgl.currencyconverter.provider.oxr.OXRClientConfiguration.OXRClientConfigurationBuilder;
import com.mgl.currencyconverter.provider.oxr.model.OXRRate;
import java.time.Instant;
import lombok.extern.slf4j.Slf4j;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

@Slf4j
public class OXRClientIT {

    // Please get yours at https://openexchangerates.org/signup/free for example. 
    // I cannot disclose mine :(
    private static final String APP_ID = "OpenExchangeRatesAppId";

    private static OXRClient client;

    @BeforeClass
    public static void setUpClass() throws IOException {
        OXRClientConfigurationBuilder builder = OXRClientConfiguration.builderWithDefaults()
                .asyncExecutor(Executors.newSingleThreadExecutor())
                .appId(APP_ID);
        client = new OXRClient(builder.build());
        client.init();
    }

    @AfterClass
    public static void tearDownClass() {
        client.dispose();
        client = null;
    }

    @Test
    public void testLoadCurrencies() throws IOException {
        Set<XChangeDetailedCurrency> currencies = client.loadDetailedCurrencies();
        Assert.assertFalse("Loaded currencies cannot be empty", currencies.isEmpty());
        Assert.assertThat(currencies, hasItem(US_DOLLAR));
        currencies.forEach(currency -> log.info("Currency: {}", currency));
    }

    @Test
    public void testLoadRates() throws IOException {
        XChangeRate usdDollarToItselfRate = new OXRRate(US_DOLLAR, US_DOLLAR, Instant.now(), 1);
        Set<XChangeRate> rates = client.loadRates();
        Assert.assertFalse("Loaded rates cannot be empty", rates.isEmpty());
        Assert.assertThat(rates, hasItem(usdDollarToItselfRate));
        rates.forEach(rate -> log.info("Rate: {}", rate));
    }

}
