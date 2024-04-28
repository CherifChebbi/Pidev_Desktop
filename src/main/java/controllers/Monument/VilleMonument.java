package controllers.Monument;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import models.Monument;
import models.Ville;
import services.ServiceMonument;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;

public class VilleMonument {
    @FXML
    private GridPane cartesMonumentGrid;
    private ServiceMonument serviceMonument;

    public VilleMonument() {
        this.serviceMonument = new ServiceMonument();
    }

    @FXML
    private void initialize() {
        // Initialement vide car pas de traitement spécifique au démarrage
    }

    public void initData(int idPays) {
        try {
            List<Monument> MonumentList = serviceMonument.VilleMonument(idPays);
            afficherCartesMonument(MonumentList);
        } catch (SQLException e) {
            e.printStackTrace();
            // Gérer les erreurs plus explicitement ici (par exemple, afficher un message à l'utilisateur)
        }
    }

    public void afficherCartesMonument(List<Monument> MonumentList) {
        int col = 0;
        int row = 0;

        for (Monument Monument : MonumentList) {
            Node carteMonument = createMonumentCard(Monument);
            cartesMonumentGrid.add(carteMonument, col, row);
            col++;
            if (col == 3) { // 3 cartes par ligne, vous pouvez ajuster cela selon vos préférences
                col = 0;
                row++;
            }
        }
    }

    private Node createMonumentCard(Monument Monument) {
        StackPane carteMonument = new StackPane();
        carteMonument.getStyleClass().add("Monument-card");

        String imageName = Monument.getImg_monument();
        URL imageUrl = getClass().getResource("/Upload/" + imageName);

        if (imageUrl != null) {
            ImageView imageView = new ImageView(new Image(imageUrl.toExternalForm()));
            imageView.setFitWidth(300);
            imageView.setFitHeight(200);
            carteMonument.getChildren().add(imageView);
        } else {
            System.out.println("L'image n'a pas été trouvée : " + imageName);
        }

        // Création des labels pour les informations du Monument
        Label nomMonumentLabel = new Label("Nom: " + Monument.getNom_monument());
        Label desMonumentLabel = new Label("Description: " + Monument.getDesc_monument());
        //Label langueLabel = new Label("Langue: " + Monument.getLangue());
        //Label continentLabel = new Label("Continent: " + Monument.getContinent());
        //Label latitudeLabel = new Label("Latitude: " + Monument.getLatitude());
        //Label longitudeLabel = new Label("Longitude: " + Monument.getLongitude());
        //Label nbVilleLabel = new Label("Nombre de villes: " + Monument.getNb_villes());

        // Création d'une VBox pour organiser les informations verticalement
        VBox infosMonumentVBox = new VBox(5); // Espace vertical entre les informations
        infosMonumentVBox.getChildren().addAll(
                nomMonumentLabel, desMonumentLabel
                /* ,langueLabel,
                continentLabel, latitudeLabel, longitudeLabel,
                nbVilleLabel*/
        );
        // Ajout de marges entre les cartes
        VBox.setMargin(carteMonument, new Insets(10, 10, 10, 10));
        VBox.setMargin(infosMonumentVBox, new Insets(0, 10, 10, 10));

        // Création d'une VBox pour organiser l'image et les informations verticalement
        VBox carteContentVBox = new VBox(10); // Espace vertical entre l'image et les informations
        carteContentVBox.getChildren().addAll(
                carteMonument, infosMonumentVBox
        );

        return carteContentVBox;
    }
    @FXML
    void returnVilles(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Ville/VilleFront.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading FXML file: " + e.getMessage());
        }
    }
}