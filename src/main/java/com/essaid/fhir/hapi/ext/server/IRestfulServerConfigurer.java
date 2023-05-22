package com.essaid.fhir.hapi.ext.server;

import ca.uhn.fhir.rest.server.RestfulServer;

public interface IRestfulServerConfigurer {

    void configure(RestfulServer server);

}
