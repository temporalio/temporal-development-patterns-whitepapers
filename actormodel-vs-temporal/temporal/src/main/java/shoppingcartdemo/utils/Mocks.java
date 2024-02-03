package shoppingcartdemo.utils;

import com.github.javafaker.Faker;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import shoppingcartdemo.enums.PackageSize;
import shoppingcartdemo.enums.Shippers;
import shoppingcartdemo.model.*;

/**
 * This class is used to seed the shopping cart with purchaseItems as well as declare the
 * checkoutInfo used by the ShoppingCartWorkflowImpl
 */
public class Mocks {

  private static List<String> productNames;

  private static final Faker faker = new Faker();

  public static List<PurchaseItem> getRandomPurchaseItems() {
    List<PurchaseItem> purchaseItems = new ArrayList<>();
    for (int i = 0; i < 5; i++) {
      purchaseItems.add(getRandomPurchaseItem(getRandomCustomer()));
    }
    return purchaseItems;
  }

  public static PurchaseItem getRandomPurchaseItem(Customer customer) {
    return new PurchaseItem(
        UUID.randomUUID().toString(),
        customer,
        customer.getAddress(),
        getRandomProduct(),
        // get a random integer between 1 and 10
        (int) faker.number().randomDouble(0, 1, 10));
  }

  public static Customer getRandomCustomer() {
    return new Customer(
        UUID.randomUUID().toString(),
        faker.name().firstName(),
        faker.name().lastName(),
        faker.internet().emailAddress(),
        faker.phoneNumber().cellPhone(),
        getRandomAddress());
  }

  public static Address getRandomAddress() {
    return new Address(
        faker.address().streetAddress(),
        faker.address().secondaryAddress(),
        faker.address().city(),
        faker.address().state(),
        faker.address().zipCode(),
        "USA");
  }

  // Create a method that returns a new Product with a random name, size, and price
  public static Product getRandomProduct() {
    // Get a random index from the products list
    int randomIndex = faker.number().numberBetween(0, getProducts().size());
    return getProducts().get(randomIndex);
  }

  public static CheckoutInfo getRandomCheckoutInfo(Customer customer, CreditCard creditCard) {
    // get a random value from the enum Shippers and assign it to a string variable named shipper

    return new CheckoutInfo(customer, creditCard, Shippers.getRandomShipper());
  }

  public static CreditCard getRandomCreditCard(Customer customer) {
    return new CreditCard(
        customer.getFirstName() + " " + customer.getLastName(),
        faker.finance().creditCard(),
        faker.number().numberBetween(1, 12),
        faker.number().numberBetween(2021, 2030),
        faker.number().numberBetween(100, 999));
  }

  public static List<Product> getProducts() {

    List<String> productNames =
        new ArrayList<String>() {
          {
            add("Salted Peanuts");
            add("Peanut Butter");
            add("Peanut Oil");
            add("Peanut Flour");
            add("Peanut Milk");
          }
        };

    List<Product> products = new ArrayList<>();
    // Get each value from the productNames list and add it to the products list
    for (String productName : productNames) {
      // Get each value from the PackageSize enum and add it to the products list
      for (PackageSize size : PackageSize.values()) {
        products.add(new Product(productName, size, faker.number().randomDouble(2, 1, 10)));
      }
    }
    return products;
  }
}
