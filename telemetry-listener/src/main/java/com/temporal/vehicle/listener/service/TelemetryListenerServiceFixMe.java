package com.temporal.vehicle.listener.service;

import com.temporal.vehicle.common.model.VehicleTelemetry;
import com.temporal.vehicle.common.workflow.VehicleTelemetryWorkflow;
import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import io.temporal.client.WorkflowStub;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TelemetryListenerServiceFixMe {
//    private final WorkflowClient workflowClient;
//
//    @Autowired
//    public TelemetryListenerServiceFixMe(WorkflowClient workflowClient) {
//        this.workflowClient = workflowClient;
//    }
//
//    @KafkaListener(topics = "${kafka.topic.vehicle-telemetry}", groupId = "${spring.kafka.consumer.group-id}")
//    public void processTelemetry(VehicleTelemetry telemetry) {
//        String workflowId = "Vehicle-" + telemetry.getVin();
//         log.info("Processing Kafka message for VIN: {}", telemetry.getVin());
//
//        try {
//            WorkflowOptions workflowOptions = WorkflowOptions.newBuilder()
//                    .setTaskQueue("VehicleTelemetryTaskQueue")
//                    .setWorkflowId(workflowId)
//                    .build();
//
//            WorkflowStub untyped = workflowClient.newUntypedWorkflowStub(
//                    VehicleTelemetryWorkflow.class.getName(),
//                    workflowOptions
//            );
//
//            untyped.signalWithStart(
//                    "updateTelemetry",
//                    new Object[]{telemetry},
//                    new Object[]{telemetry}
//            );
//
//            log.info("Successfully processed telemetry update for VIN: {}", telemetry.getVin());
//        } catch (Exception e) {
//            log.info("Started new workflow for VIN: {}", telemetry.getVin());
//        }
//    }
}