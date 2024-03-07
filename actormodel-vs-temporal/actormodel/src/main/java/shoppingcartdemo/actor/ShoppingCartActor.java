package shoppingcartdemo.actor;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import shoppingcartdemo.model.Address;
import shoppingcartdemo.model.CreditCard;
import shoppingcartdemo.model.Customer;
import shoppingcartdemo.model.PurchaseItem;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;

public class ShoppingCartActor extends AbstractActor {
  // Declare the parent system
  private final akka.actor.ActorSystem actorSystem;
  private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
  private final List<PurchaseItem> purchaseItems = new ArrayList<>();

  public ShoppingCartActor(akka.actor.ActorSystem actorSystem) {
    this.actorSystem = actorSystem;
  }

  @Override
  public Receive createReceive() {
    return receiveBuilder()
        .match(AddItem.class, this::handleAddItem)
        .match(RemoveItem.class, this::handleRemoveItem)
        .match(EmptyCart.class, this::handleEmptyCart)
        .match(Checkout.class, this::handleCheckoutCart)
        .build();
  }

  private void handleAddItem(AddItem msg) {
    this.purchaseItems.add(msg.getPurchaseItem());
  }

  private void handleRemoveItem(RemoveItem msg) {
    this.purchaseItems.remove(msg.purchaseItem);
  }

  private void handleEmptyCart(EmptyCart msg) {
    this.log.info("ShoppingCart is emptying the cart.");
    this.purchaseItems.clear();
  }

  private void handleCheckoutCart(Checkout msg) {
    if (this.purchaseItems.size() == 0) {
      throw new RuntimeException("No purchase items to checkout");
    }

    // Use the customer in the first purchase item as the customer
    Customer customer = purchaseItems.get(0).getCustomer();

    String firstName = customer.getFirstName();
    String lastName = customer.getLastName();
    this.log.info(
        "Starting a checkout of {} items for {} {} .", purchaseItems.size(), firstName, lastName);

    // Do the payment
    BigDecimal totalAmount =
        this.purchaseItems.stream()
            .map(PurchaseItem::getTotal)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

    this.log.info(
        "{} {} is paying {} using credit card {}.",
        firstName,
        lastName,
        totalAmount,
        msg.getCreditCard());

    // Send a payment receipt to the customer as a fire and forget message
    CustomerActor.PaymentReceipt paymentReceipt =
        new CustomerActor.PaymentReceipt("PR01", customer, totalAmount);
    ActorRef customerActor =
        this.actorSystem.actorOf(
            Props.create(CustomerActor.class, this.actorSystem), "customerActor");

    customerActor.tell(paymentReceipt, customerActor);

    // Do the shipping
    this.log.info(
        "{} is shipping {} purchase items to {}",
        msg.getShipper(),
        this.purchaseItems.size(),
        msg.billingAddress);

    // Send a shipping receipt to the customer as a fire and forget message
    CustomerActor.ShippingReceipt shippingReceipt =
        new CustomerActor.ShippingReceipt(
            "SR01", customer, new Vector<>(purchaseItems), msg.getShipper());

    customerActor.tell(shippingReceipt, customerActor);

    this.purchaseItems.clear();
  }

  public static class AddItem {
    private final PurchaseItem purchaseItem;

    public AddItem(PurchaseItem purchaseItem) {
      this.purchaseItem = purchaseItem;
    }

    public PurchaseItem getPurchaseItem() {
      return this.purchaseItem;
    }
  }

  public static class RemoveItem {
    private final PurchaseItem purchaseItem;

    public RemoveItem(PurchaseItem purchaseItem) {
      this.purchaseItem = purchaseItem;
    }

    public PurchaseItem getPurchaseItem() {
      return this.purchaseItem;
    }
  }

  public static class EmptyCart {
    private final Date emptyCartDate;

    public EmptyCart() {
      this.emptyCartDate = new Date();
    }

    public Date getEmptyCartDate() {
      return this.emptyCartDate;
    }
  }

  public static class Checkout {
    private final CreditCard creditCard;
    private final Address billingAddress;
    private final Address shippingAddress;
    private final String shipper;

    public Checkout(
        CreditCard creditCard, Address billingAddress, Address shippingAddress, String shipper) {
      this.creditCard = creditCard;
      this.billingAddress = billingAddress;
      this.shippingAddress = shippingAddress;
      this.shipper = shipper;
    }

    public CreditCard getCreditCard() {
      return this.creditCard;
    }

    public Address getBillingAddress() {
      return this.billingAddress;
    }

    public Address getShippingAddress() {
      return this.shippingAddress;
    }

    public String getShipper() {
      return this.shipper;
    }
  }
}
