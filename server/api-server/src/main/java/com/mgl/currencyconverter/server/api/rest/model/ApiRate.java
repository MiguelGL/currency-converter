package com.mgl.currencyconverter.server.api.rest.model;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.Serializable;

import javax.validation.constraints.Min;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.mgl.currencyconverter.server.api.entity.Currency;
import lombok.Data;
import lombok.NonNull;

@Data
@ApiModel(
    description = "An exchange rate"
)
public class ApiRate implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    @Size(min = Currency.CODE_LEN, max = Currency.CODE_LEN)
    @ApiModelProperty(
            readOnly = true, required = true,
            example = "GBP",
            value = "The target currency code")
    private final @NonNull String targetCurrency;

    @NotNull
    @Min(0)
    @ApiModelProperty(
            readOnly = true, required = true,
            example = "0.78",
            value = "The exchange rate")
    private final double value;

    public ApiRate(
            @JsonProperty("targetCurrency") String targetCurrency,
            @JsonProperty("value") double value) {
        this.targetCurrency = checkNotNull(targetCurrency, "targetCurrency");
        this.value = value;
    }

}
