package agh.oop.darwin_world.model.world_elements;
import agh.oop.darwin_world.model.enums.MapDirection;
import agh.oop.darwin_world.model.mutation.AbstractMutation;
import agh.oop.darwin_world.model.utils.Vector2d;
import agh.oop.darwin_world.presenter.UserConfigurationRecord;
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
    private List<Integer> genes = new ArrayList<>();
    private AbstractMutation mutation;


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
        System.out.println(side + " " + percentageofparent1 + " " + percentageofparent2);
        System.out.println(parent1genes.toString() + " " + parent2genes.toString());
        if (side == 0) {


            //TODO to chyba coś nie tak bo ten kod jest taki sam


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
        int rotation = genes.get(iterator% genomeLength);
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


    public int getKids(){
        return kids.size();
    }

    public int getDescendants() {
        return kids.size() + kids.stream()
                .mapToInt(Animal::getDescendants)
                .sum();
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

    public String info(){
        return genes.toString()+" "+animalOrientation.toString()+" "+animalPosition.toString()+" "+energy;
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

}