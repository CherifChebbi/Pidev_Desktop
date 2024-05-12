package org.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainFX extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        try {
            // Charger la vue de la page "BackHome"
            FXMLLoader backHomeLoader = new FXMLLoader(getClass().getResource("/Event/HomeBack.fxml"));
            Parent backHomeRoot = backHomeLoader.load();
            Scene backHomeScene = new Scene(backHomeRoot);

            // Créer une nouvelle fenêtre pour la scène "BackHome"
            Stage backHomeStage = new Stage();
            backHomeStage.setScene(backHomeScene);
            backHomeStage.setTitle("Back");

            // Afficher la fenêtre
            backHomeStage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
