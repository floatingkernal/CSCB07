package game;

import java.util.HashMap;

/** A Grid class made up of HashMaps.
 * @author sharif53
 *
 */
public class MapGrid<T> extends Grid<T> {
  private HashMap<Integer, HashMap<Integer, T>> grid;
  private int numRows;
  private int numColumns;
  
  
  /** Constructs the MapGrid with the dimensions provided.
   * @param numRows of type int
   * @param numColumns of type int
   */
  public MapGrid(int numRows, int numColumns) {
    this.numColumns = numColumns;
    this.numRows = numRows;
    
    this.grid = new HashMap<Integer, HashMap<Integer, T>>(numRows);
    
    // create the grid with hash map
    for (int x = 0; x < numRows; x++) {
      HashMap<Integer, T> cols = new HashMap<Integer, T>(numColumns);
      grid.put(x, cols);
    }

  }

  /* (non-Javadoc)
   * @see game.Grid#getCell(int, int)
   */
  @Override
  public T getCell(int row, int column) {
    
    HashMap<Integer, T> ro = this.grid.get(row);
    return ro.get(column);
  }

  /* (non-Javadoc)
   * @see game.Grid#setCell(int, int, java.lang.Object)
   */
  @Override
  public void setCell(int row, int column, T item) {
    HashMap<Integer, T> ro = this.grid.get(row);
    ro.put(column, item);
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
    MapGrid<T> other = (MapGrid<T>) obj;
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
