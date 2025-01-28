package agh.oop.darwin_world.model.worlds;


import agh.oop.darwin_world.model.enums.MapDirection;
import agh.oop.darwin_world.model.utils.RandomPositionGenerator;
import agh.oop.darwin_world.model.utils.SortedLinkedList;
import agh.oop.darwin_world.model.utils.Vector2d;
import agh.oop.darwin_world.model.utils.MapVisualizer;
import agh.oop.darwin_world.model.world_elements.Animal;
import agh.oop.darwin_world.model.world_elements.Plant;
import agh.oop.darwin_world.model.world_elements.WorldElement;
import agh.oop.darwin_world.presenter.MapChangeListener;
import agh.oop.darwin_world.presenter.UserConfigurationRecord;

import java.lang.module.Configuration;
import java.security.SecureRandom;
import java.util.*;

public abstract class AbstractWorldMap implements WorldMap
{
    protected Map<Vector2d, SortedLinkedList<Animal>> animalsAtPositions = new HashMap<>();
    protected Map<Vector2d, WorldElement> plants = new HashMap<>();
    protected final MapVisualizer visualizer;

    protected final Boundary boundary;
    protected final Boundary equatorBoundary;
    protected final Boundary underEquatorBoundary;
    protected final Boundary overEquatorBoundary;
    protected final int numberOfFields;

    protected final int energyFromOnePlant;
    protected final int startingPlantsCount;
    protected final UserConfigurationRecord config;
    protected final UUID id;
    protected final List<MapChangeListener> observers = new ArrayList<>();
    protected final RandomPositionGenerator randomPositionGenerator = new RandomPositionGenerator();;


    protected AbstractWorldMap(UserConfigurationRecord config) {
        this.visualizer = new MapVisualizer(this);
        this.boundary = config.mapBoundary();
        this.numberOfFields = (boundary.upperRight().getY() - boundary.lowerLeft().getY()+1)*(boundary.upperRight().getX() - boundary.lowerLeft().getX()+1);
        this.config = config;
        this.id = UUID.randomUUID();
        int equatorYmin = (int) (0.4*(boundary.upperRight().getY()-boundary.lowerLeft().getY()));
        int equatorYmax = (int) (0.6*(boundary.upperRight().getY()-boundary.lowerLeft().getY()));
        this.equatorBoundary = new Boundary(new Vector2d(boundary.lowerLeft().getX(),equatorYmin), new Vector2d(boundary.upperRight().getX(),equatorYmax));
        this.overEquatorBoundary = new Boundary(boundary.lowerLeft(), new Vector2d(boundary.upperRight().getX(),equatorYmin-1));
        this.underEquatorBoundary = new Boundary(new Vector2d(boundary.lowerLeft().getX(),equatorYmax+1),boundary.upperRight());
        this.energyFromOnePlant = config.energyFromPlant();
        this.startingPlantsCount = config.startingPlantNumber();
        generateEnvironment(startingPlantsCount,0);

    }

    @Override
    public void reproduce() {


        for (Vector2d position : animalsAtPositions.keySet()) {
            SortedLinkedList<Animal> animalsInPosition = animalsAtPositions.get(position);
            if(animalsInPosition.canIntercourse()){
                Animal firstAnimal = animalsInPosition.getHead();
                Animal secondAnimal = animalsInPosition.getSecondElement();
                if (secondAnimal.getEnergy()>=config.animalsEnergyToCopulate()){

                    Animal kid = new Animal(firstAnimal,secondAnimal,config);
                    Vector2d kidPosition = new Vector2d(firstAnimal.getPosition().getX(),firstAnimal.getPosition().getY());
                    addAnimal(kidPosition,kid);
                    firstAnimal.addKid(kid);
                    secondAnimal.addKid(kid);
                    firstAnimal.subtractEnergy(config.animalsEnergySpentOnCopulation());
                    secondAnimal.subtractEnergy(config.animalsEnergySpentOnCopulation());
                    System.out.println("New kid on " + kidPosition);
                }

            }

        }


    }


    //sprawdza jedynie czy zwierze nie wychodzi poza mapę
    @Override
    public boolean canMoveTo(Vector2d position)
    {
        return position.follows(boundary.lowerLeft()) && position.precedes(boundary.upperRight());
    }

    @Override
    public void place (Animal animal)// throws IncorrectPositionException
    {
        Vector2d position = animal.getPosition();
        if (canMoveTo(animal.getPosition())) {
            addAnimal(position, animal);
            //notifyObservers("placed at " + position);
        } else {// throw new IncorrectPositionException(position);}
        }
    }

    public void addAnimal(Vector2d position, Animal animal) {
        // Pobierz listę zwierząt na danej pozycji lub utwórz nową, jeśli nie istnieje
        SortedLinkedList<Animal> animalsAtPosition = animalsAtPositions.computeIfAbsent(position, k -> new SortedLinkedList<>());

        // Dodaj zwierzę do posortowanej listy
        animalsAtPosition.add(animal);
    }
    public void removeAnimal(Vector2d position, Animal animal) {
        // Pobierz listę zwierząt na danej pozycji
        SortedLinkedList<Animal> animalsAtPosition = animalsAtPositions.get(position);

        if (animalsAtPosition != null) {
            // Usuń zwierzę z listy
            animalsAtPosition.remove(animal);

            // Jeśli lista jest pusta po usunięciu, usuń klucz z mapy
            if (animalsAtPosition.isEmpty()) {
                animalsAtPositions.remove(position);
            }
        }
    }

    @Override
    public void rotate(Animal animal)
    {
        animal.rotate();
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
            //notifyObservers("Move from " + oldCoordinates + " to " + newCoordinates);
        }
        else if(newCoordinates.getY()>boundary.upperRight().getY() || newCoordinates.getY()<boundary.lowerLeft().getY()) {
            MapDirection currentOrientation = animal.getAnimalOrientation();
            animal.setAnimalOrientation(currentOrientation.reverse());
            //notifyObservers("Reversed");

        }
        else if(newCoordinates.getX()<boundary.lowerLeft().getX()) {
            Vector2d newPosition = new Vector2d(boundary.upperRight().getX(), newCoordinates.getY());
            if(canMoveTo(newPosition)) {
                animal.setAnimalPosition(newPosition);
                removeAnimal(oldCoordinates, animal);
                addAnimal(newPosition, animal);
            }

            //notifyObservers("Move from " + oldCoordinates + " to " + newPosition);

        }
        else{
            Vector2d newPosition = new Vector2d(boundary.lowerLeft().getX(), newCoordinates.getY());
            if(canMoveTo(newPosition)) {
                animal.setAnimalPosition(newPosition);
                removeAnimal(oldCoordinates, animal);
                addAnimal(newPosition, animal);
            }

            //notifyObservers("Move from " + oldCoordinates + " to " + newPosition);

        }
    }

    public void displayLinkedLists() {
        for (Vector2d position : animalsAtPositions.keySet()) {
            SortedLinkedList<Animal> animalsInPosition = animalsAtPositions.get(position);

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
        for(Vector2d position : animalsAtPositions.keySet()){
            SortedLinkedList<Animal> animalsInPosition = animalsAtPositions.get(position);
            for(Animal animal : animalsInPosition){
                animalsList.add(animal);
            }
        }
        return animalsList;
    }

    public void animalsEatPlants(){
        for(Vector2d position : animalsAtPositions.keySet()){
            if(plants.containsKey(position)){
                SortedLinkedList<Animal> animalsInPosition = animalsAtPositions.get(position);
                Animal animal = animalsInPosition.getHead();
                animal.eat(energyFromOnePlant);
                //notifyObservers("Plant eaten on " + position);
                plants.remove(position);
            }
        }
    }

    public void dayPasses(int days){
        for(Vector2d position : animalsAtPositions.keySet()){
            SortedLinkedList<Animal> animalsInPosition = animalsAtPositions.get(position);
            for(Animal animal : animalsInPosition){
                animal.dayPasses();
            }

        }

    }


    @Override
    public boolean plantCanBePlaced(Vector2d position){
        return !plants.containsKey(position);
    }

    public void generateEnvironment(int plantsCount, int day){
        SecureRandom rand = new SecureRandom();
        int i=0;
        while (i<plantsCount && plants.size()<numberOfFields){
            Vector2d generatedPosition;
            double v = rand.nextDouble();
            if(v<0.8){generatedPosition=randomPositionGenerator.generate(equatorBoundary);}
            else{
                if(v<0.9){generatedPosition=randomPositionGenerator.generate(overEquatorBoundary);}
                else{generatedPosition=randomPositionGenerator.generate(underEquatorBoundary);}
            }
            if(plantCanBePlaced(generatedPosition)){
                Plant plant = new Plant(generatedPosition);
                plants.put(generatedPosition, plant);
                i++;
            }
        }
    }

    @Override
    public WorldElement returnObjectAt(Vector2d position) {
        // Sprawdzenie, czy na pozycji znajdują się zwierzęta
        if (animalsAtPositions.containsKey(position)) {
            SortedLinkedList<Animal> animalsAtPosition = animalsAtPositions.get(position);

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

    public WorldElement returnEnvironmentAt(Vector2d position) {
        if(plants.containsKey(position)){
            return plants.get(position);
        }
        return null;
    }

    public WorldElement returnAnimalAt(Vector2d position) {
        if(animalsAtPositions.containsKey(position)){
            return animalsAtPositions.get(position).getHead();
        }
        return null;
    }


    @Override
    public boolean isOccupied(Vector2d position)
    {
        return returnObjectAt(position) != null;
    }

    //TODO
    @Override
    public LinkedList<WorldElement> getElements()
    {
        return new LinkedList<>(); //dopisze sie to do visualizera w javafx i pewnie metodę reprezentation
    }


    @Override
    public String getIdString() {
        return id.toString();
    }
    @Override
    public String toString() {
        return visualizer.draw(boundary.lowerLeft(),boundary.upperRight());

    }

    //Używane aby komunikować się z ux np w teminalu bądź javafx

    public void addObserver(MapChangeListener observer) {
        observers.add(observer);
    }

    public void removeObserver(MapChangeListener observer) {
        observers.remove(observer);
    }
    public void notifyObservers(String message)
    {
        for (MapChangeListener observer : observers) {
            observer.mapChanged(this,message);
        }
    }
    @Override
    public Boundary getCurrentBounds(){
        return boundary;
    }

    public int getNumberOfAnimals(){
        return getAnimalsToList().size();
    }

    public double averageAnimalEnergy(){
        int sum = 0;
        for(Animal animal : getAnimalsToList()){
            sum+=animal.getEnergy();
        }
        return (double) sum/getAnimalsToList().size();
    }

    public int getNumberOfPlants(){
        return plants.size();
    }

    public int getNumberOfEmptyFields(){
        int emptyFields = 0;
        for(int x=boundary.lowerLeft().getX();x<=boundary.upperRight().getX();x++){
            for(int y=boundary.lowerLeft().getY();y<=boundary.upperRight().getY();y++){
                if(returnObjectAt(new Vector2d(x,y))==null){
                    emptyFields++;
                }
            }
        }
        return emptyFields;
    }




}