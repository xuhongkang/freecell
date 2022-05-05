import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.List;
import java.util.Random;

import cs3500.freecell.model.PileType;
import cs3500.freecell.model.PlayingCard;
import cs3500.freecell.model.multimove.EnhancedFreecellModel;

/**
 * Test for Enhanced Freecell Model.
 */
public class EnhancedFreecellModelTest {

  EnhancedFreecellModel model1;
  List<PlayingCard> deck1;

  @Before
  public void setup() {
    model1 = new EnhancedFreecellModel();
    deck1 = model1.getDeck();
    Collections.shuffle(deck1, new Random(182378));
    model1.startGame(deck1, 20, 3, false);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testBuildMoveNoEmptyCascade() {
    model1.move(PileType.CASCADE, 16, 1, PileType.CASCADE, 17);
    model1.move(PileType.CASCADE, 3, 2, PileType.OPEN, 0);
    model1.move(PileType.CASCADE, 2, 2, PileType.OPEN, 1);
    model1.move(PileType.CASCADE, 1, 2, PileType.OPEN, 2);
    model1.move(PileType.CASCADE, 17, 1, PileType.CASCADE, 6);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testBuildMoveTargEmptyCascade() {
    model1.move(PileType.CASCADE, 16, 1, PileType.CASCADE, 17);
    model1.move(PileType.CASCADE, 0, 2, PileType.OPEN, 0);
    model1.move(PileType.CASCADE, 0, 1, PileType.OPEN, 1);
    model1.move(PileType.CASCADE, 0, 0, PileType.OPEN, 2);
    model1.move(PileType.CASCADE, 17, 0, PileType.CASCADE, 6);
  }
}
