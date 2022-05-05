package cs3500.freecell.model;

import java.util.Objects;

import cs3500.freecell.model.multimove.EnhancedFreecellModel;

/**
 * Creates a freecell model that accepts wither singlemove or multimove moves.
 */
public class FreecellModelCreator {
  /**
   * GameType Enum to for factory method create params.
   */
  public enum GameType {
    SINGLEMOVE, MULTIMOVE
  }

  /**
   * Public Static Factory Method to create Freecell Model of given GameType.
   * Throws Illegal Argument Exception if null GameType supplied.
   * @param gt refers to the target game type (single/multi move)
   * @return Simple Freecell Model or Enhanced Freecell Model
   */
  public static FreecellModel create(GameType gt) {
    if (Objects.isNull(gt)) {
      throw new IllegalArgumentException("Null GameType Supplied");
    } else if (gt.equals(GameType.SINGLEMOVE)) {
      return new SimpleFreecellModel();
    } else if (gt.equals(GameType.MULTIMOVE)) {
      return new EnhancedFreecellModel();
    } else {
      throw new IllegalArgumentException("Input Not Recognized");
    }
  }
}
