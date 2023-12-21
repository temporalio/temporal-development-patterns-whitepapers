package publishingdemo;

import io.temporal.activity.ActivityOptions;
import io.temporal.common.RetryOptions;
import io.temporal.failure.ActivityFailure;
import io.temporal.workflow.Async;
import io.temporal.workflow.Promise;
import io.temporal.workflow.Workflow;
import io.temporal.workflow.WorkflowQueue;
import java.time.Duration;
import org.slf4j.Logger;
import publishingdemo.model.Document;
import io.temporal.workflow.Saga;

/**
 * PublicationWorkflow implementation that demonstrates the use of Temporal's Saga feature.
 *
 * The publish() activity within the startWorkflow(Document document) applies a compensation
 * to each activity executed. The only activity that fails. However,
 * that method is wrapped in a Saga that will compensate the activity that failed. The compensation
 * has corrective behavior for the method, publish(), that failed.
 */
public class PublicationWorkflowImpl implements PublicationWorkflow {
  private final PublishingActivities activities;
  private static final Logger logger = Workflow.getLogger(PublicationWorkflowImpl.class);
  private final WorkflowQueue<Runnable> queue = Workflow.newWorkflowQueue(1024);
  private boolean exit = false;
  private boolean copyEditComplete = false;
  private boolean graphicEditComplete = false;
  private boolean publishingComplete = false;

  public PublicationWorkflowImpl() {
    ActivityOptions options =
        ActivityOptions.newBuilder()
            .setScheduleToCloseTimeout(Duration.ofSeconds(10))
            .setTaskQueue("PublishingDemo")
                .setRetryOptions(
                        RetryOptions.newBuilder()
                                .setInitialInterval(Duration.ofSeconds(1))
                                .setMaximumAttempts(1)
                                .build())
            .build();
    this.activities = Workflow.newActivityStub(PublishingActivities.class, options);
  }

  /**
   * This method starts the workflow. Notice that the copyEdit, graphicEdit and publish activities
   * are wrapped in a Temporal Saga. The Saga class provides the mechanism by which compensation behavior is
   * invoked. Take a look at method, PublishingActivitiesImpl.compensate(String activityName, Document document)
   * to see how the general compensation behavior is implemented.
   * @param document, the Document to be processed
   */
  @Override
  public void startWorkflow(Document document) {
    Saga saga = new Saga(new Saga.Options.Builder().setParallelCompensation(false).build());
    logger.info("Starting Workflow for Publishing");
    try {
      Promise<Void> copyEditPromise = Async.procedure(activities::copyEdit, document);
      Promise<Void> grppicEditPromise = Async.procedure(activities::graphicEdit, document);

      saga.addCompensation(activities::compensate, "copyEdit", document);
      copyEditPromise.get();

      saga.addCompensation(activities::compensate, "graphicEdit", document);
      grppicEditPromise.get();

      if (copyEditPromise.isCompleted()) {
        logger.info("Copy edit complete");
        copyEditComplete = true;
      }
      if (grppicEditPromise.isCompleted()) {
        logger.info("Graphic edit complete");
        graphicEditComplete = true;
      }

      Workflow.await(() -> graphicEditComplete && copyEditComplete);
      Promise<Void> publishPromise = Async.procedure(activities::publish, document);
      saga.addCompensation(activities::compensate, "publish", document);
      publishPromise.get();

      if (publishPromise.isCompleted()) {
        logger.info("Publishing complete");
        publishingComplete = true;
      }
      Workflow.await(() -> copyEditComplete && graphicEditComplete && publishingComplete);
    } catch (ActivityFailure e) {
      saga.compensate();
      throw e;
    }
  }
}
