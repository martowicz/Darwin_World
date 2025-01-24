package agh.oop.darwin_world.model.utils;
import agh.oop.darwin_world.model.worlds.Boundary;
import java.util.Random;
public class RandomPositionGenerator {
    private final Boundary boundary;
    private final Random random;
    // Konstruktor przyjmujący obiekt Boundary
    public RandomPositionGenerator(Boundary boundary) {
        this.boundary = boundary;
        this.random = new Random();
    }
    // Metoda generująca losową pozycję wewnątrz boundary
    public Vector2d generate() {
        // Pobranie zakresów dla osi x i y
        int xMin = boundary.lowerLeft().getX();
        int xMax = boundary.upperRight().getX();
        int yMin = boundary.lowerLeft().getY();
        int yMax = boundary.upperRight().getY();
        // Generowanie losowych współrzędnych w podanych granicach
        int randomX = random.nextInt(xMax - xMin + 1) + xMin;
        int randomY = random.nextInt(yMax - yMin + 1) + yMin;
        return new Vector2d(randomX, randomY);
    }
}