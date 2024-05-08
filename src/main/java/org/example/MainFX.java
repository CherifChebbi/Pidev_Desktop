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

            // Charger la vue de la page "ReservationFront"
            FXMLLoader reservationFrontLoader = new FXMLLoader(getClass().getResource("/Event/indexEvent.fxml"));
            Parent reservationFrontRoot = reservationFrontLoader.load();
            Scene reservationFrontScene = new Scene(reservationFrontRoot);

            // Créer une nouvelle fenêtre pour chaque scène
            Stage backHomeStage = new Stage();
            backHomeStage.setScene(backHomeScene);
            backHomeStage.setTitle("Back");

            Stage reservationFrontStage = new Stage();
            reservationFrontStage.setScene(reservationFrontScene);
            reservationFrontStage.setTitle("Front");

            // Afficher les fenêtres
            backHomeStage.show();
            reservationFrontStage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}


