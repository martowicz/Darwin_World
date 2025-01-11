package agh.oop.darwin_world;
import agh.oop.darwin_world.model.enums.MapDirection;
import agh.oop.darwin_world.model.genoms.Genome;
import agh.oop.darwin_world.model.utils.Vector2d;
import agh.oop.darwin_world.model.world_elements.Animal;
import agh.oop.darwin_world.model.worlds.AbstractWorldMap;
import agh.oop.darwin_world.model.worlds.RectangularMap;
import agh.oop.darwin_world.model.worlds.WorldMap;
import agh.oop.darwin_world.presenter.ConsoleMapDisplay;
import agh.oop.darwin_world.simulation.Simulation;

import java.util.List;

public class World {
    public static void main(String[] args) {
        System.out.println("system wystartował");
        Vector2d position = new Vector2d(5,5);
        Animal parent1 = new Animal(position, 20, new Genome(7));
        Animal parent2= new Animal(position, 20, new Genome(7));
        Genome genome = new Genome(parent1.getListOfGenes(),parent2.getListOfGenes(),10,5);
        System.out.println(genome.getGenes().toString());



        AbstractWorldMap newRecMap = new RectangularMap(8,8);
        newRecMap.addObserver(new ConsoleMapDisplay()); //obserwator w terminalu
        List<MapDirection> directions = OptionsParser.parse(args);
        List<Vector2d> positions = List.of(new Vector2d(2, 2), new Vector2d(3, 4), new Vector2d(1, 1));
        Simulation simulation = new Simulation(positions,directions,newRecMap);
        simulation.Run();


        System.out.println("System zakonczyl dzialanie");

    }
}
