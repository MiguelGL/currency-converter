package com.mgl.currencyconverter.provider.api;

import java.io.IOException;

import com.mgl.currencyconverter.provider.api.model.XChangeRate;

import java.util.Set;

import com.mgl.currencyconverter.provider.api.model.XChangeDetailedCurrency;

public interface XChangeClient {

    void init() throws IOException;

    Set<XChangeDetailedCurrency> loadDetailedCurrencies() throws IOException;

    Set<XChangeRate> loadRates() throws IOException ;

    void dispose() throws IOException;

}
