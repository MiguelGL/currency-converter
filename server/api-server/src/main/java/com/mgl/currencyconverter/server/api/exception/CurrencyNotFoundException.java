package com.mgl.currencyconverter.server.api.exception;

import static com.google.common.base.Preconditions.checkNotNull;
import lombok.Getter;
import lombok.NonNull;

@Getter
public class CurrencyNotFoundException extends Exception {

    private static final long serialVersionUID = 1L;

    private final @NonNull String code;

    public CurrencyNotFoundException(String code) {
        super(String.format("No such currency with code '%s'", code));
        this.code = checkNotNull(code, "code");
    }

}
