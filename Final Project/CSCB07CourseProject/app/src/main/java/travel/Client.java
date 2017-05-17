package travel;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.Date;

/**
 * Client class. A subclass of User class.
 *
 * @author Cameron, Li
 *
 */
public class Client extends User implements java.io.Serializable {
  private static final long serialVersionUID = 3421028323564581666L;
  private DateFormat yyyyMMdd = new SimpleDateFormat("yyyy-MM-dd");
  private String personalAddress;
  private String creditCardNumber;
  private Date expiry;
  private String firstName;
  private String lastName;

  public Client() {
    super("");
  }

  /**
   * Creates a new <code>Client</code> with the given personal address,credit
   * card number, expiry date, first name, last name, and email address.
   *
   * @param email
   *          inherits from the superclass the email address of the new
   *          <code>client</code>
   * @param personalAddress
   *          the personal address of the new <code>Client</code>, such as phone
   *          number
   * @param creditCardNumber
   *          the credit card number of the new <code>Client</code>
   * @param expiryString
   *          the expiry date of the new <code>Client</code>
   * @param firstName
   *          the first name of the new <code>Client</code>
   * @param lastName
   *          the last name of the new <code>Client</code>
   */
  public Client(String email, String personalAddress, String creditCardNumber, String expiryString,
      String firstName, String lastName) {
    super(email);
    this.personalAddress = personalAddress;
    this.creditCardNumber = creditCardNumber;

    try {
      this.expiry = yyyyMMdd.parse(expiryString);
    } catch (ParseException ex) {
      ex.printStackTrace();
    }

    this.firstName = firstName;
    this.lastName = lastName;
  }

  /**
   * Change the credit card number of this <code>Client</code> to the given
   * credit card number.
   *
   * @param creditCardNumber
   *          the new credit card number for this <code>Client</code>
   */
  public void setcreditCardNumber(String creditCardNumber) {
    this.creditCardNumber = creditCardNumber;
  }

  /**
   * Return credit card number of this <code>Client</code>.
   *
   * @return credit card number of this <code>Client</code>
   */
  public String getCreditCardNumber() {
    return this.creditCardNumber;
  }

  /**
   * Return the expiry date of this <code>Client</code>.
   *
   * @return the expiry date of this <code>Client</code>
   */
  public Date getExpiry() {
    return this.expiry;
  }

  /**
   * Change the expiry date of this <code>Client</code> to the given expiry
   * date.
   *
   * @param expiry
   *          the new expiry date for this <code>Client</code>
   */
  public void setExpiry(Date expiry) {
    this.expiry = expiry;
  }

  /**
   * Return the first name of this <code>Client</code>.
   *
   * @return the first name of this <code>Client</code>
   */
  public String getFirstName() {
    return this.firstName;
  }

  /**
   * Change the first name of this <code>Client</code> to the given first name.
   *
   * @param firstName
   *          the new first name for this <code>Client</code>
   */
  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  /**
   * Return the last name of this <code>Client</code>.
   *
   * @return the last name of this <code>Client</code>
   */
  public String getLastName() {
    return lastName;
  }

  /**
   * Change the last name of this <code>Client</code> to the given last name.
   *
   * @param lastName
   *          the new last name for this <code>Client</code>
   */
  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  /**
   * Return the personal address of this <code>Client</code>.
   *
   * @return the personal address of this <code>Client</code>
   */
  public String getPersonalAddress() {
    return this.personalAddress;
  }

  /**
   * Change the personal address of this <code>Client</code> to the given.
   * personal address
   *
   * @param personalAddress
   *          the new personal address for this <code>Client</code>
   */
  public void setPersonalAddress(String personalAddress) {
    this.personalAddress = personalAddress;
  }
}