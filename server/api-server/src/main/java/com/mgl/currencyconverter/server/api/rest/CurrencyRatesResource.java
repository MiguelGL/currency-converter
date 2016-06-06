package com.mgl.currencyconverter.server.api.rest;

import com.mgl.currencyconverter.server.api.entity.Currency;
import com.mgl.currencyconverter.server.api.exception.CurrencyNotFoundException;
import com.mgl.currencyconverter.server.api.rest.model.ApiExchangeRate;
import com.mgl.currencyconverter.server.api.rest.model.ApiExchangeRates;
import com.mgl.currencyconverter.server.api.rest.model.ApiRate;
import com.mgl.currencyconverter.server.api.rest.spec.CurrencyRatesApi;
import com.mgl.currencyconverter.server.api.service.CurrencyService;
import com.mgl.currencyconverter.server.api.service.RateService;
import com.mgl.currencyconverter.server.api.service.model.TimestampedRate;
import com.mysema.commons.lang.Pair;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

@Stateless
public class CurrencyRatesResource implements CurrencyRatesApi {

    private @Inject CurrencyService currencyService;
    private @Inject RateService rateService;

    @Override
    public ApiExchangeRates getCurrencyExchangeRates(
            String baseCurrencyCode,
            List<String> targetCurrencyCodes)
    throws CurrencyNotFoundException {
        Currency baseCurrency = currencyService.findExistingByCode(baseCurrencyCode);
        List<Currency> targetCurrencies = targetCurrencyCodes.stream()
                .map((code) -> currencyService.maybeFindByCode(code))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());

        if (targetCurrencies.isEmpty()) {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }

        Map<String, TimestampedRate> tsdRates = targetCurrencies.stream()
                .map(targetCurrency -> new Pair<>(
                        targetCurrency.getCode(),
                        rateService.calculateExchangeRate(baseCurrency, targetCurrency)))
                .collect(Collectors.toMap(Pair::getFirst, Pair::getSecond));

        // Can safely .get() the optional because I ensured list is not empty above
        Instant minTs = tsdRates.values().stream().map(TimestampedRate::getTs).sorted().findFirst().get();

        List<ApiRate> rates = tsdRates.entrySet().stream()
                .map(entry -> new ApiRate(entry.getKey(), entry.getValue().getRate()))
                .collect(Collectors.toList());

        return new ApiExchangeRates(baseCurrencyCode, minTs, rates);
    }

    @Override
    public ApiExchangeRate getCurrencyExchangeRate(
            String baseCurrencyCode,
            String targetCurrencyCode)
    throws CurrencyNotFoundException {
        Currency baseCurrency = currencyService.findExistingByCode(baseCurrencyCode);
        Currency targetCurrency = currencyService.findExistingByCode(targetCurrencyCode);

        TimestampedRate tsdRate = rateService.calculateExchangeRate(baseCurrency, targetCurrency);

        return new ApiExchangeRate(
                tsdRate.getTs(),
                baseCurrency.getCode(),
                targetCurrency.getCode(),
                tsdRate.getRate());
    }

}
