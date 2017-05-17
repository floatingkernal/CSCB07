package sprites;

import game.Constants;

/** A class for Wall Sprite.
 * @author sharif53
 *
 */
public class Wall extends Sprite {

  /** Constructs the Wall.
   * @param row of type int must be less than num rows
   * @param column of type int must be less than num columns
   */
  public Wall(int row, int column) {
    super(Constants.WALL, row, column);
  }

}
