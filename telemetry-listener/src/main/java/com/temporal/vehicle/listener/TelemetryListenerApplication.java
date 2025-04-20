package com.temporal.vehicle.listener;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.temporal.vehicle"})
public class TelemetryListenerApplication {
    public static void main(String[] args) {
        SpringApplication.run(TelemetryListenerApplication.class, args);
    }
} 