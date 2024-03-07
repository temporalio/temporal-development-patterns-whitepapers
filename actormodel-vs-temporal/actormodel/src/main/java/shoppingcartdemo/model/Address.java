package shoppingcartdemo.model;

import org.json.JSONObject;

public class Address {
  private final String address1;
  private final String address2;
  private final String city;
  private final String stateProvince;
  private final String zipRegionCode;
  private final String countryCode;

  public Address(
      String address1,
      String address2,
      String city,
      String stateProvince,
      String zipRegionCode,
      String countryCode) {
    this.address1 = address1;
    this.address2 = address2;
    this.city = city;
    this.stateProvince = stateProvince;
    this.zipRegionCode = zipRegionCode;
    this.countryCode = countryCode;
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

  public String getCountryCode() {
    return countryCode;
  }

  @Override
  public String toString() {
    return new JSONObject(this).toString();
  }
}
