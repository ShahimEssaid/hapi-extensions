package com.essaid.fhir.hapi.ext.test.example;

import ca.uhn.fhir.rest.server.RestfulServer;
import com.essaid.fhir.hapi.ext.server.IRestfulServerConfigurer;
import org.springframework.stereotype.Component;

@Component("test-configurer-name")
public class RestfulServerConfigurerExample implements IRestfulServerConfigurer {
    @Override
    public void configure(RestfulServer server) {
       System.out.println(server);
    }
}
