import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import cs3500.freecell.model.CardSuit;
import cs3500.freecell.model.ColorType;
import cs3500.freecell.model.PileType;
import cs3500.freecell.model.PlayingCard;
import cs3500.freecell.model.SimpleFreecellModel;

import static org.junit.Assert.assertEquals;

/**
 * JUnit test cases for the freecell model.
 */
public class FreecellModelTest {
  PlayingCard testCard1;
  PlayingCard testCard2;
  PlayingCard testCard3;
  PlayingCard testCard4;
  List<PlayingCard> testDeck1;
  SimpleFreecellModel testModel1;
  SimpleFreecellModel testModel2;

  @Before
  public void setUp() {
    testModel1 = new SimpleFreecellModel();
    testModel2 = new SimpleFreecellModel();
    testModel2.startGame(testModel2.getDeck(),4,4, false);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCardSuit() {
    testCard1 = new PlayingCard(null, 14);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCardValue() {
    testCard1 = new PlayingCard(CardSuit.CLUB, 14);
    testCard2 = new PlayingCard(CardSuit.CLUB, 0);
  }

  @Test
  public void testCardColor() {
    testCard1 = new PlayingCard(CardSuit.CLUB, 13);
    assertEquals(testCard1.getColor(), ColorType.BLACK);
  }

  @Test
  public void testCardString() {
    testCard1 = new PlayingCard(CardSuit.CLUB, 13);
    testCard2 = new PlayingCard(CardSuit.SPADE, 12);
    testCard3 = new PlayingCard(CardSuit.DIAMOND, 11);
    testCard4 = new PlayingCard(CardSuit.HEART, 1);
    assertEquals(testCard1.toString(), "K♣");
    assertEquals(testCard2.toString(), "Q♠");
    assertEquals(testCard3.toString(), "J♦");
    assertEquals(testCard4.toString(), "A♥");
  }

  @Test
  public void testCardEquals() {
    testCard1 = new PlayingCard(CardSuit.CLUB, 13);
    testCard2 = new PlayingCard(CardSuit.SPADE, 13);
    testCard3 = new PlayingCard(CardSuit.CLUB, 12);
    assertEquals(false, testCard1.equals(testCard2));
    assertEquals(false, testCard2.equals(testCard3));
    assertEquals(false, testCard3.equals(testCard1));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testModelCheckDeck() {
    testCard1 = new PlayingCard(CardSuit.CLUB, 13);
    testCard2 = new PlayingCard(CardSuit.CLUB, 13);
    testDeck1 = new ArrayList<PlayingCard>();
    testDeck1.add(testCard1);
    testDeck1.add(testCard2);
    testModel1.startGame(testDeck1,8,4, true);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testStartDeck() {
    testModel1.startGame(testModel1.getDeck(),3,0, true);
  }

  @Test
  public void testMove() {
    testModel2.move(PileType.CASCADE, 3, 12, PileType.OPEN, 0);
    assertEquals(testModel2.getOpenCardAt(0), new PlayingCard(CardSuit.DIAMOND, 13));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMove2() {
    testModel2.move(PileType.CASCADE, 3, 12, PileType.FOUNDATION, 1);
  }

  @Test
  public void testGetNumCardsInFoundationPile() {
    assertEquals(0, testModel2.getNumCardsInFoundationPile(0));
  }

  @Test
  public void testGetNumCascadePiles() {
    assertEquals(4, testModel2.getNumCascadePiles());
  }

  @Test
  public void testGetNumCardsInCascadePile() {
    assertEquals(13, testModel2.getNumCardsInCascadePile(0));
  }

  @Test
  public void testGetNumCardsInOpenPile() {
    assertEquals(0, testModel2.getNumCardsInOpenPile(0));
  }

  @Test
  public void testGetNumOpenPiles() {
    assertEquals(4, testModel2.getNumOpenPiles());
  }

  @Test
  public void testGetCascadeCardAt() {
    assertEquals(new PlayingCard(CardSuit.DIAMOND, 10), testModel2.getCascadeCardAt(0, 12));
  }
}