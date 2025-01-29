package agh.oop.darwin_world.model.worlds;

import agh.oop.darwin_world.model.enums.AnimalMutationType;
import agh.oop.darwin_world.model.enums.WorldMapType;
import agh.oop.darwin_world.model.utils.Vector2d;
import agh.oop.darwin_world.model.world_elements.Animal;
import agh.oop.darwin_world.presenter.UserConfigurationRecord;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AbstractWorldMapTest {
    UserConfigurationRecord config= new UserConfigurationRecord(
            new Boundary(new Vector2d(0,0),new Vector2d(10,10)),
            WorldMapType.ROUND_WORLD,
            5,
            5,
            5,
            5,
            5,
            2,
            5,
            0,
            5,
            7,
            AnimalMutationType.LIGHT_CORRECTION_MUTATION
    );

    @Test
    void testPlaceAnimal() {
        //Given
        AbstractWorldMap worldMap = config.mapType().enumToMap(config);
        Animal animal = new Animal(config, new Vector2d(3, 4));

        //When
        worldMap.place(animal);

        // Then
        assertEquals(animal, worldMap.returnAnimalAt(new Vector2d(3, 4)));
    }

    @Test
    void testReproduce() {
        //Given
        AbstractWorldMap worldMap = config.mapType().enumToMap(config);
        Animal animal1 = new Animal(config, new Vector2d(2, 3));
        Animal animal2 = new Animal(config, new Vector2d(2, 3));
        worldMap.place(animal1);
        worldMap.place(animal2);

        //When
        worldMap.reproduce();

        //Then
        assertEquals(3, worldMap.getNumberOfAnimals());
    }

    @Test
    void testMove() {
        //Given
        AbstractWorldMap worldMap = config.mapType().enumToMap(config);
        Animal animal = new Animal(config, new Vector2d(1, 1));
        worldMap.place(animal);
        Vector2d oldPosition = animal.getPosition();

        //When
        worldMap.move(animal);

        //Then
        assertNotEquals(oldPosition, animal.getPosition());
    }

    @Test
    void testGetNumberOfAnimals() {
        //Given
        AbstractWorldMap worldMap = config.mapType().enumToMap(config);
        Animal animal1 = new Animal(config, new Vector2d(1, 1));
        Animal animal2 = new Animal(config, new Vector2d(2, 2));
        worldMap.place(animal1);
        worldMap.place(animal2);

        //When
        int numberOfAnimals = worldMap.getNumberOfAnimals();

        //Then
        assertEquals(2, numberOfAnimals);
    }

    @Test
    void testAverageAnimalEnergy() {
        //Given
        AbstractWorldMap worldMap = config.mapType().enumToMap(config);
        Animal animal1 = new Animal(config, new Vector2d(1, 1));
        Animal animal2 = new Animal(config, new Vector2d(2, 2));
        worldMap.place(animal1);
        worldMap.place(animal2);

        //When
        animal1.setEnergy(50);
        animal2.setEnergy(30);
        double averageEnergy = worldMap.averageAnimalEnergy();

        //Then
        assertEquals(40, averageEnergy);
    }



}
