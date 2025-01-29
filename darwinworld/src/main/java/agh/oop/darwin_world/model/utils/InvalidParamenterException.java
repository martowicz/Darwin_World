package agh.oop.darwin_world.model.utils;

public class InvalidParamenterException extends Exception {

  private static final String ERROR_MESSAGE="This parameter is incorrect: %s";
  public InvalidParamenterException(String errorMessage) {
    super(String.format(ERROR_MESSAGE,errorMessage));

  }
}