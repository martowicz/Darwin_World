package agh.oop.darwin_world.model.world_elements;
import agh.oop.darwin_world.model.utils.Vector2d;
import javafx.scene.paint.Color;

import java.util.Objects;

public class EquatorPlant extends Plant {


    public EquatorPlant(Vector2d GrassPosition) {
        super(GrassPosition);
    }

    @Override
    public Color getColor() {

        return Color.rgb(63,105,34);
    }



}