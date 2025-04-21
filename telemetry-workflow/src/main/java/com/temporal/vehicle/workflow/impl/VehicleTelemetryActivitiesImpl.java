package com.temporal.vehicle.workflow.impl;

import com.temporal.vehicle.common.activity.VehicleTelemetryActivities;
import com.temporal.vehicle.common.model.VehicleTelemetry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class VehicleTelemetryActivitiesImpl implements VehicleTelemetryActivities {

    private final MongoTemplate mongoTemplate;

    @Override
    public VehicleTelemetry validateTelemetry(VehicleTelemetry telemetry) {
        log.info("Validating telemetry for VIN: {}", telemetry.getVin());
        
        // Simple validation logic
        if (telemetry.getSpeed() < 0) {
            telemetry.setStatus("INVALID_SPEED");
        } else if (telemetry.getFuelLevel() < 0 || telemetry.getFuelLevel() > 100) {
            telemetry.setStatus("INVALID_FUEL_LEVEL");
        } else if (telemetry.getEngineTemperature() < 0 || telemetry.getEngineTemperature() > 150) {
            telemetry.setStatus("INVALID_ENGINE_TEMPERATURE");
        } else {
            telemetry.setStatus("VALID");
        }
        
        return telemetry;
    }

    @Override
    public VehicleTelemetry enrichTelemetry(VehicleTelemetry telemetry) {
        log.info("Enriching telemetry for VIN: {}", telemetry.getVin());
        
        // Simple enrichment logic
        if (telemetry.getSpeed() > 100) {
            telemetry.setStatus("HIGH_SPEED");
        } else if (telemetry.getFuelLevel() < 20) {
            telemetry.setStatus("LOW_FUEL");
        } else if (telemetry.getEngineTemperature() > 100) {
            telemetry.setStatus("HIGH_TEMPERATURE");
        }
        
        return telemetry;
    }

    @Override
    public void persistTelemetry(VehicleTelemetry telemetry) {
        log.info("Persisting telemetry for VIN: {}, speed: {}", telemetry.getVin(), telemetry.getSpeed());
        mongoTemplate.save(telemetry);
    }
} 