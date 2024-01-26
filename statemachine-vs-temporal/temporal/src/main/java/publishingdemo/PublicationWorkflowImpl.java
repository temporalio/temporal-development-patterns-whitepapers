package publishingdemo;

import io.temporal.activity.ActivityOptions;
import io.temporal.common.RetryOptions;
import io.temporal.failure.ActivityFailure;
import io.temporal.workflow.Async;
import io.temporal.workflow.Promise;
import io.temporal.workflow.Workflow;
import io.temporal.workflow.WorkflowQueue;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import publishingdemo.model.Document;

/**
 * This class implements the PublicationWorkflow interface. It calls the copyEdit(), graphicEdit()
 * and publish() activities
 */
public class PublicationWorkflowImpl implements PublicationWorkflow {
  private final PublishingActivities activities;
  private static final Logger logger = Workflow.getLogger(PublicationWorkflowImpl.class);

    public PublicationWorkflowImpl() {
    ActivityOptions options =
        ActivityOptions.newBuilder()
            .setStartToCloseTimeout(Duration.ofSeconds(1))
            .setTaskQueue("PublishingDemo")
            .setRetryOptions(
                RetryOptions.newBuilder()
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
    logger.info("Starting workflow for publishing document: " + document.getUrl());
    try {
      // Make it so that the copyEdit() and graphicEdit() activities are executed in parallel ...
      List<Promise<Void>> promisesList = new ArrayList<>();
      promisesList.add(Async.procedure(activities::copyEdit, document));
      promisesList.add(Async.procedure(activities::graphicEdit, document));

      Promise.allOf(promisesList).get();

      // ...then execute the publish() activity
      activities.publish(document);
      logger.info("Publishing complete for document: " + document.getUrl());

    } catch (ActivityFailure e) {
      throw e;
    }
  }
}
