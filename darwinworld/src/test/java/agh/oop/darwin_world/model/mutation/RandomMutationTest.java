package agh.oop.darwin_world.model.mutation;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class RandomMutationTest {

    @Test
    void testChangeGeneWithinRange() {
        // Given
        RandomMutation mutation = new RandomMutation(1, 10);
        int geneValue = 5;

        //When
        int changedGene = mutation.changeGene(geneValue);

        //Then
        assertTrue(changedGene >= 0 && changedGene < 8);
    }

}
