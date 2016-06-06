package com.mgl.currencyconverter.server.api.rest.exception;

import com.mgl.currencyconverter.server.api.exception.CurrencyNotFoundException;
import javax.ws.rs.core.Response;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class CurrencyNotFoundExceptionMapper implements ExceptionMapper<CurrencyNotFoundException> {

    @Override
    public Response toResponse(CurrencyNotFoundException exception) {
        return Response
                .status(NOT_FOUND)
                .entity(exception.getMessage())
                .build();
    }

}
