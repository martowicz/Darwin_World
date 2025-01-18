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


    public Simulation(UserConfigurationRecord config)
    {
        this.numberOfAnimals=config.animalsCountAtStart();
        Boundary boundary = config.mapBoundary();
        this.worldMap = config.mapType().enumToMap(config);

        this.worldMap.addObserver(new ConsoleMapDisplay()); //obserwator w terminalu

        placeAnimalsOnTheMap(config);

    }

    public void Run() throws InterruptedException {
        for (int i = 0; i < 3; i++)
        {
            Animal animal =  animals.get(i%numberOfAnimals);
            worldMap.move(animal);
            TimeUnit.MILLISECONDS.sleep(1000);
            //worldMap.displayLinkedLists();
        }
    }

    public List<Animal> getAnimals() {
        return animals;
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


}