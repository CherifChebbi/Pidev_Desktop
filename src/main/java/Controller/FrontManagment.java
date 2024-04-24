package Controller;

import Entity.Category;
import Entity.Hebergement;
import Services.ServiceCategory;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.io.File;
import java.sql.SQLException;

public class FrontManagment {
    @FXML
    private ScrollPane front;

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

                anchorPane.getChildren().addAll(label, imageView, descLabel);
                layoutY += 150.0; // Increment layoutY for the next label, image, and description
            }
            front.setContent(anchorPane);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to add a new category label to the scroll pane



}
