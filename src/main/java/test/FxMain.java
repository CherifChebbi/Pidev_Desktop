package test;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class FxMain extends Application {

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass()
                .getResource("/Pays/AfficherPays.fxml"));
        Parent parent = loader.load();
        AnchorPane root = new AnchorPane();

        Scene scene = new Scene(parent);

        stage.setTitle("Liste des pays ");
        stage.setScene(scene);

        stage.show();

    }
}
