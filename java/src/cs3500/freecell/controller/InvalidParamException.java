package cs3500.freecell.controller;

/**
 * Invalid Param Exception is an exception when the parameters for move in Freecell Model.
 * returns an exception.
 */
public class InvalidParamException extends Exception {
  /**
   * Constructor for Invalid Param Exception.
   * @param message is the exception message.
   */
  public InvalidParamException(String message) {
    super(message);
  }
}
