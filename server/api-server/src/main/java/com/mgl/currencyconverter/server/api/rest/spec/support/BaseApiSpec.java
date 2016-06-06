package com.mgl.currencyconverter.server.api.rest.spec.support;

import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import static java.net.HttpURLConnection.HTTP_BAD_REQUEST;
import static java.net.HttpURLConnection.HTTP_NOT_FOUND;

@ApiResponses({
    @ApiResponse(
        code = HTTP_NOT_FOUND, message = "Not Found"),
    @ApiResponse(
        code = HTTP_BAD_REQUEST, message = "Bad Request")
})
public interface BaseApiSpec {

    static final String TAG_CURRENCIES = "Currencies";
    static final String TAG_RATES = "Rates";
    static final String TAG_EXCHANGES = "Exchanges";

}
