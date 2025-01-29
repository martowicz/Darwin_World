package agh.oop.darwin_world.model.world_elements;

import agh.oop.darwin_world.model.enums.AnimalMutationType;
import agh.oop.darwin_world.model.enums.WorldMapType;
import agh.oop.darwin_world.model.utils.Vector2d;
import agh.oop.darwin_world.model.worlds.Boundary;
import agh.oop.darwin_world.presenter.UserConfigurationRecord;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class LakeTest {
    UserConfigurationRecord config= new UserConfigurationRecord(
            new Boundary(new Vector2d(0,0),new Vector2d(10,10)),
            WorldMapType.ROUND_WORLD,
            5,
            5,
            5,
            5,
            5,
            5,
            5,
            0,
            5,
            7,
            AnimalMutationType.LIGHT_CORRECTION_MUTATION
    );

    @Test
    void testExtendLake() {
        //Given
        Lake lake = new Lake(config);
        int initialRadius = lake.getRadius();

        //When
        lake.extendLake();

        // Then
        assertTrue(lake.getRadius() >= initialRadius, "Promień jeziora powinien wzrosnąć");
    }

    @Test
    void testReduceLake() {
        //Given
        Lake lake = new Lake(config);
        lake.extendLake();
        int initialRadius = lake.getRadius();

        //When
        lake.reduceLake();

        //Then
        assertTrue(lake.getRadius() <= initialRadius, "Promień jeziora powinien się zmniejszyć");
    }


}
