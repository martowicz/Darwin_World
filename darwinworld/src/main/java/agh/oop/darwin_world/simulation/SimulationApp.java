package agh.oop.darwin_world.simulation;

import agh.oop.darwin_world.presenter.SimulationWindowPresenter;
import agh.oop.darwin_world.presenter.StartingWindowPresenter;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class SimulationApp extends Application {

    @Override
    public void start(Stage configStage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getClassLoader().getResource("startingWindow.fxml"));

        //odpala okno
        BorderPane viewRoot = fxmlLoader.load();
        configureStage(configStage, viewRoot);

        configStage.setOnCloseRequest(event -> {
            System.out.println("Zamykanie aplikacji...");
            System.exit(0); // Zamyka całą aplikację
        });
        StartingWindowPresenter presenter = fxmlLoader.getController();
        presenter.setThisStage(configStage);

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
