package shoppingcartdemo;

import io.temporal.workflow.*;
import java.util.List;
import shoppingcartdemo.model.CheckoutInfo;
import shoppingcartdemo.model.PurchaseItem;

@WorkflowInterface
public interface ShoppingCartWorkflow {

  @WorkflowMethod
  void startWorkflow();

  @UpdateMethod
  void addItem(PurchaseItem purchaseItem);

  @UpdateValidatorMethod(updateName = "addItem")
  void addItemValidator(PurchaseItem purchaseItem);

  @UpdateMethod
  void addItems(List<PurchaseItem> purchaseItems);

  @UpdateValidatorMethod(updateName = "addItems")
  void addItemsValidator(List<PurchaseItem> purchaseItems);

  @UpdateMethod
  void removeItem(PurchaseItem purchaseItem);

  @UpdateMethod
  void emptyCart();

  @SignalMethod
  void checkout(CheckoutInfo checkoutInfo);

  @QueryMethod
  Boolean isCompleted();
}
