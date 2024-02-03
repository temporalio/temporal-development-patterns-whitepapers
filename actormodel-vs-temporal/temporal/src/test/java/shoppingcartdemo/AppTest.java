import java.net.MalformedURLException;
import java.util.List;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import shoppingcartdemo.model.*;
import shoppingcartdemo.utils.Mocks;

/** Unit test for simple App. */
public class AppTest extends TestCase {
  /**
   * Create the test case
   *
   * @param testName name of the test case
   */
  public AppTest(String testName) {
    super(testName);
  }

  /**
   * @return the suite of tests being tested
   */
  public static Test suite() {
    return new TestSuite(AppTest.class);
  }

  // Write a function that runs the code in App.java
  public void testMainOutput() throws MalformedURLException, InterruptedException {
    // App.main(new String[] {});
  }

  // write a function that tests the getRandomPurchaseItems function in Mocks.java
  public void testGetProducts() {
    List<Product> products = Mocks.getProducts();
    assertNotNull(products);
  }

  public void testCreditCard() {
    CreditCard creditCart = Mocks.getRandomCreditCard(Mocks.getRandomCustomer());
    assertNotNull(creditCart);
  }

  public void testCheckoutInfo() {
    Customer customer = Mocks.getRandomCustomer();
    CheckoutInfo checkoutInfo = Mocks.getRandomCheckoutInfo(customer, Mocks.getRandomCreditCard(customer));
    assertNotNull(checkoutInfo);
  }

  // write a function that tests the getRandomPurchaseItem function in Mocks.java
  public void testGetRandomPurchaseItem() {
    PurchaseItem purchaseItem = Mocks.getRandomPurchaseItem(Mocks.getRandomCustomer());
    assertNotNull(purchaseItem);
  }
}
