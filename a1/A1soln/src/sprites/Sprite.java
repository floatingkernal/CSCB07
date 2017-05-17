package sprites;

/** A object represented as sprite.
 * @author sharif53
 *
 */
public abstract class Sprite {
  private char symbol;
  private int row;
  private int column;
  
  /** Constructs the Sprite.
   * @param symbol char
   * @param row int
   * @param column int
   */
  public Sprite(char symbol, int row, int column) {
    super();
    this.symbol = symbol;
    this.row = row;
    this.column = column;
  }

  /** Gets the Symbol.
   * @return the symbol
   */
  public char getSymbol() {
    return symbol;
  }

  /** Gets the row.
   * @return the row
   */
  public int getRow() {
    return row;
  }

  /** Gets the column.
   * @return the column
   */
  public int getColumn() {
    return column;
  }

  /* (non-Javadoc)
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + column;
    result = prime * result + row;
    result = prime * result + symbol;
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
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    Sprite other = (Sprite) obj;
    if (column != other.column) {
      return false;
    }
    if (row != other.row) {
      return false;
    }
    if (symbol != other.symbol) {
      return false;
    }
    return true;
  }

  /* (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    String res = new String();
    res += this.symbol;
    return res;
  }
  
  /** Changes the sprite's coordinates.
   * @param row int
   * @param column int
   */
  protected void updateCoordinates(int row, int column) {
    this.column = column;
    this.row = row;
  }
}
