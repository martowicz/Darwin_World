package agh.oop.darwin_world.model.mutation;

import java.util.Random;

public class LightCorrectionMutation extends AbstractMutation{

    public LightCorrectionMutation(int min, int max) {
        super(min, max);
    }

    @Override
    int changeGene(int geneValue) {
        Random rand = new Random();
        int version=rand.nextInt(2);
        if (version==0){return geneValue-1;}
        else{return geneValue+1;}
    }


}
