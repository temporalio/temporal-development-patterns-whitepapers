package shoppingcartdemo.temporal.shoppingcart;

import io.temporal.workflow.*;
import java.util.List;
import shoppingcartdemo.model.CheckoutInfo;
import shoppingcartdemo.model.PurchaseItem;

@WorkflowInterface
public interface ShoppingCartWorkflow {

  @WorkflowMethod
  void startWorkflow();

  @UpdateMethod
  void addItems(List<PurchaseItem> purchaseItems);

  @UpdateMethod
  void removeItem(PurchaseItem purchaseItem);

  @UpdateMethod
  void emptyCart();

  @SignalMethod
  void checkout(CheckoutInfo checkoutInfo);

  @QueryMethod
  Boolean isCompleted();
}
