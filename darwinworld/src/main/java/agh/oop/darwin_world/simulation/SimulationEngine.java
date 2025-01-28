package agh.oop.darwin_world.simulation;

import agh.oop.darwin_world.simulation.Simulation;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class SimulationEngine {

    private final ExecutorService threadPool = Executors.newFixedThreadPool(4);
    public void addToAsyncInThreadPool(Simulation simulation) {

        threadPool.submit(simulation);
    }

    public void shutdown(Simulation simulation) {
           simulation.end();


}
}