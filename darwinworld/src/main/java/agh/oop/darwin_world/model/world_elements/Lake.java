package agh.oop.darwin_world.model.world_elements;
import agh.oop.darwin_world.model.utils.RandomPositionGenerator;
import agh.oop.darwin_world.model.utils.Vector2d;
import agh.oop.darwin_world.presenter.UserConfigurationRecord;
import javafx.scene.paint.Color;

public class Lake implements WorldElement {
    private int radius=0;
    private Vector2d source_position;
    public Lake(UserConfigurationRecord config) {
        RandomPositionGenerator r = new RandomPositionGenerator();
        this.source_position=r.generate(config.mapBoundary());
    }
    public void extendLake(){
        radius+=1;
    }
    public void reduceLake(){
        if(radius>0){
            radius-=1;
        }
    }
    public boolean occupiedByLake(Vector2d position) {
        double distanceSquared = Math.pow(source_position.getX() - position.getX(), 2) +
                Math.pow(source_position.getY() - position.getY(), 2);
        return distanceSquared < Math.pow(radius, 2);
    }

    @Override
    public String toString() {
        return "8";
    }
    @Override
    public Vector2d getPosition() {return source_position;}

    @Override
    public Color getColor() {
        return Color.BLUE;
    }

}