# Temporal Vehicle Telemetry Demo

This project demonstrates a vehicle telemetry system using Temporal for workflow orchestration, Kafka for event streaming, and MongoDB for data storage.

## Prerequisites

- Java 17 or higher
- Maven
- MongoDB (via Homebrew)
- Kafka (via Homebrew)
- Temporal CLI

## Running the Services

### 1. Start MongoDB
```bash
brew services start mongodb-community
```

### 2. Start Kafka
```bash
# Start Zookeeper
brew services start zookeeper

# Start Kafka Server
brew services start kafka
```

### 3. Start Temporal Server
```bash
temporal server start-dev
```

### 4. Build and Run the Applications

#### Telemetry Workflow Service
```bash
cd telemetry-workflow
mvn spring-boot:run
```

#### Telemetry Listener Service
```bash
cd telemetry-listener
mvn spring-boot:run
```

## Architecture

The system consists of two main components:

1. **Telemetry Workflow Service** (`telemetry-workflow`)
   - Handles the core workflow logic
   - Manages vehicle telemetry data processing
   - Runs on port 8082

2. **Telemetry Listener Service** (`telemetry-listener`)
   - Listens for telemetry events
   - Processes and forwards data to the workflow service
   - Runs on port 8081

## Configuration

### Telemetry Workflow Service
- Server port: 8082
- Temporal server: localhost:7233
- MongoDB: localhost:27017

### Telemetry Listener Service
- Server port: 8081
- Kafka: localhost:9092
- Temporal server: localhost:7233

## API Endpoints

### Telemetry Workflow Service
- POST `/api/telemetry/start` - Start a new telemetry workflow
- GET `/api/telemetry/{workflowId}` - Get workflow status
- POST `/api/telemetry/{workflowId}/stop` - Stop a workflow

### Telemetry Listener Service
- POST `/api/telemetry/events` - Send telemetry events
- GET `/api/telemetry/health` - Health check endpoint

## Development

### Building
```bash
mvn clean install
```

### Running Tests
```bash
mvn test
```

## System Components

1. **telemetry-listener**: A Spring Boot application that:
   - Listens to Kafka topics for vehicle telemetry data
   - Uses `signalWithStart` to start or join existing Temporal workflows
   - Runs on port 8080

2. **telemetry-workflow**: A Spring Boot application that:
   - Implements the Temporal workflow and activities
   - Processes telemetry data through validation, enrichment, and persistence
   - Runs on port 8081

3. **telemetry-common**: A shared module containing:
   - Common models (`VehicleTelemetry`)
   - Workflow and activity interfaces
   - Shared dependencies

## System Architecture

The system follows this flow:

1. Vehicle telemetry data is published to a Kafka topic
2. The telemetry-listener receives the data and:
   - Creates a workflow ID based on the VIN
   - Uses `signalWithStart` to either start a new workflow or join an existing one
3. The workflow processes the data through activities:
   - `validateTelemetry`: Checks data validity
   - `enrichTelemetry`: Adds additional information
   - `persistTelemetry`: Saves to MongoDB

## Testing the System

### Sending Test Data

You can test the system by sending telemetry data to the Kafka topic. Here are a few ways to do this:

1. Using kafkacat:
```bash
echo '{"vin":"ABC123","speed":80,"fuelLevel":75,"engineTemperature":85,"latitude":37.7749,"longitude":-122.4194,"timestamp":"2024-03-20T12:00:00Z"}' | kafkacat -b localhost:9092 -t vehicle-telemetry -P
```

2. Using curl (if you have a REST endpoint):
```bash
curl -X POST http://localhost:8080/api/telemetry \
  -H "Content-Type: application/json" \
  -d '{"vin":"ABC123","speed":80,"fuelLevel":75,"engineTemperature":85,"latitude":37.7749,"longitude":-122.4194,"timestamp":"2024-03-20T12:00:00Z"}'
```

### Verifying the Results

1. Check MongoDB for persisted data:
```bash
mongosh
use vehicle_telemetry
db.vehicle_telemetry.find()
```

2. Check Temporal Web UI at http://localhost:8233

## Troubleshooting

1. **Kafka Connection Issues**:
   - Verify Kafka is running
   - Check Kafka logs

2. **Temporal Connection Issues**:
   - Verify Temporal server is running
   - Check Temporal logs

3. **MongoDB Connection Issues**:
   - Verify MongoDB is running
   - Check MongoDB logs