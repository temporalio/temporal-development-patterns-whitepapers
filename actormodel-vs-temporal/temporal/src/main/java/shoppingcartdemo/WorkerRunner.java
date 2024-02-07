package shoppingcartdemo;

import io.temporal.client.WorkflowClient;
import io.temporal.serviceclient.WorkflowServiceStubs;
import io.temporal.worker.Worker;
import io.temporal.worker.WorkerFactory;
import java.lang.management.ManagementFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import shoppingcartdemo.temporal.ShoppingCartActivitiesImpl;
import shoppingcartdemo.temporal.ShoppingCartWorkflowImpl;

public class WorkerRunner {
  private static final Logger logger = LoggerFactory.getLogger(WorkerRunner.class);

  public static void main(String[] args) {

    String hostSpecificTaskQueue = ManagementFactory.getRuntimeMXBean().getName();

    // gRPC stubs wrapper that talks to the local docker instance of temporal service.
    WorkflowServiceStubs service = WorkflowServiceStubs.newLocalServiceStubs();
    // client that can be used to start and signal workflows
    WorkflowClient client = WorkflowClient.newInstance(service);

    // worker factory that can be used to create workers for specific task queues
    WorkerFactory factory = WorkerFactory.newInstance(client);

    Worker worker = factory.newWorker(Constants.TASK_QUEUE);

    // Workflows are stateful. So you need a type to create instances.
    worker.registerWorkflowImplementationTypes(ShoppingCartWorkflowImpl.class);
    worker.registerActivitiesImplementations(new ShoppingCartActivitiesImpl());

    // Start the worker created by this factory.
    factory.start();

    logger.info("The worker has started and is listening on task queue: {}.", Constants.TASK_QUEUE);

    // Start all work
    factory.start();
  }
}
