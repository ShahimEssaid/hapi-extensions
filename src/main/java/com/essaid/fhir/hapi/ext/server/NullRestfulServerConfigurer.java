package com.essaid.fhir.hapi.ext.server;

import ca.uhn.fhir.rest.server.RestfulServer;
import org.springframework.stereotype.Component;

@Component
public class NullRestfulServerConfigurer implements IRestfulServerConfigurer{
    @Override
    public void configure(RestfulServer server) {

    }
}
