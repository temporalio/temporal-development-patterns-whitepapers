package shoppingcartdemo;

import io.temporal.activity.ActivityOptions;
import io.temporal.common.RetryOptions;
import io.temporal.workflow.Workflow;
import io.temporal.workflow.WorkflowQueue;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import shoppingcartdemo.model.CheckoutInfo;
import shoppingcartdemo.model.PurchaseItem;

public class ShoppingCartWorkflowImpl implements ShoppingCartWorkflow {
  private static final Logger logger = Workflow.getLogger(ShoppingCartWorkflowImpl.class);
  private final ShoppingCartActivities activities;
  private final List<PurchaseItem> purchaseItems = new ArrayList<>();
  private final WorkflowQueue<Runnable> queue = Workflow.newWorkflowQueue(1024);
  private final boolean exit = false;

  public ShoppingCartWorkflowImpl() {
    ActivityOptions options =
        ActivityOptions.newBuilder()
            .setStartToCloseTimeout(Duration.ofSeconds(1))
            .setTaskQueue("PublishingDemo")
            .setRetryOptions(RetryOptions.newBuilder().setMaximumAttempts(1).build())
            .build();
    this.activities = Workflow.newActivityStub(ShoppingCartActivities.class, options);
  }

  @Override
  public void startWorkflow() {}

  @Override
  public void addItem(PurchaseItem purchaseItem) {
    this.purchaseItems.add(purchaseItem);
  }

  @Override
  public void removeItem(PurchaseItem purchaseItem) {
    this.purchaseItems.remove(purchaseItem);
  }

  @Override
  public void emptyCart() {
    // Empty the list of purchase items
    this.purchaseItems.clear();
  }

  @Override
  public void checkout(CheckoutInfo checkoutInfo) {
    // Call the activity pay as a Promise
    activities.pay(this.purchaseItems, checkoutInfo);

    // Call the activity getItemsFromInventory as a Promise
    activities.getItemsFromInventory(this.purchaseItems);

    // Call the activity ship as a Promise
    activities.ship(this.purchaseItems, checkoutInfo);
  }
}
