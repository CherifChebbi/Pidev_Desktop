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
            // Charger la vue de la page d'accueil "HomeBack"
            FXMLLoader homeBackLoader = new FXMLLoader(getClass().getResource("/HomeBack.fxml"));
            Parent homeBackRoot = homeBackLoader.load();
            Scene homeBackScene = new Scene(homeBackRoot);
            primaryStage.setScene(homeBackScene);
            primaryStage.setTitle("HomeBack");

            // Afficher la fenêtre principale
            primaryStage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}

