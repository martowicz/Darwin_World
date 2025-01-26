package agh.oop.darwin_world.model.world_elements;
import agh.oop.darwin_world.model.utils.RandomPositionGenerator;
import agh.oop.darwin_world.model.utils.Vector2d;
import agh.oop.darwin_world.presenter.UserConfigurationRecord;
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
    public boolean occupiedByLake(Vector2d position){
        return (source_position.getX()-radius<=position.getX() &&
                source_position.getX()+radius>=position.getX() &&
                source_position.getY()-radius<=position.getY() &&
                source_position.getY()+radius>=position.getY());
    }
    @Override
    public String toString() {
        return "8";
    }
    @Override
    public Vector2d getPosition() {return source_position;}
}