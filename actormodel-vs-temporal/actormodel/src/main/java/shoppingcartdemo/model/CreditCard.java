package shoppingcartdemo.model;

import org.json.JSONObject;

public class CreditCard {
  private final String nameOnCard;
  private final String creditCardNumber;
  private final Integer expirationMonth;
  private final Integer expirationYear;
  private final Integer securityCode;

  public CreditCard(
      String nameOnCard,
      String creditCardNumber,
      Integer expirationMonth,
      Integer expirationYear,
      Integer securityCode) {
    this.nameOnCard = nameOnCard;
    this.creditCardNumber = creditCardNumber;
    this.expirationMonth = expirationMonth;
    this.expirationYear = expirationYear;
    this.securityCode = securityCode;
  }

  public String getNameOnCard() {
    return nameOnCard;
  }

  public String getCreditCardNumber() {
    return creditCardNumber;
  }

  public Integer getExpirationMonth() {
    return expirationMonth;
  }

  public Integer getExpirationYear() {
    return expirationYear;
  }

  public Integer getSecurityCode() {
    return securityCode;
  }

  @Override
  public String toString() {
    return new JSONObject(this).toString();
  }
}
