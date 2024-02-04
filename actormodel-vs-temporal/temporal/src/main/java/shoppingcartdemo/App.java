package shoppingcartdemo;

import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import io.temporal.serviceclient.WorkflowServiceStubs;
import io.temporal.worker.Worker;
import io.temporal.worker.WorkerFactory;
import java.net.MalformedURLException;
import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import shoppingcartdemo.model.Customer;
import shoppingcartdemo.model.PurchaseItem;
import shoppingcartdemo.utils.Mocks;

public class App {

  private static final Logger logger = LoggerFactory.getLogger(App.class);

  @SuppressWarnings("CatchAndPrintStackTrace")
  public static void main(String[] args) throws MalformedURLException, InterruptedException {
    String TASK_QUEUE = "ShoppingCartDemo";

    WorkflowServiceStubs service = WorkflowServiceStubs.newLocalServiceStubs();
    // client that can be used to start and signal workflows
    WorkflowClient client = WorkflowClient.newInstance(service);

    // Start the worker and hold onto the WorkerFactory that created it
    WorkerFactory factory = startWorkerWithFactory(client, TASK_QUEUE);

    runShoppingCartProcess(TASK_QUEUE, client, factory);
    // Put the main thread to sleep for 5 seconds so that the workflow can complete
    // shutdownWorker(factory);
  }

  /**
   * @param taskQueue, the task queue to listen on
   * @param client, the workflow client
   * @throws MalformedURLException
   */
  private static void runShoppingCartProcess(
      String taskQueue, WorkflowClient client, WorkerFactory factory) throws MalformedURLException {

    WorkflowOptions options =
        WorkflowOptions.newBuilder()
            .setTaskQueue(taskQueue)
            .setWorkflowId(UUID.randomUUID().toString())
            .build();
    ShoppingCartWorkflow wf = client.newWorkflowStub(ShoppingCartWorkflow.class, options);
    List<PurchaseItem> purchaseItems = Mocks.getRandomPurchaseItems();
    Customer customer = purchaseItems.get(0).getCustomer();

    WorkflowClient.start(wf::startWorkflow);

    wf.addItems(purchaseItems);
    wf.checkout(Mocks.getRandomCheckoutInfo(customer, Mocks.getRandomCreditCard(customer)));

    while (!wf.isCompleted()) {
      try {
        Thread.sleep(500); // Wait for 500 milliseconds
      } catch (InterruptedException e) {
        logger.info(e.getMessage());
      }
    }
    shutdownWorker(factory);
  }

  /**
   * @param client, the workflow client
   * @param taskQueue, the task queue to listen on
   * @return, the WorkerFactory that created the worker
   */
  private static WorkerFactory startWorkerWithFactory(WorkflowClient client, String taskQueue) {
    // worker factory that can be used to create workers for specific task queues
    WorkerFactory factory = WorkerFactory.newInstance(client);

    // Worker that listens on a task queue and hosts both workflow and activity
    // implementations.
    Worker worker = factory.newWorker(taskQueue);

    // Workflows are stateful. So you need a type to create instances.
    worker.registerWorkflowImplementationTypes(ShoppingCartWorkflowImpl.class);
    worker.registerActivitiesImplementations(new ShoppingCartActivitiesImpl());

    // Start the worker created by this factory.
    factory.start();

    logger.info("The worker has started and is listening on task queue: {}.", taskQueue);

    return factory;
  }

  /**
   * @param factory, the WorkerFactory that created the worker
   */
  private static void shutdownWorker(WorkerFactory factory) {
    factory.shutdown();
    logger.info("The worker has been shutdown. That's all folks!");
  }
}
