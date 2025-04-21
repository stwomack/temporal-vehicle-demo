package com.temporal.vehicle.listener.service;

import com.temporal.vehicle.common.model.VehicleTelemetry;
import com.temporal.vehicle.common.workflow.VehicleTelemetryWorkflow;
import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import io.temporal.client.WorkflowStub;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

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

        // Create workflow stub
        WorkflowStub untypedWorkflowStub = workflowClient.newUntypedWorkflowStub("VehicleTelemetryWorkflow",
                WorkflowOptions.newBuilder()
                        .setWorkflowId(workflowId)
                        .setTaskQueue(taskQueue)
                        .build());

        untypedWorkflowStub.signalWithStart("updateTelemetry", new Object[] {telemetry}, new Object[] {telemetry});
    }
}