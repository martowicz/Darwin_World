package agh.oop.darwin_world.simulation;
import agh.oop.darwin_world.model.utils.Vector2d;
import agh.oop.darwin_world.model.world_elements.Animal;
import agh.oop.darwin_world.model.worlds.AbstractWorldMap;
import agh.oop.darwin_world.presenter.ConsoleMapDisplay;
import agh.oop.darwin_world.presenter.UserConfigurationRecord;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
        //this.worldMap.addObserver(new ConsoleMapDisplay());
        this.plantsGrowingEveryDay = config.plantsGrowingDaily(); //obserwator w terminalu
        placeAnimalsOnTheMap(config);
        this.animals = this.worldMap.getAnimalsToList();

    }

    public AbstractWorldMap getWorldMap() {
        return worldMap;
    }

    @Override
    public void run(){

        while (!animals.isEmpty()) {
            animals = worldMap.getAnimalsToList();
            days += 1;
            //1-Usunięcie martwych zwierzaków z mapy.
            //--
            removeDeadAnimals(days);
            //--

            worldMap.notifyObservers("dniii:"+days);
            sleep();

            //2-Skręt i przemieszczenie każdego zwierzaka.
            //--
            rotateAllAnimals();


            worldMap.notifyObservers("dni:"+days);
            worldMap.displayLinkedLists();

            sleep();

            moveAllAnimals();


            //--


            //3-Konsumpcja roślin, na których pola weszły zwierzaki.
            //--
            worldMap.animalsEatPlants();
            //--


            //4-Rozmnażanie się najedzonych zwierzaków znajdujących się na tym samym polu.
            //--
            worldMap.reproduce();
            //--


            //5-Wzrastanie nowych roślin na wybranych polach mapy.
            //--
            worldMap.generateEnvironment(plantsGrowingEveryDay, days);
            //--

            //6-Koniec dnia(zwierzęta tracą energie)
            //--
            worldMap.dayPasses(days);
            System.out.println(days + " days passed");

            //--


        }

    }
    private void sleep() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);

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