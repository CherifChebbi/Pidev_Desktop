package controllers.CategoryH;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import models.CategoryH;
import services.ServiceCategoryH;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

public class FrontManagment {
    @FXML
    private ScrollPane front;

    @FXML
    private Pagination pagination;
    @FXML
    private Button excel;


    private ServiceCategoryH sc = new ServiceCategoryH();

    public void initialize() {
        afficherCategories();
    }

    private void afficherCategories() {
        try {
            ObservableList<CategoryH> categories = sc.afficher();
            AnchorPane anchorPane = new AnchorPane();
            double layoutY = 10.0; // Initial layoutY value for the first label
            for (CategoryH categoryH : categories) {
                Label label = new Label(categoryH.getNom());
                label.setLayoutX(10.0);
                label.setLayoutY(layoutY);

                ImageView imageView = new ImageView();
                imageView.setFitWidth(100);
                imageView.setFitHeight(100);
                imageView.setLayoutX(150.0);
                imageView.setLayoutY(layoutY);

                try {
                    // Attempt to load the image
                    Image image = new Image(new File(categoryH.getImage()).toURI().toString());
                    imageView.setImage(image);
                } catch (Exception e) {
                    System.err.println("Error loading image: " + e.getMessage());
                }

                Label descLabel = new Label(categoryH.getDescription());
                descLabel.setLayoutX(10.0);
                descLabel.setLayoutY(layoutY + 110); // Position the description label below the image

                Button reservationButton = new Button("Reserve");
                reservationButton.setLayoutX(10.0);
                reservationButton.setLayoutY(layoutY + 140); // Position the reservation button below the description label

                reservationButton.setOnAction(event -> {
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ReservationH/ReservationFront.fxml"));
                        Parent root = loader.load();
                        Stage stage = new Stage();
                        stage.setScene(new Scene(root));
                        stage.show();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                });

                anchorPane.getChildren().addAll(label, imageView, descLabel, reservationButton);
                layoutY += 200.0; // Increment layoutY for the next label, image, description, and button
            }
            front.setContent(anchorPane);
        } catch (SQLException e) {
            e.printStackTrace();
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

    @FXML
    private void Front_Event (ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Event/indexEvent.fxml"));
        Parent profileInterface = loader.load();
        Scene profileScene = new Scene(profileInterface);
        Stage profileStage = new Stage();
        profileStage.setScene(profileScene);

        // Close the current stage (assuming verifierButton is accessible from here)
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.close();

        // Show the login stage
        profileStage.show();
    }
    @FXML
    private void Front_Hebergement (ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/CategoryH/Front.fxml"));
        Parent profileInterface = loader.load();
        Scene profileScene = new Scene(profileInterface);
        Stage profileStage = new Stage();
        profileStage.setScene(profileScene);

        // Close the current stage (assuming verifierButton is accessible from here)
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.close();

        // Show the login stage
        profileStage.show();
    }
    @FXML
    public void Front_Restaurant(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Restaurant/index.fxml"));
        Parent profileInterface = loader.load();
        Scene profileScene = new Scene(profileInterface);
        Stage profileStage = new Stage();
        profileStage.setScene(profileScene);

        // Close the current stage (assuming verifierButton is accessible from here)
        Stage currentStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        currentStage.close();

        // Show the login stage
        profileStage.show();
    }
    @FXML
    private void editProfile (ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/User/EditProfile.fxml"));
        Parent profileInterface = loader.load();
        Scene profileScene = new Scene(profileInterface);
        Stage profileStage = new Stage();
        profileStage.setScene(profileScene);

        // Close the current stage (assuming verifierButton is accessible from here)
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.close();

        // Show the login stage
        profileStage.show();
    }



}