package ui;

import game.Constants;
import game.VacuumGame;
import sprites.Vacuum;

import java.awt.Container;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;

/** A simple GUI for the game. */
public class Gui extends JFrame implements Ui {

  private static final long serialVersionUID = 1L; // default
  private VacuumGame game;
  private JLabel[][] tiles;

  /** Initializes a GUI for the given VacuumGame.
   * @param game The VacuumGame of this GUI 
   */
  public Gui(VacuumGame game) {
    this.game = game;
  }

  /** Returns the VacuumGame of this GUI.
   * @return the VacuumGame of this GUI
   */
  public VacuumGame getGame() {
    return game;
  }

  @Override
  public void launchGame() {
    int numRows = game.getNumRows();
    int numCols = game.getNumColumns();

    Container container = this.getContentPane();
    container.setLayout(new GridLayout(numRows, numCols));
    tiles = new JLabel[numRows][numCols];

    GuiListener listener = new GuiListener(this);
    addKeyListener(listener);

    for (int i = 0; i < numRows; i++) {
      for (int j = 0; j < numCols; j++) {
        JLabel label = new JLabel();
        label.setText(game.getSprite(i, j).toString());        
        label.setFont(new Font(null, Font.BOLD, 18));
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setVerticalAlignment(SwingConstants.CENTER);
        container.add(label);
        tiles[i][j] = label;
      }
    }
    setVisible(true);
    pack();
  }

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
    }
    JOptionPane.showMessageDialog(null, message);
    setVisible(false);
  }

  /** Update the grid display. */
  public void updateLabels() {
    int numRows = game.getNumRows();
    int numCols = game.getNumColumns();

    for (int i = 0; i < numRows; i++) {
      for (int j = 0; j < numCols; j++) {
        this.tiles[i][j].setText(game.getSprite(i, j).toString());
      }
    }
    displayWinner();
  }
}