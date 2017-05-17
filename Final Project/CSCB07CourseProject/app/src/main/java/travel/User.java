package travel;

/**
 * Abstract class User.
 *
 * @author Cameron, Li
 *
 */
public abstract class User implements java.io.Serializable {
  private static final long serialVersionUID = -5611951610128903738L;
  private String email;

  public User() {
    email = "";
  }

  /**
   * Creates a new <code>User</code> with given email.
   *
   * @param email
   *          the email address of the new <code>User</code>
   */
  public User(String email) {
    this.email = email;
  }

  /**
   * Return the email address of this <code>User</code>.
   *
   * @return the email address of this <code>User</code>
   */
  public String getEmail() {
    return email;
  }
}