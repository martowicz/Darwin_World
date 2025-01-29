package agh.oop.darwin_world.model.mutation;

import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LightCorrectionMutationTest {
    @Test
    void testChangeGeneIncrease() {
        // Given
        LightCorrectionMutation mutation = new LightCorrectionMutation(1, 10);
        int geneValue = 5;

        //When
        int changedGene = mutation.changeGene(geneValue);

        //Then
        assertTrue(changedGene == geneValue - 1 || changedGene == geneValue + 1);

    }
}
