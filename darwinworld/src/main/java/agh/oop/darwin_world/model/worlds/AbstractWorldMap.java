package agh.oop.darwin_world.model.worlds;

import agh.oop.darwin_world.model.enums.MapDirection;
import agh.oop.darwin_world.model.utils.RandomPositionGenerator;
import agh.oop.darwin_world.model.utils.SortedLinkedList;
import agh.oop.darwin_world.model.utils.Vector2d;
import agh.oop.darwin_world.model.utils.MapVisualizer;
import agh.oop.darwin_world.model.world_elements.Animal;
import agh.oop.darwin_world.model.world_elements.EquatorPlant;
import agh.oop.darwin_world.model.world_elements.Plant;
import agh.oop.darwin_world.model.world_elements.WorldElement;
import agh.oop.darwin_world.presenter.MapChangeListener;
import agh.oop.darwin_world.presenter.UserConfigurationRecord;
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

    protected AbstractWorldMap(UserConfigurationRecord config) {
        this.visualizer = new MapVisualizer(this);
        this.boundary = config.mapBoundary();
        this.numberOfFields = (boundary.upperRight().y() - boundary.lowerLeft().y()+1)*(boundary.upperRight().x() - boundary.lowerLeft().x()+1);
        this.config = config;
        this.id = UUID.randomUUID();
        int equatorYmin = (int) (0.4*(boundary.upperRight().y()-boundary.lowerLeft().y()));
        int equatorYmax = (int) (0.6*(boundary.upperRight().y()-boundary.lowerLeft().y()));
        this.equatorBoundary = new Boundary(new Vector2d(boundary.lowerLeft().x(),equatorYmin), new Vector2d(boundary.upperRight().x(),equatorYmax));
        this.overEquatorBoundary = new Boundary(boundary.lowerLeft(), new Vector2d(boundary.upperRight().x(),equatorYmin-1));
        this.underEquatorBoundary = new Boundary(new Vector2d(boundary.lowerLeft().x(),equatorYmax+1),boundary.upperRight());
        this.energyFromOnePlant = config.energyFromPlant();
        this.startingPlantsCount = config.startingPlantNumber();
        generateEnvironment(startingPlantsCount,0);
    }

    @Override
    public void reproduce() {
        for (Vector2d position : animalsAtPositions.keySet()) {
            SortedLinkedList<Animal> animalsInPosition = animalsAtPositions.get(position);
            if(animalsInPosition.canIntercourse()) {
                Animal firstAnimal = animalsInPosition.getHead();
                Animal secondAnimal = animalsInPosition.getSecondElement();
                if (secondAnimal.getEnergy() >= config.animalsEnergyToCopulate()) {
                    Animal kid = new Animal(firstAnimal, secondAnimal, config);
                    Vector2d kidPosition = new Vector2d(firstAnimal.getPosition().x(), firstAnimal.getPosition().y());
                    addAnimal(kidPosition, kid);
                    firstAnimal.addKid(kid);
                    secondAnimal.addKid(kid);
                    firstAnimal.subtractEnergy(config.animalsEnergySpentOnCopulation());
                    secondAnimal.subtractEnergy(config.animalsEnergySpentOnCopulation());
                   // System.out.println("New kid on " + kidPosition);
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
    public void place (Animal animal)
    {
        Vector2d position = animal.getPosition();
        if (canMoveTo(animal.getPosition())) {
            addAnimal(position, animal);
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
        }
        else if(newCoordinates.y()>boundary.upperRight().y() || newCoordinates.y()<boundary.lowerLeft().y()) {
            MapDirection currentOrientation = animal.getAnimalOrientation();
            animal.setAnimalOrientation(currentOrientation.reverse());
        }
        else if(newCoordinates.x()<boundary.lowerLeft().x()) {
            Vector2d newPosition = new Vector2d(boundary.upperRight().x(), newCoordinates.y());
            if(canMoveTo(newPosition)) {
                animal.setAnimalPosition(newPosition);
                removeAnimal(oldCoordinates, animal);
                addAnimal(newPosition, animal);
            }
        }
        else{
            Vector2d newPosition = new Vector2d(boundary.lowerLeft().x(), newCoordinates.y());
            if(canMoveTo(newPosition)) {
                animal.setAnimalPosition(newPosition);
                removeAnimal(oldCoordinates, animal);
                addAnimal(newPosition, animal);
            }
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

        RandomPositionGenerator randomPositionGenerator = new RandomPositionGenerator();
        List<Vector2d> underEquatorPositions = randomPositionGenerator.generateAllPositions(underEquatorBoundary);
        List<Vector2d> equatorPositions = randomPositionGenerator.generateAllPositions(equatorBoundary);
        List<Vector2d> overEquatorPositions = randomPositionGenerator.generateAllPositions(overEquatorBoundary);
        int i=0;
        SecureRandom rand = new SecureRandom();
        while (config.plantsGrowingDaily()>i && !underEquatorPositions.isEmpty() && !overEquatorPositions.isEmpty() && !equatorPositions.isEmpty()) {
            
            Vector2d positionCandidate = null;
            double v = rand.nextDouble();
            if(v<0.8 && !equatorPositions.isEmpty()) {
                positionCandidate = equatorPositions.removeFirst();
                if(plantCanBePlaced(positionCandidate)){
                    EquatorPlant equatorPlant = new EquatorPlant(positionCandidate);
                    plants.put(positionCandidate, equatorPlant);
                    i++;
                }
            }
            
            else if(v<0.9 && !overEquatorPositions.isEmpty()) {
                positionCandidate = overEquatorPositions.removeFirst();
                if(plantCanBePlaced(positionCandidate)){
                    Plant plant = new Plant(positionCandidate);
                    plants.put(positionCandidate, plant);
                    i++;
                }
            }
            
            else if(!underEquatorPositions.isEmpty()) {
                positionCandidate = underEquatorPositions.removeFirst();
                if(plantCanBePlaced(positionCandidate)){
                    Plant plant = new Plant(positionCandidate);
                    plants.put(positionCandidate, plant);
                    i++;
                }
               
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
        for(int x = boundary.lowerLeft().x(); x<=boundary.upperRight().x(); x++){
            for(int y = boundary.lowerLeft().y(); y<=boundary.upperRight().y(); y++){
                if(returnObjectAt(new Vector2d(x,y))==null){
                    emptyFields++;
                }
            }
        }
        return emptyFields;
    }
}