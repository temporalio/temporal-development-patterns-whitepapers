package shoppingcartdemo;

import io.temporal.client.WorkflowClient;
import io.temporal.serviceclient.WorkflowServiceStubs;
import io.temporal.worker.Worker;
import io.temporal.worker.WorkerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import shoppingcartdemo.temporal.backorder.BackorderActivitiesImpl;
import shoppingcartdemo.temporal.backorder.BackorderWorkflowImpl;
import shoppingcartdemo.temporal.shoppingcart.ShoppingCartActivitiesImpl;
import shoppingcartdemo.temporal.shoppingcart.ShoppingCartWorkflowImpl;

public class WorkerRunner {
  private static final Logger logger = LoggerFactory.getLogger(WorkerRunner.class);

  public static void main(String[] args) {
    // gRPC stubs wrapper that talks to the local docker instance of temporal service.
    WorkflowServiceStubs service = WorkflowServiceStubs.newLocalServiceStubs();
    // client that can be used to start and signal workflows
    WorkflowClient client = WorkflowClient.newInstance(service);
    // worker factory that can be used to create workers for specific task queues
    WorkerFactory factory = WorkerFactory.newInstance(client);

    Worker shoppingcartWorker = factory.newWorker(Constants.SHOPPING_CART_TASK_QUEUE);
    Worker backorderWorker = factory.newWorker(Constants.BACKORDER_TASK_QUEUE);

    shoppingcartWorker.registerWorkflowImplementationTypes(ShoppingCartWorkflowImpl.class);
    shoppingcartWorker.registerActivitiesImplementations(new ShoppingCartActivitiesImpl());

    logger.info(
        "The Shopping Cart Worker has started and is listening on task queue: {}.",
        Constants.SHOPPING_CART_TASK_QUEUE);

    backorderWorker.registerWorkflowImplementationTypes(BackorderWorkflowImpl.class);
    backorderWorker.registerActivitiesImplementations(new BackorderActivitiesImpl());

    logger.info(
        "The Backorder Worker has started and is listening on task queue: {}.",
        Constants.BACKORDER_TASK_QUEUE);

    // Start the workers created by this factory.
    factory.start();
  }
}
