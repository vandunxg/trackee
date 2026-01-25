/* Copyright (c) 2026 Trackee */
package com.trackee;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

/**
 * @author vandunxg
 */
@SpringBootApplication
@ConfigurationPropertiesScan
public class TrackeeApplication {

    public static void main(String[] args) {
        SpringApplication.run(TrackeeApplication.class, args);
    }
}
