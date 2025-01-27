package agh.oop.darwin_world.presenter;

import agh.oop.darwin_world.model.utils.Vector2d;
import agh.oop.darwin_world.model.world_elements.Animal;
import agh.oop.darwin_world.model.world_elements.WorldElement;
import agh.oop.darwin_world.model.worlds.AbstractWorldMap;
import agh.oop.darwin_world.model.worlds.Boundary;
import agh.oop.darwin_world.model.worlds.WorldMap;
import agh.oop.darwin_world.simulation.Simulation;
import agh.oop.darwin_world.simulation.SimulationEngine;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;


public class SimulationWindowPresenter implements MapChangeListener {

    @FXML
    public ScrollPane mapScrollPane;
    @FXML
    public GridPane mapGrid;
    @FXML
    private Label numberOfAnimalsLabel;
    @FXML
    private Label numberOfPlantsLabel;
    @FXML
    private Label averageEnergyLabel;
    @FXML
    private Label dayLabel;
    @FXML
    private Button pauseStartButton;

    AbstractWorldMap worldMap;
    Simulation simulation;


    @FXML
    public Label genomeTrackingLabel;
    @FXML
    public Label activeGeneTrackingLabel;
    @FXML
    public Label energyTrackingLabel;
    @FXML
    public Label plantsEatenTrackingLabel;
    @FXML
    public Label kidsTrackingLabel;
    @FXML
    public Label descendantsTrackingLabel;
    @FXML
    public Label lifespanTrackingLabel;
    @FXML
    public GridPane animalTrackingPanel;

    private boolean animalIsTracked = false;
    private Animal currentAnimalTracked;


    @FXML
    public GridPane statsPanel;
    @FXML
    public Label numberOfEmptyFieldsLabel;
    @FXML
    public Label averageLifeSpanLabel;
    @FXML
    public Label averageNumberOfChildrenLabel;
    @FXML
    public Label mostPopularGenotypeLabel;


    private static final Color EMPTY_CELL_COLOR = javafx.scene.paint.Color.rgb(69, 38, 38);
    Stage newWindowStage;
    @Override
    public void mapChanged(WorldMap worldMap, String message) {
        Platform.runLater(() -> {
            drawMap();
            updateAnimalStats();
            updateSimulationStats();
        });
    }

    private void clearGrid() {
        mapGrid.getChildren().retainAll(mapGrid.getChildren().get(0));
        mapGrid.getColumnConstraints().clear();
        mapGrid.getRowConstraints().clear();
    }

    public void drawMap() {
        clearGrid();
        Boundary boundary = worldMap.getCurrentBounds();
        int gridSize = calculateGridSize(boundary);
        drawGrid(boundary, gridSize);
    }

    private int calculateGridSize(Boundary boundary) {
        int mapWidth = boundary.upperRight().getX() - boundary.lowerLeft().getX() + 1;
        int mapHeight = boundary.upperRight().getY() - boundary.lowerLeft().getY() + 1;

        int maxGridSize = Math.max(mapWidth, mapHeight);
        int cellSize = 800 / maxGridSize;

        return cellSize;
    }

    private void drawGrid(Boundary boundary, int cellSize) {
        for (int i = boundary.lowerLeft().getY(); i <= boundary.upperRight().getY(); i++) {
            for (int j = boundary.lowerLeft().getX(); j <= boundary.upperRight().getX(); j++) {
                Vector2d position = new Vector2d(j, i);
                drawCell(position, j - boundary.lowerLeft().getX() + 1, boundary.upperRight().getY() - i + 1, cellSize);
            }
        }
    }

    private void drawCell(Vector2d position, int column, int row, int cellSize) {
        Node node = createNodeForElement(position, cellSize);
        mapGrid.add(node, column, row);
    }


    private Node createNodeForElement(Vector2d position, int cellSize) {
        StackPane stackPane = createStackPane(cellSize);
        Rectangle cell = createCell(position, cellSize);
        stackPane.getChildren().add(cell);
        WorldElement element = worldMap.returnAnimalAt(position);
        if(element instanceof Animal){
            Circle circle = createAnimalCircle((Animal) element, cellSize);
            if(currentAnimalTracked!=null && currentAnimalTracked.getPosition().equals(position)){
                circle.setFill(Color.RED);
            }
            stackPane.getChildren().add(circle);
            Label label = new Label(((Animal) element).getAnimalOrientation().toString());
            double fontSize = cellSize * 0.5; // np. 30% rozmiaru cellSize
            label.setStyle("-fx-font-size: " + fontSize + "px;");
            stackPane.getChildren().add(label);
            stackPane.setOnMouseClicked(event -> handleAnimalClick((Animal) element));

        }
        return stackPane;
    }



    private StackPane createStackPane(int cellSize) {
        StackPane stackPane = new StackPane();
        stackPane.setMinSize(cellSize, cellSize);
        stackPane.setMaxSize(cellSize, cellSize);
        return stackPane;
    }

    private Rectangle createCell(Vector2d position, int cellSize) {
         Rectangle cell = new Rectangle(cellSize, cellSize);
         WorldElement environmentObject = worldMap.returnEnvironmentAt(position);
         if (environmentObject != null) {
            cell.setFill(environmentObject.getColor());
        }
        else{cell.setFill(Color.BLACK);}
        return cell;
    }

    private Circle createAnimalCircle(Animal animal, int cellSize) {
        Circle circle = new Circle((int) (cellSize / 4));
        circle.setFill(animal.getColor());
        return circle;
    }

    private void handleAnimalClick(Animal animal) {
        System.out.println(animal.getAnimalOrientation().toString());
        if(!animalIsTracked){
            this.currentAnimalTracked = animal;
            genomeTrackingLabel.setText(this.currentAnimalTracked.getGenes().toString());
            activeGeneTrackingLabel.setText(String.valueOf(this.currentAnimalTracked.getActiveGene()));
            energyTrackingLabel.setText(String.valueOf(this.currentAnimalTracked.getEnergy()));
            plantsEatenTrackingLabel.setText(String.valueOf(this.currentAnimalTracked.getPlantsEaten()));
            kidsTrackingLabel.setText(String.valueOf(this.currentAnimalTracked.getKids()));
            descendantsTrackingLabel.setText(String.valueOf(this.currentAnimalTracked.getDescendants()));
            lifespanTrackingLabel.setText(String.valueOf(this.currentAnimalTracked.getDeathDay()));
        }
        animalIsTracked = !animalIsTracked;


    }


    public void updateSimulationStats() {
        int numberOfAnimals = worldMap.getNumberOfAnimals();
        numberOfAnimalsLabel.setText(String.valueOf(numberOfAnimals));
        averageEnergyLabel.setText(String.format("%.2f",worldMap.averageAnimalEnergy()));
        numberOfPlantsLabel.setText(String.valueOf(worldMap.getNumberOfPlants()));
        dayLabel.setText(String.valueOf(simulation.getDay()));
        averageLifeSpanLabel.setText(String.format("%.2f",simulation.averageAgeForDeadAnimals()));
        averageNumberOfChildrenLabel.setText(String.format("%.2f",simulation.averageKidsNumberForAliveAnimals()));
        numberOfEmptyFieldsLabel.setText(String.valueOf(worldMap.getNumberOfEmptyFields()));

    }

    public void updateAnimalStats(){
        if(animalIsTracked){
            genomeTrackingLabel.setText(this.currentAnimalTracked.getGenes().toString());
            activeGeneTrackingLabel.setText(String.valueOf(this.currentAnimalTracked.getActiveGene()));
            energyTrackingLabel.setText(String.valueOf(this.currentAnimalTracked.getEnergy()));
            plantsEatenTrackingLabel.setText(String.valueOf(this.currentAnimalTracked.getPlantsEaten()));
            kidsTrackingLabel.setText(String.valueOf(this.currentAnimalTracked.getKids()));
            descendantsTrackingLabel.setText(String.valueOf(this.currentAnimalTracked.getDescendants()));
            lifespanTrackingLabel.setText(String.valueOf(this.currentAnimalTracked.getDeathDay()));
        }
    }







    @FXML
    void initialize()
    {
        System.out.println("Simulation Window presenter");
        pauseStartButton.setText("Pause");

    }

    public void onPauseStartButtonClicked() {
        try{
            if(simulation.isRunning()){
                simulation.pauseSimulation();
                pauseStartButton.setText("Start");
            }
            else{
                simulation.resumeSimulation();
                pauseStartButton.setText("Pause");
            }
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }



    public void runSimulation(UserConfigurationRecord config, SimulationEngine simulationEngine,Stage newWindowStage) {

        System.out.println("Simulation running...");
        Simulation simulation = new Simulation(config);
        this.simulation = simulation;
        simulation.getWorldMap().addObserver(this);
        simulationEngine.addToAsyncInThreadPool(simulation);

        this.newWindowStage=newWindowStage;
        this.worldMap=simulation.getWorldMap();
   }
}
