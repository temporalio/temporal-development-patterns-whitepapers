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

/**
 * This class implements the PublicationWorkflow interface. It calls the copyEdit(), graphicEdit()
 * and publish() activities
 */
public class PublicationWorkflowImpl implements PublicationWorkflow {
  private final PublishingActivities activities;
  private static final Logger logger = Workflow.getLogger(PublicationWorkflowImpl.class);
  private final WorkflowQueue<Runnable> queue = Workflow.newWorkflowQueue(1024);
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
   * This method starts the workflow. It calls the copyEdit(), graphicEdit() and publish()
   * activities
   *
   * @param document, the Document to be processed
   */
  @Override
  public void startWorkflow(Document document) {
    logger.info("Starting Workflow for Publishing");
    try {
      Promise<Void> copyEditPromise = Async.procedure(activities::copyEdit, document);
      Promise<Void> grppicEditPromise = Async.procedure(activities::graphicEdit, document);
      copyEditPromise.get();
      grppicEditPromise.get();

      if (copyEditPromise.isCompleted()) {
        logger.info("Copy edit complete");
        copyEditComplete = true;
      }
      if (grppicEditPromise.isCompleted()) {
        logger.info("Graphic edit complete");
        graphicEditComplete = true;
      }
      // Wait for the flags to be set that indicate that the copyEdit and
      // graphicEdit activities are complete.
      Workflow.await(() -> graphicEditComplete && copyEditComplete);
      Promise<Void> publishPromise = Async.procedure(activities::publish, document);
      publishPromise.get();

      if (publishPromise.isCompleted()) {
        logger.info("Publishing complete");
        publishingComplete = true;
      }
      // Wait for the flags to be set that indicate that the copyEdit,
      // activities are complete.
      Workflow.await(() -> copyEditComplete && graphicEditComplete && publishingComplete);
    } catch (ActivityFailure e) {
      throw e;
    }
  }
}
