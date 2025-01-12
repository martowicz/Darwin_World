package agh.oop.darwin_world.model.world_elements;

import agh.oop.darwin_world.model.utils.Vector2d;

public class Water implements WorldElement{
    Vector2d waterPosition;
    private static final String waterRepresentation = "+";

    public Water(Vector2d position) {
        this.waterPosition = position;
    }

    @Override
    public String toString() {
        return waterRepresentation;
    }

    @Override
    public Vector2d getPosition() {
        return null;
    }
}
