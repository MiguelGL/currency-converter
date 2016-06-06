package com.mgl.currencyconverter.provider.api.model;

import java.time.Instant;

public interface XChangeRate {

    XChangeCurrency getBaseCurrency();
    XChangeCurrency getRatedCurrency();
    
    Instant getTs();

    double getRate();

}
