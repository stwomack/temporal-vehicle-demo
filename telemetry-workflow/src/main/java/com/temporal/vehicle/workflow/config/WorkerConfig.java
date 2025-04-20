package com.temporal.vehicle.workflow.config;

import com.temporal.vehicle.common.activity.VehicleTelemetryActivities;
import com.temporal.vehicle.common.workflow.VehicleTelemetryWorkflow;
import com.temporal.vehicle.workflow.impl.VehicleTelemetryWorkflowImpl;
import io.temporal.client.WorkflowClient;
import io.temporal.worker.Worker;
import io.temporal.worker.WorkerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WorkerConfig {

    @Bean
    public WorkerFactory workerFactory(WorkflowClient workflowClient) {
        return WorkerFactory.newInstance(workflowClient);
    }

    @Bean
    public Worker worker(WorkerFactory workerFactory, VehicleTelemetryActivities activities) {
        Worker worker = workerFactory.newWorker("vehicle-telemetry-queue");
        worker.registerWorkflowImplementationTypes(VehicleTelemetryWorkflowImpl.class);
        worker.registerActivitiesImplementations(activities);
        workerFactory.start();
        return worker;
    }
} 