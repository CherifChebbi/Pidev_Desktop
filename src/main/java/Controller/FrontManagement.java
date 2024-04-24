package Controller;

import Entity.Plat;
import Entity.Restaurant;
import Services.ServiceRestaurant;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class FrontManagement {
    @FXML
    private GridPane gridPane;

    @FXML
    private TextField searchNameField;

    @FXML
    private TextField searchLocationField;

    private ServiceRestaurant serviceRestaurant = new ServiceRestaurant();

    @FXML
    public void initialize() {
        try {
            displayRestaurants();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void displayRestaurants() throws SQLException {
        List<Restaurant> restaurantList = serviceRestaurant.afficher();

        populateGridPane(restaurantList);
    }

    @FXML
    private void populateGridPane(List<Restaurant> restaurants) {
        gridPane.getChildren().clear(); // Clear existing content from gridPane

        int column = 0;
        int row = 0;
        for (Restaurant restaurant : restaurants) {
            ImageView imageView = new ImageView(new Image("file:" + restaurant.getImage()));
            imageView.setFitWidth(200);
            imageView.setFitHeight(150);

            Label nameLabel = new Label(restaurant.getNom());
            Label descriptionLabel = new Label(restaurant.getDescription());

            Button reserveButton = new Button("Réserver");
            reserveButton.setOnAction(event -> reserveForRestaurant(event, restaurant)); // Set action handler

            Button viewPlatsButton = new Button("View Plats");
            viewPlatsButton.setOnAction(event -> viewPlatsForRestaurant(event, restaurant)); // Set action handler

            gridPane.add(imageView, column, row);
            gridPane.add(nameLabel, column, row + 1); // Add the name below the image
            gridPane.add(descriptionLabel, column, row + 2); // Add the description below the name
            gridPane.add(reserveButton, column, row + 3); // Add the reserve button below the description
            gridPane.add(viewPlatsButton, column, row + 4); // Add the view plats button below the reserve button

            // Increment row and reset column if necessary
            column++;
            if (column == 3) {
                column = 0;
                row += 5; // Increment by 5 to leave space for image, name, description, reserve button, and view plats button
            }
        }
    }

    @FXML
    public void viewPlatsForRestaurant(ActionEvent event, Restaurant restaurant) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/RestaurantDetails.fxml"));
            Parent root = loader.load();

            // Pass the selected restaurant's details to the RestaurantDetailsController
            RestaurantDetailsController controller = loader.getController();

            // Get the list of plats for the selected restaurant
            List<Plat> plats = serviceRestaurant.getPlatsForRestaurant(restaurant.getIdR());

            // Initialize data with restaurant details and plats
            controller.initData(restaurant.getNom(), restaurant.getDescription(), plats, restaurant.getImage());

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }















    @FXML
    public void search(ActionEvent actionEvent) {
        String nameFilter = searchNameField.getText();
        String locationFilter = searchLocationField.getText();

        List<Restaurant> filteredRestaurants = serviceRestaurant.afficher(nameFilter, locationFilter);
        populateGridPane(filteredRestaurants);
    }

    @FXML
    public void reserveForRestaurant(ActionEvent event, Restaurant restaurant) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/reservation.fxml"));
            Parent root = loader.load();

            // Pass the selected restaurant's ID and name to the ReservationManagement controller
            ReservationManagement controller = loader.getController();
            controller.initData(restaurant.getIdR(), restaurant.getNom()); // Pass the restaurant ID and name

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
