package agh.oop.darwin_world.model.enums;

import agh.oop.darwin_world.model.utils.Vector2d;
import agh.oop.darwin_world.model.worlds.AbstractWorldMap;
import agh.oop.darwin_world.model.worlds.*;
public enum WorldMapType {
    ROUND_WORLD,
    WATER_WORLD,
    RECTANGULAR_WORLD;


    //czy private
    public AbstractWorldMap enumToMap(Boundary boundary)
    {
        return switch (this){
            case ROUND_WORLD -> new Earth(boundary);
            case WATER_WORLD -> new FlowsAndDrains(boundary);
            case RECTANGULAR_WORLD -> new RectangularMap(boundary);
        };
    }

    @Override
    public String toString() {
        return switch (this){
            case ROUND_WORLD -> "Round World";
            case WATER_WORLD -> "Water World";
            case RECTANGULAR_WORLD -> "Rectangular World";
        };
    }
}
