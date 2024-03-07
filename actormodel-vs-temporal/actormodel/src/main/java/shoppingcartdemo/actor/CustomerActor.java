package shoppingcartdemo.actor;

import akka.actor.AbstractActor;
import akka.actor.ActorSystem;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import shoppingcartdemo.model.Customer;
import shoppingcartdemo.model.PurchaseItem;
import java.math.BigDecimal;
import java.util.Vector;

public class CustomerActor extends AbstractActor {
  // We'll leave a reference to the base system
  // in, just in case CustomerActor wants to use it later
  // on
  private final ActorSystem actorSystem;
  private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

  public CustomerActor(ActorSystem actorSystem) {
    this.actorSystem = actorSystem;
  }

  @Override
  public Receive createReceive() {
    return receiveBuilder()
            .match(ShippingReceipt.class, this::handleShippingReceipt)
            .match(PaymentReceipt.class, this::handlePaymentReceipt)
            .build();
  }

  private void handleShippingReceipt(CustomerActor.ShippingReceipt msg) {
    this.log.info(
            "Customer {} received shipping confirmation id {} for {} purchase items shipped via {}.",
            msg.getCustomer().getFirstName() + " " + msg.getCustomer().getLastName(),
            msg.getId(),
            msg.getPurchaseItems().size(),
            msg.getShipper());
  }

  private void handlePaymentReceipt(CustomerActor.PaymentReceipt msg) {
    this.log.info(
            "Customer {} received payment confirmation id {} for the purchase total of {}.",
            msg.getCustomer().getFirstName() + " " + msg.getCustomer().getLastName(),
            msg.getId(),
            msg.getPurchaseTotal());
  }

  public static class ShippingReceipt {
    private final String id;
    private final Customer customer;
    private final Vector<PurchaseItem> purchaseItems;
    private final String shipper;

    public ShippingReceipt(
            String id, Customer customer, Vector<PurchaseItem> purchaseItems, String shipper) {
      this.id = id;
      this.customer = customer;
      this.purchaseItems = purchaseItems;
      this.shipper = shipper;
    }

    public String getId() {
      return this.id;
    }

    public Customer getCustomer() {
      return this.customer;
    }

    public Vector<PurchaseItem> getPurchaseItems() {
      return this.purchaseItems;
    }

    public String getShipper() {
      return this.shipper;
    }
  }

  public static class PaymentReceipt {
    private final String id;
    private final Customer customer;
    private final BigDecimal purchaseTotal;

    public PaymentReceipt(String id, Customer customer, BigDecimal purchaseTotal) {
      this.id = id;
      this.customer = customer;
      this.purchaseTotal = purchaseTotal;
    }

    public String getId() {
      return this.id;
    }

    public Customer getCustomer() {
      return this.customer;
    }

    public BigDecimal getPurchaseTotal() {
      return this.purchaseTotal;
    }
  }
}
