package com.mgl.currencyconverter.server.api.dao;

import java.util.Optional;
import java.util.Set;

import com.mgl.currencyconverter.server.api.dao.support.BaseDao;
import com.mgl.currencyconverter.server.api.entity.Currency;
import com.mgl.currencyconverter.server.api.entity.QCurrency;
import javax.persistence.NoResultException;

public class CurrencyDao extends BaseDao<Currency, QCurrency> {

    CurrencyDao() {
        super(Currency.class, QCurrency.currency);
    }

    public Optional<Currency> maybeFindByCode(String code) {
        return Optional.ofNullable(
                queryFromMe()
                    .where(pathBase.code.eq(code))
                    .fetchOne());
    }

    public Currency findExistingByCode(String code) {
        Currency currency = queryFromMe()
                .where(pathBase.code.eq(code))
                .fetchOne();
        if (currency == null) {
            throw new NoResultException();
        } else {
            return currency;
        }
    }

    public long deleteByCodes(Set<String> codes) {
        return deleteFromMe()
                .where(pathBase.code.in(codes))
                .execute();
    }

}
