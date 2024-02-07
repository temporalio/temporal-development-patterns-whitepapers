package shoppingcartdemo.temporal;

import java.lang.reflect.Type;
import java.util.List;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import shoppingcartdemo.model.CheckoutInfo;
import shoppingcartdemo.model.PurchaseItem;

public class ShoppingCartActivitiesImpl implements ShoppingCartActivities {

  private static final Logger logger = LoggerFactory.getLogger((ShoppingCartActivitiesImpl.class));

  private final String str =
      "I am Amazing Shopping Cart AI and Robotics. I have the smarts to %s to the Shopping Cart with data: %s";

  @Override
  public void pay(List<PurchaseItem> purchaseItems, CheckoutInfo checkoutInfo) {

    String info =
        (String.format(str, "pay", purchaseItems.toString() + " " + checkoutInfo.toString()));
    logger.info(info);
  }

  @Override
  public void getItemsFromInventory(List<PurchaseItem> purchaseItems) {
    String info = (String.format(str, "getItemsFromInventory", purchaseItems.toString()));
    logger.info(info);
  }

  @Override
  public void ship(List<PurchaseItem> purchaseItems, CheckoutInfo checkoutInfo) {
    String info =
        (String.format(
            str,
            "ship",
            "purchaseItems: "
                + purchaseItems.toString()
                + " | "
                + " checkoutInfo: "
                + checkoutInfo.toString()));
    logger.info(info);
  }

  @Override
  public void backorder(List<PurchaseItem> purchaseItems) {
    // Create a Gson instance
    Gson gson = new Gson();

    // Define the type of the list
    Type listType = new TypeToken<List<PurchaseItem>>() {}.getType();

    // Serialize the list to a JSON string
    String json =  gson.toJson(purchaseItems, listType);
    String str = String.format("The following items are on backorder: %s", json);
    logger.info(str);
  }
}
