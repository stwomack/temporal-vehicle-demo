#!/bin/bash

# Generate unique telemetry data for each entry
for i in {1..100}; do
    # Simulate realistic changing values
    speed=$((RANDOM % 200))
    fuel_level=$((75 - (i / 5)))  # Gradually decrease fuel
    engine_temp=$((80 + (RANDOM % 20)))  # 80-99 range
    
    # Simulate route movement (rough circle around SF)
    lat_offset=$(echo "scale=6; ($i * 0.001) * c($i * 0.1)" | bc -l)
    lon_offset=$(echo "scale=6; ($i * 0.001) * s($i * 0.1)" | bc -l)
    latitude=$(echo "37.7749 + $lat_offset" | bc -l)
    longitude=$(echo "-122.4194 + $lon_offset" | bc -l)
    
    # Generate timestamp incrementing by minute
    timestamp=$(date -Iseconds | sed 's/+00:00/Z/')
    
    echo "{\"vin\":\"CAR12345\",\"speed\":$speed,\"fuelLevel\":$fuel_level,\"engineTemperature\":$engine_temp,\"latitude\":$latitude,\"longitude\":$longitude,\"timestamp\":\"$timestamp\"}" | kcat -b localhost:9092 -t vehicle-telemetry -P
    
    # Small delay between messages
    sleep 0.1
done

echo "Sent 100 unique telemetry data points"