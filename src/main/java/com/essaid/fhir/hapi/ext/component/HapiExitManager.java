package com.essaid.fhir.hapi.ext.component;

import org.springframework.boot.ExitCodeEvent;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.stereotype.Component;

@Component
public class HapiExitManager implements ExitCodeGenerator, ApplicationListener<ApplicationEvent> {

  private int finalExitCode = Integer.MIN_VALUE;
  private int hapiExitCode = Integer.MIN_VALUE;
  private boolean contextClosed;
  private ApplicationContext context;

  public HapiExitManager(ApplicationContext context) {
    this.context = context;
  }

  public int getHapiExitCode() {
    return hapiExitCode;
  }

  public void setHapiExitCode(int hapiExitCode) {
    this.hapiExitCode = hapiExitCode;
  }

  @Override
  public int getExitCode() {
    return this.hapiExitCode;
  }

  @Override
  public void onApplicationEvent(ApplicationEvent event) {

    if (event instanceof ExitCodeEvent) {
      this.finalExitCode = ((ExitCodeEvent) event).getExitCode();
      System.out.println("\tReceived ExitCodeEvent with code: " + this.finalExitCode);
    }
    if (event instanceof ContextClosedEvent) {
      System.out.println("\tReceived ContextClosedEvent.");
      this.contextClosed = true;
    }
  }

  public int waitForExit(int checkIntervalSeconds) throws InterruptedException {
    while (true) {
      System.out.println(this.toString());
      Thread.sleep(checkIntervalSeconds * 1000L);
      if (hapiExitCode != Integer.MIN_VALUE) {
        SpringApplication.exit(this.context);
      }
      if (this.contextClosed) {
        return this.finalExitCode == Integer.MIN_VALUE ? 0 : this.finalExitCode;
      }
    }
  }

  @Override
  public String toString() {
    return "HapiExitManager{" +
            "finalExitCode=" + finalExitCode +
            ", hapiExitCode=" + hapiExitCode +
            ", contextClosed=" + contextClosed +
            ", context=" + context +
            '}';
  }
}
