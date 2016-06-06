package com.mgl.currencyconverter.server.api.service;

import java.io.IOException;
import java.io.Serializable;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;

import com.mgl.currencyconverter.provider.api.XChangeClient;
import com.mgl.currencyconverter.provider.api.model.XChangeDetailedCurrency;
import com.mgl.currencyconverter.server.api.entity.Currency;
import lombok.extern.slf4j.Slf4j;

/**
 * Helper class to initialise the list of supported currencies from the external provider.
 *
 * I am using this just for convenience, instead of facilitating a population
 * SQL script or similar.
 *
 */
@Slf4j
@Startup
@Singleton
public class CurrenciesInitialiser implements Serializable {

    private static final long serialVersionUID = 1L;

    private @Inject XChangeClient xChangeClient;

    private @Inject CurrencyService currencyService;
    private @Inject ExchangeRatesUpdater exchangeRatesUpdater;

    @PostConstruct
    public void initialiseSupportedCurrencies() {
        log.info("Initialising supported currencies ...");
        Set<XChangeDetailedCurrency> xCurrencies;
        try {
            xCurrencies = xChangeClient.loadDetailedCurrencies();
        } catch (IOException ex) {
            log.error("Error supported currencies", ex);
            // This is a fatal error, so fail miserably now and stop deployment.
            throw new RuntimeException(ex);
        }
        log.info("Loaded {} currencies to support", xCurrencies.size());

        // First, make sure the ones we got are supported
        Set<String> supportedCurrencyCodes = xCurrencies.stream()
                .map(this::ensureSupportedCurrency)
                .map(Currency::getCode)
                .collect(Collectors.toSet());
        log.info("Found {} supported currencies", supportedCurrencyCodes.size());

        // Now, cleanup those we may have that are now not supported anymore
        Set<String> unsupportedCurrencyCodes = xCurrencies.stream()
                .filter(xCurrency -> !supportedCurrencyCodes.contains(xCurrency.getCode()))
                .map(XChangeDetailedCurrency::getCode)
                .collect(Collectors.toSet());

        long deletedCnt = currencyService.ensureCurrenciesAreNotSupported(unsupportedCurrencyCodes);
        log.info("Found {} no longer supported currencies", deletedCnt);

        exchangeRatesUpdater.updateExchangeRates();
    }

    private Currency ensureSupportedCurrency(XChangeDetailedCurrency xCurrency) {
        return currencyService.ensureCurrencyIsSupported(xCurrency.getCode(), xCurrency.getName());
    }

}
