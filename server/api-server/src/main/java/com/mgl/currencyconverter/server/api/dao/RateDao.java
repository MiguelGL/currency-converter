package com.mgl.currencyconverter.server.api.dao;

import java.util.Optional;

import com.mgl.currencyconverter.server.api.dao.support.BaseDao;
import com.mgl.currencyconverter.server.api.entity.Currency;
import com.mgl.currencyconverter.server.api.entity.QRate;
import com.mgl.currencyconverter.server.api.entity.Rate;

public class RateDao extends BaseDao<Rate, QRate> {

    RateDao() {
        super(Rate.class, QRate.rate);
    }

    public Optional<Rate> maybeFindByCurrencies(Currency baseCurrency, Currency ratedCurrency) {
        return Optional.ofNullable(
                queryFromMe()
                    .where(pathBase.baseCurrency.eq(baseCurrency),
                           pathBase.ratedCurrency.eq(ratedCurrency))
                    .fetchOne());
    }

}
