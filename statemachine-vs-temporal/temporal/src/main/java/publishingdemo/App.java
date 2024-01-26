package publishingdemo;

import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import io.temporal.serviceclient.WorkflowServiceStubs;
import io.temporal.worker.Worker;
import io.temporal.worker.WorkerFactory;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import publishingdemo.model.Document;

public class App {

  private static final Logger logger = LoggerFactory.getLogger(App.class);

  // Create a List<String> with three URLs of the documents you want to publish
  private static final List<String> documentUrls =
      Arrays.asList(
          "https://learn.temporal.io/getting_started/#set-up-your-development-environment",
          "https://docs.temporal.io/docs/java/hello-world",
          "https://docs.temporal.io/docs/server/production-deployment");

  @SuppressWarnings("CatchAndPrintStackTrace")
  public static void main(String[] args) throws MalformedURLException, InterruptedException {
    String TASK_QUEUE = "PublishingDemo";

    WorkflowServiceStubs service = WorkflowServiceStubs.newLocalServiceStubs();
    // client that can be used to start and signal workflows
    WorkflowClient client = WorkflowClient.newInstance(service);

    // Start the worker and hold onto the WorkerFactory that created it
    WorkerFactory factory = startWorkerWithFactory(client, TASK_QUEUE);

    runPublicationProcess(TASK_QUEUE, client);
    //Put the main thread to sleep for 5 seconds so that the workflow can complete
    shutdownWorker(factory);
  }

  /**
   * @param taskQueue, the task queue to listen on
   * @param client, the workflow client
   * @throws MalformedURLException
   */
  private static void runPublicationProcess( String taskQueue, WorkflowClient client)
      throws MalformedURLException {

    try {
      // Iterate through the list of document URLs and start a workflow for each one
      for (String url : documentUrls) {
        URL docUrl = new URL(url);
        Document document = new Document(docUrl);
        WorkflowOptions options =
            WorkflowOptions.newBuilder()
                .setTaskQueue(taskQueue)
                .setWorkflowId(document.getId().toString())
                .build();
        PublicationWorkflow wf = client.newWorkflowStub(PublicationWorkflow.class, options);
        wf.startWorkflow(document);
      }
    } catch (Exception e) {
      // Just rethrow for now
      throw e;
    }
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
    worker.registerWorkflowImplementationTypes(PublicationWorkflowImpl.class);
    worker.registerActivitiesImplementations(new PublishingActivitiesImpl());

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
    System.exit(0);
  }
}
