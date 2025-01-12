package agh.oop.darwin_world.model.worlds;


import agh.oop.darwin_world.model.enums.MapDirection;
import agh.oop.darwin_world.model.utils.IncorrectPositionException;
import agh.oop.darwin_world.model.utils.Vector2d;
import agh.oop.darwin_world.model.utils.MapVisualizer;
import agh.oop.darwin_world.model.world_elements.Animal;
import agh.oop.darwin_world.model.world_elements.Plant;
import agh.oop.darwin_world.model.world_elements.WorldElement;
import agh.oop.darwin_world.presenter.MapChangeListener;

import java.security.SecureRandom;
import java.util.*;

public abstract class AbstractWorldMap implements WorldMap
{
    protected Map<Vector2d, WorldElement> animals = new HashMap<>();
    protected Map<Vector2d, WorldElement> plants = new HashMap<>();
    protected final MapVisualizer visualizer;

    protected final Boundary boundary;
    protected final int equatorYmin;
    protected final int equatorYmax;

    protected final UUID id;
    protected final List<MapChangeListener> observers = new ArrayList<>();

    protected AbstractWorldMap(Boundary boundary) {
        this.visualizer = new MapVisualizer(this);
        this.boundary = boundary;
        this.id = UUID.randomUUID();
        this.equatorYmin = (int) (0.4*(boundary.upperRight().getY()-boundary.lowerLeft().getY()));
        this.equatorYmax = (int) (0.6*(boundary.upperRight().getY()-boundary.lowerLeft().getY()));
        generateGrass(20);

    }

    @Override
    public boolean canMoveTo(Vector2d position)
    {
        return position.follows(boundary.lowerLeft()) && position.precedes(boundary.upperRight());
    }


    protected void notifyObservers(String message)
    {
        for (MapChangeListener observer : observers) {
            observer.mapChanged(this,message);
        }
    }

    @Override
    public void place (Animal animal)// throws IncorrectPositionException
    {
        Vector2d position = animal.getPosition();
        if (canMoveTo(animal.getPosition())) {
            animals.put(position, animal);
            notifyObservers("placed at " + position);
        } else {// throw new IncorrectPositionException(position);}
        }
    }

    @Override
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
            animal.setAnimalPosition(newCoordinates);
        }
    }

    public void generateGrass(int startingGrassCount){
        SecureRandom rand = new SecureRandom();
        Random xx = new Random();
        Random yy= new Random();
        for(int i = 0; i < startingGrassCount; i++) {
            int yCoordinate;
            double v = rand.nextDouble();
            if(v>0.8){
                //equator
                v=rand.nextInt(2);
                if(v==0){
                    //top-half
                    yCoordinate = yy.nextInt(boundary.upperRight().getY()-equatorYmax+1)+equatorYmax;
                }
                else{
                    //bottomhalf
                    yCoordinate = yy.nextInt(equatorYmin - boundary.lowerLeft().getY()+1)+boundary.lowerLeft().getY();
                }
            }
            else{
                yCoordinate = yy.nextInt(equatorYmax - equatorYmin + 1) + equatorYmin;
                //rest
            }
            int xCoordinate = xx.nextInt(boundary.upperRight().getY()-boundary.lowerLeft().getY()+1)+boundary.lowerLeft().getY();
            Vector2d newPosition = new Vector2d(xCoordinate, yCoordinate);
            Plant plant = new Plant(newPosition);
            plants.put(newPosition, plant);
        }
    }



    @Override
    public boolean isOccupied(Vector2d position)
    {
        return objectAt(position) != null;
    }

    @Override
    public WorldElement objectAt(Vector2d position) {

        if (animals.containsKey(position)) {
            return animals.get(position);
        }
        if (plants.containsKey(position)) {
            return plants.get(position);
        }
        return null;
    }

    @Override
    public LinkedList<WorldElement> getElements()
    {
        return new LinkedList<>(animals.values());
    }

    @Override
    public String getId() {
        return id.toString();
    }


    @Override
    public String toString() {
        return visualizer.draw(boundary.lowerLeft(),boundary.upperRight());

    }
    public void addObserver(MapChangeListener observer) {
        observers.add(observer);
    }

    public void removeObserver(MapChangeListener observer) {
        observers.remove(observer);
    }
    public Boundary getCurrentBounds() {
        return boundary;
    }





}