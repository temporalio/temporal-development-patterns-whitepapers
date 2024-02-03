package shoppingcartdemo.model;

import org.json.JSONObject;

public class Address {
  private String address1;
  private String address2;
  private String city;
  private String stateProvince;
  private String zipRegionCode;
  private String country;

  /*
  Add a parameterless constructor and setters to make this serializable
  by the serializer.
  */
  public Address() {}

  public Address(
      String address1,
      String address2,
      String city,
      String stateProvince,
      String zipRegionCode,
      String country) {
    this.address1 = address1;
    this.address2 = address2;
    this.city = city;
    this.stateProvince = stateProvince;
    this.zipRegionCode = zipRegionCode;
    this.country = country;
  }

  public String getAddress1() {
    return address1;
  }

  public String getAddress2() {
    return address2;
  }

  public String getCity() {
    return city;
  }

  public String getStateProvince() {
    return stateProvince;
  }

  public String getZipRegionCode() {
    return zipRegionCode;
  }

  public String getCountry() {
    return country;
  }

  @Override
  public String toString() {
    return new JSONObject(this).toString();
  }
}
