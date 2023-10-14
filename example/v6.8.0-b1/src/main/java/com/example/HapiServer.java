package com.example;

import ca.uhn.fhir.jpa.starter.Application;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Import;

import javax.persistence.EntityManagerFactory;

@SpringBootApplication
@Import({Application.class})
public class HapiServer {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(HapiServer.class, args);
        EntityManagerFactory entityManagerFactory = context.getBean(EntityManagerFactory.class);

        System.out.println(entityManagerFactory);

    }
}
