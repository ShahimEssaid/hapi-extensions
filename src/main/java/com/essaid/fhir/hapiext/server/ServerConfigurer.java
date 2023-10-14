package com.essaid.fhir.hapiext.server;

import ca.uhn.fhir.rest.server.RestfulServer;

public interface ServerConfigurer {

    void configure(RestfulServer server);

}
