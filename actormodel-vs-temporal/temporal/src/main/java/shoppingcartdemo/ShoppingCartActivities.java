package shoppingcartdemo;

import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;
import java.util.List;
import shoppingcartdemo.model.CheckoutInfo;
import shoppingcartdemo.model.PurchaseItem;

@ActivityInterface
public interface ShoppingCartActivities {
  @ActivityMethod
  void pay(List<PurchaseItem> purchaseItems, CheckoutInfo checkoutInfo);

  @ActivityMethod
  void getItemsFromInventory(List<PurchaseItem> purchaseItems);

  @ActivityMethod
  void ship(List<PurchaseItem> purchaseItems, CheckoutInfo checkoutInfo);

}
