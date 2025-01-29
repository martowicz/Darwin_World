package agh.oop.darwin_world.model.world_elements;

import agh.oop.darwin_world.model.enums.AnimalMutationType;
import agh.oop.darwin_world.model.enums.MapDirection;
import agh.oop.darwin_world.model.enums.WorldMapType;
import agh.oop.darwin_world.model.utils.Vector2d;
import agh.oop.darwin_world.model.worlds.Boundary;
import agh.oop.darwin_world.presenter.UserConfigurationRecord;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AnimalTest {
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
    void testRotate() {
        Vector2d position = new Vector2d(2, 3);
        Animal animal = new Animal(config, position);

        MapDirection initialOrientation = animal.getAnimalOrientation();
        animal.rotate();
        MapDirection newOrientation = animal.getAnimalOrientation();

        // Testujemy, czy orientacja zmienia się po rotacji
        assertNotEquals(initialOrientation, newOrientation);
    }

    @Test
    void testEat() {
        Vector2d position = new Vector2d(2, 3);
        Animal animal = new Animal(config, position);

        int initialEnergy = animal.getEnergy();
        int energyFromPlant = 10; // Przykład energii, którą zwierzę dostaje z rośliny

        animal.eat(energyFromPlant);
        assertEquals(initialEnergy + energyFromPlant, animal.getEnergy());
    }

    @Test
    void testAddKid() {
        Vector2d position = new Vector2d(2, 3);
        Animal parent1 = new Animal(config, position);
        Animal parent2 = new Animal(config, position);

        parent1.addKid(new Animal(config, position)); // Dodajemy dziecko
        assertEquals(1, parent1.getKids());  // Sprawdzamy, czy liczba dzieci wzrosła
    }

    @Test
    void testCompareTo() {
        Vector2d position = new Vector2d(2, 3);
        Animal animal1 = new Animal(config, position);
        Animal animal2 = new Animal(config, position);

        animal2.subtractEnergy(1);

        assertTrue(animal1.compareTo(animal2) > 0);
    }

    @Test
    void testGetDescendants() {
        Vector2d position = new Vector2d(2, 3);
        Animal grandParent = new Animal(config, position);
        Animal parent = new Animal(config, position);
        Animal child = new Animal(config, position);

        grandParent.addKid(parent);
        parent.addKid(child);

        assertEquals(2, grandParent.getDescendants());  // Sprawdzamy, czy liczba potomków jest poprawna
    }

}
