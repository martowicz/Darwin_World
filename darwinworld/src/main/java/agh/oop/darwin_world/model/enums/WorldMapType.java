package agh.oop.darwin_world.model.enums;

import agh.oop.darwin_world.model.utils.Vector2d;
import agh.oop.darwin_world.model.worlds.AbstractWorldMap;
import agh.oop.darwin_world.model.worlds.*;
import agh.oop.darwin_world.presenter.UserConfigurationRecord;

public enum WorldMapType {
    ROUND_WORLD,
    WATER_WORLD;

    public AbstractWorldMap enumToMap(UserConfigurationRecord config)
    {
        return switch (this){
            case ROUND_WORLD -> new Earth(config);
            case WATER_WORLD -> new FlowsAndDrains(config);
        };
    }

    @Override
    public String toString() {
        return switch (this){
            case ROUND_WORLD -> "Earth_Globe";
            case WATER_WORLD -> "Flows_and_Drains";
        };
    }
    
    public static WorldMapType stringToEnum(String str)
    {
        return switch (str){
            case "Earth_Globe" ->ROUND_WORLD;
            case "Flows_and_Drains" -> WATER_WORLD;
            default -> throw new IllegalStateException("Unexpected value: " + str);
        };
    }
}
