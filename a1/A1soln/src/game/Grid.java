package game;

/** An abstract class for the Grid object.
 * @author sharif53
 *
 * @param <T> of what the grid will store
 */
public abstract class Grid<T> {
  
  /** Gets the cell at the location provided.
   * @param row of type int must be less than numRows
   * @param column of type int must be less than numColumns
   * @return the object of type T located at this cell
   */
  public abstract T getCell(int row, int column);
  
  /** Sets the item into the cell.
   * @param row of type int must be less than numRows
   * @param column of type int must be less than numColumns
   * @param item of type T
   */
  public abstract void setCell(int row, int column, T item);
  
  /** Gets the number of rows.
   * @return int
   */
  public abstract int getNumRows();
  
  /** Gets the number of columns.
   * @return int
   */
  public abstract int getNumColumn();
  
  /* (non-Javadoc)
   * @see java.lang.Object#hashCode()
   */
  public abstract int hashCode();
  
  
  /* (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    String res = new String();
    int rows = this.getNumRows();
    int cols = this.getNumColumn();
    // loop over each element in the grid and add it to the end of the string
    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < cols; j++) {
        res += this.getCell(i, j);
      }
      res += "\n";
    }
    return res;
  }
  
  
}
