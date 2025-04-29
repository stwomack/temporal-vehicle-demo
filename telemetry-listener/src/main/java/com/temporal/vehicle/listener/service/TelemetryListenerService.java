package com.temporal.vehicle.listener.service;

import com.temporal.vehicle.common.model.VehicleTelemetry;
import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import io.temporal.client.WorkflowStub;
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

        String workflowId = "vehicle-telemetry-" + telemetry.getVin();
        String taskQueue = "vehicle-telemetry-queue";

        try {
            WorkflowStub existingWorkflowStub = workflowClient.newUntypedWorkflowStub(workflowId);
            existingWorkflowStub.signal("updateTelemetry", telemetry);
         log.info("Processing Kafka message for VIN: {}", telemetry.getVin());
        } catch (io.temporal.client.WorkflowNotFoundException ex) {
            WorkflowStub newWorkflowStub = workflowClient.newUntypedWorkflowStub("VehicleTelemetryWorkflow",
                    WorkflowOptions.newBuilder()
                            .setWorkflowId(workflowId)
                            .setTaskQueue(taskQueue)
                            .setWorkflowExecutionTimeout(Duration.ofDays(36000))
                            .build());

            newWorkflowStub.start(telemetry);
            log.info("Started new workflow for VIN: {}", telemetry.getVin());
        }
    }
}