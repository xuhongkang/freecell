package cs3500.freecell.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

/**
 * Simple Freecell Model Implementation that Implements the Freecell Model Interface.
 */
public class SimpleFreecellModel implements FreecellModel<PlayingCard> {
  protected List<List<PlayingCard>> cascadePiles;
  protected List<List<PlayingCard>> foundationPiles;
  protected List<List<PlayingCard>> openPiles;
  protected boolean gameStarted;

  /**
   * Constructor for simple freecell model, initializes all to null or false.
   */
  public SimpleFreecellModel() {
    this.cascadePiles = new ArrayList<List<PlayingCard>>();
    this.openPiles = new ArrayList<List<PlayingCard>>();
    this.foundationPiles = new ArrayList<List<PlayingCard>>();
    this.gameStarted = false;
  }

  @Override
  public List<PlayingCard> getDeck() {
    List<PlayingCard> deck = new ArrayList<PlayingCard>();
    CardSuit[] suit_choices = {CardSuit.SPADE, CardSuit.CLUB, CardSuit.HEART, CardSuit.DIAMOND};
    for (CardSuit suit_choice:suit_choices) {
      for (int i = 1; i < 14; i++) {
        PlayingCard card = new PlayingCard(suit_choice, i);
        deck.add(card);
      }
    }
    return deck;
  }

  private void checkDeck(List<PlayingCard> deck) {
    // Check Deck Size
    if (deck.isEmpty()) {
      throw new IllegalArgumentException("Empty Deck");
    } else {
      if (deck.size() != 52) {
        throw new IllegalArgumentException("Invalid Deck, Size Not 52");
      }
    }
    // Check Duplicates in Deck
    if (!deck.stream().allMatch(a -> a instanceof PlayingCard)) {
      throw new IllegalArgumentException("Invalid Deck, Not Playingcard");
    }
    // Check Duplicates in Deck
    if (!deck.stream().allMatch(new HashSet<>()::add)) {
      throw new IllegalArgumentException("Invalid Deck, Duplicate PlayingCard");
    }
  }

  @Override
  public void startGame(List<PlayingCard> deck, int numCascadePiles, int numOpenPiles,
                        boolean shuffle) {
    // Check if Deck is Valid
    this.checkDeck(deck);
    // Check Parameters
    if (numCascadePiles < 4) {
      throw new IllegalArgumentException("Invalid Parameters, Number of Cascade Piles should " +
              "be no less than 4");
    }
    if (numOpenPiles < 1) {
      throw new IllegalArgumentException("Invalid Parameters, Number of Cascade Piles should " +
              "be no less than 1");
    }
    if (this.gameStarted) {
      this.cascadePiles = new ArrayList<List<PlayingCard>>();
      this.openPiles = new ArrayList<List<PlayingCard>>();
      this.foundationPiles = new ArrayList<List<PlayingCard>>();
      this.gameStarted = false;
    }
    // Shuffle Deck if Shuffle True
    if (shuffle) {
      Collections.shuffle(deck);
      this.checkDeck(deck);
    }
    // Initiate Cascade Piles
    for (int i = 0; i < numCascadePiles; i++) {
      this.cascadePiles.add(new ArrayList<PlayingCard>());
    }
    // Initiate Open Piles
    for (int i = 0; i < numOpenPiles; i++) {
      this.openPiles.add(new ArrayList<PlayingCard>());
    }
    // Initiate Foundation Piles
    for (int i = 0; i < 4; i++) {
      this.foundationPiles.add(new ArrayList<PlayingCard>());
    }
    // Deal Cards Round Robin Style
    for (int i = 0; i < numCascadePiles; i++) {
      for (int n = i; n < 52; n += numCascadePiles) {
        this.cascadePiles.get(i).add(deck.get(n));
      }
    }
    this.gameStarted = true;
  }

  private void checkGameStart() {
    // Check if Game has Started
    if (!this.gameStarted) {
      throw new IllegalStateException("Invalid Move, Game Hasn't Started Yet");
    }
  }

  @Override
  public void move(PileType source, int pileNumber, int cardIndex, PileType destination,
                   int destPileNumber) {
    PlayingCard card = this.getSourceCard(source, pileNumber, cardIndex);
    this.moveToDestination(card, destination, destPileNumber);
    this.removeCard(source, pileNumber, cardIndex);
  }

  private void removeCard(PileType source, int pileNumber, int cardIndex) {
    if (source.equals(PileType.OPEN)) {
      this.openPiles.get(pileNumber).remove(cardIndex);
    } else {
      // Only Cascade Cards at this point
      this.cascadePiles.get(pileNumber).remove(cardIndex);
    }
  }

  private PlayingCard getSourceCard(PileType source, int pileNumber, int cardIndex) {
    // Return Card Based On Source Type
    if (source.equals(PileType.OPEN)) {
      if (cardIndex == 0) {
        return getOpenCardAt(pileNumber);
      } else {
        throw new IllegalArgumentException("Target Open Pile Slot Has Only One Card");
      }
    } else if (source.equals(PileType.CASCADE)) {
      return getCascadeCardAt(pileNumber, cardIndex);
    } else {
      // Cannot Move From Foundations
      throw new IllegalArgumentException("Cannot Move Card From Foundation Pile");
    }
  }

  private void moveToDestination(PlayingCard card, PileType tarType, int tarNum) {
    // Get Card Properties
    CardSuit cardSuit = card.getSuit();
    int cardValue = card.getValue();
    ColorType cardColor = card.getColor();

    // Search Destination Conditions
    if (tarType.equals(PileType.CASCADE)) {
      // For Moving to Cascade Pile
      // Get Properties of Last Card
      List<PlayingCard> tarPile = this.cascadePiles.get(tarNum);
      if (tarPile.isEmpty()) {
        tarPile.add(card);
      } else {
        PlayingCard lastCard = tarPile.get(tarPile.size() - 1);
        ColorType lCardColor = lastCard.getColor();
        int lCardValue = lastCard.getValue();
        // Check If the Conditions Match
        if (!lCardColor.equals(cardColor)) {
          if (lCardValue != cardValue + 1) {
            tarPile.add(card);
          } else {
            throw new IllegalArgumentException("Illegal Move, Not One Less than Last Card");
          }
        } else {
          throw new IllegalArgumentException("Illegal Move, Not Opposite Colors with Last Card");
        }
      }
    } else if (tarType.equals(PileType.FOUNDATION)) {
      // For Moving to Foundation Pile
      List<PlayingCard> tarPile = this.foundationPiles.get(tarNum);
      // If empty
      if (tarPile.isEmpty()) {
        if (cardValue == 1) {
          tarPile.add(card);
        } else {
          throw new IllegalArgumentException("Invalid Move, Foundation Piles Can Only Start With " +
                  "Aces");
        }
      } else {
        PlayingCard lastCard = tarPile.get(tarPile.size() - 1);
        CardSuit lCardSuit = lastCard.getSuit();
        int lCardValue = lastCard.getValue();
        // Check if Conditions Match
        if (lCardSuit.equals(cardSuit)) {
          if (lCardValue == cardValue - 1) {
            tarPile.add(card);
          } else {
            throw new IllegalArgumentException("Illegal Move, Not One Bigger than Last Card");
          }
        } else {
          throw new IllegalArgumentException("Illegal Move, Not Same Suit as Last Card");
        }
      }
    } else {
      // For Moving to Open Pile
      if (this.openPiles.get(tarNum).isEmpty()) {
        this.openPiles.get(tarNum).add(card);
      } else {
        throw new IllegalArgumentException("Invalid Move, Attempt to Move to Occupied Open Pile " +
                "Slot");
      }
    }
  }

  @Override
  public boolean isGameOver() {
    boolean gameOver = true;
    for (int n = 0; n < 4; n++ ) {
      if (getNumCardsInFoundationPile(n) != 13) {
        gameOver = false;
      }
    }
    return gameOver;
  }

  @Override
  public int getNumCardsInFoundationPile(int index) {
    this.checkGameStart();
    if (index < 4) {
      List<PlayingCard> tarPile = this.foundationPiles.get(index);
      return tarPile.size();
    } else {
      throw new IllegalArgumentException("Target Foundation Pile Index Not Found");
    }
  }

  @Override
  public int getNumCascadePiles() {
    if (!this.gameStarted) {
      return -1;
    }
    return this.cascadePiles.size();
  }

  @Override
  public int getNumCardsInCascadePile(int index) {
    this.checkGameStart();
    if (index < this.getNumCascadePiles() && (index >= 0)) {
      List<PlayingCard> tarPile = this.cascadePiles.get(index);
      return tarPile.size();
    } else {
      throw new IllegalArgumentException("Target Cascade Pile Index Not Found");
    }
  }

  @Override
  public int getNumCardsInOpenPile(int index) {
    this.checkGameStart();
    if (index < this.getNumOpenPiles() && (index >= 0)) {
      return this.openPiles.get(index).size();
    } else {
      throw new IllegalArgumentException("Target Car Pile Index Not Found");
    }
  }

  @Override
  public int getNumOpenPiles() {
    if (!this.gameStarted) {
      return -1;
    }
    return this.openPiles.size();
  }

  @Override
  public PlayingCard getFoundationCardAt(int pileIndex, int cardIndex) {
    if (cardIndex < getNumCardsInFoundationPile(pileIndex) && (cardIndex >= 0)) {
      return this.foundationPiles.get(pileIndex).get(cardIndex);
    } else {
      throw new IllegalArgumentException("Target Card Index Not Found in Foundation Pile");
    }
  }

  @Override
  public PlayingCard getCascadeCardAt(int pileIndex, int cardIndex) {
    if (cardIndex < getNumCardsInCascadePile(pileIndex) && (cardIndex >= 0)) {
      return this.cascadePiles.get(pileIndex).get(cardIndex);
    } else {
      throw new IllegalArgumentException("Target Card Index Not Found in Cascade Pile");
    }
  }

  @Override
  public PlayingCard getOpenCardAt(int pileIndex) {
    this.checkGameStart();
    if (pileIndex < this.getNumOpenPiles() && pileIndex >= 0) {
      List<PlayingCard> tarPile = this.openPiles.get(pileIndex);
      if (!tarPile.isEmpty()) {
        return tarPile.get(0);
      } else {
        return null;
      }
    } else {
      throw new IllegalArgumentException("Target Open Pile Slot Not Found");
    }
  }
}
