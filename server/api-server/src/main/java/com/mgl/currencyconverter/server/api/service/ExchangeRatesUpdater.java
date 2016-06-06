package com.mgl.currencyconverter.server.api.service;

import java.io.IOException;
import java.io.Serializable;
import java.util.Optional;
import java.util.Set;

import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.inject.Inject;

import com.mgl.currencyconverter.provider.api.XChangeClient;
import com.mgl.currencyconverter.provider.api.model.XChangeRate;
import com.mgl.currencyconverter.server.api.entity.Currency;
import lombok.extern.slf4j.Slf4j;

/**
 * This class is in charge of periodically refreshing the exchange rates
 * from our external provider.
 *
 * It will do so every hour (fixed), but this value could be made configurable by a
 * more elaborate usage of the EJB Timer Service and reading some config values (period?)
 * from somewhere in a @PostConstruct method, for example.
 */
@Slf4j
@Singleton
// No need for this dependency as the "initialise" process uses us.
// @DependsOn(value = "CurrenciesInitialiser")
public class ExchangeRatesUpdater implements Serializable {

    private static final long serialVersionUID = 1L;

    private @Inject XChangeClient xChangeClient;

    private @Inject CurrencyService currencyService;
    private @Inject RateService rateService;

    @Schedule(year = "*", month = "*", dayOfMonth = "*", hour = "*/1", minute = "0", second = "0",
              persistent = false,
              info = "Currency Exchanges Refresh Timer (from external provider)")
    public void updateExchangeRates() {
        log.info("Updating exchange rates from external provider ...");
        Set<XChangeRate> externalRates;
        try {
            externalRates = xChangeClient.loadRates();
        } catch (IOException ex) {
            log.error("Error updating exchange rates from external provider", ex);
            return;
        }

        int updatedCnt = externalRates.stream().mapToInt(this::updateExchangeRate).sum();
        log.info("Updated {} exchange rates from external provider", updatedCnt);
    }

    private int updateExchangeRate(XChangeRate xRate) {
        Optional<Currency> maybeBaseCurrency =
                currencyService.maybeFindByCode(xRate.getBaseCurrency().getCode());
        if (!maybeBaseCurrency.isPresent()) {
            log.warn("Unsupported currency code: {}", xRate.getBaseCurrency().getCode());
            return 0;
        }

        Optional<Currency> maybeTargetCurrency =
                currencyService.maybeFindByCode(xRate.getRatedCurrency().getCode());
        if (!maybeBaseCurrency.isPresent()) {
            log.warn("Unsupported currency code: {}", xRate.getRatedCurrency().getCode());
            return 0;
        }

        Currency baseCurrency = maybeBaseCurrency.get();
        Currency targetCurrency = maybeTargetCurrency.get();

        boolean createdOrUpdated = rateService.createOrUpdateRateIfRequired(
                baseCurrency, targetCurrency,
                xRate.getRate(), xRate.getTs());

        return createdOrUpdated ? 1 : 0;
    }

}
