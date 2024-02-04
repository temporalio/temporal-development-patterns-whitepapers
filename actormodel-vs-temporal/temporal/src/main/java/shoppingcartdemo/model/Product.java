package shoppingcartdemo.model;

import java.math.BigDecimal;
import java.util.UUID;
import shoppingcartdemo.enums.PackageSize;

public class Product {
  private String id;
  private String name;

  private PackageSize size;
  private double price;

  public Product() {}

  public Product(String name, PackageSize size, double price) {
    this.size = size;
    this.id = UUID.randomUUID().toString();
    this.name = name;
    this.price = price;
  }

  public String getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public PackageSize getSize() {
    return size;
  }

  public BigDecimal getPrice() {
    return BigDecimal.valueOf(price);
  }
}
