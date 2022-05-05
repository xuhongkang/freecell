package cs3500.freecell.model;

import java.util.Objects;

/**
 * Card Class For Standard 52-Deck Playing Cards.
 */
public class PlayingCard {
  private final CardSuit suit;
  private final int value;

  /**
   * Constructor for PlayingCard class.
   * @param suit is the suit of the card.
   * @param value is the value of the card, from 1 to 13 (A to K).
   * @throws IllegalArgumentException for invalid card value.
   */
  public PlayingCard(CardSuit suit, int value) {
    if (Objects.isNull(suit)) {
      throw new IllegalArgumentException("Null Suit Supplied");
    }
    if ((value < 0) || (value > 13)) {
      throw new IllegalArgumentException("Invalid Value Outside Range");
    }
    this.suit = suit;
    this.value = value;
  }

  /**
   * Get the value of the Card.
   * @return Int Card Value.
   */
  public int getValue() {
    return this.value;
  }

  /**
   * Get the suit of the Card.
   * @return CardSuit.
   */
  public CardSuit getSuit() {
    return this.suit;
  }

  /**
   * Get the color matching the suit of the card.
   * @return Colortype of the CardSuit.
   */
  public ColorType getColor() {
    if ((suit == CardSuit.SPADE) || (suit == CardSuit.CLUB)) {
      return ColorType.BLACK;
    } else {
      return ColorType.RED;
    }
  }

  /**
   * Overrides built in toString method.
   * @return String representation.
   */
  @Override
  public String toString() {
    String suitStr = this.getSuitStr();
    String valueStr = this.getValueStr();
    return valueStr + suitStr;
  }

  /**
   * Helper method for toString, gets suit symbol.
   * @return String representation.
   */
  private String getSuitStr() {
    if (this.suit == CardSuit.SPADE) {
      return "♠";
    } else if (this.suit == CardSuit.CLUB) {
      return "♣";
    } else if (this.suit == CardSuit.DIAMOND) {
      return "♦";
    } else {
      return "♥";
    }
  }

  /**
   * Helper method for toString, gets value string representation.
   * @return String representation.
   */
  private String getValueStr() {
    if (this.value == 1) {
      return "A";
    } else if (this.value == 11) {
      return "J";
    } else if (this.value == 12) {
      return "Q";
    } else if (this.value == 13) {
      return "K";
    } else {
      return Integer.toString(this.value);
    }
  }


  /**
   * Overrides built-in equals method.
   * @param o is another object.
   * @return boolean for whether equal.
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof PlayingCard)) {
      return false;
    }

    PlayingCard that = (PlayingCard) o;
    return this.suit == that.suit && this.value == that.value;
  }

  /**
   * Overrides built-in hashCode method.
   * @return int for hashcode address with stored objects.
   */
  @Override
  public int hashCode() {
    return Objects.hash(suit, value);
  }
}
