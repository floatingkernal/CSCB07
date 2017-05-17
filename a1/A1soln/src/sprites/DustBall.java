package sprites;

import game.Constants;

/** A dirt represented as dustball.
 * @author sharif53
 *
 */
public class DustBall extends Dirt {

  /** Constructs the DustBall.
   * @param row int must be less than num rows
   * @param column int must be less than num columns.
   * @param value of the dustball when cleaned up 
   */
  public DustBall(int row, int column, int value) {
    super(Constants.DUST_BALL, row, column, value);
  }
  
  /** Moves the DustBall.
   * @param row int must be less than num rows
   * @param column int must be less than num columns.
   */
  public void moveTo(int row, int column) {
    // if the new cord is not negative or has a wall, then move this vacuum to the new cord.
    if (row >= 0 && column >= 0) {
      super.updateCoordinates(row, column);
    }
  }

}
