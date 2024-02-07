package shoppingcartdemo.enums;

import java.util.Random;

public enum Shippers {
  UPS,
  FEDEX,
  USPS,
  DHL;

  private static final Random random = new Random();

  public static String getRandomShipper() {
    // Get a random enum value
    Shippers randomShipper = values()[random.nextInt(values().length)];
    // Return its string name
    return randomShipper.name();
  }
}
