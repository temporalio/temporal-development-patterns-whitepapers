package shoppingcartdemo.temporal.backorder;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import java.lang.reflect.Type;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import shoppingcartdemo.model.PurchaseItem;

public class BackorderActivitiesImpl implements BackorderActivities {
  private static final Logger logger = LoggerFactory.getLogger((BackorderActivitiesImpl.class));

  @Override
  public void backorder(List<PurchaseItem> purchaseItems) {
    // Create a Gson instance
    Gson gson = new Gson();

    // Define the type of the list
    Type listType = new TypeToken<List<PurchaseItem>>() {}.getType();

    // Serialize the list to a JSON string
    String json = gson.toJson(purchaseItems, listType);
    String str = String.format("The following items are on backorder: %s", json);
    logger.info(str);
  }
}
