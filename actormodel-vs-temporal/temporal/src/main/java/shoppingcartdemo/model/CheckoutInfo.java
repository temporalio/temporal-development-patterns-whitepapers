package shoppingcartdemo.model;

public class CheckoutInfo {
  private CreditCard creditCard;
  private Customer customer;
  private String shipper;

  /*
  Add a parameterless constructor and setters to make this serializable
  by the serializer.
  */
  public CheckoutInfo() {}

  public CheckoutInfo(Customer customer, CreditCard creditCard, String shipper) {
    this.creditCard = creditCard;
    this.customer = customer;
    this.shipper = shipper;
  }

  public CreditCard getCreditCard() {
    return creditCard;
  }

  public String getShipper() {
    return shipper;
  }
}
