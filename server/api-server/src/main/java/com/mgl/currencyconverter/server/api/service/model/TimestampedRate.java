package com.mgl.currencyconverter.server.api.service.model;

import com.mgl.currencyconverter.server.api.entity.Rate;
import java.time.Instant;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data @RequiredArgsConstructor(staticName = "of")
public class TimestampedRate {

    private final Instant ts;
    private final double rate;

    public static TimestampedRate from(Rate rate) {
        return new TimestampedRate(rate.getLastUpdateTs(), rate.getRateValue());
    }

}
