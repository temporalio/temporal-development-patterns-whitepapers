package shoppingcartdemo.model;

public class Customer {
  private final String id;
  private final String firstName;
  private final String lastName;
  private final String email;
  private final String phone;
  private final Address address;

  public Customer(
      String id, String firstName, String lastName, String email, String phone, Address address) {
    this.id = id;
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
    this.phone = phone;
    this.address = address;
  }

  public String getId() {
    return this.id;
  }

  public String getFirstName() {
    return firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public String getEmail() {
    return email;
  }

  public String getPhone() {
    return phone;
  }

  public Address getAddress() {
    return address;
  }
}
