package agh.oop.darwin_world.model.worlds;

import agh.oop.darwin_world.World;
import agh.oop.darwin_world.model.enums.MapDirection;
import agh.oop.darwin_world.model.utils.Vector2d;
import agh.oop.darwin_world.model.world_elements.Animal;

import java.awt.*;

public class Earth extends AbstractWorldMap{

    private static final String MAP_NAME = "Earth";


    public Earth(Boundary boundary) {
        super(boundary);
    }

    public void move(Animal animal)
    {
        Vector2d oldCoordinates = animal.getPosition();
        animal.rotate(); //rotacja
        notifyObservers("rotated");
        Vector2d potential_move = animal.getAnimalOrientation().toUnitVector();
        Vector2d newCoordinates = oldCoordinates.add(potential_move);
        if(canMoveTo(newCoordinates)) {
            animals.remove(oldCoordinates);
            animals.put(newCoordinates, animal);
            notifyObservers("move from " + oldCoordinates + " to " + newCoordinates);
            animal.setAnimalPosition(newCoordinates);
        }
        else{
            if(newCoordinates.getY()>boundary.upperRight().getY() || newCoordinates.getY()<boundary.lowerLeft().getY()) {
                MapDirection newOrientation = animal.getAnimalOrientation().reverse();
                notifyObservers("Animal changed orientation from " + animal.getAnimalOrientation() + " to " + newOrientation);
                animal.setAnimalOrientation(newOrientation);


            }
            else if(newCoordinates.getX()>boundary.upperRight().getX()){
                Vector2d newPosition = new Vector2d(boundary.lowerLeft().getX(), newCoordinates.getY());
                animal.setAnimalPosition(newPosition);
                animals.remove(oldCoordinates);
                animals.put(newPosition, animal);
                notifyObservers("move from " + oldCoordinates + " to " + newPosition);
            }
            else{
                Vector2d newPosition = new Vector2d(boundary.upperRight().getX(), newCoordinates.getY());
                animal.setAnimalPosition(newPosition);
                animals.remove(oldCoordinates);
                animals.put(newPosition, animal);
                notifyObservers("move from " + oldCoordinates + " to " + newPosition);
            }
        }

    }






    @Override
    public String getId(){
        return String.format("%s %s",MAP_NAME,id.toString());

    }




}
