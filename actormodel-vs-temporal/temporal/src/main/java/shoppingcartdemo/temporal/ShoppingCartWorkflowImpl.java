package shoppingcartdemo.temporal;

import com.google.gson.Gson;
import io.temporal.activity.ActivityOptions;
import io.temporal.common.RetryOptions;
import io.temporal.failure.ApplicationFailure;
import io.temporal.workflow.Async;
import io.temporal.workflow.Promise;
import io.temporal.workflow.Workflow;
import io.temporal.workflow.WorkflowQueue;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import shoppingcartdemo.exceptions.OutOfStockException;
import shoppingcartdemo.model.CheckoutInfo;
import shoppingcartdemo.model.PurchaseItem;

public class ShoppingCartWorkflowImpl implements ShoppingCartWorkflow {
  private static final Logger logger = Workflow.getLogger(ShoppingCartWorkflowImpl.class);
  private final ShoppingCartActivities activities;
  private final List<PurchaseItem> purchaseItems = new ArrayList<>();
  private final WorkflowQueue<Runnable> queue = Workflow.newWorkflowQueue(1024);
  private boolean payDone = false;
  private boolean inventoryDone = false;
  private boolean shipDone = false;

  public ShoppingCartWorkflowImpl() {
    ActivityOptions options =
        ActivityOptions.newBuilder()
            .setStartToCloseTimeout(Duration.ofSeconds(1))
            .setTaskQueue("ShoppingCartDemo")
            .setRetryOptions(RetryOptions.newBuilder().setMaximumAttempts(1).build())
            .build();
    this.activities = Workflow.newActivityStub(ShoppingCartActivities.class, options);
  }

  @Override
  public void startWorkflow() {
    logger.info("Shopping cart started");
    Workflow.await(() -> payDone && inventoryDone && shipDone);
    logger.info("Shopping cart completed");
  }

  @Override
  public void addItem(PurchaseItem purchaseItem) {
    if (!this.isInInventory(purchaseItem)) {
      throw new OutOfStockException(purchaseItem);
    }
    this.purchaseItems.add(purchaseItem);
  }

  @Override
  public void addItemValidator(PurchaseItem purchaseItem) {
    if (purchaseItem.getQuantity() < 0) {
      throw new IllegalArgumentException("Quantity cannot be negative");
    }
    this.isInInventory(purchaseItem);
  }

  @Override
  public void addItems(List<PurchaseItem> purchaseItems) {
    List<PurchaseItem> badItems = new ArrayList<>();
    List<PurchaseItem> goodItems = new ArrayList<>();
    for (PurchaseItem purchaseItem : purchaseItems) {
      if (this.isInInventory(purchaseItem)) {
        logger.info(
            "Adding item "
                + purchaseItem.getProduct().getName()
                + " "
                + purchaseItem.getProduct().getSize()
                + " to the shopping cart");
        goodItems.add(purchaseItem);
      } else {
        logger.info(
            "Out Of Stock "
                + purchaseItem.getProduct().getName()
                + " "
                + purchaseItem.getProduct().getSize()
                + ".");
        badItems.add(purchaseItem);
      }
    }
    this.purchaseItems.addAll(goodItems);
    if (!badItems.isEmpty()) {
      Gson gson = new Gson();
      String json = gson.toJson(badItems);
      throw ApplicationFailure.newFailure(json, "OutOfStockException");
    }
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
  public void backorder(List<PurchaseItem> purchaseItems) {
    Promise<Void> backorder = Async.procedure(activities::backorder, purchaseItems);
    backorder.get();
  }

  @Override
  public void checkout(CheckoutInfo checkoutInfo) {
    logger.info("Checking out the shopping cart");

    // Call the activity pay as a Promise
    Promise<Void> pay = Async.procedure(activities::pay, this.purchaseItems, checkoutInfo);
    pay.get();
    payDone = true;

    // Call the activity getItemsFromInventory as a Promise
    Promise<Void> inventory =
        Async.procedure(activities::getItemsFromInventory, this.purchaseItems);
    inventory.get();
    inventoryDone = true;

    // Call the activity ship as a Promise
    Promise<Void> ship = Async.procedure(activities::ship, this.purchaseItems, checkoutInfo);
    ship.get();
    shipDone = true;
  }

  @Override
  public Boolean isCompleted() {
    return payDone && inventoryDone && shipDone;
  }

  private boolean isInInventory(PurchaseItem purchaseItem) {
    String outOfStockItem = "Salted Peanuts";
    return !purchaseItem.getProduct().getName().equals(outOfStockItem);
  }

  private String createExceptionMessage(List<PurchaseItem> purchaseItems) {
    StringBuilder messageBuilder = new StringBuilder("The following items are out of stock:");
    for (PurchaseItem purchaseItem : purchaseItems) {
      messageBuilder
          .append("\n - ")
          .append(purchaseItem.getProduct().getName())
          .append(" ")
          .append(purchaseItem.getProduct().getSize());
    }
    return messageBuilder.toString();
  }
}
