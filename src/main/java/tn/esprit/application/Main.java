package tn.esprit.application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import tn.esprit.application.controllers.Login;

public class Main extends Application {

    public void start(Stage primaryStage) throws Exception {
        // Load the FXML file
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/login.fxml"));
        Parent root = loader.load();

        // Get the controller instance and set it to the FXMLLoader

        primaryStage.setTitle("Signup");
        primaryStage.setScene(new Scene(root, 800, 400));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
