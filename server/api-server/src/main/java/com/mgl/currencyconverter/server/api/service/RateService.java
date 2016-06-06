package com.mgl.currencyconverter.server.api.service;

import com.mgl.currencyconverter.server.api.dao.CurrencyDao;
import java.io.Serializable;
import java.time.Instant;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.mgl.currencyconverter.server.api.dao.RateDao;
import com.mgl.currencyconverter.server.api.entity.Currency;
import com.mgl.currencyconverter.server.api.entity.Rate;
import com.mgl.currencyconverter.server.api.service.model.TimestampedRate;

/**
 * Provides business services related to rates.
 */
@Stateless
public class RateService implements Serializable {

    private static final long serialVersionUID = 1L;

    private @Inject CurrencyService currencyService;

    private @Inject RateDao rateDao;

    public boolean createOrUpdateRateIfRequired(
            Currency baseCurrency, Currency ratedCurrency,
            double rateValue, Instant asOfTs) {
        Optional<Rate> maybeRate = rateDao.maybeFindByCurrencies(baseCurrency, ratedCurrency);
        if (maybeRate.isPresent()) {
            Rate rate = maybeRate.get();
            if (asOfTs.isAfter(rate.getLastUpdateTs())) {
                rate.setRateValue(rateValue);
                rate.setLastUpdateTs(asOfTs);
                rateDao.update(rate);
                return true;
            } else {
                return false;
            }
        } else {
            Rate rate = new Rate(baseCurrency, ratedCurrency, rateValue, asOfTs);
            rateDao.create(rate);
            return true;
        }
    }

    public TimestampedRate calculateExchangeRate(Currency baseCurrency, Currency targetCurrency) {
        // Do we have this rate already?
        Optional<Rate> maybeRate = rateDao.maybeFindByCurrencies(baseCurrency, targetCurrency);
        if (maybeRate.isPresent()) {
            return TimestampedRate.from(maybeRate.get());
        } else {
            Currency refCurrency = currencyService.referenceCurrency();
            Rate baseRefRate = rateDao.maybeFindByCurrencies(refCurrency, baseCurrency)
                    .orElseThrow(IllegalStateException::new);
            Rate targetRefRate = rateDao.maybeFindByCurrencies(refCurrency, targetCurrency)
                    .orElseThrow(IllegalStateException::new);
            return TimestampedRate.from(baseRefRate.relativise(targetRefRate));
        }
    }

}
