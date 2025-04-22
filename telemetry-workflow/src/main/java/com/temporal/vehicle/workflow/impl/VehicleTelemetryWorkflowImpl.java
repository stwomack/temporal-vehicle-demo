package com.temporal.vehicle.workflow.impl;

import com.temporal.vehicle.common.activity.VehicleTelemetryActivities;
import com.temporal.vehicle.common.model.VehicleTelemetry;
import com.temporal.vehicle.common.workflow.VehicleTelemetryWorkflow;
import io.temporal.activity.ActivityOptions;
import io.temporal.workflow.Workflow;
import io.temporal.workflow.WorkflowInfo;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;

@Slf4j
public class VehicleTelemetryWorkflowImpl implements VehicleTelemetryWorkflow {
    private boolean isEndOfLife = false;
    private final VehicleTelemetryActivities activities;
    private VehicleTelemetry currentState;
    private WorkflowInfo info;

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
        info = Workflow.getInfo();

        while (!info.isContinueAsNewSuggested()) {
            currentState = activities.validateTelemetry(telemetry);
            currentState = activities.enrichTelemetry(currentState);
            activities.persistTelemetry(currentState);
            log.info("Completed processing telemetry input for VIN: {}", currentState.getVin());
            Workflow.await(() -> isEndOfLife);
            activities.performFinalProcessing(currentState);
        }

        Workflow.continueAsNew(telemetry);
    }

    @Override
    public void updateTelemetry(VehicleTelemetry telemetry) {
        log.info("Updating telemetry for VIN: {}", telemetry.getVin());
        currentState = activities.validateTelemetry(telemetry);
        currentState = activities.enrichTelemetry(currentState);
        activities.persistTelemetry(currentState);
        log.info("Telemetry Updated for VIN: {}", telemetry.getVin());
    }

    @Override
    public void vehicleEndOfLife() {
        this.isEndOfLife = true;
    }

    @Override
    public VehicleTelemetry getCurrentState() {
        return currentState;
    }
} 