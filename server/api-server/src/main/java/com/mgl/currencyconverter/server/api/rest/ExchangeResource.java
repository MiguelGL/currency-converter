package com.mgl.currencyconverter.server.api.rest;


import javax.ejb.Stateless;
import javax.inject.Inject;

import com.mgl.currencyconverter.server.api.entity.Currency;
import com.mgl.currencyconverter.server.api.exception.CurrencyNotFoundException;
import com.mgl.currencyconverter.server.api.rest.model.ApiExchangeValue;
import com.mgl.currencyconverter.server.api.rest.spec.ExchangeApi;
import com.mgl.currencyconverter.server.api.service.CurrencyService;
import com.mgl.currencyconverter.server.api.service.RateService;
import com.mgl.currencyconverter.server.api.service.model.TimestampedRate;

@Stateless
public class ExchangeResource implements ExchangeApi {

    private @Inject CurrencyService currencyService;
    private @Inject RateService rateService;

    @Override
    public ApiExchangeValue getCurrencyExchangeValue(
            String baseCurrencyCode,
            String targetCurrencyCode,
            double quantity) 
    throws CurrencyNotFoundException {
        Currency baseCurrency = currencyService.findExistingByCode(baseCurrencyCode);
        Currency targetCurrency = currencyService.findExistingByCode(targetCurrencyCode);

        TimestampedRate tsdRate = rateService.calculateExchangeRate(baseCurrency, targetCurrency);

        double value = quantity * tsdRate.getRate();

        return new ApiExchangeValue(
                tsdRate.getTs(), 
                baseCurrency.getCode(),
                targetCurrency.getCode(),
                quantity,
                value);
    }

}
