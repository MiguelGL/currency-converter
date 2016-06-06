package com.mgl.currencyconverter.server.api.service;

import java.io.Serializable;
import java.util.Optional;
import java.util.Set;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.mgl.currencyconverter.server.api.dao.CurrencyDao;
import com.mgl.currencyconverter.server.api.entity.Currency;
import com.mgl.currencyconverter.server.api.exception.CurrencyNotFoundException;
import java.util.List;
import javax.persistence.NoResultException;

/**
 * Provides business services related to currencies.
 */
@Stateless
public class CurrencyService implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final String USD_CODE = "USD";

    private @Inject CurrencyDao dao;

    public Currency referenceCurrency() {
        return dao.findExistingByCode(USD_CODE);
    }

    public Optional<Currency> maybeFindByCode(String code) {
        return dao.maybeFindByCode(code);
    }

    public Currency findExistingByCode(String code)
    throws CurrencyNotFoundException {
        try {
            return dao.findExistingByCode(code);
        } catch (NoResultException nre) {
            throw new CurrencyNotFoundException(code);
        }
    }

    public List<Currency> listSupportedCurrencies() {
        return dao.findAll();
    }

    public Currency ensureCurrencyIsSupported(String code, String name) {
        Optional<Currency> maybeCurrency = dao.maybeFindByCode(code);
        if (maybeCurrency.isPresent()) {
            Currency currency = maybeCurrency.get();
            if (currency.getName().equals(name)) {
                return currency;
            } else {
                currency.setName(name);
                return dao.update(currency);
            }
        } else {
            Currency currency = new Currency(code, name);
            dao.create(currency);
            return currency;
        }
    }

    public long ensureCurrenciesAreNotSupported(Set<String> codes) {
        return dao.deleteByCodes(codes);
    }

}
