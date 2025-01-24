package agh.oop.darwin_world.presenter;

import agh.oop.darwin_world.model.enums.AnimalMutationType;
import agh.oop.darwin_world.model.enums.WorldMapType;
import agh.oop.darwin_world.model.utils.Vector2d;
import agh.oop.darwin_world.model.worlds.Boundary;
import agh.oop.darwin_world.simulation.Simulation;
import agh.oop.darwin_world.simulation.SimulationApp;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class StartingWindowPresenter {

    @FXML

    public ComboBox<String> csvCombo;
    @FXML
    public ComboBox<WorldMapType> mapType;
    @FXML
    public ComboBox<AnimalMutationType> mutationType;
    @FXML
    public Spinner<Integer> widthSpinner;
    @FXML
    public Spinner<Integer> heightSpinner;
    @FXML
    public Spinner<Integer> animalCountSpinner;
    @FXML
    public Spinner<Integer> startingEnergySpinner;
    @FXML
    public Spinner<Integer> reproductionEnergySpinner;
    @FXML
    public Spinner<Integer> childEnergySpinner;
    @FXML
    public Spinner<Integer> energyFromPlantSpinner;
    @FXML
    public Spinner<Integer> genomeLengthSpinner;
    @FXML
    public Spinner<Integer> minMutationsSpinner;
    @FXML
    public Spinner<Integer> maxMutationsSpinner;
    @FXML
    public Spinner<Integer> plantsAtStartSpinner;
    @FXML
    public Spinner<Integer> plantsPerDaySpinner;
    @FXML
    public Label errorLabel;

    @FXML
    private Label errorLabel;

    int mapWidth;
    int mapHeight;
    int animalCount;
    boolean saveLog;

    int startingEnergy;
    int reproductionEnergy;
    int childEnergy;
    int energyFromPlant;
    int genomeLength;
    int minMutations;
    int maxMutations;
    int plantsAtStart;
    int plantsPerDay;


    @FXML
    void initialize()
    {
        csvComboBox.getItems().addAll("Config1", "Config2", "Config3");

        for(WorldMapType typeOfMap : WorldMapType.values()) {
            mapTypeBox.getItems().add(typeOfMap);
            }
        for(AnimalMutationType typeOfMutation : AnimalMutationType.values()) {
            mutationTypeBox.getItems().add(typeOfMutation);
            }
        // Set default values for spinners
        setSpinnerDefaults(widthSpinner, 1, 100, 10);
        setSpinnerDefaults(heightSpinner, 1, 100, 10);
        setSpinnerDefaults(animalCountSpinner, 1, 100, 10);
        setSpinnerDefaults(startingEnergySpinner, 1, 100, 10);
        setSpinnerDefaults(reproductionEnergySpinner, 1, 100, 10);
        setSpinnerDefaults(childEnergySpinner, 1, 100, 10);
        setSpinnerDefaults(energyFromPlantSpinner, 1, 100, 10);
        setSpinnerDefaults(genomeLengthSpinner, 1, 100, 10);
        setSpinnerDefaults(minMutationsSpinner, 0, 50, 5);
        setSpinnerDefaults(maxMutationsSpinner, 0, 50, 10);
        setSpinnerDefaults(plantsAtStartSpinner, 1, 100, 10);
        setSpinnerDefaults(plantsPerDaySpinner, 1, 50, 5);
    }

    private void setSpinnerDefaults(Spinner<Integer> spinner, int min, int max, int initialValue) {
        spinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(min, max, initialValue));
    }


    @FXML
    private void onSaveClicked() {
        try {
            String selectedConfig = csvComboBox.getValue();
            WorldMapType selectedMapType = mapTypeBox.getValue();
            AnimalMutationType selectedMutationType = mutationTypeBox.getValue();

            mapWidth = widthSpinner.getValue();
            mapHeight = heightSpinner.getValue();
            animalCount = animalCountSpinner.getValue();
            saveLog = saveLogCheck.isSelected();

            // Collect other parameters
            startingEnergy = startingEnergySpinner.getValue();
            reproductionEnergy = reproductionEnergySpinner.getValue();
            childEnergy = childEnergySpinner.getValue();
            energyFromPlant = energyFromPlantSpinner.getValue();
            genomeLength = genomeLengthSpinner.getValue();
            minMutations = minMutationsSpinner.getValue();
            maxMutations = maxMutationsSpinner.getValue();
            plantsAtStart = plantsAtStartSpinner.getValue();
            plantsPerDay = plantsPerDaySpinner.getValue();

            // Save configuration logic
            System.out.println("Saving configuration:");
            System.out.printf("Config: %s, Map Type: %s, Mutation Type: %s%n", selectedConfig, selectedMapType, selectedMutationType);
            System.out.printf("Width: %d, Height: %d, Animals: %d%n", mapWidth, mapHeight, animalCount);
            System.out.printf("Starting Energy: %d, Reproduction Energy: %d, Child Energy: %d%n", startingEnergy, reproductionEnergy, childEnergy);
            System.out.printf("Energy from Plant: %d, Genome Length: %d, Mutations: %d-%d%n", energyFromPlant, genomeLength, minMutations, maxMutations);
            System.out.printf("Plants at Start: %d, Plants per Day: %d%n", plantsAtStart, plantsPerDay);
            System.out.println("Save Log: " + saveLog);

            errorLabel.setText("");
        } catch (Exception e) {
            errorLabel.setText("Error saving configuration!");
        }
    }

    @FXML
    private void onSimulationStartClicked() throws IOException {
        System.out.println("Simulation started!");

        UserConfigurationRecord config = new UserConfigurationRecord(
                new Boundary(new Vector2d(0,0),new Vector2d(widthSpinner.getValue(),heightSpinner.getValue())),
                mapTypeBox.getValue(),
                plantsAtStartSpinner.getValue(),
                energyFromPlantSpinner.getValue(),
                plantsPerDaySpinner.getValue(),
                animalCountSpinner.getValue(),
                startingEnergySpinner.getValue(),
                reproductionEnergySpinner.getValue(),
                childEnergySpinner.getValue(),
                minMutationsSpinner.getValue(),
                maxMutationsSpinner.getValue(),
                genomeLengthSpinner.getValue(),
                mutationTypeBox.getValue()
        );

        //errorLabel.setText("");


        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getClassLoader().getResource("simulationWindow.fxml"));

        BorderPane viewRoot = fxmlLoader.load();
        Stage newWindowStage = new Stage();

        SimulationWindowPresenter presenter = fxmlLoader.getController();
        configureStage(newWindowStage, viewRoot);

        newWindowStage.show();
        presenter.runSimulation(config);


    }
    private void configureStage(Stage primaryStage, BorderPane viewRoot) {
        var scene = new Scene(viewRoot);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Simulation app");
        primaryStage.minWidthProperty().bind(viewRoot.minWidthProperty());
        primaryStage.minHeightProperty().bind(viewRoot.minHeightProperty());
    }
}