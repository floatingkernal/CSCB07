package travel;

import java.io.Serializable;

/**
 * Administrator class. A subclass of User class.
 *
 * @author Li
 *
 */
public class Administrator extends User implements Serializable {
  private static final long serialVersionUID = -1199524990130401955L;

  /**
   * Inherits <code>User</code>
   *
   * @param email
   *          Create a new <code>Administrator</code> using <code>User</code> as
   *          a superclass.
   */
  public Administrator(String email) {
    super(email);
  }
}