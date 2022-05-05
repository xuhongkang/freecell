import java.io.InputStreamReader;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import cs3500.freecell.controller.SimpleFreecellController;
import cs3500.freecell.model.FreecellModel;
import cs3500.freecell.model.PlayingCard;
import cs3500.freecell.model.multimove.EnhancedFreecellModel;

/**
 * Main Class for freecell game.
 */
public class Main {
  /**
   * Main method of main class.
   * @param args is proper setup duh just kidding its the list of arguments to call luckily none.
   */
  public static void main(String[] args) {
    FreecellModel model1 = new EnhancedFreecellModel();
    List<PlayingCard> deck1 = model1.getDeck();
    Collections.shuffle(deck1, new Random(182378));
    SimpleFreecellController controller = new SimpleFreecellController(model1,
            new InputStreamReader(System.in), System.out);
    controller.playGame(deck1, 20, 3, false);
  }
}

