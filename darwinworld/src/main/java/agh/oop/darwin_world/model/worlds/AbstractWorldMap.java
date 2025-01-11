package agh.oop.darwin_world.model.worlds;


import agh.oop.darwin_world.model.enums.MapDirection;
import agh.oop.darwin_world.model.utils.IncorrectPositionException;
import agh.oop.darwin_world.model.utils.Vector2d;
import agh.oop.darwin_world.model.utils.MapVisualizer;
import agh.oop.darwin_world.model.world_elements.Animal;
import agh.oop.darwin_world.model.world_elements.WorldElement;
import agh.oop.darwin_world.presenter.MapChangeListener;

import java.util.*;

public abstract class AbstractWorldMap implements WorldMap
{
    protected final Map<Vector2d, Animal> animals;
    protected final MapVisualizer visualizer;
    protected Vector2d lowerLeft = new Vector2d(0, 0);
    protected Vector2d upperRight = new Vector2d(8, 8);
    protected final Boundary boundary;//użyć teog potem
    protected final UUID id;
    protected final List<MapChangeListener> observers = new ArrayList<>();

    protected AbstractWorldMap() {
        this.animals = new HashMap<>();
        this.visualizer = new MapVisualizer(this);
        this.boundary = new Boundary(lowerLeft, upperRight);
        this.id = UUID.randomUUID();
    }




    public boolean isOccupiedByAnimal(Vector2d position)
    {
        return animalAt(position) != null;
    }
    @Override
    public boolean canMoveTo(Vector2d position)
    {
        return position.follows(lowerLeft) && position.precedes(upperRight) && !isOccupiedByAnimal(position);
    }


    public Animal animalAt (Vector2d position)
    {
        return animals.get(position);
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
    public void move(Animal animal, MapDirection direction)
    {
        Vector2d oldCoordinates = animal.getPosition();
        animal.move(direction,this);
        animals.remove(oldCoordinates);
        Vector2d newCoordinates = animal.getPosition();
        animals.put(newCoordinates, animal);

        notifyObservers("move from " + oldCoordinates + " to " + newCoordinates);

    }



    @Override
    public boolean isOccupied(Vector2d position)
    {
        return objectAt(position) != null;
    }

    @Override
    public WorldElement objectAt(Vector2d position)
    {
        return animals.get(position);
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
        return visualizer.draw(lowerLeft, upperRight);

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