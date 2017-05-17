package sprites;

/** A sprite for Dirt.
 * @author sharif53
 *
 */
public abstract class Dirt extends Sprite {
  private int value;

  /** Constructs the Dirt.
   * @param symbol char that defines the sprite
   * @param row int must be less than num rows in the grid
   * @param column int must be less than num columns in the grid
   * @param value of the dirt that will get added to the score
   */
  public Dirt(char symbol, int row, int column, int value) {
    super(symbol, row, column);
    this.value = value;
  }

  /** Gets the value of the dirt.
   * @return the value
   */
  public int getValue() {
    return value;
  }

  /* (non-Javadoc)
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + value;
    return result;
  }

  /* (non-Javadoc)
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!super.equals(obj)) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    Dirt other = (Dirt) obj;
    if (value != other.value) {
      return false;
    }
    return super.equals(obj);
  }
  
}
