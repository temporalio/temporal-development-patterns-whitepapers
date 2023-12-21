package publishingdemo;

import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import io.temporal.common.RetryOptions;
import io.temporal.serviceclient.WorkflowServiceStubs;
import io.temporal.worker.Worker;
import io.temporal.worker.WorkerFactory;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.Scanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import publishingdemo.model.Document;

public class App {

  private static final Logger logger = LoggerFactory.getLogger(App.class);

  @SuppressWarnings("CatchAndPrintStackTrace")
  public static void main(String[] args) throws MalformedURLException, InterruptedException {

    String defaultTaskQueue = "PublishingDemo";
    String TASK_QUEUE = defaultTaskQueue;
    Scanner scanner = new Scanner(System.in);
    String strTaskQueue;

    System.out.println("Enter the TASK QUEUE name: ");
    strTaskQueue = scanner.nextLine().trim();
    if (strTaskQueue.isEmpty())
      System.out.println(
          "You did not enter a value for TASK QUEUE to we'll use the default value: "
              + defaultTaskQueue);
    strTaskQueue = strTaskQueue.isEmpty() ? defaultTaskQueue : strTaskQueue;

    TASK_QUEUE = strTaskQueue;

    WorkflowServiceStubs service = WorkflowServiceStubs.newLocalServiceStubs();
    // client that can be used to start and signal workflows
    WorkflowClient client = WorkflowClient.newInstance(service);

    // Start the worker and hold onto the WorkerFactory for later use, if necessary.
    WorkerFactory factory = startWorkerWithFactory(client, TASK_QUEUE);
    // Just keep running the publication process until the user enters "exit"
    while (true) {
      System.out.println("Enter 'exit' to exit or any other key to add a new Document URL: ");
      String strExit = scanner.nextLine().trim();
      if (strExit.equalsIgnoreCase("exit")) {
        factory.shutdown();
        logger.info("The worker has been shutdown. That's all folks!");
        System.exit(0);
      }
      runPublicationProcess(scanner, TASK_QUEUE, client);
    }
  }

  private static void runPublicationProcess(
      Scanner scanner, String taskQueue, WorkflowClient client) throws MalformedURLException {
    String strDocumentUrl;
    String defaultUrl =
        "https://learn.temporal.io/getting_started/#set-up-your-development-environment";
    System.out.println("Enter Document URL: ");
    strDocumentUrl = scanner.nextLine().trim();
    if (strDocumentUrl.isEmpty())
      System.out.println(
          "You did not enter a value for Document URL so we'll use the default value: "
              + defaultUrl);
    strDocumentUrl = strDocumentUrl.isEmpty() ? defaultUrl : strDocumentUrl;

    try {
      URL url = new URL(strDocumentUrl);
      Document document = new Document(url);
      WorkflowOptions options =
          WorkflowOptions.newBuilder()
              .setTaskQueue(taskQueue)
              .setWorkflowId(document.getId().toString())
              // set the retry options
              .setRetryOptions(
                  RetryOptions.newBuilder()
                      .setInitialInterval(Duration.ofSeconds(1))
                      .setMaximumInterval(Duration.ofSeconds(10))
                      .build())
              .build();

      PublicationWorkflow wf = client.newWorkflowStub(PublicationWorkflow.class, options);
      WorkflowClient.start(wf::startWorkflow, document);
    } catch (Exception ex) {
      // Just rethrow for now
      throw ex;
    }
  }

  /**
   * @param client, the workflow client
   * @return, the WorkerFactory that created the worker
   */
  private static WorkerFactory startWorkerWithFactory(WorkflowClient client, String taskQueue) {
    // worker factory that can be used to create workers for specific task queues
    WorkerFactory factory = WorkerFactory.newInstance(client);

    // Worker that listens on a task queue and hosts both workflow and activity
    // implementations.
    Worker worker = factory.newWorker(taskQueue);

    // Workflows are stateful. So you need a type to create instances.
    worker.registerWorkflowImplementationTypes(PublicationWorkflowImpl.class);
    worker.registerActivitiesImplementations(new PublishingActivitiesImpl());

    // Start the worker created by this factory.
    factory.start();

    logger.info("The worker has started and is listening on task queue: {}.", taskQueue);

    return factory;
  }
}
