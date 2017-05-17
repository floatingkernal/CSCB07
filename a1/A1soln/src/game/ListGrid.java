package game;

import java.util.ArrayList;

/** A Grid made out of ArrayLists.
 * @author sharif53
 *
 */
public class ListGrid<T> extends Grid<T> {
  private int numRows;
  private int numColumns;
  private ArrayList<ArrayList> grid;
  
  /** Constructs the ListGrid with the dimensions provided.
   * @param numRows of type int
   * @param numColumns of type int
   */
  public ListGrid(int numRows, int numColumns) {
    this.numColumns = numColumns;
    this.numRows = numRows;
    this.grid = new ArrayList<>(numRows);
    for (int i = 0; i < numRows; i++) {
      ArrayList<T> columns = new ArrayList<>(numColumns);
      for (int j = 0; j < numColumns; j++) {
        columns.add(null);
      }
      this.grid.add(columns);
    }
  }

  /* (non-Javadoc)
   * @see game.Grid#getCell(int, int)
   */
  @Override
  public T getCell(int row, int column) {
    return (T) this.grid.get(row).get(column);
  }

  /* (non-Javadoc)
   * @see game.Grid#setCell(int, int, java.lang.Object)
   */
  @Override
  public void setCell(int row, int column, T item) {
    this.grid.get(row).set(column, item);
  }

  /* (non-Javadoc)
   * @see game.Grid#getNumRows()
   */
  @Override
  public int getNumRows() {
    return this.numRows;
  }

  /* (non-Javadoc)
   * @see game.Grid#getNumColumn()
   */
  @Override
  public int getNumColumn() {
    return this.numColumns;
  }

  /* (non-Javadoc)
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((grid == null) ? 0 : grid.hashCode());
    result = prime * result + numColumns;
    result = prime * result + numRows;
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
    ListGrid other = (ListGrid) obj;
    if (grid == null) {
      if (other.grid != null) {
        return false;
      }
    } else if (!grid.equals(other.grid)) {
      return false;
    }
    if (numColumns != other.numColumns) {
      return false;
    }
    if (numRows != other.numRows) {
      return false;
    }
    return true;
  }


}
