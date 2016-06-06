package com.mgl.currencyconverter.server.api.rest.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.NonNull;

import javax.validation.constraints.NotNull;

import java.io.Serializable;
import java.time.Instant;

import static com.google.common.base.Preconditions.checkNotNull;

import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mgl.currencyconverter.server.api.entity.Currency;

@Data
@ApiModel(
    description = "A singular information of a exchange rate"
)
// FIXME: Fix Instant timestamp issue
public class ApiExchangeRate implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    @ApiModelProperty(
            readOnly = true, required = true, dataType = "String",
            example = "2016-04-21T19:47:33.723Z",
            value = "The most recent rate timestamp")
    @JsonProperty @Getter(AccessLevel.PACKAGE)
    private final @NonNull /* Instant */ String timestamp;

    @NotNull
    @Size(min = Currency.CODE_LEN, max = Currency.CODE_LEN)
    @ApiModelProperty(
            readOnly = true, required = true,
            example = "EUR",
            value = "The reference currency code")
    private final @NonNull String baseCurrency;

    @NotNull
    @ApiModelProperty(
            readOnly = true, required = true,
            example = "GBP",
            value = "The target currency code")
    @Size(min = Currency.CODE_LEN, max = Currency.CODE_LEN)
    private final @NonNull String targetCurrency;

    @NotNull
    @ApiModelProperty(
            readOnly = true, required = true,
            example = "0.78",
            value = "The exchange rate")
    @Min(0)
    private final double value;

    public ApiExchangeRate(
            @JsonProperty("timestamp") Instant timestamp,
            @JsonProperty("baseCurrency") String baseCurrency,
            @JsonProperty("targetCurrency") String targetCurrency,
            @JsonProperty("value") double value) {
        this.timestamp = checkNotNull(timestamp, "timestamp").toString();
        this.baseCurrency = checkNotNull(baseCurrency, "baseCurrency");
        this.targetCurrency = checkNotNull(targetCurrency, "targetCurrency");
        this.value = value;
    }

    @JsonIgnore
    public Instant getInstantTimestamp() {
        return Instant.parse(getTimestamp());
    }

}
