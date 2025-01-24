package agh.oop.darwin_world.presenter;

import agh.oop.darwin_world.model.enums.AnimalMutationType;
import agh.oop.darwin_world.model.enums.WorldMapType;
import agh.oop.darwin_world.model.utils.Vector2d;
import agh.oop.darwin_world.model.worlds.Boundary;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class StartingWindowPresenter{
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


    public void initialize(){
        for(WorldMapType typeOfMap : WorldMapType.values()) {
            mapType.getItems().add(typeOfMap);
        }
        for(AnimalMutationType typeOfMutation : AnimalMutationType.values()) {
            mutationType.getItems().add(typeOfMutation);
        }
    }







    public void onSimulationStartClicked() {
            UserConfigurationRecord config = new UserConfigurationRecord(
                    new Boundary(new Vector2d(0,0),new Vector2d(widthSpinner.getValue(),heightSpinner.getValue())),
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
            //Konfiguracja noweg okna
            //Stage newStage = new Stage(); //tworzenie nowego okna
            //FXMLLoader loader = new FXMLLoader();
            //loader.setLocation(getClass().getClassLoader().getResource("simulationWindow.fxml"));
            //BorderPane viewRoot = loader.load();
            //SimulationWindowPresenter presenter = loader.getController(); //podpinanie presentera


    }
    public void onSaveClicked() {

    }
}
