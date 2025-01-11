package agh.oop.darwin_world.model.world_elements;

import agh.oop.darwin_world.model.enums.MapDirection;
import agh.oop.darwin_world.model.utils.Vector2d;
import agh.oop.darwin_world.model.worlds.WorldMap;

public class Animal implements WorldElement {


    private static final Vector2d DEFAULT_VECTOR = new Vector2d(2, 2);
    private static final MapDirection DEFAULT_ANIMAL_ORIENTATION = MapDirection.N;

    private MapDirection animalOrientation;
    private Vector2d animalCoordinates;

    public Animal()
    {
        this(DEFAULT_VECTOR);
    }
    public Animal (Vector2d inputAnimalCoordinates)
    {
        this.animalOrientation= DEFAULT_ANIMAL_ORIENTATION;
        this.animalCoordinates = inputAnimalCoordinates;
    }
    @Override
    public String toString()
    {
        return animalOrientation.toString();

    }

    @Override
    public Vector2d getPosition() {
        return animalCoordinates;
    }

    public void move(MapDirection direction, WorldMap worldMap) {

        Vector2d movecandidate =  new Vector2d(animalCoordinates.getX(), animalCoordinates.getY());

        movecandidate =movecandidate.add(direction.toUnitVector());
        if (worldMap.canMoveTo(movecandidate))
                {
                    animalCoordinates = movecandidate;
                    animalOrientation = direction;

                }

    }

    public MapDirection getAnimalOrientation()
    {
        return animalOrientation;
    }





}