package com.mgl.currencyconverter.server.api.rest;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationPath("/v1") // Chose to version via URL path vs. meta information in HTTP headers.
public class RestApplication extends Application {

}