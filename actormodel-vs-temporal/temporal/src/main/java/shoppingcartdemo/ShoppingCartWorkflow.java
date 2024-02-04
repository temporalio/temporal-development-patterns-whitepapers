package shoppingcartdemo;

import io.temporal.workflow.QueryMethod;
import io.temporal.workflow.SignalMethod;
import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;
import java.util.List;
import shoppingcartdemo.model.CheckoutInfo;
import shoppingcartdemo.model.PurchaseItem;

@WorkflowInterface
public interface ShoppingCartWorkflow {

  @WorkflowMethod
  void startWorkflow();

  @SignalMethod
  void addItem(PurchaseItem purchaseItem);

  @SignalMethod
  void addItems(List<PurchaseItem> purchaseItems);

  @SignalMethod
  void removeItem(PurchaseItem purchaseItem);

  @SignalMethod
  void emptyCart();

  @SignalMethod
  void checkout(CheckoutInfo checkoutInfo);

  @QueryMethod
  Boolean isCompleted();
}
