package shoppingcartdemo;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import io.temporal.client.WorkflowUpdateException;
import io.temporal.serviceclient.WorkflowServiceStubs;
import java.lang.reflect.Type;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import shoppingcartdemo.model.Customer;
import shoppingcartdemo.model.PurchaseItem;
import shoppingcartdemo.temporal.backorder.BackorderWorkflow;
import shoppingcartdemo.temporal.shoppingcart.ShoppingCartWorkflow;
import shoppingcartdemo.utils.Mocks;

public class App {
  private static final Logger logger = LoggerFactory.getLogger(App.class);

  @SuppressWarnings("CatchAndPrintStackTrace")
  public static void main(String[] args) {
    logger.info(
        "Starting the Shopping Cart Demo bound to Task Queue: "
            + Constants.SHOPPING_CART_TASK_QUEUE);

    WorkflowServiceStubs service = WorkflowServiceStubs.newLocalServiceStubs();
    // client that can be used to start and signal workflows
    WorkflowClient client = WorkflowClient.newInstance(service);

    WorkflowOptions shoppingCartOptions =
        WorkflowOptions.newBuilder()
            .setTaskQueue(Constants.SHOPPING_CART_TASK_QUEUE)
            .setWorkflowId(UUID.randomUUID().toString())
            .build();
    ShoppingCartWorkflow shoppingCartWorkflow =
        client.newWorkflowStub(ShoppingCartWorkflow.class, shoppingCartOptions);

    List<PurchaseItem> purchaseItems = Mocks.getRandomPurchaseItems();
    Customer customer = purchaseItems.get(0).getCustomer();
    WorkflowClient.start(shoppingCartWorkflow::startWorkflow);

    try {
      shoppingCartWorkflow.addItems(purchaseItems);
    } catch (WorkflowUpdateException e) {
      String exceptionString = e.getCause().getMessage();

      List<PurchaseItem> items = getPurchaseItemsFromException(exceptionString);

      WorkflowOptions backorderOptions =
          WorkflowOptions.newBuilder()
              .setTaskQueue(Constants.BACKORDER_TASK_QUEUE)
              .setWorkflowId(UUID.randomUUID().toString())
              .build();
      BackorderWorkflow backorderWorkflow =
          client.newWorkflowStub(BackorderWorkflow.class, backorderOptions);

      logger.info("Starting the Backorder bound to Task Queue: " + Constants.BACKORDER_TASK_QUEUE);

      WorkflowClient.start(backorderWorkflow::startWorkflow);
      backorderWorkflow.backorder(items);
    }

    shoppingCartWorkflow.checkout(
        Mocks.getRandomCheckoutInfo(customer, Mocks.getRandomCreditCard(customer)));

    while (!shoppingCartWorkflow.isCompleted()) {
      try {
        Thread.sleep(500); // Wait for 500 milliseconds
      } catch (InterruptedException e) {
        logger.info(e.getMessage());
      }
    }
  }

  private static List<PurchaseItem> getPurchaseItemsFromException(String inputString) {
    String jsonString = getJsonString(inputString);
    Type purchaseItemType = new TypeToken<List<PurchaseItem>>() {}.getType();
    return new Gson().fromJson(jsonString, purchaseItemType);
  }

  private static String getJsonString(String inputString) {
    // Define the regex pattern
    String regexPattern = "message='(.*?)'";
    // Create a Pattern object
    Pattern pattern = Pattern.compile(regexPattern);
    // Create a Matcher object
    Matcher matcher = pattern.matcher(inputString);
    // Find the match and print the result
    if (matcher.find()) {
      return matcher.group(1).trim();
    }
    return null;
  }
}
