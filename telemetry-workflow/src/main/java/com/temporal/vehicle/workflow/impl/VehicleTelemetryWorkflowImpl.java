package com.temporal.vehicle.workflow.impl;

import com.temporal.vehicle.common.activity.VehicleTelemetryActivities;
import com.temporal.vehicle.common.model.VehicleTelemetry;
import com.temporal.vehicle.common.workflow.VehicleTelemetryWorkflow;
import io.temporal.activity.ActivityOptions;
import io.temporal.workflow.Workflow;
import io.temporal.workflow.WorkflowInfo;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.util.ArrayDeque;
import java.util.Queue;

@Slf4j
public class VehicleTelemetryWorkflowImpl implements VehicleTelemetryWorkflow {
    int totalSignalCount = 0;
    private boolean isEndOfLife = false;
    private final VehicleTelemetryActivities activities;
    private VehicleTelemetry vehicleTelemetryCurrentState;
    private final Queue<VehicleTelemetry> vehicleTelemetrySignals;
    private WorkflowInfo info;
    private final long workflowStartTime;
    private static final long CONTINUE_AS_NEW_INTERVAL_MILLIS = Duration.ofHours(24).toMillis();

    public VehicleTelemetryWorkflowImpl() {
        this.activities = Workflow.newActivityStub(
                VehicleTelemetryActivities.class,
                ActivityOptions.newBuilder()
                        .setStartToCloseTimeout(Duration.ofSeconds(10))
                        .build());
        this.vehicleTelemetrySignals = new ArrayDeque<>();
        this.workflowStartTime = Workflow.currentTimeMillis();
    }

    @Override
    public VehicleTelemetry processTelemetry(VehicleTelemetry telemetry) {
        log.info("Processing telemetry for VIN: {}", telemetry.getVin());
        info = Workflow.getInfo();

        // Check if Temporal suggests continuing as new
        if (info.isContinueAsNewSuggested()) {
            log.info("Temporal suggests continuing as new for VIN: {}", telemetry.getVin());
            return continueWorkflowAsNew();
        }

        vehicleTelemetryCurrentState = activities.validateTelemetry(telemetry);
        vehicleTelemetryCurrentState = activities.enrichTelemetry(vehicleTelemetryCurrentState);
        activities.persistTelemetry(vehicleTelemetryCurrentState);
        log.info("Completed processing initial telemetry for VIN: {}", vehicleTelemetryCurrentState.getVin());

        while (!isEndOfLife) {
            // Wait for either: 1) a signal arrives, 2) workflow should end, or 3) 24 hours passed
            Workflow.await(() ->
                    isEndOfLife ||
                            !vehicleTelemetrySignals.isEmpty() ||
                            shouldContinueAsNew()
            );

            if (shouldContinueAsNew()) {
                log.info("24 hours elapsed, continuing workflow as new for VIN: {}",
                        vehicleTelemetryCurrentState.getVin());
                return continueWorkflowAsNew();
            }

            if (!isEndOfLife && !vehicleTelemetrySignals.isEmpty()) {
                VehicleTelemetry signalTelemetry = vehicleTelemetrySignals.poll();
                vehicleTelemetryCurrentState = activities.validateTelemetry(signalTelemetry);
                vehicleTelemetryCurrentState = activities.enrichTelemetry(vehicleTelemetryCurrentState);
                activities.persistTelemetry(vehicleTelemetryCurrentState);
                log.info("Completed processing signal telemetry for VIN: {}", vehicleTelemetryCurrentState.getVin());
            }
        }

        return activities.performFinalProcessing(vehicleTelemetryCurrentState);
    }

    private boolean shouldContinueAsNew() {
        return (Workflow.currentTimeMillis() - workflowStartTime) >= CONTINUE_AS_NEW_INTERVAL_MILLIS;
    }

    private VehicleTelemetry continueWorkflowAsNew() {
        log.info("Continuing workflow as new with current state for VIN: {}",
                vehicleTelemetryCurrentState != null ? vehicleTelemetryCurrentState.getVin() : "unknown");
        Workflow.continueAsNew(vehicleTelemetryCurrentState);
        return vehicleTelemetryCurrentState;
    }

    @Override
    public void updateTelemetry(VehicleTelemetry telemetry) {
        log.info("Updating telemetry for VIN: {}", telemetry.getVin());
        vehicleTelemetrySignals.add(telemetry);
        totalSignalCount++;
        log.info("Signals received so far: {} ", totalSignalCount);
    }

    @Override
    public void vehicleEndOfLife() {
        this.isEndOfLife = true;
    }

    @Override
    public VehicleTelemetry getVehicleTelemetryCurrentState() {
        return vehicleTelemetryCurrentState;
    }
}