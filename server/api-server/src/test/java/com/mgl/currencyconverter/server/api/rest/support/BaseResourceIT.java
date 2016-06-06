package com.mgl.currencyconverter.server.api.rest.support;

import com.jayway.restassured.RestAssured;
import org.junit.BeforeClass;

public class BaseResourceIT {

    @BeforeClass
    public static void setUpClass() {
        RestAssured.baseURI = String.format("%s://%s", "http", "localhost");
        RestAssured.port = 8080;
        RestAssured.basePath = "/converter/v1";
    }

}
