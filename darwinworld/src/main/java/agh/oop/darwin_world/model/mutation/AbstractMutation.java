package agh.oop.darwin_world.model.mutation;

import java.util.List;
import java.util.Random;

public abstract class AbstractMutation {
    int min;
    int max;


    public AbstractMutation(int minMutation,int maxMutation) {
        max=maxMutation;
        min=minMutation;
    }

    public List<Integer> mutateGenes(List<Integer> genes){
        int range = Math.min(genes.size(),max);
        Random r = new Random();
        int number_of_genes_to_change = r.nextInt(range);
        for (int i = 0; i < number_of_genes_to_change; i++) {
            int gene_to_change= r.nextInt(genes.size());
            int geneValue = genes.get(gene_to_change);
            genes.set(gene_to_change,changeGene(geneValue));
        }
        return genes;
    }

    abstract int changeGene(int geneValue);

}
