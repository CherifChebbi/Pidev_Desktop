package Controller;

import Entity.Plat;
import Entity.Restaurant;
import Services.ServiceRestaurant;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
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

    @FXML
    private TextField maximum;

    @FXML
    private TextField minimum;

    private ServiceRestaurant serviceRestaurant = new ServiceRestaurant();

    @FXML
    public void initialize() {
        try {
            displayRestaurants();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de l'affichage des restaurants", "Une erreur s'est produite lors de l'affichage des restaurants. Veuillez réessayer.");
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
            reserveButton.setStyle("-fx-background-color: #BEFAFF; -fx-border-color: red; -fx-background-radius: 20; -fx-border-radius: 20; -fx-position-shape: 10; -fx-text-fill: black;"); // Apply CSS styling
            reserveButton.setOnAction(event -> reserveForRestaurant(event, restaurant)); // Set action handler

            Button viewPlatsButton = new Button("View Plats");
            viewPlatsButton.setStyle("-fx-background-color: #BEFAFF; -fx-border-color: red; -fx-background-radius: 20; -fx-border-radius: 20; -fx-position-shape: 10; -fx-text-fill: black;"); // Apply CSS styling
            viewPlatsButton.setOnAction(event -> viewPlatsForRestaurant(event, restaurant)); // Set action handler/ Set action handler

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

        // Perform input validation for minimum and maximum prices



            List<Restaurant> filteredRestaurants = serviceRestaurant.afficher(nameFilter, locationFilter);
            populateGridPane(filteredRestaurants);

    }



    @FXML
    public void showAlert(Alert.AlertType alertType, String title, String headerText, String contentText) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
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


    @FXML
    private TextField userInputField;

    @FXML
    private TextArea chatArea;

    // Handle user input and generate responses
    @FXML
    private void handleUserInput(ActionEvent event) {
        String userInput = userInputField.getText().trim();
        if (!userInput.isEmpty()) {
            String response = generateResponse(userInput);
            chatArea.appendText("User: " + userInput + "\n");
            chatArea.appendText("Chatbot: " + response + "\n\n");
            userInputField.clear();
        }
    }

    // Generate responses based on user input
    private String generateResponse(String userInput) {
        if (userInput.contains("bonjour")) {
            return "Bonjour! Est ce que je peut vous aidez ";
        } else if (userInput.contains("comment reserver")) {
            return " Pour effectuer une réservation, veuillez fournir le nom du restaurant et la date/heure de la réservation";
        }
        if (userInput.contains("teranova")) {
            return "Notre application vous permet de rechercher des restaurants par nom, cuisine ou emplacement. Que recherchez-vous";
        } else if (userInput.contains("reservation existante")) {
            return "Pour consulter vos réservations existantes, veuillez vous connecter à votre compte et accéder à la section réservations.";
        } else {
            return "Je suis là pour vous aider . N'hésitez pas à me poser des questions !";
        }
    }

}
