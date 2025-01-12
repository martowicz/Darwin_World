package agh.oop.darwin_world.model.mutation;

import java.util.List;
import java.util.Random;

public class LightCorrectionMutation extends AbstractMutation{

    public LightCorrectionMutation(int min, int max) {
        super(min, max);
    }

    @Override
    int changeGene(int geneValue) {
        Random rand = new Random();
        int version=rand.nextInt(2);
        if (version==0){
            System.out.println("Zmiana z "+ geneValue+" na "+ (geneValue-1));
            return geneValue-1;
        }
        else{
            return geneValue+1;
        }
    }


}
