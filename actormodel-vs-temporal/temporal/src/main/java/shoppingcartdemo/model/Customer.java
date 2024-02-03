package shoppingcartdemo.model;

public class Customer {
  private String id;
  private String firstName;
  private String lastName;
  private String email;
  private String phone;
  private Address address;

  /*
  Add a parameterless constructor and setters to make this serializable
  by the serializer.
  */
  public Customer() {}

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
    return this.firstName;
  }

  public String getLastName() {
    return this.lastName;
  }

  public String getEmail() {
    return this.email;
  }

  public String getPhone() {
    return this.phone;
  }

  public Address getAddress() {
    return this.address;
  }

  public void setId(String id) {
    this.id = id;
  }
}
