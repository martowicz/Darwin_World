package agh.oop.darwin_world.model.world_elements;
import agh.oop.darwin_world.model.utils.Vector2d;
import javafx.scene.paint.Color;

import java.util.Objects;

public class Plant implements WorldElement
{

    private static final String PLANT_SYMBOL = "*";
    Vector2d plantPosition;

    public Plant(Vector2d GrassPosition)
    {
        this.plantPosition = GrassPosition;
    }

    @Override
    public Vector2d getPosition()
    {
        return plantPosition;
    }

    @Override
    public Color getColor() {
        return Color.GREEN;
    }

    @Override
    public String toString()
    {

        return PLANT_SYMBOL;
    }


    @Override
    public int hashCode() {
        return Objects.hashCode(plantPosition);
    }


}