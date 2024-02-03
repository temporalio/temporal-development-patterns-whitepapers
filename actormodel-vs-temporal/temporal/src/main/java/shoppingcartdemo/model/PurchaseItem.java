package shoppingcartdemo.model;

import java.math.BigDecimal;

public class PurchaseItem {
  private String id;
  private Customer customer;

  private Address shippingAddress;
  private Product product;
  private Integer quantity;

  private BigDecimal total;

  /*
  Add a parameterless constructor and setters to make this serializable
  by the serializer.
  */
  public PurchaseItem() {}

  public PurchaseItem(
      String id, Customer customer, Address shippingAddress, Product product, Integer quantity) {
    this.id = id;
    this.product = product;
    this.customer = customer;
    this.shippingAddress = shippingAddress;
    this.quantity = quantity;
    // Calculate the total price to be quantity * product.price
    this.total = product.getPrice().multiply(BigDecimal.valueOf(quantity));
  }

  public String getId() {
    return id;
  }

  public Customer getCustomer() {
    return customer;
  }

  public Address getShippingAddress() {
    return shippingAddress;
  }

  public Product getProduct() {
    return product;
  }

  public Integer getQuantity() {
    return quantity;
  }

  public BigDecimal getTotal() {
    return this.total;
  }
}
