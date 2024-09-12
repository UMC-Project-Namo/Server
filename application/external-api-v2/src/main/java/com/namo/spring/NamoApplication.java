package com.namo.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class NamoApplication {

    public static void main(String[] args) {
        SpringApplication.run(NamoApplication.class, args);
    }

}
