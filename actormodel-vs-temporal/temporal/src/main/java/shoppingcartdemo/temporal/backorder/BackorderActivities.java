package shoppingcartdemo.temporal.backorder;

import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;
import java.util.List;
import shoppingcartdemo.model.PurchaseItem;

@ActivityInterface
public interface BackorderActivities {
  @ActivityMethod
  void backorder(List<PurchaseItem> purchaseItems);
}
