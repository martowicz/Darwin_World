package agh.oop.darwin_world.model.utils;

public class IncorrectPositionException extends Exception {
  private static final String ERROR_MESSAGE="Position %s is incorrect";
  public IncorrectPositionException(Vector2d place) {
    super(String.format(ERROR_MESSAGE,place));

  }
}