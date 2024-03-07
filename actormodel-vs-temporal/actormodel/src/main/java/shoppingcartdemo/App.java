package shoppingcartdemo;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import shoppingcartdemo.actor.ShoppingCartActor;
import shoppingcartdemo.model.Address;
import shoppingcartdemo.model.CreditCard;
import shoppingcartdemo.model.Customer;
import shoppingcartdemo.model.PurchaseItem;
import java.math.BigDecimal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class App {
  public static void main(String[] args) {
    Logger logger = LoggerFactory.getLogger(ShoppingCartActor.class);

    logger.info("Starting Shopping Cart Demo.");

    akka.actor.ActorSystem system = ActorSystem.create("ShoppingCartDemoSystem");

    Address address = new Address("123 Main Street", "Apt 1", "Anytown", "CA", "99999-9999", "USA");

    Customer customer =
        new Customer(
            "Customer01", "Barney", "Rubble", "barney@rubble.com", "310 878 9999", address);

    // Create the shopping cart actor
    ActorRef shoppingCartActor =
        system.actorOf(Props.create(ShoppingCartActor.class, system), "shoppingCartActor");

    // Create some purchase items to add them to the shopping cart

    // Pass the AddItem message on to the ShoppingCartActor
    ShoppingCartActor.AddItem shoppingCartItem =
        new ShoppingCartActor.AddItem(
            new PurchaseItem(
                "PI01",
                customer,
                "Barry's Gourmet Peanuts Large",
                5,
                new BigDecimal("1"),
                new BigDecimal("10.99"),
                address,
                address));
    shoppingCartActor.tell(shoppingCartItem, shoppingCartActor);

    shoppingCartItem =
        new ShoppingCartActor.AddItem(
            new PurchaseItem(
                "PI02",
                customer,
                "Barry's Gourmet Peanuts Medium",
                3,
                new BigDecimal("2"),
                new BigDecimal("7.99"),
                address,
                address));
    shoppingCartActor.tell(shoppingCartItem, shoppingCartActor);

    shoppingCartItem =
        new ShoppingCartActor.AddItem(
            new PurchaseItem(
                "PI03",
                customer,
                "Barry's Gourmet Peanuts Small",
                1,
                new BigDecimal("3"),
                new BigDecimal("4.99"),
                address,
                address));
    shoppingCartActor.tell(shoppingCartItem, shoppingCartActor);

    // Get  the credit card
    String firstName = customer.getFirstName();
    String lastName = customer.getLastName();
    CreditCard creditCard =
        new CreditCard(firstName + " " + lastName, "1111222233334444", 8, 26, 111);

    // Use the customer's base address for billing and shipping
    Address billingAddress = customer.getAddress();
    Address shippingAddress = customer.getAddress();
    String shipper = "FEDEX";

    // Checkout
    ShoppingCartActor.Checkout checkout =
        new ShoppingCartActor.Checkout(creditCard, billingAddress, shippingAddress, shipper);

    // Send a checkout message to the ShoppingCartActor to start the checkout process
    shoppingCartActor.tell(checkout, shoppingCartActor);
  }
}
