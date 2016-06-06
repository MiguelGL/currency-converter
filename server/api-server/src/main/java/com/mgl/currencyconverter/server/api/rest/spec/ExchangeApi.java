package com.mgl.currencyconverter.server.api.rest.spec;

import static com.mgl.currencyconverter.server.api.rest.spec.support.BaseApiSpec.TAG_RATES;
import static java.net.HttpURLConnection.HTTP_NOT_FOUND;
import static java.net.HttpURLConnection.HTTP_OK;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.mgl.currencyconverter.server.api.exception.CurrencyNotFoundException;
import com.mgl.currencyconverter.server.api.rest.model.ApiExchangeValue;
import com.mgl.currencyconverter.server.api.rest.spec.support.BaseApiSpec;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Path("/currencies/{baseCurrency}/exchanges/{targetCurrency}/{quantity}")
@Api(value = "/currencies/{baseCurrency}/exchanges/{targetCurrency}/{quantity}",
     produces = MediaType.APPLICATION_JSON)
public interface ExchangeApi extends BaseApiSpec {

    // This implementation could be made more general by for example also allowing
    // text/plain to be returned with the plain value. Would then need to return a
    // jax.ws.rs.Response, setting its 'entity' and 'type' accordingly afer
    // also examining the Request object for the accepted content-type.
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(
            value = "Exchange an amount of money from one currency to another",
            tags = TAG_EXCHANGES)
    @ApiResponses({
        @ApiResponse(
            code = HTTP_OK, message = "OK"),
        @ApiResponse(
            code = HTTP_NOT_FOUND, message = "Currency not found")
    })
    public ApiExchangeValue getCurrencyExchangeValue(
            @ApiParam(value = "The base currency", required = true)
            @PathParam("baseCurrency") String srcCurrency,
            @ApiParam(value = "The target currency", required = true)
            @PathParam("targetCurrency") String dstCurrency,
            @ApiParam(value = "The amount of money to exchange")
            @PathParam("quantity") double quantity)
    throws CurrencyNotFoundException;

}
