package cs3500.freecell.controller;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import cs3500.freecell.model.FreecellModel;
import cs3500.freecell.model.PileType;
import cs3500.freecell.model.PlayingCard;
import cs3500.freecell.view.FreecellTextView;

/**
 * Simple Freecell Controller is an implementation of the freecell controller interface using.
 * playing cards.
 */
public class SimpleFreecellController implements FreecellController<PlayingCard> {
  private static FreecellModel<PlayingCard> model;
  private static FreecellTextView view;
  private static Readable input;
  private static Appendable output;

  /**
   * Constructor for simple freecell controller.
   * @param model is the freecell model.
   * @param rd is the readable input.
   * @param ap is the appendable output.
   */
  public SimpleFreecellController(FreecellModel<PlayingCard> model, Readable rd, Appendable ap) {
    if (model == null) {
      throw new IllegalArgumentException("Null Model.");
    }
    if (rd == null) {
      throw new IllegalArgumentException("Null Readable.");
    }
    if (ap == null) {
      throw new IllegalArgumentException("Null Appendable.");
    }
    this.model = model;
    this.input = rd;
    this.output = ap;
    this.view = new FreecellTextView(this.model, this.output);
  }

  /**
   * Play Game Method, starts a game with model and controller.
   * @param deck        the deck to be used to play this game.
   * @param numCascades the number of cascade piles.
   * @param numOpens    the number of open piles.
   * @param shuffle     shuffle the deck if true, false otherwise.
   */
  @Override
  public void playGame(List<PlayingCard> deck, int numCascades, int numOpens, boolean shuffle) {
    try {
      // Check null for deck
      if (deck == null) {
        throw new IllegalArgumentException("Null Deck.");
      }
      // Start the game with provided parameters
      // Catch IAE
      try {
        this.model.startGame(deck, numCascades, numOpens, shuffle);

      } catch (IllegalArgumentException iae) {
        // Early Fail
        this.view.renderMessage("Could not start game.");
        return;
      } catch (IllegalStateException iae) {
        this.view.renderMessage("Could not start game.");
        return;
      }
      Scanner scan = new Scanner(this.input);
      while (!this.model.isGameOver()) {
        this.view.renderBoard();
        this.view.renderMessage("\n");
        // Generate New Input
        // Initiate variables
        PileType sourcePileType = null;
        PileType destinationPileType = null;
        boolean sourcePileNumFound = false;
        boolean destinationPileNumFound = false;
        boolean cardIndexFound = false;
        int sourcePileNumber = -1;
        int cardIndex = -1;
        int destinationPileNumber = -1;

        // Check Input 1
        while (!sourcePileNumFound) {
          try {
            if (!scan.hasNext()) {
              throw new IllegalStateException();
            }
            String sourcePileRepresentation = scan.next();
            this.checkQuit(sourcePileRepresentation);
            sourcePileType = this.getPileType(sourcePileRepresentation);
            sourcePileNumber = this.getPileNumber(sourcePileRepresentation);
            sourcePileNumFound = true;
          } catch (InvalidParamException ipe) {
            this.view.renderMessage(ipe.getMessage() + ", Please Enter Again.\n");
          } catch (PrematureQuitGameException qge) {
            this.view.renderMessage("Game quit prematurely.");
            return;
          }
        }

        // Check Input 2
        while (!cardIndexFound) {
          if (!scan.hasNext()) {
            throw new IllegalStateException();
          }
          try {
            String cardIndexRepresentation = scan.next();
            this.checkQuit(cardIndexRepresentation);
            cardIndex = Integer.parseInt(cardIndexRepresentation);
            cardIndexFound = true;
          } catch (NumberFormatException nfe) {
            this.view.renderMessage(nfe.getMessage() + ", Please Enter Again.\n");
          } catch (PrematureQuitGameException qge) {
            this.view.renderMessage("Game quit prematurely.");
            return;
          }
        }

        // Check Input 3
        while (!destinationPileNumFound) {
          if (!scan.hasNext()) {
            throw new IllegalStateException();
          }
          try {
            String destinationPileRepresentation = scan.next();
            this.checkQuit(destinationPileRepresentation);
            destinationPileType = this.getPileType(destinationPileRepresentation);
            destinationPileNumber = this.getPileNumber(destinationPileRepresentation);
            destinationPileNumFound = true;
          } catch (InvalidParamException ipe) {
            this.view.renderMessage(ipe.getMessage() + ", Please Enter Again.\n");
          } catch (PrematureQuitGameException qge) {
            this.view.renderMessage("Game quit prematurely.");
            return;
          }
        }
        try {
          this.model.move(sourcePileType, sourcePileNumber - 1, cardIndex - 1,
                  destinationPileType, destinationPileNumber - 1);
        } catch (IllegalArgumentException | IndexOutOfBoundsException exception) {
          this.view.renderMessage("Invalid move. Try again.\n");
          this.view.renderMessage(exception.getMessage() + "\n");
        }
      }
      // If Game is Over then show final board, message and return
      this.view.renderBoard();
      this.view.renderMessage("\nGame over.");
      return;
    } catch (IOException ioe) {
      throw new IllegalStateException();
    }
  }

  private void checkQuit(String s) throws PrematureQuitGameException, IOException {
    if (s.equals("Q") || s.equals("q")) {
      throw new PrematureQuitGameException();
    }
  }

  private int getPileNumber(String somePileRepresentation) throws InvalidParamException {
    if (somePileRepresentation.length() < 2) {
      throw new InvalidParamException("No PileType/Integer Present");
    }
    String pileIndexRepresentation = somePileRepresentation.substring(1);
    int pileIndex = -1;
    try {
      pileIndex = Integer.parseInt(pileIndexRepresentation);
    } catch (NumberFormatException nfe) {
      throw new InvalidParamException("Inputted Pile Number Not an Integer");
    }
    return pileIndex;
  }

  private PileType getPileType(String somePileRepresentation) throws InvalidParamException {
    String firstLetter = somePileRepresentation.substring(0,1);
    if (firstLetter.equals("C")) {
      return PileType.CASCADE;
    } else if (firstLetter.equals("O")) {
      return PileType.OPEN;
    } else if (firstLetter.equals("F")) {
      return PileType.FOUNDATION;
    }
    throw new InvalidParamException("Intelligible PileType");
  }
}
