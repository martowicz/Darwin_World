package agh.oop.darwin_world.model.worlds;


import agh.oop.darwin_world.model.enums.MapDirection;
import agh.oop.darwin_world.model.utils.IncorrectPositionException;
import agh.oop.darwin_world.model.utils.SortedLinkedList;
import agh.oop.darwin_world.model.utils.Vector2d;
import agh.oop.darwin_world.model.utils.MapVisualizer;
import agh.oop.darwin_world.model.world_elements.Animal;
import agh.oop.darwin_world.model.world_elements.Plant;
import agh.oop.darwin_world.model.world_elements.WorldElement;
import agh.oop.darwin_world.presenter.MapChangeListener;
import agh.oop.darwin_world.presenter.UserConfigurationRecord;

import java.security.SecureRandom;
import java.util.*;

public abstract class AbstractWorldMap implements WorldMap
{
    protected Map<Vector2d, SortedLinkedList<Animal>> animal_test = new HashMap<>();
    protected Map<Vector2d, WorldElement> plants = new HashMap<>();
    protected final MapVisualizer visualizer;

    protected final Boundary boundary;
    protected final int equatorYmin;
    protected final int equatorYmax;
    protected final int energyFromOnePlant;
    protected final int startingPlantsCount;

    protected final UUID id;
    protected final List<MapChangeListener> observers = new ArrayList<>();

    protected AbstractWorldMap(UserConfigurationRecord config) {
        this.visualizer = new MapVisualizer(this);
        this.boundary = config.mapBoundary();
        this.id = UUID.randomUUID();
        this.equatorYmin = (int) (0.4*(boundary.upperRight().getY()-boundary.lowerLeft().getY()));
        this.equatorYmax = (int) (0.6*(boundary.upperRight().getY()-boundary.lowerLeft().getY()));
        this.energyFromOnePlant = config.energyFromPlant();
        this.startingPlantsCount = config.startingPlantNumber();
        generatePlants(startingPlantsCount);

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
            addAnimal(position, animal);
            notifyObservers("placed at " + position);
        } else {// throw new IncorrectPositionException(position);}
        }
    }

    public void addAnimal(Vector2d position, Animal animal) {
        // Pobierz listę zwierząt na danej pozycji lub utwórz nową, jeśli nie istnieje
        SortedLinkedList<Animal> animalsAtPosition = animal_test.computeIfAbsent(position, k -> new SortedLinkedList<>());

        // Dodaj zwierzę do posortowanej listy
        animalsAtPosition.add(animal);
    }
    public void removeAnimal(Vector2d position, Animal animal) {
        // Pobierz listę zwierząt na danej pozycji
        SortedLinkedList<Animal> animalsAtPosition = animal_test.get(position);

        if (animalsAtPosition != null) {
            // Usuń zwierzę z listy
            animalsAtPosition.remove(animal);

            // Jeśli lista jest pusta po usunięciu, usuń klucz z mapy
            if (animalsAtPosition.isEmpty()) {
                animal_test.remove(position);
            }
        }
    }



    @Override
    public void move(Animal animal)
    {
        Vector2d oldCoordinates = animal.getPosition();

        Vector2d potential_move = animal.getAnimalOrientation().toUnitVector();
        Vector2d newCoordinates = oldCoordinates.add(potential_move);
        if(canMoveTo(newCoordinates)) {
            removeAnimal(oldCoordinates, animal);
            addAnimal(newCoordinates, animal);
            animal.setAnimalPosition(newCoordinates);
            notifyObservers("Move from " + oldCoordinates + " to " + newCoordinates);
        }
        else if(newCoordinates.getY()>boundary.upperRight().getY() || newCoordinates.getY()<boundary.lowerLeft().getY()) {
            MapDirection currentOrientation = animal.getAnimalOrientation();
            animal.setAnimalOrientation(currentOrientation.reverse());
            notifyObservers("Reversed");

        }
        else if(newCoordinates.getX()<boundary.lowerLeft().getX()) {
            Vector2d newPosition = new Vector2d(boundary.upperRight().getX(), newCoordinates.getY());
            animal.setAnimalPosition(newPosition);
            removeAnimal(oldCoordinates, animal);
            addAnimal(newPosition, animal);
            notifyObservers("Move from " + oldCoordinates + " to " + newPosition);

        }
        else{
            Vector2d newPosition = new Vector2d(boundary.lowerLeft().getX(), newCoordinates.getY());
            animal.setAnimalPosition(newPosition);
            removeAnimal(oldCoordinates, animal);
            addAnimal(newPosition, animal);
            notifyObservers("Move from " + oldCoordinates + " to " + newPosition);

        }
        animal.rotate(); //rotacja po ruchu?
    }

    public void displayLinkedLists() {
        for (Vector2d position : animal_test.keySet()) {
            SortedLinkedList<Animal> animalsInPosition = animal_test.get(position);

            System.out.println("Pozycja: " + position);
            if (animalsInPosition.isEmpty()) {
                System.out.println("  Brak zwierząt");
            } else {
                animalsInPosition.display();
            }
        }
    }



    public List<Animal> getAnimalsToList(){
        List<Animal> animalsList = new ArrayList<>();
        for(Vector2d position : animal_test.keySet()){
            SortedLinkedList<Animal> animalsInPosition = animal_test.get(position);
            for(Animal animal : animalsInPosition){
                animalsList.add(animal);
            }
        }
        return animalsList;
    }

    public void animalsEatPlants(){
        for(Vector2d position : animal_test.keySet()){
            if(plants.containsKey(position)){
                SortedLinkedList<Animal> animalsInPosition = animal_test.get(position);
                Animal animal = animalsInPosition.getHead();
                animal.eat(energyFromOnePlant);
                notifyObservers("Plant eaten on " + position);
                plants.remove(position);
            }
        }
    }

    public void dayPasses(){
        for(Vector2d position : animal_test.keySet()){
            SortedLinkedList<Animal> animalsInPosition = animal_test.get(position);
            for(Animal animal : animalsInPosition){
                animal.dayPasses();
            }
        }
    }




    public void generatePlants(int startingGrassCount){
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
        // Sprawdzenie, czy na pozycji znajdują się zwierzęta
        if (animal_test.containsKey(position)) {
            SortedLinkedList<Animal> animalsAtPosition = animal_test.get(position);

            // Jeśli lista nie jest pusta, zwracamy zwierzę o najwyższej energii (głowa listy)
            if (!animalsAtPosition.isEmpty()) {
                return animalsAtPosition.getHead(); // Zakładamy, że metoda getHead zwraca pierwszego elementa
            }
        }

        // Sprawdzenie, czy na pozycji znajdują się rośliny
        if (plants.containsKey(position)) {
            return plants.get(position); // Zwracamy roślinę
        }

        // Jeśli na pozycji nic nie ma, zwracamy null
        return null;
    }


    @Override
    public LinkedList<WorldElement> getElements()
    {
        return new LinkedList<>(); //dopisze sie to
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