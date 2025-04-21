package com.temporal.vehicle.common.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Data
@Getter
@Setter
@Document(collection = "vehicle_telemetry")
public class VehicleTelemetry {
    @Id
    private String vin;
    private double speed;
    private double fuelLevel;
    private double engineTemperature;
    private double latitude;
    private double longitude;
    private Instant timestamp;
    private boolean processed;
    private String status;
} 