package agh.oop.darwin_world.presenter;

import agh.oop.darwin_world.World;
import agh.oop.darwin_world.model.utils.Vector2d;
import agh.oop.darwin_world.model.world_elements.WorldElement;
import agh.oop.darwin_world.model.worlds.AbstractWorldMap;
import agh.oop.darwin_world.model.worlds.Boundary;
import agh.oop.darwin_world.model.worlds.WorldMap;
import agh.oop.darwin_world.simulation.Simulation;
import agh.oop.darwin_world.simulation.SimulationEngine;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.util.Collections;


public class SimulationWindowPresenter implements MapChangeListener {

    @FXML
    public ScrollPane mapScrollPane;

    @FXML
    public GridPane mapGrid;

    @FXML
    private Label moveDescriptionLabel;

    @FXML
    private Label numberOfAnimalsLabel;

    @FXML
    private Label numberOfPlantsLabel;

    @FXML
    private Label averageEnergyLabel;

    AbstractWorldMap worldMap;

    Stage newWindowStage;
    @Override
    public void mapChanged(WorldMap worldMap, String message) {
        Platform.runLater(() -> {
            drawMap();
            moveDescriptionLabel.setText(message);
            updateStatistics();
        });
    }

    public void drawMap() {
        clearGrid();
        Boundary boundary = worldMap.getCurrentBounds();
        int minX = boundary.lowerLeft().getX();
        int minY = boundary.lowerLeft().getY();
        int maxX = boundary.upperRight().getX();
        int maxY = boundary.upperRight().getY();

        configureMapGrid(minX, maxX, minY, maxY);
        fillMapGridWithElements(minX, maxX, minY, maxY);

        for (Node label : mapGrid.getChildren())
            GridPane.setHalignment(label, HPos.CENTER);

    }

    public void updateStatistics() {
        int numberOfAnimals = worldMap.getNumberOfAnimals();
        numberOfAnimalsLabel.setText(String.valueOf(numberOfAnimals));
        averageEnergyLabel.setText(String.format("%.2f",worldMap.averageAnimalEnergy()));
        numberOfPlantsLabel.setText(String.valueOf(worldMap.getNumberOfPlants()));
    }

    public void fillMapGridWithElements(int minX, int maxX, int minY, int maxY) {
        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <=maxY; y++) {
                Label label = new Label(" ");
                if(worldMap.isOccupied(new Vector2d(x, y)))
                {
                    WorldElement el = worldMap.returnObjectAt(new Vector2d(x, y));
                    label.setText(el.toString());
                }
                else
                {
                    label.setText(" ");


                }
                label.getStyleClass().add("mapAnimalLabel");
                mapGrid.add(label, x - minX + 1, maxY - y + 1);

            }
        }
    }





    public void configureMapGrid(int xMin, int xMax, int yMin, int yMax) {

        int MAP_WIDTH = xMax-xMin+1;
        int MAP_HEIGHT = yMax-yMin+1;
        int RECT_CELL_SIZE;
        int MAX_MAP_SIZE  = (int)(newWindowStage.getHeight()*0.7);
        if (MAP_WIDTH>MAP_HEIGHT)
        {
            RECT_CELL_SIZE = MAX_MAP_SIZE /MAP_WIDTH;
        }
        else
        {
            RECT_CELL_SIZE = MAX_MAP_SIZE /MAP_HEIGHT;
        }

        mapGrid.add(new Label("y\\x"), 0, 0);

        for (int x = xMin; x <= xMax; x++)
            mapGrid.add(new Label("%d".formatted(x)), x - xMin + 1, 0);
        for (int y = yMin; y <= yMax; y++)
            mapGrid.add(new Label("%d".formatted(y)), 0, yMax - y + 1);

        for (int i = 0; i < xMax - xMin + 2; i++)
            mapGrid.getColumnConstraints().add(new ColumnConstraints(RECT_CELL_SIZE));
        for (int i = 0; i < yMax - yMin + 2; i++)
            mapGrid.getRowConstraints().add(new RowConstraints(RECT_CELL_SIZE));

    }

    private void clearGrid() {
        mapGrid.getChildren().retainAll(mapGrid.getChildren().getFirst()); // hack to retain visible grid lines
        mapGrid.getColumnConstraints().clear();
        mapGrid.getRowConstraints().clear();
    }

    @FXML
    void initialize()
    {
        System.out.println("Simulation Window presenter");

    }

    public void onPauseButtonClicked() {

    }



    public void runSimulation(UserConfigurationRecord config, SimulationEngine simulationEngine,Stage newWindowStage) {

        System.out.println("Simulation running...");
        Simulation simulation = new Simulation(config);
        simulation.getWorldMap().addObserver(this);
        simulationEngine.addToAsyncInThreadPool(simulation);

        this.newWindowStage=newWindowStage;
        this.worldMap=simulation.getWorldMap();
   }
}
