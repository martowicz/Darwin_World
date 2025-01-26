package agh.oop.darwin_world.model.worlds;
import agh.oop.darwin_world.model.utils.Vector2d;
import agh.oop.darwin_world.model.world_elements.Animal;
import agh.oop.darwin_world.model.world_elements.WorldElement;

import java.util.LinkedList;


public interface WorldMap{


    boolean canMoveTo(Vector2d position);

    void place(Animal animal);// throws IncorrectPositionException;

    void move(Animal animal);

    boolean isOccupied(Vector2d position);

    WorldElement returnObjectAt(Vector2d position);

    LinkedList<WorldElement> getElements();

    String getIdString();

    void reproduce();

    void rotate(Animal animal);

    Boundary getCurrentBounds();



}