package com.temporal.vehicle.common.activity;

import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;
import com.temporal.vehicle.common.model.VehicleTelemetry;

@ActivityInterface
public interface VehicleTelemetryActivities {
    @ActivityMethod
    VehicleTelemetry validateTelemetry(VehicleTelemetry telemetry);

    @ActivityMethod
    VehicleTelemetry enrichTelemetry(VehicleTelemetry telemetry);

    @ActivityMethod
    void persistTelemetry(VehicleTelemetry telemetry);

    @ActivityMethod
    VehicleTelemetry performFinalProcessing(VehicleTelemetry currentState);
}