package agh.oop.darwin_world.simulation;
import agh.oop.darwin_world.model.utils.Vector2d;
import agh.oop.darwin_world.model.world_elements.Animal;
import agh.oop.darwin_world.model.worlds.AbstractWorldMap;
import agh.oop.darwin_world.model.worlds.Boundary;
import agh.oop.darwin_world.presenter.ConsoleMapDisplay;
import agh.oop.darwin_world.presenter.UserConfigurationRecord;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Simulation implements Runnable {

    private int numberOfAnimals;
    private int movesCount = 0;
    private int animalsEnergyAtStart;
    private List<Animal> animals = new ArrayList<>();
    private AbstractWorldMap worldMap;
    private final int plantsGrowingEveryDay;
    private int days = 0;

    public Simulation(UserConfigurationRecord config)
    {
        this.numberOfAnimals=config.animalsCountAtStart();
        this.worldMap = config.mapType().enumToMap(config);
        this.worldMap.addObserver(new ConsoleMapDisplay());
        this.plantsGrowingEveryDay = config.plantsGrowingDaily(); //obserwator w terminalu
        placeAnimalsOnTheMap(config);
        worldMap.generatePlants(plantsGrowingEveryDay);
    }
    @Override
    public void run(){
        for(int i=0;i<100000;i++) {
            days +=1;
            //1-Usunięcie martwych zwierzaków z mapy.
            removeDeadAnimals(days);
            //2-Skręt i przemieszczenie każdego zwierzaka.
            animals = worldMap.getAnimalsToList();
            rotateAllAnimals();
            moveAllAnimals();
            //3-Konsumpcja roślin, na których pola weszły zwierzaki.
            worldMap.animalsEatPlants();
            //4-Rozmnażanie się najedzonych zwierzaków znajdujących się na tym samym polu.
            worldMap.reproduce();
            //5-Wzrastanie nowych roślin na wybranych polach mapy.
            worldMap.generatePlants(plantsGrowingEveryDay);
            //6-Koniec dnia(zwierzęta tracą energie)
            worldMap.dayPasses();
            for(Animal animal : animals) {
                System.out.println("energia zwierzecia "+ animal.getEnergy());
            }
        }
    }

    private void placeAnimalsOnTheMap(UserConfigurationRecord config) {
        Random r = new Random();
        int i=0;
        while (i<numberOfAnimals) {

            int xCoordinate = r.nextInt(config.mapBoundary().upperRight().getX());
            int yCoordinate = r.nextInt(config.mapBoundary().upperRight().getY());
            Vector2d moveCandidate = new Vector2d(xCoordinate, yCoordinate);
            if(this.worldMap.canMoveTo(moveCandidate))
            {
                Animal animal = new Animal(config,moveCandidate);
                this.worldMap.place(animal);
                i++;
            }
        }
    }

    private void moveAllAnimals() {
        for(Animal animal : animals){
            worldMap.move(animal);
        }
    }

    private void rotateAllAnimals() {


        for(Animal animal : animals){
            worldMap.rotate(animal);
        }
    }

    private void removeDeadAnimals(int day){

        for(Animal animal : animals){
            if(animal.getEnergy()<=0){
                Vector2d position = animal.getPosition();
                animal.dies(day);
                worldMap.removeAnimal(position,animal);
            }
        }

    }


}