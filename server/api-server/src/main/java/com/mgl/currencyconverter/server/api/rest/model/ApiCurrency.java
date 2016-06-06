package com.mgl.currencyconverter.server.api.rest.model;

import java.io.Serializable;

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
    description = "Currency details"
)
public class ApiCurrency implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    @Size(min = Currency.CODE_LEN, max = Currency.CODE_LEN)
    @ApiModelProperty(
            required = true, readOnly = true,
            value = "The three letter, uppercase code for the currency",
            example = "EUR")
    private final @NonNull String code;

    @NotNull
    @Size(min = Currency.NAME_MIN_LEN, max = Currency.NAME_MAX_LEN)
    @ApiModelProperty(
            required = true, readOnly = true,
            value = "The three letter, uppercase code for the currency",
            example = "European Euro")
    private final @NonNull String name;

    public ApiCurrency(
            @JsonProperty("code") String code,
            @JsonProperty("name") String name) {
        this.code = code;
        this.name = name;
    }

}
