package com.mgl.currencyconverter.server.api.service;

import java.io.IOException;
import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.enterprise.concurrent.ManagedExecutorService;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;

import com.mgl.currencyconverter.provider.api.XChangeClient;
import com.mgl.currencyconverter.provider.oxr.OXRClient;
import com.mgl.currencyconverter.provider.oxr.OXRClientConfiguration;
import com.mgl.currencyconverter.provider.oxr.OXRClientConfiguration.OXRClientConfigurationBuilder;
import lombok.extern.slf4j.Slf4j;

/**
 * This class is a CDI producer for a client of the external exchange provider we are using.
 *
 * It generates an application wide client because these are expected to be thread safe.
 *
 */
@Slf4j
public class XChangeClientProducer implements Serializable {

    private static final long serialVersionUID = 1L;

    // Ideally, get these from somewhere in @PostConstruct
    // Please get yours at https://openexchangerates.org/signup/free for example.
    // I cannot disclose mine :(
    private static final String OPEN_XCHANGE_RATES_API_KEY = "OpenExchangeRatesAppId";
    private static final int CONNECTIONS_POOL_SIZE = 2;
    private static final int TIMEOUT = 30_000;

    // Will not really be used, but clients need one and when null they create their own one,
    // which is not what we want in a managed application server environment.
    @Resource private ManagedExecutorService mes;

    private XChangeClient client;

    @PostConstruct
    public void init() {
        log.info("Creating external exchanges provider client");
        OXRClientConfigurationBuilder builder = OXRClientConfiguration.builderWithDefaults()
                .asyncExecutor(mes)
                .timeout(TIMEOUT)
                .connectionPoolSize(CONNECTIONS_POOL_SIZE)
                .appId(OPEN_XCHANGE_RATES_API_KEY);
        client = new OXRClient(builder.build());
        try {
            log.info("Initialising external exchanges provider client");
            client.init();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Produces @ApplicationScoped
    public XChangeClient produceXChangeClient() {
        log.info("Producing external exchanges provider client");
        return client;
    }

    public void disposeXChangeClient(@Disposes XChangeClient xChangeClient) {
        log.info("Disposing external exchanges provider client");
        try {
            client.dispose();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

}
