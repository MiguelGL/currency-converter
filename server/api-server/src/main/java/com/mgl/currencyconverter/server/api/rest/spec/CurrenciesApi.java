package com.mgl.currencyconverter.server.api.rest.spec;

import com.mgl.currencyconverter.server.api.rest.spec.support.BaseApiSpec;
import static java.net.HttpURLConnection.HTTP_OK;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;

import com.mgl.currencyconverter.server.api.rest.model.ApiCurrency;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Path("/currencies")
@Api(value = "/currencies", produces = MediaType.APPLICATION_JSON)
public interface CurrenciesApi extends BaseApiSpec {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(
            value = "List supported currencies",
            tags = TAG_CURRENCIES)
    @ApiResponses({
        @ApiResponse(
            code = HTTP_OK, message = "OK",
            response = ApiCurrency.class,
            responseContainer = "List")
    })
    public GenericEntity<List<ApiCurrency>> getCurrencies();

}
