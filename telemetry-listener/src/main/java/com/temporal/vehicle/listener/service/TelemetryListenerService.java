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
        log.info("Processing Kafka message for VIN: {}", telemetry.getVin());

        String workflowId = "vehicle-telemetry-" + telemetry.getVin();
        String taskQueue = "vehicle-telemetry-queue";

        WorkflowStub workflowStub = workflowClient.newUntypedWorkflowStub("VehicleTelemetryWorkflow",
                WorkflowOptions.newBuilder()
                        .setWorkflowId(workflowId)
                        .setTaskQueue(taskQueue)
                        .setWorkflowExecutionTimeout(Duration.ofDays(36000))
                        .build());

        workflowStub.signalWithStart("updateTelemetry", new Object[]{telemetry}, new Object[]{telemetry});
        log.info("SignalWithStart executed for VIN: {} - will start new workflow if needed or signal existing one", telemetry.getVin());
    }
}