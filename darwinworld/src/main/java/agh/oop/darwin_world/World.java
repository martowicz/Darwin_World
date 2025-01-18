package agh.oop.darwin_world;
import agh.oop.darwin_world.model.enums.AnimalMutationType;
import agh.oop.darwin_world.model.enums.WorldMapType;
import agh.oop.darwin_world.model.utils.Vector2d;
import agh.oop.darwin_world.model.worlds.AbstractWorldMap;
import agh.oop.darwin_world.model.worlds.Boundary;
import agh.oop.darwin_world.model.worlds.Earth;
import agh.oop.darwin_world.presenter.UserConfigurationRecord;
import agh.oop.darwin_world.simulation.Simulation;

public class World {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("system wystartował");

        UserConfigurationRecord config = new UserConfigurationRecord(
                new Boundary(new Vector2d(0,0),new Vector2d(8,8)),
                WorldMapType.WATER_WORLD,
                6,
                5,
                2,
                6,
                12,
                0,
                5,
                7,
                AnimalMutationType.RANDOM_MUTATION
        );


        Simulation simulation = new Simulation(config);
        simulation.Run();

        System.out.println("System zakonczyl dzialanie");

    }
}
