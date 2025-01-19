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

public class Simulation {

    private int numberOfAnimals;
    private int movesCount = 0;
    private int animalsEnergyAtStart;
    private int genomLength;
    private List<Animal> animals = new ArrayList<>();
    private AbstractWorldMap worldMap;
    private final int plantsGrowingEveryDay;


    public Simulation(UserConfigurationRecord config)
    {
        this.numberOfAnimals=config.animalsCountAtStart();
        Boundary boundary = config.mapBoundary();
        this.worldMap = config.mapType().enumToMap(config);
        this.worldMap.addObserver(new ConsoleMapDisplay());
        this.plantsGrowingEveryDay = config.plantsGrowingDaily(); //obserwator w terminalu
        placeAnimalsOnTheMap(config);


    }

    public void Run() throws InterruptedException {
        for(int i=0;i<100;i++) {
            removeDeadAnimals(i);
            animals = worldMap.getAnimalsToList();

            moveAllAnimals();

            worldMap.animalsEatPlants();

            //reproduction();

            worldMap.generatePlants(plantsGrowingEveryDay);

            worldMap.dayPasses();
            for(Animal animal : animals) {
                System.out.println(animal.getEnergy());
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
                this.animals.add(animal);
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