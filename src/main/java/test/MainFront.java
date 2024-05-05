package test;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainFront extends Application {
    @Override
    public void start(Stage stage) throws Exception {
<<<<<<< HEAD
        FXMLLoader loader = new FXMLLoader(MainFX.class.getResource("/Back.fxml"));
=======
        FXMLLoader loader = new FXMLLoader(MainFX.class.getResource("/RestaurantCalendar.fxml"));
>>>>>>> 6340eeb7b3df9b49a518d53dd8bc41491e14862f
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setTitle("Teranova");
        stage.setScene(scene);
        stage.show();
    }



}
