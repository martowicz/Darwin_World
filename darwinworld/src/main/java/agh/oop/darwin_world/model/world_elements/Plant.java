package agh.oop.darwin_world.model.world_elements;
import agh.oop.darwin_world.model.utils.Vector2d;

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
    public String toString()
    {
        String greenText = "\u001B[32m";
        String resetText = "\u001B[0m";
        return greenText+PLANT_SYMBOL+resetText;
    }


    @Override
    public int hashCode() {
        return Objects.hashCode(plantPosition);
    }
}