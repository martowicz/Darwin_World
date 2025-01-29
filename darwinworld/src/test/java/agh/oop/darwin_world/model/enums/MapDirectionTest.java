package agh.oop.darwin_world.model.enums;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MapDirectionTest {
    @Test
    void testReverse() {
        assertEquals(MapDirection.S, MapDirection.N.reverse());
        assertEquals(MapDirection.W, MapDirection.E.reverse());
        assertEquals(MapDirection.N, MapDirection.S.reverse());
        assertEquals(MapDirection.E, MapDirection.W.reverse());
        assertEquals(MapDirection.SW, MapDirection.NE.reverse());
        assertEquals(MapDirection.SE, MapDirection.NW.reverse());
        assertEquals(MapDirection.NW, MapDirection.SE.reverse());
        assertEquals(MapDirection.NE, MapDirection.SW.reverse());
    }

    @Test
    void testGetRandomPosition() {
        MapDirection randomPosition = MapDirection.getRandomPosition();
        assertTrue(randomPosition == MapDirection.N || randomPosition == MapDirection.NE || randomPosition == MapDirection.E || randomPosition == MapDirection.SE || randomPosition == MapDirection.S || randomPosition == MapDirection.SW || randomPosition == MapDirection.W || randomPosition == MapDirection.NW);
    }

    @Test
    void testRotate() {
        assertEquals(MapDirection.N, MapDirection.N.rotate(8));
        assertEquals(MapDirection.E, MapDirection.NE.rotate(1));
        assertEquals(MapDirection.S, MapDirection.N.rotate(4));
    }
}
