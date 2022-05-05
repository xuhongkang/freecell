package cs3500.freecell.controller;

import java.util.List;

/**
 * Interface for the freecell game controller. An implementation will work with
 * the IFreeCellModel interface to provide a game of freecell
 */
public interface FreecellController<K> {

  /**
   * Start and play a new game of freecell with the provided deck. This deck
   * should be used as-is. This method returns only when the game is over
   * (either by winning or by quitting).
   * @param deck the deck to be used to play this game
   * @param numCascades the number of cascade piles
   * @param numOpens the number of open piles
   * @param shuffle shuffle the deck if true, false otherwise
   * @throws IllegalStateException if writing to the Appendable object fails
   * @throws IllegalArgumentException if the deck provided to it are null
   */
  void playGame(List<K> deck, int numCascades, int numOpens, boolean shuffle);

}
