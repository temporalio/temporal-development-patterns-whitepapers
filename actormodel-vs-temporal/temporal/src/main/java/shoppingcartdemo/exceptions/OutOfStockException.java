package shoppingcartdemo.exceptions;

import java.util.List;
import shoppingcartdemo.model.PurchaseItem;

public class OutOfStockException extends RuntimeException {

  private List<PurchaseItem> rejectedItems;

  public OutOfStockException(String message) {
    super(message);
  }

  public OutOfStockException(PurchaseItem purchaseItem) {
    super(
        String.format(
            "Item %s %s is out of stock",
            purchaseItem.getProduct().getName(), purchaseItem.getProduct().getSize()));
  }

  public OutOfStockException(List<PurchaseItem> rejectedItems, String message) {
    super(message);
    this.rejectedItems = rejectedItems;
  }

  public OutOfStockException(String message, Throwable cause) {
    super(message, cause);
  }

  public OutOfStockException(Throwable cause) {
    super(cause);
  }
}
