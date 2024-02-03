package shoppingcartdemo.model;

public class SKU {
  private String id;
  private String description;
  private int packageSize;
  private String location;

  public SKU() {}

  public SKU(String id, String description, int packageSize, String location) {
    this.id = id;
    this.description = description;
    this.packageSize = packageSize;
    this.location = location;
  }

  public String getId() {
    return id;
  }

  public String getDescription() {
    return description;
  }

  public int getPackageSize() {
    return packageSize;
  }

  public String getLocation() {
    return location;
  }
}
