package shoppingcartdemo.temporal.shoppingcart;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.List;
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
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    String json = gson.toJson(purchaseItems);

    String info = (String.format(str, "getItemsFromInventory", json));
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
}
