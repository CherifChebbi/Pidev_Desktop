package controllers.Ville;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.scene.control.Label;
import models.Ville;
import org.json.JSONObject;


public class weatherVille implements Initializable {

    @FXML
    private Label cityLabel;

    @FXML
    private Label temperatureLabel;

    @FXML
    private Label weatherLabel;

    @FXML
    private Label pressureLabel;

    @FXML
    private Label humidityLabel;
    @FXML
    private Label dateLabel;
    private double villeLatitude;
    private double villeLongitude;

    @FXML
    private ImageView imageView_ville;



    private String city;
    private String Img_ville;

    // Remplacez "YOUR_API_KEY" par votre clé API OpenWeather
    private final String API_KEY = "b3e5982431cbbbd73d4279bf18254df0";


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Vous pouvez initialiser des éléments ici si nécessaire
    }

    public void setCity(String city,String Img_ville) {
        this.city = city;
        this.Img_ville = Img_ville;
        updateWeather();
        ImgVille();

    }
    public void setVilleCoordinates(double latitude, double longitude) {
        this.villeLatitude = latitude;
        this.villeLongitude = longitude;
    }

    private void updateWeather() {
        try {
            //String apiUrl = "http://api.openweathermap.org/data/2.5/weather?lat=" + villeLatitude + "&lon=" + villeLongitude + "&appid=" + API_KEY + "&units=metric";
            // Appel à l'API OpenWeather
            String apiUrl = "http://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + API_KEY + "&units=metric";
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // Traitement de la réponse JSON
            JSONObject json = new JSONObject(response.toString());
            String cityName = json.getString("name");
            JSONObject main = json.getJSONObject("main");
            double temperature = main.getDouble("temp");
            String weatherDescription = json.getJSONArray("weather").getJSONObject(0).getString("description");
            double pressure = main.getDouble("pressure"); // Récupération de la pression atmosphérique
            int humidity = main.getInt("humidity"); // Récupération de l'humidité
            long timestamp = json.getLong("dt"); // Récupération du timestamp

            // Convertir le timestamp en date
            Date date = new Date(timestamp * 1000);
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            String formattedDate = dateFormat.format(date);

            // Mise à jour des labels avec les données météo
            cityLabel.setText(cityName);
            temperatureLabel.setText(temperature + "°C");
            weatherLabel.setText(weatherDescription);
            pressureLabel.setText("Pression: " + pressure + " hPa");
            humidityLabel.setText("Humidité: " + humidity + "%");
            dateLabel.setText("Date: " + formattedDate);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void ImgVille() {
            if (Img_ville != null && !Img_ville.isEmpty()) {
                try {
                    // Construire l'URL de l'image en utilisant le chemin relatif
                    URL imageUrl = getClass().getResource("/Upload/" + Img_ville);
                    if (imageUrl != null) {
                        // Charger l'image à partir de l'URL
                        javafx.scene.image.Image image = new Image(imageUrl.toExternalForm());
                        imageView_ville.setImage(image);
                    } else {
                        System.out.println("L'image n'a pas été trouvée : " + Img_ville);
                    }
                } catch (Exception e) {
                    System.out.println("Erreur lors du chargement de l'image : " + e.getMessage());
                }
            }
    }
    @FXML
    void Front_Pays(ActionEvent event) {
        try {
            // Charger le fichier FXML de la page d'ajout de pays
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Ville/VilleFront.fxml"));
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
