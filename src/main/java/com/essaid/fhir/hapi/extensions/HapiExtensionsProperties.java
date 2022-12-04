package com.essaid.fhir.hapi.extensions;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@ConfigurationProperties(prefix = "com.essaid.fhir.hapi.extensions")
@Configuration
public class HapiExtensionsProperties {
  
  public List<String> getProviderClassNames() {
    return providerClassNames;
  }
  
  public void setProviderClassNames(List<String> providerClassNames) {
    this.providerClassNames = providerClassNames;
  }
  
  private List<String> providerClassNames;
  
}
