package sprites;

import game.Constants;

/** A sprite for Vacuum.
 * @author sharif53
 *
 */
public class Vacuum extends Sprite {
  private int score;
  private int capacity;
  private int fullness;
  private Sprite under;
  
  /** Constructs the Vacuum.
   * @param symbol of type char of the Vacuum
   * @param row of type int must be less than num rows
   * @param column of type int must be less than num columns
   */
  public Vacuum(char symbol, int row, int column, int capacity) {
    super(symbol, row, column);
    this.capacity = capacity;
    this.fullness = 0;
  }
  
  /** Moves the Vacuum.
   * @param row of type int must be less than num rows
   * @param column of type int must be less than num columns
   */
  public void moveTo(int row, int column) {
    // if the new cord is not negative or has a wall, then move this vacuum to the new cord.
    if (row >= 0 && column >= 0) {
      super.updateCoordinates(row, column);
    }
  }
  
  /** Cleans what is under the Vacuum.
   * @param score int
   * @return boolean
   */
  public boolean clean(int score) {
    boolean res = false;
    // check if the vac is not full
    if (this.fullness < capacity) {
      // check if under is a dust or a dust ball
      if (under instanceof Dirt) {
        // increment fullness and score
        // place a clean hallway under vacuum
        fullness += Constants.FULLNESS_INC;
        this.score += ((Dirt) under).getValue();
        under = new CleanHallway(super.getRow(), super.getColumn());
        res = true;
      }
    }

    return res;
  }
  
  /** Empties the vacuum.
   * 
   */
  public void empty() {
    // if the vacuum is on top of a dumpster
    if (under.getSymbol() == Constants.DUMPSTER) {
      // set the fullness of the vacuum to fullness init.
      fullness = 0;
    }
  }
  
  /** Gets the sprite under.
   * @return the under
   */
  public Sprite getUnder() {
    return under;
  }

  /** Sets the sprite under.
   * @param under of sprite to set
   */
  public void setUnder(Sprite under) {
    this.under = under;
  }

  /** Gets the score.
   * @return int
   */
  public int getScore() {
    return this.score;
  }
}
