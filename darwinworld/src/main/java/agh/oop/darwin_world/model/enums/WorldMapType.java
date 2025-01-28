package agh.oop.darwin_world.model.enums;

import agh.oop.darwin_world.model.utils.Vector2d;
import agh.oop.darwin_world.model.worlds.AbstractWorldMap;
import agh.oop.darwin_world.model.worlds.*;
import agh.oop.darwin_world.presenter.UserConfigurationRecord;

public enum WorldMapType {
    ROUND_WORLD,
    WATER_WORLD,
    RECTANGULAR_WORLD;


    //czy private
    public AbstractWorldMap enumToMap(UserConfigurationRecord config)
    {
        return switch (this){
            case ROUND_WORLD -> new Earth(config);
            case WATER_WORLD -> new FlowsAndDrains(config);
            case RECTANGULAR_WORLD -> new RectangularMap(config);
        };
    }

    @Override
    public String toString() {
        return switch (this){
            case ROUND_WORLD -> "Earth_Globe";
            case WATER_WORLD -> "Flows_and_Drains";
            case RECTANGULAR_WORLD -> "Rectangular World";
        };
    }
}
