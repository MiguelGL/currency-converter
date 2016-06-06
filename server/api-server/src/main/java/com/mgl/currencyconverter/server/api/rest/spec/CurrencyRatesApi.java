package com.mgl.currencyconverter.server.api.rest.spec;

import com.mgl.currencyconverter.server.api.exception.CurrencyNotFoundException;
import com.mgl.currencyconverter.server.api.rest.spec.support.BaseApiSpec;
import static java.net.HttpURLConnection.HTTP_BAD_METHOD;
import static java.net.HttpURLConnection.HTTP_BAD_REQUEST;
import static java.net.HttpURLConnection.HTTP_NOT_FOUND;
import static java.net.HttpURLConnection.HTTP_OK;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.mgl.currencyconverter.server.api.rest.model.ApiExchangeRate;
import com.mgl.currencyconverter.server.api.rest.model.ApiExchangeRates;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import static java.net.HttpURLConnection.HTTP_PRECON_FAILED;
import javax.ws.rs.QueryParam;

@Path("/currencies/{baseCurrency}/rates")
@Api(value = "/currencies/{baseCurrency}/rates", produces = MediaType.APPLICATION_JSON)
@ApiResponses({
    @ApiResponse(
        code = HTTP_NOT_FOUND, message = "Not Found"),
    @ApiResponse(
        code = HTTP_BAD_REQUEST, message = "Bad Request"),
    @ApiResponse(
        code = HTTP_BAD_METHOD, message = "Method not allowed"),
})
public interface CurrencyRatesApi extends BaseApiSpec {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(
            value = "Get currencies exchange rates",
            tags = TAG_RATES)
    @ApiResponses({
        @ApiResponse(
            code = HTTP_OK, message = "OK"),
        @ApiResponse(
            code = HTTP_NOT_FOUND, message = "Currency not found"),
        @ApiResponse(
            code = HTTP_PRECON_FAILED, message = "Invalid list of target currencies")
    })
    public ApiExchangeRates getCurrencyExchangeRates(
            @ApiParam(value = "The base currency", required = true)
            @PathParam("baseCurrency") String srcCurrency,
            @ApiParam(value = "The target currency(ies)", required = true, allowMultiple = true)
            @QueryParam("targetCurrency") List<String> dstCurrencies)
    throws CurrencyNotFoundException;

    @GET
    @Path("/{targetCurrency}")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(
            value = "Get the exchange rate for the target currency",
            tags = TAG_RATES)
    @ApiResponses({
        @ApiResponse(
            code = HTTP_OK, message = "OK"),
        @ApiResponse(
            code = HTTP_NOT_FOUND, message = "Currency not found"),
    })
    public ApiExchangeRate getCurrencyExchangeRate(
            @ApiParam(value = "The base currency", required = true)
            @PathParam("baseCurrency") String srcCurrency,
            @ApiParam(value = "The target currency", required = true)
            @PathParam("targetCurrency") String dstCurrency)
    throws CurrencyNotFoundException;

}
