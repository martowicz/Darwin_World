package agh.oop.darwin_world.model.worlds;

import agh.oop.darwin_world.model.utils.Vector2d;

public class RectangularMap extends AbstractWorldMap
{
    private static final String MAP_NAME="RectangularMap";

    public RectangularMap (int width, int height)
    {
        super();
        this.lowerLeft = new Vector2d(0,0);
        this.upperRight = new Vector2d(width,height);
    }
    @Override
    public String getId(){
        return String.format("%s %s",MAP_NAME,id.toString());

    }



}