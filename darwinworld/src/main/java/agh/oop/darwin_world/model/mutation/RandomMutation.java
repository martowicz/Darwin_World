package agh.oop.darwin_world.model.mutation;

import java.util.Random;

public class RandomMutation extends AbstractMutation{

    public RandomMutation(int minMutation, int maxMutation) {
        super(minMutation, maxMutation);
    }

    @Override
    int changeGene(int geneValue) {
        Random rand = new Random();
        return rand.nextInt(8);
    }


}
