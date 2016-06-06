package com.mgl.currencyconverter.server.api.rest.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.ImmutableList;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.NonNull;

import javax.validation.constraints.NotNull;

import java.io.Serializable;
import java.time.Instant;
import java.util.Collection;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mgl.currencyconverter.server.api.entity.Currency;

@Data
@ApiModel(
    description = "Multiple information of exchange rates for a same currency"
)
// FIXME: Fix Instant timestamp issue
public class ApiExchangeRates implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    @ApiModelProperty(
            readOnly = true, required = true, dataType = "String",
            example = "2016-04-21T19:47:33.723Z",
            value = "The oldest from most recent rates timestamps")
    @JsonProperty @Getter(AccessLevel.PACKAGE)
    private final @NonNull /* Instant */ String timestamp;

    @NotNull
    @ApiModelProperty(
            readOnly = true, required = true,
            example = "EUR",
            value = "The reference currency code")
    @Size(min = Currency.CODE_LEN, max = Currency.CODE_LEN)
    private final @NonNull String baseCurrency;

    @NotNull
    @ApiModelProperty(
            readOnly = true, required = true,
            value = "The exchange rates")
    private final @NonNull List<ApiRate> rates;

    public ApiExchangeRates(
            @JsonProperty("baseCurrency") String baseCurrency,
            @JsonProperty("timestamp") Instant timestamp,
            @JsonProperty("rates") Collection<ApiRate> rates) {
        this.baseCurrency = checkNotNull(baseCurrency, "baseCurrency");
        this.timestamp = checkNotNull(timestamp, "timestamp").toString();
        this.rates = ImmutableList.copyOf(rates);
    }

    @JsonIgnore
    public Instant getInstantTimestamp() {
        return Instant.parse(getTimestamp());
    }

}
