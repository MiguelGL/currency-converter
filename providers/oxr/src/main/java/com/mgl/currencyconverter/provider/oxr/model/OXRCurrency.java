package com.mgl.currencyconverter.provider.oxr.model;

import com.mgl.currencyconverter.provider.api.model.XChangeDetailedCurrency;
import lombok.Data;
import lombok.NonNull;

@Data
public class OXRCurrency implements XChangeDetailedCurrency {

    private final @NonNull String code;
    private final @NonNull String name;

}
