package controllers.Pays;

import controllers.Ville.PaysVille;
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
import models.Pays;
import services.ServicePays;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;

public class PaysFront {
    @FXML
    private GridPane cartesPaysGrid;
    private ServicePays servicePays;

    public PaysFront() {
        this.servicePays = new ServicePays();
    }

    @FXML
    private void initialize() {
        loadPaysData();
    }

    private void loadPaysData() {
        try {
            List<Pays> paysList = servicePays.Afficher();
            afficherCartesPays(paysList);
        } catch (SQLException e) {
            e.printStackTrace();
            // Gérer les erreurs
        }
    }

    public void afficherCartesPays(List<Pays> paysList) {
        int col = 0;
        int row = 0;

        for (Pays pays : paysList) {
            Node cartePays = createPaysCard(pays);
            cartesPaysGrid.add(cartePays, col, row);
            col++;
            if (col == 3) { // 3 cartes par ligne, vous pouvez ajuster cela selon vos préférences
                col = 0;
                row++;
            }
        }
    }

    private Node createPaysCard(Pays pays) {
        StackPane cartePays = new StackPane();
        cartePays.getStyleClass().add("pays-card");

        String imageName = pays.getImg_pays();
        URL imageUrl = getClass().getResource("/Upload/" + imageName);

        if (imageUrl != null) {
            ImageView imageView = new ImageView(new Image(imageUrl.toExternalForm()));
            imageView.setFitWidth(300);
            imageView.setFitHeight(200);
            cartePays.getChildren().add(imageView);
        } else {
            System.out.println("L'image n'a pas été trouvée : " + imageName);
        }

        // Création des labels pour les informations du pays
        Label nomPaysLabel = new Label("Nom: " + pays.getNom_pays());
        Label desPaysLabel = new Label("Description: " + pays.getDesc_pays());
        //Label langueLabel = new Label("Langue: " + pays.getLangue());
        //Label continentLabel = new Label("Continent: " + pays.getContinent());
        //Label latitudeLabel = new Label("Latitude: " + pays.getLatitude());
        //Label longitudeLabel = new Label("Longitude: " + pays.getLongitude());
        //Label nbVilleLabel = new Label("Nombre de villes: " + pays.getNb_villes());


        // Création d'un bouton pour accéder à la page des villes
        Button voirVillesButton = new Button("Voir les villes");
        voirVillesButton.setStyle("-fx-background-color: rgb(160, 21, 62); -fx-text-fill: white;-fx-font-weight: bold;");
        voirVillesButton.setOnAction(event -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Ville/PaysVille.fxml"));
                Parent root = loader.load();
                PaysVille controller = loader.getController();
                controller.initData(pays.getId_pays()); // Passer l'ID du pays à la page des villes
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
                System.err.println("Error loading FXML file: " + e.getMessage());
            }
        });
        // Création d'une VBox pour organiser les informations verticalement
        VBox infosPaysVBox = new VBox(5); // Espace vertical entre les informations
        infosPaysVBox.getChildren().addAll(
                nomPaysLabel, desPaysLabel,voirVillesButton
                /* ,langueLabel,
                continentLabel, latitudeLabel, longitudeLabel,
                nbVilleLabel*/
        );

        // Ajout de marges entre les cartes
        VBox.setMargin(cartePays, new Insets(10, 10, 10, 10));
        VBox.setMargin(infosPaysVBox, new Insets(0, 10, 10, 10));

        // Création d'une VBox pour organiser l'image et les informations verticalement
        VBox carteContentVBox = new VBox(10); // Espace vertical entre l'image et les informations
        carteContentVBox.getChildren().addAll(
                cartePays, infosPaysVBox
        );

        return carteContentVBox;
    }
    @FXML
    void listVille(ActionEvent event) {
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
