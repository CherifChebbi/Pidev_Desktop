package test;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws Exception {

        FXMLLoader loader = new FXMLLoader(MainFX.class.getResource("/Front.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setTitle("Hebergement");
        stage.setScene(scene);
        stage.show();
    }
}
