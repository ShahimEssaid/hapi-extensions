package com.essaid.fhir.hapiext.ext.test.example;

import ca.uhn.fhir.rest.server.RestfulServer;
import com.essaid.fhir.hapiext.server.ServerConfigurer;
import org.springframework.stereotype.Component;

@Component("test-configurer-name")
public class RestfulServerConfigurerExample implements ServerConfigurer {
    @Override
    public void configure(RestfulServer server) {
       System.out.println(server);
    }
}
