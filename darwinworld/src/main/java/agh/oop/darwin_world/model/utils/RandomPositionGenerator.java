package agh.oop.darwin_world.model.utils;
import agh.oop.darwin_world.model.worlds.Boundary;

import java.security.SecureRandom;
import java.util.*;

public class RandomPositionGenerator {


    public List<Vector2d> generateAllPositions(Boundary boundary) {
        // Pobranie zakresów dla osi x i y
        int xMin = boundary.lowerLeft().getX();
        int xMax = boundary.upperRight().getX();
        int yMin = boundary.lowerLeft().getY();
        int yMax = boundary.upperRight().getY();

        List<Vector2d> positions = new ArrayList<>();
        for (int x = xMin; x <= xMax; x++) {
            for (int y = yMin; y <= yMax; y++) {
                positions.add(new Vector2d(x, y));
            }
        }
        Collections.shuffle(positions);
        return positions;
    }
    public Vector2d getRandomPosition(Boundary boundary) {
        // Pobranie zakresów dla osi x i y
        int xMin = boundary.lowerLeft().getX();
        int xMax = boundary.upperRight().getX();
        int yMin = boundary.lowerLeft().getY();
        int yMax = boundary.upperRight().getY();
        SecureRandom rand = new SecureRandom();
        return new Vector2d(rand.nextInt( xMax-xMin) + xMin, rand.nextInt(yMax-yMin) + yMin);





    }

}