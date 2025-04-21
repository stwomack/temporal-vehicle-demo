package com.temporal.vehicle.workflow.impl;

import com.temporal.vehicle.common.activity.VehicleTelemetryActivities;
import com.temporal.vehicle.common.model.VehicleTelemetry;
import com.temporal.vehicle.common.workflow.VehicleTelemetryWorkflow;
import io.temporal.activity.ActivityOptions;
import io.temporal.workflow.Workflow;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;

@Slf4j
public class VehicleTelemetryWorkflowImpl implements VehicleTelemetryWorkflow {

    private final VehicleTelemetryActivities activities;
    private VehicleTelemetry currentState;

    public VehicleTelemetryWorkflowImpl() {
        this.activities = Workflow.newActivityStub(
                VehicleTelemetryActivities.class,
                ActivityOptions.newBuilder()
                        .setStartToCloseTimeout(Duration.ofSeconds(10))
                        .build());
    }

    @Override
    public void processTelemetry(VehicleTelemetry telemetry) {
        log.info("Processing telemetry for VIN: {}", telemetry.getVin());
        
        // Validate telemetry
        currentState = activities.validateTelemetry(telemetry);
        currentState = activities.enrichTelemetry(currentState);
//        Workflow.sleep(Duration.ofSeconds(1)); // YOLO
        activities.persistTelemetry(currentState);
//        currentState = activities.persistTelemetry(currentState); //TODO Implement this
        log.info("Completed processing telemetry for VIN: {}", currentState.getVin());
    }

    @Override
    public void updateTelemetry(VehicleTelemetry telemetry) {
        log.info("Updating telemetry for VIN: {}", telemetry.getVin());
        currentState = telemetry;
    }

    @Override
    public VehicleTelemetry getCurrentState() {
        return currentState;
    }
} 