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
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class FrontManagement {
    @FXML
    private VBox restaurantsContainer;

    @FXML
    private TextField searchNameField;

    @FXML
    private TextField searchLocationField;

    @FXML
    private ChoiceBox<String> questionChoiceBox;


    @FXML
    private TextArea chatArea;

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
        populateRestaurantsContainer(restaurantList);
    }

    private void populateRestaurantsContainer(List<Restaurant> restaurants) {
        restaurantsContainer.getChildren().clear(); // Clear existing content from restaurantsContainer

        for (int i = 0; i < restaurants.size(); i += 6) {
            HBox row = createRow(restaurants.subList(i, Math.min(i + 6, restaurants.size())));
            restaurantsContainer.getChildren().add(row);
        }
    }

    private HBox createRow(List<Restaurant> restaurants) {
        HBox row = new HBox(20); // Spacing between restaurants
        row.setStyle("-fx-background-color: transparent;"); // Set background to transparent

        for (Restaurant restaurant : restaurants) {
            // Create image view for the restaurant
            ImageView imageView = new ImageView(new Image("file:" + restaurant.getImage()));
            imageView.setFitWidth(200);
            imageView.setFitHeight(150);

            // Create labels for the name and description of the restaurant
            Label nameLabel = new Label(restaurant.getNom());
            nameLabel.setWrapText(true); // Allow wrapping if the text is too long
            nameLabel.setMaxWidth(200); // Limit the width to prevent overlapping
            Label descriptionLabel = new Label(restaurant.getDescription());
            descriptionLabel.setWrapText(true); // Allow wrapping if the text is too long
            descriptionLabel.setMaxWidth(200); // Limit the width to prevent overlapping

            // Create buttons for reserving and viewing plats
            Button reserveButton = new Button("Réserver");
            reserveButton.setOnAction(event -> reserveForRestaurant(event, restaurant)); // Set action handler
            Button viewPlatButton = new Button("Voir Plat");
            viewPlatButton.setOnAction(event -> viewPlatsForRestaurant(event, restaurant)); // Set action handler

            // Create like and dislike buttons
            Button likeButton = new Button("Like");
            likeButton.setOnAction(event -> likeRestaurant(event, restaurant)); // Set action handler
            Button dislikeButton = new Button("Dislike");
            dislikeButton.setOnAction(event -> dislikeRestaurant(event, restaurant)); // Set action handler

            // Create a VBox to contain the labels and buttons
            VBox restaurantInfo = new VBox(5); // Spacing between elements
            restaurantInfo.getChildren().addAll(nameLabel, descriptionLabel, reserveButton, viewPlatButton, likeButton, dislikeButton);

            // Create a VBox to contain the image view and restaurant info
            VBox restaurantBox = new VBox(5); // Spacing between elements
            restaurantBox.getChildren().addAll(imageView, restaurantInfo);

            // Add the restaurant box to the row
            row.getChildren().add(restaurantBox);
        }

        return row;
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
        populateRestaurantsContainer(filteredRestaurants);
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


    @FXML
    private void handleQuestionSelection(ActionEvent event) {
        String selectedQuestion = questionChoiceBox.getValue();
        if (selectedQuestion != null) {
            String response = generateResponse(selectedQuestion);
            chatArea.appendText("User: " + selectedQuestion + "\n");
            chatArea.appendText("Chatbot: " + response + "\n\n");
        }

    }

    // Generate responses based on user input
    private String generateResponse(String selectedQuestion) {
        if (selectedQuestion.contains("Bonjour")) {
            return "Bonjour! Est-ce que je peux vous aider?";
        } else if (selectedQuestion.contains("Comment puis-je effectuer une réservation")) {
            return "Pour effectuer une réservation, veuillez fournir le nom du restaurant et la date/heure de la réservation";
        } else {
            return "Je suis là pour vous aider. N'hésitez pas à me poser des questions!";
        }
    }


    @FXML
    private void likeRestaurant(ActionEvent event, Restaurant restaurant) {
        // Handle like action (e.g., update database, UI, etc.)
        System.out.println("Liked restaurant: " + restaurant.getNom());
    }

    @FXML
    private void dislikeRestaurant(ActionEvent event, Restaurant restaurant) {
        // Handle dislike action (e.g., update database, UI, etc.)
        System.out.println("Disliked restaurant: " + restaurant.getNom());
    }
}