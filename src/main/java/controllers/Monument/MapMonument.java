package controllers.Monument;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class MapMonument {
    @FXML
    private WebView webView;
    private double monumentLatitude;
    private double monumentLongitude;


    public void initialize() {

        WebEngine webEngine = webView.getEngine();
        URL url = getClass().getResource("/Monument/map.html");
        webEngine.load(url.toExternalForm());
    }
    public void setMonumentCoordinates(double latitude, double longitude) {
        this.monumentLatitude = latitude;
        this.monumentLongitude = longitude;
    }

    @FXML
    void loadMonumentMap(ActionEvent event) {
        executeJavaScript("map.setCenter(new google.maps.LatLng(" + monumentLatitude + ", " + monumentLongitude + "));");
    }

    @FXML
    private void executeJavaScript(String script) {
        webView.getEngine().executeScript(script);
    }
    @FXML
    void Front_Pays(ActionEvent event) {
        try {
            // Charger le fichier FXML de la page d'ajout de pays
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Pays/PaysFront.fxml"));
            Parent root = loader.load();

            // Créer une nouvelle scène avec le contenu de la page d'ajout de pays
            Scene scene = new Scene(root);

            // Obtenir la fenêtre principale à partir de l'événement du bouton ou d'une autre manière
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Définir la nouvelle scène sur la fenêtre principale
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
