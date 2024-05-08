package controllers.CategoryH;

import models.Category;
import services.ServiceCategory;
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


    private ServiceCategory sc = new ServiceCategory();

    public void initialize() {
        afficherCategories();
    }

    private void afficherCategories() {
        try {
            ObservableList<Category> categories = sc.afficher();
            AnchorPane anchorPane = new AnchorPane();
            double layoutY = 10.0; // Initial layoutY value for the first label
            for (Category category : categories) {
                Label label = new Label(category.getNom());
                label.setLayoutX(10.0);
                label.setLayoutY(layoutY);

                ImageView imageView = new ImageView();
                imageView.setFitWidth(100);
                imageView.setFitHeight(100);
                imageView.setLayoutX(150.0);
                imageView.setLayoutY(layoutY);

                try {
                    // Attempt to load the image
                    Image image = new Image(new File(category.getImage()).toURI().toString());
                    imageView.setImage(image);
                } catch (Exception e) {
                    System.err.println("Error loading image: " + e.getMessage());
                }

                Label descLabel = new Label(category.getDescription());
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

    // Method to add a new category label to the scroll pane



}