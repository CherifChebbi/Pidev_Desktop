package test;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainFX extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {

<<<<<<< HEAD
        FXMLLoader loader = new FXMLLoader(MainFX.class.getResource("/Category.fxml"));
=======
        FXMLLoader loader = new FXMLLoader(MainFX.class.getResource("/Hebergement.fxml"));
>>>>>>> f8b6b9103f8fd6fe7dd117de01e732d0fbe523cc
        Parent root = loader.load();
         Scene scene = new Scene(root);
        primaryStage.setTitle("Hebergement");
        primaryStage.setScene(scene);
        primaryStage.show();

    }

}
