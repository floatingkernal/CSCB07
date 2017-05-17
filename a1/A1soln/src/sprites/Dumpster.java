package sprites;

import game.Constants;

/** A sprite for Dumpster.
 * @author sharif53
 *
 */
public class Dumpster extends Sprite {

  /** Constructs the Dumpster.
   * @param row int must be less than number of rows
   * @param column int must be less than number of columns
   */
  public Dumpster(int row, int column) {
    super(Constants.DUMPSTER, row, column);
  }

}
