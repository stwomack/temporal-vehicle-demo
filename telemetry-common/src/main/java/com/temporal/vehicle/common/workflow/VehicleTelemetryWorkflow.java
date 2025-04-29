package com.temporal.vehicle.common.workflow;

import io.temporal.workflow.*;
import com.temporal.vehicle.common.model.VehicleTelemetry;

@WorkflowInterface
public interface VehicleTelemetryWorkflow {
    @WorkflowMethod
    VehicleTelemetry processTelemetry(VehicleTelemetry telemetry);

    @SignalMethod
    void updateTelemetry(VehicleTelemetry telemetry);

    @SignalMethod
    void vehicleEndOfLife();

    @QueryMethod
    VehicleTelemetry getVehicleTelemetryCurrentState();
} 