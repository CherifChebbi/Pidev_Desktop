// RestaurantDetailsController.java


package Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import Entity.Plat;
import java.util.List;

public class RestaurantDetailsController {

    @FXML
    private ImageView restaurantImageView;

    @FXML
    private Label restaurantNameLabel;

    @FXML
    private Label restaurantDescriptionLabel;

    @FXML
    private GridPane platsGridPane;

    public void initData(String restaurantName, String restaurantDescription, List<Plat> plats, String imagePath) {
        // Set restaurant name and description
        restaurantNameLabel.setText(restaurantName);
        restaurantDescriptionLabel.setText(restaurantDescription);

        // Set restaurant image
        restaurantImageView.setImage(new Image("file:" + imagePath));

        // Check if plats is not null before displaying
        if (plats != null) {
            // Display plats
            displayPlats(plats);
        } else {
            System.out.println("Plats list is null.");
        }
    }

    private void displayPlats(List<Plat> plats) {
        platsGridPane.getChildren().clear(); // Clear existing content from platsGridPane

        int column = 0;
        int row = 0;
        for (Plat plat : plats) {
            // Create an ImageView for the plat image
            ImageView imageView = new ImageView(new Image("file:" + plat.getImage()));
            imageView.setFitWidth(200);
            imageView.setFitHeight(150);

            // Create a Label for the plat name
            Label nameLabel = new Label(plat.getNom()); // Display plat name

            // Create a Label for the plat price
            Label priceLabel = new Label("Prix: " + plat.getPrix()); // Display plat price

            // Add the image, name, and price to the GridPane
            platsGridPane.add(imageView, column, row);
            platsGridPane.add(nameLabel, column, row + 1); // Add the name below the image
            platsGridPane.add(priceLabel, column, row + 2); // Add the price below the name

            // Increment row and reset column if necessary
            column++;
            if (column == 3) {
                column = 0;
                row += 3; // Increment by 3 to leave space for image, name, and price
            }
        }
    }







    @FXML
    private void goBack() {
        // Handle going back to the previous view
        // You can close the current stage or navigate to another view
    }
}
