package agh.oop.darwin_world.model.world_elements;
import agh.oop.darwin_world.model.utils.RandomPositionGenerator;
import agh.oop.darwin_world.model.utils.Vector2d;
import agh.oop.darwin_world.presenter.UserConfigurationRecord;
import javafx.scene.paint.Color;

public class Lake implements WorldElement {
    private int radius=0;
    private final Vector2d source_position;
    UserConfigurationRecord config;
    private static final double MAX_LAKE_PROPORTION = 0.25;
    private static final double MIN_LAKE_PROPORTION = 0.1;

    public Lake(UserConfigurationRecord config) {

        RandomPositionGenerator r = new RandomPositionGenerator();
        this.source_position=r.getRandomPosition(config.mapBoundary());
        this.config=config;
    }
    public void extendLake(){
        if(radius<MAX_LAKE_PROPORTION*config.mapBoundary().upperRight().x() && radius<MAX_LAKE_PROPORTION*config.mapBoundary().upperRight().y())
        { radius+=1;}
    }
    public void reduceLake(){
        if(radius>0 && radius>MIN_LAKE_PROPORTION*config.mapBoundary().upperRight().x()&& radius>MIN_LAKE_PROPORTION*config.mapBoundary().upperRight().y()){
            radius-=1;
        }
    }
    public boolean occupiedByLake(Vector2d position) {
        double distanceSquared = Math.pow(source_position.x() - position.x(), 2) +
                Math.pow(source_position.y() - position.y(), 2);
        return distanceSquared < Math.pow(radius, 2);
    }

    @Override
    public String toString() {
        return "8";
    }
    @Override
    public Vector2d getPosition() {return source_position;}

    public int getRadius() {return radius;}


    @Override
    public Color getColor() {
        return Color.LIGHTBLUE;
    }

}