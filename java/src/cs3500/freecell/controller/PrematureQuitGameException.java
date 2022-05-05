package cs3500.freecell.controller;

/**
 * Premature Quit Game Exception is an exception that is run when the game prematurely quits.
 * Due to input.
 */
public class PrematureQuitGameException extends Exception {
  /**
   * Constructor for premature quit game exception.
   */
  public PrematureQuitGameException() {
    super("Quit Via Input");
  }
}
