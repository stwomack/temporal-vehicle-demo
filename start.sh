echo '{"vin":"CAR12345","speed":60,"fuelLevel":45,"engineTemperature":85,"latitude":37.7749,"longitude":-22.4194,"timestamp":"2024-03-20T12:00:00Z"}' | kcat -b localhost:9092 -t vehicle-telemetry -P
echo '{"vin":"CAR12345","speed":20,"fuelLevel":55,"engineTemperature":85,"latitude":37.7749,"longitude":-2.4194,"timestamp":"2024-03-20T12:00:00Z"}' | kcat -b localhost:9092 -t vehicle-telemetry -P
echo '{"vin":"CAR12345","speed":50,"fuelLevel":56,"engineTemperature":85,"latitude":37.7749,"longitude":-1.9494,"timestamp":"2024-03-20T12:00:00Z"}' | kcat -b localhost:9092 -t vehicle-telemetry -P
echo '{"vin":"CAR12345","speed":7,"fuelLevel":15,"engineTemperature":85,"latitude":37.7749,"longitude":-922.4194,"timestamp":"2024-03-20T12:00:00Z"}' | kcat -b localhost:9092 -t vehicle-telemetry -P
echo '{"vin":"CAR12345","speed":60,"fuelLevel":45,"engineTemperature":85,"latitude":37.7749,"longitude":-22.4194,"timestamp":"2024-03-20T12:00:00Z"}' | kcat -b localhost:9092 -t vehicle-telemetry -P
