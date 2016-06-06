package com.mgl.currencyconverter.server.api.rest;

import static com.jayway.restassured.RestAssured.get;
import static org.hamcrest.Matchers.hasItems;

import static com.jayway.restassured.RestAssured.*;
import static com.jayway.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import org.junit.Test;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.mgl.currencyconverter.server.api.rest.support.BaseResourceIT;

public class CurrenciesResourceIT extends BaseResourceIT {

    @Test
    public void testGetCurrencies() {
        get("/currencies")
                .then()
                .contentType(ContentType.JSON)
                .body("code", hasItems("USD"));
    }

}
