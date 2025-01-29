package agh.oop.darwin_world.model.world_elements;
import agh.oop.darwin_world.model.utils.Vector2d;
import javafx.scene.paint.Color;

public interface WorldElement {
        Vector2d getPosition();
        Color getColor();
}