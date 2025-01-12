package agh.oop.darwin_world;
import agh.oop.darwin_world.model.enums.AnimalMutationType;
import agh.oop.darwin_world.model.enums.WorldMapType;
import agh.oop.darwin_world.presenter.UserConfiguration;
import agh.oop.darwin_world.simulation.Simulation;

public class World {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("system wystartował");


        UserConfiguration config = new UserConfiguration();


        config.setAnimalsCountAtStart(3);
        config.setGenomLength(7);
        config.setAnimalsEnergyAtStart(20);
        config.setMutationType(AnimalMutationType.RANDOM_MUTATION);
        config.setMinimalMutations(0);
        config.setMaximalMutations(5); //zapytać czy ograniczyć do genom length i czy można wylosowac ten sam gen do mutacji
        config.setMapType(WorldMapType.RECTANGULAR_WORLD);
        config.setMapBoundary(8,8);


        Simulation simulation = new Simulation(config);
        simulation.Run();

        System.out.println("System zakonczyl dzialanie");

    }
}
