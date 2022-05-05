import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import cs3500.freecell.model.FreecellModel;
import cs3500.freecell.model.PlayingCard;
import cs3500.freecell.model.SimpleFreecellModel;
import cs3500.freecell.view.FreecellTextView;

import static org.junit.Assert.assertEquals;

/**
 * JUnit test cases for the text model.
 */
public class FreecellViewTest {
  FreecellModel<PlayingCard> testModel;
  FreecellTextView testView;
  FreecellTextView testViewF;
  String testString;

  @Before
  public void setUp() {
    testModel = new SimpleFreecellModel();
    testModel.startGame(testModel.getDeck(),4,4, false);
    testView = new FreecellTextView(testModel);
    testViewF = new FreecellTextView(testModel, new FailingAppendable());
    testString = "F1:\n" +
            "F2:\n" +
            "F3:\n" +
            "F4:\n" +
            "O1:\n" +
            "O2:\n" +
            "O3:\n" +
            "O4:\n" +
            "C1: A♠, 5♠, 9♠, K♠, 4♣, 8♣, Q♣, 3♥, 7♥, J♥, 2♦, 6♦, 10♦\n" +
            "C2: 2♠, 6♠, 10♠, A♣, 5♣, 9♣, K♣, 4♥, 8♥, Q♥, 3♦, 7♦, J♦\n" +
            "C3: 3♠, 7♠, J♠, 2♣, 6♣, 10♣, A♥, 5♥, 9♥, K♥, 4♦, 8♦, Q♦\n" +
            "C4: 4♠, 8♠, Q♠, 3♣, 7♣, J♣, 2♥, 6♥, 10♥, A♦, 5♦, 9♦, K♦";
  }

  @Test
  public void testToString() {
    assertEquals(testString, testView.toString());
  }

  @Test
  public void testFailRenderBoard() {
    try {
      testViewF.renderBoard();
    } catch (IOException ioe) {
      assertEquals("Fail!", ioe.getMessage());
    }
  }

  @Test
  public void testFailRenderMessage() {
    try {
      testViewF.renderMessage("Random Message");
    } catch (IOException ioe) {
      assertEquals("Fail!", ioe.getMessage());
    }
  }
}
