package Controller;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

public class DashboardController {

    @FXML
    private Button platButton;

    @FXML
    private Button restaurantButton;

    @FXML
    private AnchorPane rootPane;

    @FXML
    void openPlatSection(ActionEvent event) {
        // Load the Plat section view and set it on the root pane
        // Example:
        // FXMLLoader loader = new FXMLLoader(getClass().getResource("PlatSection.fxml"));
        // Parent platSection = loader.load();
        // rootPane.getChildren().setAll(platSection);
    }

    @FXML
    void openRestaurantSection(ActionEvent event) {
        // Load the Restaurant section view and set it on the root pane
        // Example:
        // FXMLLoader loader = new FXMLLoader(getClass().getResource("RestaurantSection.fxml"));
        // Parent restaurantSection = loader.load();
        // rootPane.getChildren().setAll(restaurantSection);
    }
}
