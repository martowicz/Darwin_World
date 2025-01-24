package agh.oop.darwin_world.model.worlds;

import agh.oop.darwin_world.World;
import agh.oop.darwin_world.model.enums.MapDirection;
import agh.oop.darwin_world.model.utils.Vector2d;
import agh.oop.darwin_world.model.world_elements.Animal;
import agh.oop.darwin_world.model.world_elements.Water;
import agh.oop.darwin_world.model.world_elements.WorldElement;
import agh.oop.darwin_world.presenter.UserConfigurationRecord;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Earth extends AbstractWorldMap{

    private static final String MAP_NAME = "Earth";


    public Earth(UserConfigurationRecord config) {
        super(config);
    }
    @Override
    public String getIdString(){
        return String.format("%s %s",MAP_NAME,id.toString());

    }




}
