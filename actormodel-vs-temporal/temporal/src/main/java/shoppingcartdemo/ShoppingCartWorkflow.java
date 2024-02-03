package shoppingcartdemo;

import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;
import shoppingcartdemo.model.CheckoutInfo;
import shoppingcartdemo.model.PurchaseItem;

@WorkflowInterface
public interface ShoppingCartWorkflow {
  @WorkflowMethod
  void startWorkflow();

  @WorkflowMethod
  void addItem(PurchaseItem purchaseItem);

  @WorkflowMethod
  void removeItem(PurchaseItem purchaseItem);

  @WorkflowMethod
  void emptyCart();

  @WorkflowMethod
  void checkout(CheckoutInfo checkoutInfo);
}
