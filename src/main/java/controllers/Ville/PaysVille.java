package controllers.Ville;

import controllers.Monument.VilleMonument;
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
import models.Ville;
import services.ServiceVille;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;

public class PaysVille {
    @FXML
    private GridPane cartesVilleGrid;
    private ServiceVille serviceVille;

    public PaysVille() {
        this.serviceVille = new ServiceVille();
    }

    @FXML
    private void initialize() {
        // Initialement vide car pas de traitement spécifique au démarrage
    }

    public void initData(int idPays) {
        try {
            List<Ville> VilleList = serviceVille.PaysVille(idPays);
            afficherCartesVille(VilleList);
        } catch (SQLException e) {
            e.printStackTrace();
            // Gérer les erreurs plus explicitement ici (par exemple, afficher un message à l'utilisateur)
        }
    }

    public void afficherCartesVille(List<Ville> VilleList) {
        int col = 0;
        int row = 0;

        for (Ville Ville : VilleList) {
            Node carteVille = createVilleCard(Ville);
            cartesVilleGrid.add(carteVille, col, row);
            col++;
            if (col == 3) { // 3 cartes par ligne, vous pouvez ajuster cela selon vos préférences
                col = 0;
                row++;
            }
        }
    }

    private Node createVilleCard(Ville Ville) {
        StackPane carteVille = new StackPane();
        carteVille.getStyleClass().add("Ville-card");

        String imageName = Ville.getImg_ville();
        URL imageUrl = getClass().getResource("/Upload/" + imageName);

        if (imageUrl != null) {
            ImageView imageView = new ImageView(new Image(imageUrl.toExternalForm()));
            imageView.setFitWidth(300);
            imageView.setFitHeight(200);
            carteVille.getChildren().add(imageView);
        } else {
            System.out.println("L'image n'a pas été trouvée : " + imageName);
        }

        // Création des labels pour les informations du Ville
        Label nomVilleLabel = new Label("Nom: " + Ville.getNom_ville());
        Label desVilleLabel = new Label("Description: " + Ville.getDesc_ville());
        //Label langueLabel = new Label("Langue: " + Ville.getLangue());
        //Label continentLabel = new Label("Continent: " + Ville.getContinent());
        //Label latitudeLabel = new Label("Latitude: " + Ville.getLatitude());
        //Label longitudeLabel = new Label("Longitude: " + Ville.getLongitude());
        //Label nbVilleLabel = new Label("Nombre de villes: " + Ville.getNb_villes());


        // Création d'un bouton pour accéder à la page des villes
        Button voirMonumentButton = new Button("Voir les monuments");
        voirMonumentButton.setStyle("-fx-background-color: rgb(160, 21, 62); -fx-text-fill: white; -fx-font-weight: bold;");

        voirMonumentButton.setOnAction(event -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Monument/VilleMonument.fxml"));
                Parent root = loader.load();
                VilleMonument controller = loader.getController();
                controller.initData(Ville.getId_ville()); // Passer l'ID du pays à la page des villes
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
        VBox infosVilleVBox = new VBox(5); // Espace vertical entre les informations
        infosVilleVBox.getChildren().addAll(
                nomVilleLabel, desVilleLabel,voirMonumentButton
                /* ,langueLabel,
                continentLabel, latitudeLabel, longitudeLabel,
                nbVilleLabel*/
        );

        // Ajout de marges entre les cartes
        VBox.setMargin(carteVille, new Insets(10, 10, 10, 10));
        VBox.setMargin(infosVilleVBox, new Insets(0, 10, 10, 10));

        // Création d'une VBox pour organiser l'image et les informations verticalement
        VBox carteContentVBox = new VBox(10); // Espace vertical entre l'image et les informations
        carteContentVBox.getChildren().addAll(
                carteVille, infosVilleVBox
        );

        return carteContentVBox;
    }

    @FXML
    void returnPays(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Pays/PaysFront.fxml"));
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
