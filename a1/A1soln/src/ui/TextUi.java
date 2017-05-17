package ui;


import game.Constants;
import game.VacuumGame;
import sprites.Vacuum;

import java.io.IOException;



/** A Text Ui for the game.
 * @author sharif53
 *
 */
public class TextUi implements Ui {
  private VacuumGame game;
  
  /** Constructs the TextUi.
   * @param game of type VacuumGame
   */
  public TextUi(VacuumGame game) {
    super();
    this.game = game;
  }
  
  /* (non-Javadoc)
   * @see ui.Ui#launchGame()
   */
  @Override
  public void launchGame() {
    
    // loop until we get a game over
    while (!game.gameOver()) {
      System.out.println(game);
      System.out.println("Next Move:");
      try {
        char input = (char) System.in.read();
        game.move(input);
      } catch (IOException except) {
        except.printStackTrace();
      }
      System.out.flush();
    }
  }

  /* (non-Javadoc)
   * @see ui.Ui#displayWinner()
   */
  @Override
  public void displayWinner() {
    if (!game.gameOver()) {
      return;
    }
    char won = game.getWinner();
    String message;
    if (won == Constants.TIE) {
      message = "It's a tie!";
    } else {
      Vacuum winner = (won == Constants.P1) ? game.getVacuumOne() : game.getVacuumTwo();
      int score = winner.getScore();
      message = String.format("Congratulations Player %s! You won the game with a score of %d.",
          winner.toString(), score);
      
      System.out.println(message);
    }
  }

}
