package com.mgl.currencyconverter.server.api.rest;

import com.mgl.currencyconverter.server.api.entity.Currency;
import com.mgl.currencyconverter.server.api.rest.model.ApiCurrency;
import com.mgl.currencyconverter.server.api.rest.spec.CurrenciesApi;
import com.mgl.currencyconverter.server.api.service.CurrencyService;
import java.util.List;
import java.util.stream.Collectors;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.core.GenericEntity;

@Stateless
public class CurrenciesResource implements CurrenciesApi {

    private @Inject CurrencyService currencyService;

    private static ApiCurrency currencyToApiCurrency(Currency currency) {
        return new ApiCurrency(currency.getCode(), currency.getName());
    }

    @Override
    public GenericEntity<List<ApiCurrency>> getCurrencies() {
        List<Currency> supportedCurrencies = currencyService.listSupportedCurrencies();
        List<ApiCurrency> apiCurrencies = supportedCurrencies.stream()
                .map(CurrenciesResource::currencyToApiCurrency)
                .collect(Collectors.toList());
        return new GenericEntity<List<ApiCurrency>>(apiCurrencies) {};
    }

}
