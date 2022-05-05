package cs3500.freecell.view;

import java.io.IOException;

import cs3500.freecell.model.FreecellModelState;

/**
 * Simple freecell text view made from strings implementing the freecellview interface.
 */
public class FreecellTextView implements FreecellView {
  private final FreecellModelState<?> model;
  private Appendable output;

  /**
   * Constructor for Freecell Text View.
   * @param model is some freecell model.
   */
  public FreecellTextView(FreecellModelState<?> model) {
    this(model, System.out);
  }

  /**
   * Constructor for freecelltextview.
   * @param model is any freecellmodel
   * @param output is the appendable output object
   */
  public FreecellTextView(FreecellModelState<?> model, Appendable output) {
    if (model == null) {
      throw new IllegalArgumentException("Model can't be null");
    }
    if (output == null) {
      throw new IllegalArgumentException("Output can't be null");
    }
    this.model = model;
    this.output = output;
  }

  @Override
  public String toString() {
    try {
      // Foundation
      String foundationStr = "";
      for (int n = 0; n < 4; n++) {
        foundationStr += "F" + (n + 1) + ":";
        for (int i = 0; i < this.model.getNumCardsInFoundationPile(n); i++) {
          foundationStr += " " + this.model.getFoundationCardAt(n, i).toString();
          if (i != this.model.getNumCardsInFoundationPile(n) - 1) {
            foundationStr += ",";
          }
        }
        if (n != 3) {
          foundationStr += "\n";
        }
      }
      // Open
      String openStr = "";
      for (int n = 0; n < this.model.getNumOpenPiles(); n++) {
        openStr += "O" + (n + 1) + ":";
        for (int i = 0; i < this.model.getNumCardsInOpenPile(n); i++) {
          openStr += " " + this.model.getOpenCardAt(n).toString();
        }
        if (n != this.model.getNumOpenPiles() - 1) {
          openStr += "\n";
        }
      }
      // Cascade
      String cascadeStr = "";
      for (int n = 0; n < this.model.getNumCascadePiles(); n++) {
        cascadeStr += "C" + (n + 1) + ":";
        for (int i = 0; i < this.model.getNumCardsInCascadePile(n); i++) {
          cascadeStr += " " + this.model.getCascadeCardAt(n, i).toString();
          if (i != this.model.getNumCardsInCascadePile(n) - 1) {
            cascadeStr += ",";
          }
        }
        if (n != this.model.getNumCascadePiles() - 1) {
          cascadeStr += "\n";
        }
      }
      return foundationStr + "\n" + openStr + "\n" + cascadeStr;
    }
    catch (IllegalStateException e) {
      return "";
    }
  }

  @Override
  public void renderBoard() throws IOException {
    this.output.append(this.toString());
  }

  @Override
  public void renderMessage(String message) throws IOException {
    if (message != null) {
      this.output.append(message);
    }

  }
}
