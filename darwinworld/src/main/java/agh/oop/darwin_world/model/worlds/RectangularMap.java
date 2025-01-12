package agh.oop.darwin_world.model.worlds;

import agh.oop.darwin_world.model.utils.Vector2d;

public class RectangularMap extends AbstractWorldMap
{
    private static final String MAP_NAME="RectangularMap";

    public RectangularMap(Boundary boundary) {
        super(boundary);
    }

    @Override
    public String getId(){
        return String.format("%s %s",MAP_NAME,id.toString());

    }



}