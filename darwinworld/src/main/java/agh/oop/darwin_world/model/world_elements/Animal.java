package agh.oop.darwin_world.model.world_elements;

import agh.oop.darwin_world.model.enums.MapDirection;

import agh.oop.darwin_world.model.mutation.AbstractMutation;
import agh.oop.darwin_world.model.utils.SortedLinkedList;
import agh.oop.darwin_world.model.utils.Vector2d;
import agh.oop.darwin_world.presenter.UserConfigurationRecord;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Animal implements WorldElement, Comparable<Animal> {
    @Override
    public int compareTo(Animal o) {
        return Integer.compare(o.energy, this.energy);
    }

    private MapDirection animalOrientation;
    private Vector2d animalPosition;

    private int iterator=0;


    private int age=0;
    private  List<Animal> kids = new ArrayList<>();
    private int energy;
    private int plantsEaten=0;
    private int dayOfDeath;
    int genomLength;

    private List<Integer> genes = new ArrayList<>();
    private AbstractMutation mutation;


    // First constructor - animal without parents
    public Animal (UserConfigurationRecord config, Vector2d position)
    {
        //New animals have random orientation and position(at the start of simulation)
        this.animalOrientation= MapDirection.getRandomPosition();

        this.animalPosition = position;

        this.genomLength = config.genomLength();
        this.energy = config.animalsEnergyAtStart();

        Random r = new Random();
        for(int i = 0; i< this.genomLength; i++ ){
            int n = r.nextInt(8);
            this.genes.add(n);
        }
        this.mutation = config.mutationType().enumToMutation(config.minMutations(), config.maxMutations());
        this.iterator = r.nextInt(this.genomLength);

    }



    //poprawić potem
    //Second constructor - animal from parents
//    public Animal(Animal parent1, Animal parent2, int startingEnergy, AbstractMutation mutation){
//        this.energy=startingEnergy;
//        this.animalPosition = parent1.getPosition();
//        this.animalOrientation = DEFAULT_ANIMAL_ORIENTATION;
//        this.mutation = mutation;
//
//        //to jest brzydkie, ale działa
//        int pe1=parent1.getEnergy();
//        int pe2=parent2.getEnergy();
//        List<Integer> pg1 = parent1.getListOfGenes();
//        List<Integer> pg2 = parent2.getListOfGenes();
//        getGenesFromParents(pg1,pg2,pe1,pe2);
//        genes = mutation.mutateGenes(genes);
//
//    }

    public void setAnimalPosition(Vector2d position){
        this.animalPosition = position;
    }

    public void setAnimalOrientation(MapDirection orientation){
        this.animalOrientation = orientation;
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
        int rotation = genes.get(iterator%genomLength);
        animalOrientation = animalOrientation.rotate(rotation);
    }

    public List<Integer> getListOfGenes(){
        return genes;
    }

    public void dayPasses(){
        this.age++;
        this.energy--;
    }

    public void dies(int dayOfDeath){
        this.dayOfDeath = dayOfDeath;
    }

//
//    public int getKids(){
//        return kids.size();
//    }
//
//    public int getDescendants() {
//        return kids.size() + kids.stream()
//                .mapToInt(Animal::getDescendants)
//                .sum();
//    }
//
//
//    public void addKid(Animal kid){
//        kids.add(kid);
//    }
    public void eat(int energyToAdd){
        this.energy += energyToAdd;
        this.plantsEaten++;
    }

    public int getEnergy(){
        return energy;
    }

    public MapDirection getAnimalOrientation()
    {
        return animalOrientation;
    }
//
//    public void getGenesFromParents(List <Integer> parent1genes, List <Integer> parent2genes, int parent1energy, int parent2energy) {
//        Random rand = new Random();
//        int side = rand.nextInt(2); //0 - lewa strona 1-prawa strona
//        double percentageofparent1 = (double) (parent1energy) / (parent1energy + parent2energy);
//        double percentageofparent2 = 1 - percentageofparent1;
//        System.out.println(side + " " + percentageofparent1 + " " + percentageofparent2);
//        System.out.println(parent1genes.toString() + " " + parent2genes.toString());
//        if (side == 0) {
//            //bierzemy silniejszego z lewej
//            if (percentageofparent2 > percentageofparent1) {
//                int divider = (int) (percentageofparent2 * parent2genes.size());
//                System.out.println(divider);
//                for (int i = 0; i < divider; i++) {
//                    genes.add(parent2genes.get(i));
//                }
//                for (int i = divider; i < parent1genes.size(); i++) {
//                    genes.add(parent1genes.get(i));
//                }
//            } else {
//                int divider = (int) (percentageofparent2 * parent1genes.size());
//                System.out.println(divider);
//                for (int i = 0; i < divider; i++) {
//                    genes.add(parent1genes.get(i));
//                }
//                for (int i = divider; i < parent1genes.size(); i++) {
//                    genes.add(parent2genes.get(i));
//                }
//            }
//        } else {
//            //bierzemy silniejszego z prawej
//            if (percentageofparent2 > percentageofparent1) {
//                int divider = (int) (percentageofparent2 * parent2genes.size());
//                System.out.println(divider);
//                for (int i = 0; i < divider; i++) {
//                    genes.add(parent1genes.get(i));
//                }
//                for (int i = divider; i < parent1genes.size(); i++) {
//                    genes.add(parent2genes.get(i));
//                }
//            } else {
//                int divider = (int) (percentageofparent2 * parent1genes.size());
//                System.out.println(divider);
//                for (int i = 0; i < divider; i++) {
//                    genes.add(parent2genes.get(i));
//                }
//                for (int i = divider; i < parent1genes.size(); i++) {
//                    genes.add(parent1genes.get(i));
//                }
//            }
//        }
//
//
//
//    }

    public String info(){
        return genes.toString()+" "+animalOrientation.toString()+" "+animalPosition.toString()+" "+energy;
    }





}