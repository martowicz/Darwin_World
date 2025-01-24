package agh.oop.darwin_world.simulation;

import agh.oop.darwin_world.presenter.StartingWindowPresenter;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class SimulationApp extends Application {

    @Override
    public void start(Stage configStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getClassLoader().getResource("startingWindow.fxml"));

        //odpala okno
        BorderPane viewRoot = fxmlLoader.load();
        configureStage(configStage, viewRoot);

        configStage.show();
    }

    private void configureStage(Stage configStage, BorderPane viewRoot) {
        var scene = new Scene(viewRoot);
        configStage.setScene(scene);
        configStage.setTitle("Starting Window");
        configStage.minWidthProperty().bind(viewRoot.minWidthProperty());
        configStage.minHeightProperty().bind(viewRoot.minHeightProperty());
    }
}
