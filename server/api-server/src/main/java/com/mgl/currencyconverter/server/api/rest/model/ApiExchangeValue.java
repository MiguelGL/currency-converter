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
    description = "A result of exchanging a certain amount of a currency to another"
)
public class ApiExchangeValue implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    @ApiModelProperty(
            readOnly = true, required = true, dataType = "String",
            example = "2016-04-21T19:47:33.723Z",
            value = "The timestamp for the used exchange rate")
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
            example = "GBP",
            value = "The target currency code")
    @Size(min = Currency.CODE_LEN, max = Currency.CODE_LEN)
    private final @NonNull String targetCurrency;

    @NotNull
    @ApiModelProperty(
            readOnly = true, required = true,
            example = "10",
            value = "The amount of money to exchange")
    @Min(0)
    private final double quantity;

    @NotNull
    @ApiModelProperty(
            readOnly = true, required = true,
            example = "7.8",
            value = "The exchange value")
    private final double value;

    ApiExchangeValue(
            @JsonProperty("timestamp") String timestamp,
            @JsonProperty("baseCurrency") String baseCurrency,
            @JsonProperty("targetCurrency") String targetCurrency,
            @JsonProperty("quantity") double quantity,
            @JsonProperty("value") double value) {
        this.timestamp = checkNotNull(timestamp, "timestamp");
        this.baseCurrency = checkNotNull(baseCurrency, "baseCurrency");
        this.targetCurrency = checkNotNull(targetCurrency, "targetCurrency");
        this.quantity = quantity;
        this.value = value;
    }

    public ApiExchangeValue(
            Instant timestamp,
            String baseCurrency,
            String targetCurrency,
            double quantity,
            double value) {
        this(checkNotNull(timestamp, "timestamp").toString(),
             baseCurrency, targetCurrency, quantity, value);
    }

    @JsonIgnore
    public Instant getInstantTimestamp() {
        return Instant.parse(getTimestamp());
    }

}
