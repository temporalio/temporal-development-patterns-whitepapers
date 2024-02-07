package shoppingcartdemo.temporal.backorder;

import io.temporal.workflow.UpdateMethod;
import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;
import java.util.List;
import shoppingcartdemo.model.PurchaseItem;

@WorkflowInterface
public interface BackorderWorkflow {

  @WorkflowMethod
  void startWorkflow();

  @UpdateMethod
  void backorder(List<PurchaseItem> purchaseItems);
}
