package com.mgl.currencyconverter.server.api.rest.support;

import javax.ws.rs.ext.ParamConverter;
import javax.ws.rs.ext.ParamConverterProvider;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.time.Instant;

// FIXME: For some reason, RestEasy seems to ignore this Provider :( @Provider
public class InstantProvider implements ParamConverterProvider {

    public static class InstantConverter implements ParamConverter<Instant> {

        @Override
        public Instant fromString(String string) {
            Instant localDateTime = Instant.parse(string);
            return localDateTime;
        }

        @Override
        public String toString(Instant t) {
            return t.toString();
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> ParamConverter<T> getConverter(Class<T> type, Type type1, Annotation[] antns) {
        if (Instant.class.equals(type)) {
            return (ParamConverter<T>) new InstantConverter();
        } else {
            return null;
        }
    }
}