package shoppingcartdemo.model;

import org.json.JSONObject;

public class CreditCard {
  private String fullName;
  private String number;
  private int expirationMonth;
  private int expirationYear;
  private int securityCode;

  /*
  Add a parameterless constructor and setters to make this serializable
  by the serializer.
  */
  public CreditCard() {}

  public CreditCard(
      String fullName, String number, int expirationMonth, int expirationYear, int securityCode) {
    this.fullName = fullName;
    this.number = number;
    this.expirationMonth = expirationMonth;
    this.expirationYear = expirationYear;
    this.securityCode = securityCode;
  }

  public String getFullName() {
    return fullName;
  }

  public String getNumber() {
    return number;
  }

  public int getExpirationMonth() {
    return expirationMonth;
  }

  public int getExpirationYear() {
    return expirationYear;
  }

  public int getSecurityCode() {
    return securityCode;
  }

  public void setFullName(String fullName) {
    this.fullName = fullName;
  }

  @Override
  public String toString() {
    return new JSONObject(this).toString();
  }
}
