package agh.oop.darwin_world.model.genoms;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Genome {
    int size;
    int iterator=0;
    List<Integer> genes = new ArrayList<Integer>();
    public Genome(int genomeSize) {
        size = genomeSize;
        for (int i=0; i<genomeSize; i++) {
            Random rand = new Random();
            int randomNum = rand.nextInt(size);
            genes.add(randomNum);
        }
    }
    public Genome (List <Integer> parent1genes, List <Integer> parent2genes, int parent1energy, int parent2energy) {
        getGenesFromParents(parent1genes, parent2genes, parent1energy, parent2energy);
    }

    public int getNextGene(){
        iterator++;
        return genes.get(iterator);
    }

    public List<Integer> getGenes(){return genes;}

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

}
