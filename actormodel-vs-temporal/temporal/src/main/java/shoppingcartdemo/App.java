package shoppingcartdemo;

import com.google.common.base.Throwables;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import io.temporal.client.WorkflowUpdateException;
import io.temporal.failure.ApplicationFailure;
import io.temporal.serviceclient.WorkflowServiceStubs;

import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import shoppingcartdemo.model.Customer;
import shoppingcartdemo.model.PurchaseItem;
import shoppingcartdemo.temporal.ShoppingCartWorkflow;
import shoppingcartdemo.utils.Mocks;

public class App {

  private static final Logger logger = LoggerFactory.getLogger(App.class);

  @SuppressWarnings("CatchAndPrintStackTrace")
  public static void main(String[] args) throws MalformedURLException {
    logger.info("Starting the Shopping Cart Demo to TASK_QUEUE" + Constants.TASK_QUEUE);

    WorkflowServiceStubs service = WorkflowServiceStubs.newLocalServiceStubs();
    // client that can be used to start and signal workflows
    WorkflowClient client = WorkflowClient.newInstance(service);

    WorkflowOptions options =
        WorkflowOptions.newBuilder()
            .setTaskQueue(Constants.TASK_QUEUE)
            .setWorkflowId(UUID.randomUUID().toString())
            .build();
    ShoppingCartWorkflow wf = client.newWorkflowStub(ShoppingCartWorkflow.class, options);
    List<PurchaseItem> purchaseItems = Mocks.getRandomPurchaseItems();
    Customer customer = purchaseItems.get(0).getCustomer();
    WorkflowClient.start(wf::startWorkflow);

    try {
      wf.addItems(purchaseItems);
    } catch (WorkflowUpdateException e) {
      // Throwable cause = Throwables.getRootCause(e);
      String exceptionString = ((ApplicationFailure) e.getCause()).getMessage();

      List<PurchaseItem> items = getPurchaseItemsFromException(exceptionString);
      wf.backorder(items);

      // logger.info("\n Update failed, root cause: " + cause.getMessage());
    }

    wf.checkout(Mocks.getRandomCheckoutInfo(customer, Mocks.getRandomCreditCard(customer)));

    while (!wf.isCompleted()) {
      try {
        Thread.sleep(500); // Wait for 500 milliseconds
      } catch (InterruptedException e) {
        logger.info(e.getMessage());
      }
    }
  }

  static private List<PurchaseItem> getPurchaseItemsFromException(String inputString) {
    String jsonString = getJsonString(inputString);
    Type purchaseItemType = new TypeToken<List<PurchaseItem>>() {}.getType();
    List<PurchaseItem> items = new Gson().fromJson(jsonString, purchaseItemType);
    return items;
  }

  static private String getJsonString(String inputString) {
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
