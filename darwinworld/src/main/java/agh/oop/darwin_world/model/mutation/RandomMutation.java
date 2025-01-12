package agh.oop.darwin_world.model.mutation;

import java.util.List;
import java.util.Random;

public class RandomMutation extends AbstractMutation{

    public RandomMutation(int min, int max) {
        super(min, max);
    }

    @Override
    int changeGene(int geneValue) {
        Random rand = new Random();
        return rand.nextInt(8);
    }


}
