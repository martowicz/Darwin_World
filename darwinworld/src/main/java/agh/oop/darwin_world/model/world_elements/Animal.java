package agh.oop.darwin_world.model.world_elements;

import agh.oop.darwin_world.model.enums.MapDirection;
import agh.oop.darwin_world.model.genoms.Genome;
import agh.oop.darwin_world.model.utils.Vector2d;
import agh.oop.darwin_world.model.worlds.WorldMap;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class Animal implements WorldElement {
    //Atrybuty
    private int age=0;
    private  List<Animal> kids = new ArrayList<>();
    private int energy;
    private int plantsEaten=0;
    private int dayOfDeath;
    private Genome genome;





    private static final Vector2d DEFAULT_VECTOR = new Vector2d(2, 2);
    private static final MapDirection DEFAULT_ANIMAL_ORIENTATION = MapDirection.N;

    private MapDirection animalOrientation;
    private Vector2d animalPosition;

    public Animal (Vector2d inputAnimalCoordinates, int startingEnergy, Genome genome)
    {
        this.animalOrientation= DEFAULT_ANIMAL_ORIENTATION;
        this.animalPosition = inputAnimalCoordinates;
        this.energy = startingEnergy;
        this.genome = genome;

    }
    @Override
    public String toString() {return animalOrientation.toString();}

    @Override
    public Vector2d getPosition() {
        return animalPosition;
    }

    public void rotate(){
        int rotation = genome.getNextGene();
        animalOrientation = animalOrientation.rotate(rotation);
    }

    public void move(WorldMap worldMap) {

        Vector2d movecandidate =  new Vector2d(animalPosition.getX(), animalPosition.getY());
        movecandidate =movecandidate.add(animalOrientation.toUnitVector());
        if (worldMap.canMoveTo(movecandidate))
                {
                    animalPosition = movecandidate;

                }

    }

    public List<Integer> getListOfGenes(){
        return genome.getGenes();
    }


    public int getKids(){
        return kids.size();
    }

    public int getDescendants() {
        return kids.size() + kids.stream()
                .mapToInt(Animal::getDescendants)
                .sum();
    }



    public MapDirection getAnimalOrientation()
    {
        return animalOrientation;
    }





}