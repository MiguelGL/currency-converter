package com.mgl.currencyconverter.provider.api;

import java.net.URI;

public interface XChangeClientConfiguration {

    URI getBaseUri();

    long getTimeout(); // ms.

}
