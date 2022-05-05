package cs3500.freecell.model.multimove;

import java.util.ArrayList;
import java.util.List;

import cs3500.freecell.model.CardSuit;
import cs3500.freecell.model.ColorType;
import cs3500.freecell.model.PileType;
import cs3500.freecell.model.PlayingCard;
import cs3500.freecell.model.SimpleFreecellModel;

/**
 * Enhanced Freecell Model Implements Multi-Move between Cascade Piles (Builds).
 */
public class EnhancedFreecellModel extends SimpleFreecellModel {

  /**
   * Constructor for enhanced freecell model.
   */
  public EnhancedFreecellModel() {
    super();
  }

  @Override
  public void move(PileType source, int pileNumber, int cardIndex, PileType destination,
                   int destPileNumber) {
    List<PlayingCard> targetCards = this.getSourceCards(source, pileNumber, cardIndex);
    if (targetCards.size() > 1) {
      this.moveBuildToDestination(targetCards, destination, destPileNumber);
    } else {
      this.moveCardToDestination(targetCards.get(0), destination, destPileNumber);
    }
    this.removeCard(source, pileNumber, cardIndex);
  }

  protected boolean checkAllowance(int buildNum, boolean tarEmpty) {
    int numEmptyOpen = (int) this.openPiles.stream().filter(i -> i.isEmpty()).count();
    int numEmptyCascade = (int) this.cascadePiles.stream().filter(i -> i.isEmpty()).count();
    if (tarEmpty) {
      numEmptyCascade -= 1;
    }
    int allowance = (int) ((numEmptyOpen + 1) * Math.pow(2, numEmptyCascade));
    return buildNum <= allowance;
  }

  protected void moveBuildToDestination(List<PlayingCard> build, PileType tarType, int tarNum) {
    if (tarType.equals(PileType.CASCADE)) {
      List<PlayingCard> tarPile = this.cascadePiles.get(tarNum);

      if (tarPile.isEmpty()) {
        tarPile.addAll(build);
      } else {
        PlayingCard lastCard = tarPile.get(tarPile.size() - 1);
        ColorType lCardColor = lastCard.getColor();
        int lCardValue = lastCard.getValue();

        PlayingCard buildHead = build.get(0);
        int cardValue = buildHead.getValue();
        ColorType cardColor = buildHead.getColor();

        // Check If the Conditions Match
        if (!lCardColor.equals(cardColor)) {
          if (lCardValue == cardValue + 1) {
            if (checkAllowance(build.size(), tarPile.isEmpty())) {
              tarPile.addAll(build);
            } else {
              throw new IllegalArgumentException("Not enough intermediate slots");
            }
          } else {
            throw new IllegalArgumentException("Illegal Move, Not One Less than Last Card");
          }
        } else {
          throw new IllegalArgumentException("Illegal Move, Not Opposite Colors with Last Card");
        }
      }
    } else {
      throw new IllegalArgumentException("Attempting to move build outside Cascade");
    }
  }

  protected void removeCard(PileType source, int pileNumber, int cardIndex) {
    if (source.equals(PileType.OPEN)) {
      this.openPiles.get(pileNumber).remove(cardIndex);
    } else {
      // Only Cascade Cards at this point
      for (int i = this.getNumCardsInCascadePile(pileNumber) - 1; i >= cardIndex; i--) {
        this.cascadePiles.get(pileNumber).remove(i);
      }
    }
  }

  protected List<PlayingCard> getBuild(int pileNumber, int cardIndex) {
    List<PlayingCard> returnList = new ArrayList<PlayingCard>();
    PlayingCard firstCard = getCascadeCardAt(pileNumber, cardIndex);
    int total = getNumCardsInCascadePile(pileNumber);
    returnList.add(firstCard);
    if (cardIndex != total - 1) {
      ColorType lastColor = firstCard.getColor();
      int lastValue = firstCard.getValue();
      for (int i = cardIndex + 1; i < total; i++) {
        PlayingCard targetCard = getCascadeCardAt(pileNumber, i);
        if (targetCard.getColor() == lastColor) {
          throw new IllegalArgumentException("Not a Build: Card Suit isn't Opposite.");
        }
        if (targetCard.getValue() != lastValue - 1) {
          throw new IllegalArgumentException("Not a Build: Not 1 less card value.");
        }
        lastColor = targetCard.getColor();
        lastValue = targetCard.getValue();
        returnList.add(targetCard);
      }
    }
    return returnList;
  }

  protected List<PlayingCard> getSourceCards(PileType source, int pileNumber, int cardIndex) {
    List<PlayingCard> returnList = new ArrayList<PlayingCard>();
    // Return Card Based On Source Type
    if (source.equals(PileType.OPEN)) {
      if (cardIndex == 0) {
        returnList.add(getOpenCardAt(pileNumber));
        return returnList;
      } else {
        throw new IllegalArgumentException("Target Open Pile Slot Has Only One Card");
      }
    } else if (source.equals(PileType.CASCADE)) {
      return getBuild(pileNumber, cardIndex);
    } else {
      // Cannot Move From Foundations
      throw new IllegalArgumentException("Cannot Move Card From Foundation Pile");
    }
  }

  protected void moveCardToDestination(PlayingCard card, PileType tarType, int tarNum) {
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
          if (lCardValue == cardValue + 1) {
            tarPile.add(card);
          } else {
            throw new IllegalArgumentException("Illegal Move, Not One Less than Last Card.");
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
          throw new IllegalArgumentException("Invalid Move, Foundation Piles Can Only Start With "
                  + "Aces");
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
        throw new IllegalArgumentException("Invalid Move, Attempt to Move to Occupied Open Pile "
                + "Slot");
      }
    }
  }
}
