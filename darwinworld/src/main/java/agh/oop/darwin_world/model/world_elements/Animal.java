package agh.oop.darwin_world.model.world_elements;

import agh.oop.darwin_world.model.enums.MapDirection;

import agh.oop.darwin_world.model.mutation.AbstractMutation;
import agh.oop.darwin_world.model.utils.Vector2d;
import agh.oop.darwin_world.model.worlds.WorldMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

public class Animal implements WorldElement {
    //Atrybuty
    private int iterator=0;
    private int age=0;
    private  List<Animal> kids = new ArrayList<>();
    private int energy;
    private int plantsEaten=0;
    private int dayOfDeath;
    private List<Integer> genes = new ArrayList<>();
    private AbstractMutation mutation;





    private static final Vector2d DEFAULT_VECTOR = new Vector2d(2, 2);
    private static final MapDirection DEFAULT_ANIMAL_ORIENTATION = MapDirection.N;

    private MapDirection animalOrientation;
    private Vector2d animalPosition;

    // First constructor - animal without parents
    public Animal (Vector2d inputAnimalCoordinates, int startingEnergy, int genomeSize, AbstractMutation mutation) //+mutation.version
    {
        this.animalOrientation= DEFAULT_ANIMAL_ORIENTATION;
        this.animalPosition = inputAnimalCoordinates;
        this.energy = startingEnergy;
        Random r = new Random();
        for( int i=0; i<genomeSize; i++ ){
            int n = r.nextInt(8);
            this.genes.add(n);
        }
        this.mutation = mutation;

    }
    //Second constructor - animal from parents
    public Animal(Animal parent1, Animal parent2, int startingEnergy, AbstractMutation mutation){
        this.energy=startingEnergy;
        this.animalPosition = parent1.getPosition();
        this.animalOrientation = DEFAULT_ANIMAL_ORIENTATION;
        this.mutation = mutation;

        //to jest brzydkie, ale działa
        int pe1=parent1.getEnergy();
        int pe2=parent2.getEnergy();
        List<Integer> pg1 = parent1.getListOfGenes();
        List<Integer> pg2 = parent2.getListOfGenes();
        getGenesFromParents(pg1,pg2,pe1,pe2);
        genes = mutation.mutateGenes(genes);

    }

    @Override
    public String toString() {
        return animalOrientation.toString();
    }

    @Override
    public Vector2d getPosition() {
        return animalPosition;
    }

    public void rotate(){
        iterator++;
        int rotation = genes.get(iterator);
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
        return genes;
    }


    public int getKids(){
        return kids.size();
    }

    public int getDescendants() {
        return kids.size() + kids.stream()
                .mapToInt(Animal::getDescendants)
                .sum();
    }

    public int getEnergy(){
        return energy;
    }

    public void addKid(Animal kid){
        kids.add(kid);
    }



    public MapDirection getAnimalOrientation()
    {
        return animalOrientation;
    }

    public void getGenesFromParents(List <Integer> parent1genes, List <Integer> parent2genes, int parent1energy, int parent2energy) {
        Random rand = new Random();
        int side = rand.nextInt(2); //0 - lewa strona 1-prawa strona
        double percentageofparent1 = (double) (parent1energy) / (parent1energy + parent2energy);
        double percentageofparent2 = 1 - percentageofparent1;
        System.out.println(side + " " + percentageofparent1 + " " + percentageofparent2);
        System.out.println(parent1genes.toString() + " " + parent2genes.toString());
        if (side == 0) {
            //bierzemy silniejszego z lewej
            if (percentageofparent2 > percentageofparent1) {
                int divider = (int) (percentageofparent2 * parent2genes.size());
                System.out.println(divider);
                for (int i = 0; i < divider; i++) {
                    genes.add(parent2genes.get(i));
                }
                for (int i = divider; i < parent1genes.size(); i++) {
                    genes.add(parent1genes.get(i));
                }
            } else {
                int divider = (int) (percentageofparent2 * parent1genes.size());
                System.out.println(divider);
                for (int i = 0; i < divider; i++) {
                    genes.add(parent1genes.get(i));
                }
                for (int i = divider; i < parent1genes.size(); i++) {
                    genes.add(parent2genes.get(i));
                }
            }
        } else {
            //bierzemy silniejszego z prawej
            if (percentageofparent2 > percentageofparent1) {
                int divider = (int) (percentageofparent2 * parent2genes.size());
                System.out.println(divider);
                for (int i = 0; i < divider; i++) {
                    genes.add(parent1genes.get(i));
                }
                for (int i = divider; i < parent1genes.size(); i++) {
                    genes.add(parent2genes.get(i));
                }
            } else {
                int divider = (int) (percentageofparent2 * parent1genes.size());
                System.out.println(divider);
                for (int i = 0; i < divider; i++) {
                    genes.add(parent2genes.get(i));
                }
                for (int i = divider; i < parent1genes.size(); i++) {
                    genes.add(parent1genes.get(i));
                }
            }
        }



    }

    public String info(){
        return genes.toString()+" "+animalOrientation.toString()+" "+animalPosition.toString()+" "+energy;
    }





}