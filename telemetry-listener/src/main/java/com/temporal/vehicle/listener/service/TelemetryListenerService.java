package com.temporal.vehicle.listener.service;

import com.temporal.vehicle.common.model.VehicleTelemetry;
import com.temporal.vehicle.common.workflow.VehicleTelemetryWorkflow;
import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
@Slf4j
public class TelemetryListenerService {

    private final WorkflowClient workflowClient;

    @KafkaListener(topics = "${kafka.topic.vehicle-telemetry}", groupId = "${spring.kafka.consumer.group-id}")
    public void listen(VehicleTelemetry telemetry) {
        log.info("Received telemetry for VIN: {}", telemetry.getVin());
        
        // Create workflow options
        WorkflowOptions options = WorkflowOptions.newBuilder()
                .setTaskQueue("vehicle-telemetry-queue")
                .setWorkflowId("vehicle-telemetry-" + telemetry.getVin())
                .setWorkflowExecutionTimeout(Duration.ofHours(1))
                .build();

        // Get or start workflow
        VehicleTelemetryWorkflow workflow = workflowClient.newWorkflowStub(
                VehicleTelemetryWorkflow.class, options);

        // Start workflow execution
        workflow.processTelemetry(telemetry);
    }
} 