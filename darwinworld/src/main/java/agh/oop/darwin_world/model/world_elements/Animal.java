package agh.oop.darwin_world.model.world_elements;
import agh.oop.darwin_world.model.enums.MapDirection;
import agh.oop.darwin_world.model.mutation.AbstractMutation;
import agh.oop.darwin_world.model.utils.Vector2d;
import agh.oop.darwin_world.presenter.UserConfigurationRecord;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Animal implements WorldElement, Comparable<Animal> {

    private MapDirection animalOrientation;
    private Vector2d animalPosition;
    private int iterator=0;
    private int age=0;
    private  List<Animal> kids = new ArrayList<>();
    private int energy;
    private int plantsEaten=0;
    private int dayOfDeath;
    int genomeLength;
    private boolean isDead=false;
    private List<Integer> genes = new ArrayList<>();
    private AbstractMutation mutation;
    private boolean shouldDie=false;





    // First constructor - animal without parents
    public Animal (UserConfigurationRecord config, Vector2d position)
    {
        //New animals have random orientation and position(at the start of simulation)
        this.animalOrientation= MapDirection.getRandomPosition();
        this.animalPosition = position;
        this.genomeLength = config.genomLength();
        this.energy = config.animalsEnergyAtStart();

        Random r = new Random();
        for(int i = 0; i< this.genomeLength; i++ ){
            int n = r.nextInt(8);
            this.genes.add(n);
        }

        this.mutation = config.mutationType().enumToMutation(config.minMutations(), config.maxMutations());
        this.iterator = r.nextInt(this.genomeLength);

    }


    //Second constructor - animal from parents
    public Animal (Animal parent1,Animal parent2, UserConfigurationRecord config)
    {
        //New animals have random orientation
        this.animalOrientation= MapDirection.getRandomPosition();

        //przemyśleć to bo w teorii np ojciec się rusza to syn też bo to byłoby to samo miejsce w pamięci
        this.animalPosition = new Vector2d(parent1.animalPosition.getX(), parent1.animalPosition.getY());
        this.genomeLength = config.genomLength();
        this.energy = 2*config.animalsEnergySpentOnCopulation();
        Random r = new Random();
        this.iterator = r.nextInt(this.genomeLength);

        //przypisywanie genów
        getGenesFromParents(parent1, parent2);


        //mutowanie genów
        this.mutation = config.mutationType().enumToMutation(config.minMutations(), config.maxMutations());
        this.mutation.mutateGenes(genes);


    }

    public void getGenesFromParents(Animal parent1,Animal parent2) {

        List <Integer> parent1genes=parent1.genes;
        List <Integer> parent2genes=parent2.genes;
        int parent1energy=parent1.energy;
        int parent2energy=parent2.energy;
        Random rand = new Random();
        int side = rand.nextInt(2); //0 - lewa strona 1-prawa strona
        double percentageofparent1 = (double) (parent1energy) / (parent1energy + parent2energy);
        double percentageofparent2 = 1 - percentageofparent1;

        if (side == 0) {
            if (percentageofparent2 > percentageofparent1) {
                int divider = (int) (percentageofparent2 * parent2genes.size());
                for (int i = 0; i < divider; i++) {genes.add(parent2genes.get(i));}
                for (int i = divider; i < parent1genes.size(); i++) {genes.add(parent1genes.get(i));}
            } else {
                int divider = (int) (percentageofparent2 * parent1genes.size());
                for (int i = 0; i < divider; i++) {genes.add(parent1genes.get(i));}
                for (int i = divider; i < parent1genes.size(); i++) {genes.add(parent2genes.get(i));}
            }
        } else {
            //bierzemy silniejszego z prawej
            if (percentageofparent2 > percentageofparent1) {
                int divider = (int) (percentageofparent2 * parent2genes.size());
                for (int i = 0; i < divider; i++) {
                    genes.add(parent1genes.get(i));
                }
                for (int i = divider; i < parent1genes.size(); i++) {
                    genes.add(parent2genes.get(i));
                }
            } else {
                int divider = (int) (percentageofparent2 * parent1genes.size());
                for (int i = 0; i < divider; i++) {
                    genes.add(parent2genes.get(i));
                }
                for (int i = divider; i < parent1genes.size(); i++) {
                    genes.add(parent1genes.get(i));
                }
            }
        }



    }

    public void setAnimalPosition(Vector2d position){
        this.animalPosition = position;
    }

    public void setAnimalOrientation(MapDirection orientation){
        this.animalOrientation = orientation;
    }

    public void setEnergy(int energy){this.energy = energy;}

    @Override
    public String toString() {

        return animalOrientation.toString();
    }

    @Override
    public Vector2d getPosition() {
        return animalPosition;
    }

    @Override
    public Color getColor() {
        int b =getEnergy();
        if(b>255) b=255;
        int r=255-b;
        int g=0;
        //(R,G,B)
        //(0,0,255) - dużo energii
        //(255,0,0) - mało energii
        return Color.rgb(r, g, b);
    }

    public int getActiveGene(){
        return genes.get(iterator%genes.size());
    }

    public List<Integer> getGenes(){
        return genes;
    }

    public int getPlantsEaten(){
        return plantsEaten;
    }

    public int getKids(){
        return kids.size();
    }

    public int getDescendants() {
        return kids.size() + kids.stream()
                .mapToInt(Animal::getDescendants)
                .sum();
    }

    public int getAge(){
        return age;
    }

    public String getDeathDay() {
        return this.isDead ? String.valueOf(this.dayOfDeath) : "Alive";}


    public void setDyingForOtherCause(){shouldDie=true;}

    public boolean diesForOtherCause(){return shouldDie;}

    public void rotate(){
        iterator++;
        iterator=iterator % genomeLength;

        int rotation = genes.get(iterator);

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
        this.isDead=true;
    }




    public void addKid(Animal kid){
        kids.add(kid);
    }
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



    @Override
    public int compareTo(Animal o)
    {
        //zwraca 1 jeśli  this.animal>o.animal
        if(this.energy > o.energy)
            return 1;
        else if(this.energy < o.energy)
            return -1;
        else
        {
            if(this.age > o.age)
                return 1;
            else if (this.age < o.age)
                return -1;
            else
            {
                if(this.getKids()>o.getKids())
                    return 1;
                else if (this.getKids()<o.getKids())
                    return -1;
                else
                {
                    Random r = new Random();
                    if(r.nextInt(2)==0)
                        return 1;
                    else
                        return -1;


                }

            }

        }
    }

    public void subtractEnergy(int energyToReproduce)
    {
        energy-=energyToReproduce;
    }

}