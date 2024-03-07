package shoppingcartdemo.model;

import java.math.BigDecimal;
import java.util.Date;

public class PurchaseItem {
  private final String id;
  private final Customer customer;
  private final String description;
  private final int packageSize;
  private final BigDecimal quantity;
  private final BigDecimal price;
  private Address billingAddress;
  private Address shippingAddress;
  private Date shipDate;

  public PurchaseItem(
      String id,
      Customer customer,
      String description,
      int packageSize,
      BigDecimal quantity,
      BigDecimal price,
      Address billingAddress,
      Address shippingAddress) {
    this.id = id;
    this.description = description;
    this.customer = customer;
    this.packageSize = packageSize;
    this.quantity = quantity;
    this.price = price;
    this.billingAddress = billingAddress;
    this.shippingAddress = shippingAddress;
    this.shipDate = null;
  }

  public String getId() {
    return this.id;
  }

  public Customer getCustomer() {
    return customer;
  }

  public int getPackageSize() {
    return packageSize;
  }

  public BigDecimal getQuantity() {
    return quantity;
  }

  public BigDecimal getPrice() {
    return price;
  }

  public BigDecimal getTotal() {
    return this.quantity.multiply(this.price);
  }

  public Address getBillingAddress() {
    return billingAddress;
  }

  public void setBillingAddress(Address billingAddress) {
    this.billingAddress = billingAddress;
  }

  public Address getShippingAddress() {
    return shippingAddress;
  }

  public void setShippingAddress(Address shippingAddress) {
    this.shippingAddress = shippingAddress;
  }

  public Date getShipDate() {
    return shipDate;
  }

  public void setShipDate(Date shipDate) {
    this.shipDate = shipDate;
  }

  public String getDescription() {
    return description;
  }
}
