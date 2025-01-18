package agh.oop.darwin_world.model.worlds;

import agh.oop.darwin_world.model.utils.Vector2d;
import agh.oop.darwin_world.presenter.UserConfigurationRecord;

public class RectangularMap extends AbstractWorldMap
{
    private static final String MAP_NAME="RectangularMap";

    public RectangularMap(UserConfigurationRecord config) {
        super(config);
    }

    @Override
    public String getId(){
        return String.format("%s %s",MAP_NAME,id.toString());

    }



}