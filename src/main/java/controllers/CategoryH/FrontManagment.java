package controllers.CategoryH;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
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
import java.net.URL;
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
            int rowCount = (categories.size() + 4) / 5; // Calculate the number of rows needed

            AnchorPane anchorPane = new AnchorPane();
            double layoutY = 10.0; // Initial layoutY value for the first row

            for (int i = 0; i < rowCount; i++) {
                // Iterate through each row
                HBox row = new HBox(10); // Create a horizontal box for each row
                row.setLayoutY(layoutY); // Set the layoutY for the row

                // Add four categories to the row or fewer if less than four categories remain
                for (int j = i * 5; j < Math.min((i + 1) * 5, categories.size()); j++) {
                    CategoryH categoryH = categories.get(j);

                    // Create an AnchorPane to hold the category elements
                    AnchorPane categoryPane = new AnchorPane();
                    categoryPane.setPrefWidth(250); // Set preferred width to hold all elements

                    String imageName = categoryH.getImage();
                    URL imageUrl = getClass().getResource("/Upload/" + imageName);
                    if (imageUrl != null) {
                        // Create an ImageView for the category image
                        ImageView imageView = new ImageView(new Image(imageUrl.toExternalForm()));
                        imageView.setFitWidth(200);
                        imageView.setFitHeight(200);
                        AnchorPane.setTopAnchor(imageView, 10.0);
                        AnchorPane.setLeftAnchor(imageView, 25.0);

                        // Create a Label for the category name
                        Label nameLabel = new Label(categoryH.getNom());
                        nameLabel.setLayoutY(220); // Position below the image
                        nameLabel.setLayoutX(25); // Align with the left side
                        nameLabel.setPrefWidth(200); // Set preferred width to prevent text overflow
                        nameLabel.setStyle("-fx-font-weight: bold");

                        // Create a Label for the category description
                        Label descLabel = new Label(categoryH.getDescription());
                        descLabel.setLayoutY(250); // Position below the name
                        descLabel.setLayoutX(25); // Align with the left side
                        descLabel.setPrefWidth(200); // Set preferred width to prevent text overflow
                        descLabel.setWrapText(true); // Allow text to wrap if it exceeds the width

                        // Create a Button for reservation
                        Button reservationButton = new Button("Reserve");
                        reservationButton.setLayoutY(290); // Position below the description
                        reservationButton.setLayoutX(25);// Align with the left side
                        AnchorPane.setBottomAnchor(descLabel, 10.0);

                        // Add padding between reservation button and description
                        AnchorPane.setBottomAnchor(descLabel, 10.0);

                        // Set the action for the reservation button
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



                        // Add all elements to the category pane
                        categoryPane.getChildren().addAll(imageView, nameLabel, descLabel, reservationButton);

                        // Add the category pane to the row
                        row.getChildren().add(categoryPane);
                    }
                }

                // Add the row to the anchor pane
                anchorPane.getChildren().add(row);

                // Increment layoutY for the next row
                layoutY += 350;
            }

            // Set the content of the scroll pane to the anchor pane
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
        Stage profileStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
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
        Stage profileStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
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
        Stage profileStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();

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