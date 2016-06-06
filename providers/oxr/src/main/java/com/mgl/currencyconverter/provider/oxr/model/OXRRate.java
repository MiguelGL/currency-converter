package com.mgl.currencyconverter.provider.oxr.model;

import java.time.Instant;

import com.mgl.currencyconverter.provider.api.model.XChangeRate;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

@Data @EqualsAndHashCode(exclude = "ts")
public class OXRRate implements XChangeRate {

    private final @NonNull OXRCurrency baseCurrency;
    private final @NonNull OXRCurrency ratedCurrency;

    private final @NonNull Instant ts;

    private final double rate;

}
