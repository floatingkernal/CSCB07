package sprites;

import game.Constants;

/** A dirt represented as dust.
 * @author sharif53
 *
 */
public class Dust extends Dirt {

  /** Constructor for dust.
   * @param row int
   * @param column int
   * @param value int
   */
  public Dust(int row, int column, int value) {
    super(Constants.DUST, row, column, value);
  }

}
