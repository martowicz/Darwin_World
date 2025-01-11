package agh.oop.darwin_world;
import agh.oop.darwin_world.model.enums.MapDirection;
import agh.oop.darwin_world.model.utils.Vector2d;
import agh.oop.darwin_world.model.worlds.RectangularMap;
import agh.oop.darwin_world.model.worlds.WorldMap;
import agh.oop.darwin_world.simulation.Simulation;

import java.util.List;

public class World {
    public static void main(String[] args) {
        System.out.println("system wystartował");


        WorldMap newRecMap = new RectangularMap(8,8);
        List<MapDirection> directions = OptionsParser.parse(args);
        List<Vector2d> positions = List.of(new Vector2d(2, 2), new Vector2d(3, 4), new Vector2d(1, 1));
        Simulation simulation = new Simulation(positions, directions,newRecMap);
        simulation.Run();


        System.out.println("System zakonczyl dzialanie");

    }
}
