package agh.oop.darwin_world.presenter;

import agh.oop.darwin_world.model.worlds.WorldMap;
import agh.oop.darwin_world.simulation.Simulation;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;


public class SimulationWindowPresenter implements MapChangeListener {

    @FXML
    public ScrollPane mapScrollPane;

    @FXML
    public GridPane mapGrid;

    @FXML
    private Label moveDescriptionLabel;


    @Override
    public void mapChanged(WorldMap worldMap, String message) {
        Platform.runLater(() -> {
            //drawMap();
            moveDescriptionLabel.setText(message);

        });
    }

    @FXML
    void initialize()
    {
        System.out.println("Simulation Window presenter");
    }


    public void runSimulation(UserConfigurationRecord config) {

        System.out.println("Simulation running...");
        Simulation simulation = new Simulation(config);
        simulation.getWorldMap().addObserver(this);
        simulation.run();

    }



}
