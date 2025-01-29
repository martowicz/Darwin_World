package agh.oop.darwin_world.presenter;

import agh.oop.darwin_world.model.enums.AnimalMutationType;
import agh.oop.darwin_world.model.enums.WorldMapType;
import agh.oop.darwin_world.model.utils.InvalidParamenterException;
import agh.oop.darwin_world.model.utils.Vector2d;
import agh.oop.darwin_world.model.worlds.Boundary;
import agh.oop.darwin_world.simulation.SimulationEngine;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Scanner;

import static java.lang.Math.max;
import static java.lang.Math.min;
import static java.nio.file.StandardOpenOption.APPEND;
import static java.nio.file.StandardOpenOption.CREATE;

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
    public CheckBox saveLogCheck;

    private static final URL path =  StartingWindowPresenter.class.getClassLoader().getResource("configurations.csv");

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
    SimulationEngine simulationEngine = new SimulationEngine();

    @FXML
    void initialize() {

        loadFromFile();

        for (WorldMapType typeOfMap : WorldMapType.values()) {
            mapType.getItems().add(typeOfMap);
        }
        for (AnimalMutationType typeOfMutation : AnimalMutationType.values()) {
            mutationType.getItems().add(typeOfMutation);
        }
        //Set default values for spinners
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
        mapType.setValue(WorldMapType.ROUND_WORLD);
        mutationType.setValue(AnimalMutationType.RANDOM_MUTATION);

        if (csvCombo.getItems() != null) {
            csvCombo.setValue(csvCombo.getItems().getFirst());
        }
        onCsvComboClicked();

    }

    private void setSpinnerDefaults(Spinner<Integer> spinner, int min, int max, int initialValue) {
        spinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(min, max, initialValue));
    }

    @FXML
    private void  onCsvComboClicked()
    {
        String configName = csvCombo.getValue();
        String[] params;

        try {
            assert path != null;
            try (Scanner scanner = new Scanner(Path.of(path.toURI()))) {

                    String line = scanner.nextLine();
                    params = line.split(";");

                while (scanner.hasNextLine() && !params[0].equals(configName)) {
                     line = scanner.nextLine();
                     params = line.split(";");
                }



                //ear
                //mut


                mapType.setValue(WorldMapType.stringToEnum(params[1]));
                mutationType.setValue(AnimalMutationType.stringToEnum(params[2]));
                setSpinnerDefaults(widthSpinner, 1, 100,Integer.parseInt(params[3]));
                setSpinnerDefaults(heightSpinner, 1, 100, Integer.parseInt(params[4]));
                setSpinnerDefaults(animalCountSpinner, 1, 100, Integer.parseInt(params[5]));
                setSpinnerDefaults(startingEnergySpinner, 1, 100, Integer.parseInt(params[6]));
                setSpinnerDefaults(reproductionEnergySpinner, 1, 100, Integer.parseInt(params[7]));
                setSpinnerDefaults(childEnergySpinner, 1, 100, Integer.parseInt(params[8]));
                setSpinnerDefaults(energyFromPlantSpinner, 1, 100, Integer.parseInt(params[9]));
                setSpinnerDefaults(genomeLengthSpinner, 1, 100, Integer.parseInt(params[10]));
                setSpinnerDefaults(minMutationsSpinner, 0, 50, Integer.parseInt(params[11]));
                setSpinnerDefaults(maxMutationsSpinner, 0, 50, Integer.parseInt(params[12]));
                setSpinnerDefaults(plantsAtStartSpinner, 1, 100, Integer.parseInt(params[13]));
                setSpinnerDefaults(plantsPerDaySpinner, 1, 50, Integer.parseInt(params[14]));





                System.out.println(Arrays.toString(params));
            }
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @FXML
    private void onSimulationStartClicked() throws IOException {
        UserConfigurationRecord config = new UserConfigurationRecord(
                new Boundary(new Vector2d(0, 0), new Vector2d(widthSpinner.getValue(), heightSpinner.getValue())),
                mapType.getValue(),
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
                mutationType.getValue()
        );


        try{

            ConfigValidator.validate(config);
            errorLabel.setText("");
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getClassLoader().getResource("simulationWindow.fxml"));

            BorderPane viewRoot = fxmlLoader.load();
            Stage newWindowStage = new Stage();

            SimulationWindowPresenter presenter = fxmlLoader.getController();
            configureStage(newWindowStage, viewRoot);

            newWindowStage.show();
            presenter.runSimulation(config,simulationEngine, newWindowStage, saveLogCheck.isSelected());

        }
        catch (InvalidParamenterException e) {
            errorLabel.setText(e.getMessage());
        }

        System.out.println("Simulation started!");


    }








    private void configureStage(Stage primaryStage, BorderPane viewRoot) {
        var scene = new Scene(viewRoot);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Simulation app");
        primaryStage.minWidthProperty().bind(viewRoot.minWidthProperty());
        primaryStage.minHeightProperty().bind(viewRoot.minHeightProperty());
    }


    @FXML
    private void onSaveClicked() {
        try {

            final Stage dialog = new Stage();
            dialog.setTitle("Save user configuration");

            VBox dialogVbox = new VBox(10);
            dialogVbox.getChildren().add(new Label("Type your config name:"));

            TextField textField = new TextField();
            textField.setPrefWidth(200);
            textField.setMaxWidth(200);
            dialogVbox.getChildren().add(textField);

            Label badSign = new Label("Your name cannot contain \";\"");
            Label nameExists = new Label("This name already exists");

            badSign.setVisible(false);
            badSign.setManaged(false);
            nameExists.setVisible(false);
            nameExists.setManaged(false);

            dialogVbox.getChildren().add(badSign);
            dialogVbox.getChildren().add(nameExists);

            Button cancel = new Button("Cancel");
            Button save = new Button("Save");

            cancel.setOnAction(event -> dialog.hide());

            save.setOnAction(event -> {
                String name = textField.getText();
                if (name.contains(";")) {
                    badSign.setVisible(true);
                    badSign.setManaged(true);
                    return;
                }
                for(String configName: csvCombo.getItems())
                {   if(configName.contains(name)){
                    nameExists.setVisible(true);
                    nameExists.setManaged(true);
                    return;}
                }
                writeCsvToFile(name);
                dialog.close();
            });

            textField.textProperty().addListener((observable, oldValue, newValue) -> {
                badSign.setVisible(false);
                badSign.setManaged(false);
                nameExists.setVisible(false);
                nameExists.setManaged(false);
            });


            HBox buttonsHBox = new HBox(10);
            buttonsHBox.getChildren().add(cancel);
            buttonsHBox.getChildren().add(save);
            buttonsHBox.setAlignment(Pos.CENTER);

            dialogVbox.getChildren().add(buttonsHBox);

            dialogVbox.setAlignment(Pos.CENTER);

            Scene dialogScene = new Scene(dialogVbox, 300, 200);
            dialog.setScene(dialogScene);
            dialog.show();


        } catch (Exception e) {
            errorLabel.setText("Error saving configuration!");
        }


    }

    private void loadFromFile()
    {
        try {
            assert path != null;
            try (Scanner scanner = new Scanner(Path.of(path.toURI()))) {
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    String[] params = line.split(";");
                    csvCombo.getItems().add(params[0]);
                }
            }
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e.getMessage());
        }

    }


    private void writeCsvToFile(String name) {
        WorldMapType selectedMapType = mapType.getValue();
        AnimalMutationType selectedMutationType = mutationType.getValue();
        //fgh

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

        csvCombo.getItems().add(name);
        csvCombo.getSelectionModel().select(name);

        String csvData = name + ";" +
                selectedMapType + ";" +
                selectedMutationType+ ";" +
                mapWidth + ";" +
                mapHeight + ";" +
                animalCount + ";" +
                startingEnergy + ";" +
                reproductionEnergy + ";" +
                childEnergy  + ";" +
                energyFromPlant + ";" +
                genomeLength + ";" +
                minMutations + ";" +
                maxMutations + ";" +
                plantsAtStart + ";" +
                plantsPerDay;

        try {
            assert path != null;
            Files.writeString(Path.of(path.toURI()), csvData + System.lineSeparator(), CREATE, APPEND);
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

}