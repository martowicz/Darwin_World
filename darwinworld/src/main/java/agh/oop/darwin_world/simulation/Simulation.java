package agh.oop.darwin_world.simulation;
import agh.oop.darwin_world.model.utils.RandomPositionGenerator;
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
    private List<Animal> deadAnimals = new ArrayList<>();
    private AbstractWorldMap worldMap;
    private final int plantsGrowingEveryDay;
    private int days = 0;
    private final RandomPositionGenerator randomPositionGenerator;
    volatile private boolean running=true;
    private final Object lock = new Object();
    volatile private boolean ended = false;
    public Simulation(UserConfigurationRecord config)
    {
        this.numberOfAnimals=config.animalsCountAtStart();
        this.worldMap = config.mapType().enumToMap(config);
        this.randomPositionGenerator = new RandomPositionGenerator();
        //this.worldMap.addObserver(new ConsoleMapDisplay());
        this.plantsGrowingEveryDay = config.plantsGrowingDaily(); //obserwator w terminalu
        placeAnimalsOnTheMap(config);
        this.animals = this.worldMap.getAnimalsToList();

    }

    public AbstractWorldMap getWorldMap() {
        return worldMap;
    }

    public int getDay(){
        return days;
    }

    @Override
    public void run(){

        while (!animals.isEmpty()) {
            if (ended) return;
            if (!running) continue;


            animals = worldMap.getAnimalsToList();

            days += 1;

            //1-Usunięcie martwych zwierzaków z mapy.
            //--
            removeDeadAnimals(days);
            //--


            animals = worldMap.getAnimalsToList();

            worldMap.notifyObservers("dni:"+days);
            sleep();

            //2-Skręt i przemieszczenie każdego zwierzaka.
            //--
            rotateAllAnimals();


            worldMap.notifyObservers("dni:"+days);
            //worldMap.displayLinkedLists();

            sleep();

            moveAllAnimals();


            //--


            //3-Konsumpcja roślin, na których pola weszły zwierzaki.
            //--
            worldMap.animalsEatPlants();
            //--


            //4-Rozmnażanie się najedzonych zwierzaków znajdujących się na tym samym polu.
            //--
            System.out.println(days);
            worldMap.reproduce();
            //--


            //5-Wzrastanie nowych roślin na wybranych polach mapy.
            //--
            worldMap.generateEnvironment(plantsGrowingEveryDay, days);
            //--


            //6-Koniec dnia(zwierzęta tracą energie)
            //--
            worldMap.dayPasses(days);
            //System.out.println(days + " days passed");

            //--
            animals = worldMap.getAnimalsToList();


        }

    }
    private void sleep() {
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);

        }
    }
    public void end() {
        ended = true;
    }

    public boolean isRunning(){
        return running;
    }

    public void pauseSimulation(){
        running=false;
    }

    public void resumeSimulation(){
        running=true;

    }

    private void placeAnimalsOnTheMap(UserConfigurationRecord config) {
        int i=0;
        while (i<numberOfAnimals) {
            Vector2d moveCandidate = randomPositionGenerator.generate(config.mapBoundary());
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
            if(animal.getEnergy()<=0 || animal.diesForOtherCause()){
                Vector2d position = animal.getPosition();
                animal.dies(day);
                worldMap.removeAnimal(position,animal);
                deadAnimals.add(animal);
            }
        }

    }

    public double averageAgeForDeadAnimals(){
        int sum=0;
        if(deadAnimals.isEmpty()){
            return 0;
        }
        for(Animal animal : deadAnimals){
            sum+=animal.getAge();
        }
        return (double) sum/deadAnimals.size();
    }

    public double averageKidsNumberForAliveAnimals(){
        if(animals.isEmpty()){
            return 0;
        }
        int sum=0;
        for(Animal animal : animals){
            sum+=animal.getKids();
        }
        return (double) sum/animals.size();
    }


}