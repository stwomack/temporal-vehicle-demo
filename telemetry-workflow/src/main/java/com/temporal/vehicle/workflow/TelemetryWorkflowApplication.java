package com.temporal.vehicle.workflow;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.temporal.vehicle"})
public class TelemetryWorkflowApplication {
    public static void main(String[] args) {
        SpringApplication.run(TelemetryWorkflowApplication.class, args);
    }
} 