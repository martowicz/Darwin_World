package agh.oop.darwin_world.model.worlds;
import agh.oop.darwin_world.model.enums.MapDirection;
import agh.oop.darwin_world.model.utils.IncorrectPositionException;
import agh.oop.darwin_world.model.utils.Vector2d;
import agh.oop.darwin_world.model.world_elements.Animal;
import agh.oop.darwin_world.model.world_elements.WorldElement;

import java.util.LinkedList;


public interface WorldMap{


    boolean canMoveTo(Vector2d position);

    void place(Animal animal);// throws IncorrectPositionException;

    void move(Animal animal, MapDirection direction);

    boolean isOccupied(Vector2d position);

    WorldElement objectAt(Vector2d position);

    LinkedList<WorldElement> getElements();


    String getId();
}