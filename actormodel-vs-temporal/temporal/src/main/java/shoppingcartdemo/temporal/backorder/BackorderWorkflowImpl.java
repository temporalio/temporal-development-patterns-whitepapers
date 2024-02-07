package shoppingcartdemo.temporal.backorder;

import io.temporal.activity.ActivityOptions;
import io.temporal.common.RetryOptions;
import io.temporal.workflow.Async;
import io.temporal.workflow.Promise;
import io.temporal.workflow.Workflow;
import java.time.Duration;
import java.util.List;
import org.slf4j.Logger;
import shoppingcartdemo.Constants;
import shoppingcartdemo.model.PurchaseItem;

public class BackorderWorkflowImpl implements BackorderWorkflow {
  private static final Logger logger = Workflow.getLogger(BackorderWorkflowImpl.class);
  private final BackorderActivities activities;
  private boolean backorderDone = false;

  public BackorderWorkflowImpl() {
    ActivityOptions options =
        ActivityOptions.newBuilder()
            .setStartToCloseTimeout(Duration.ofSeconds(1))
            .setTaskQueue(Constants.BACKORDER_TASK_QUEUE)
            .setRetryOptions(RetryOptions.newBuilder().setMaximumAttempts(1).build())
            .build();
    this.activities = Workflow.newActivityStub(BackorderActivities.class, options);
  }

  @Override
  public void startWorkflow() {
    logger.info("Backorder started");
    Workflow.await(() -> backorderDone);
    logger.info("Backorder completed");
  }

  @Override
  public void backorder(List<PurchaseItem> purchaseItems) {
    Promise<Void> backorder = Async.procedure(activities::backorder, purchaseItems);
    backorder.get();
    backorderDone = true;
  }
}
