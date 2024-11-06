package com.seccreto.infrastructure;

import com.seccreto.infrastructure.configuration.WebServerConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.AbstractEnvironment;

@SpringBootApplication
public class Main {
  public static void main(String[] args) {

    System.setProperty(AbstractEnvironment.DEFAULT_PROFILES_PROPERTY_NAME, "production");
    SpringApplication.run(WebServerConfig.class, args);

  }

}

