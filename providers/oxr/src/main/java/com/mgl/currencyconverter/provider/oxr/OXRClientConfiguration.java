package com.mgl.currencyconverter.provider.oxr;

import java.net.URI;
import java.util.concurrent.ExecutorService;

import com.mgl.currencyconverter.provider.api.XChangeClientConfiguration;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.Setter;

@Data
@Builder
@Setter(AccessLevel.PRIVATE) // Make the class effectively inmutable
public class OXRClientConfiguration implements XChangeClientConfiguration {

    private long timeout; // ms.

    private @NonNull URI baseUri;

    private @NonNull String appId;

    private int connectionPoolSize;
    private int connectionTtl; // ms.

    private ExecutorService asyncExecutor; // If null, the client will create one

    public static OXRClientConfigurationBuilder builderWithDefaults() {
        return builder()
                .timeout(15_000)
                .baseUri(URI.create("https://openexchangerates.org/api/"))
                .appId("")
                .connectionPoolSize(1)
                .connectionTtl(30_000)
                .asyncExecutor(null);
    }

}
