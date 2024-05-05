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

import javax.print.attribute.standard.Media;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import javafx.scene.Group;

import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

public class FrontManagement {
    @FXML
    private VBox restaurantsContainer;

    @FXML
    private Button videoButton;

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
            reserveButton.setStyle("-fx-background-color: #BEFAFF; -fx-border-color: red; -fx-background-radius: 20; -fx-border-radius: 20; -fx-position-shape: 10; -fx-text-fill: black;"); // Apply CSS styling
            reserveButton.setOnAction(event -> reserveForRestaurant(event, restaurant)); // Set action handler
            Button viewPlatButton = new Button("Voir Plat");
            viewPlatButton.setOnAction(event -> viewPlatsForRestaurant(event, restaurant)); // Set action handler

<<<<<<< HEAD
            // Create like and dislike buttons
            Button likeButton = new Button("Like");
            likeButton.setOnAction(event -> likeRestaurant(event, restaurant)); // Set action handler
            Button dislikeButton = new Button("Dislike");
            dislikeButton.setOnAction(event -> dislikeRestaurant(event, restaurant)); // Set action handler
=======
            Button viewPlatsButton = new Button("View Plats");
            viewPlatsButton.setStyle("-fx-background-color: #BEFAFF; -fx-border-color: red; -fx-background-radius: 20; -fx-border-radius: 20; -fx-position-shape: 10; -fx-text-fill: black;"); // Apply CSS styling
            viewPlatsButton.setOnAction(event -> viewPlatsForRestaurant(event, restaurant)); // Set action handler/ Set action handler
>>>>>>> 6340eeb7b3df9b49a518d53dd8bc41491e14862f

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

<<<<<<< HEAD
        List<Restaurant> filteredRestaurants = serviceRestaurant.afficher(nameFilter, locationFilter);
        populateRestaurantsContainer(filteredRestaurants);
    }

=======

        List<Restaurant> filteredRestaurants = serviceRestaurant.afficher(nameFilter, locationFilter);
        populateGridPane(filteredRestaurants);

    }


>>>>>>> 6340eeb7b3df9b49a518d53dd8bc41491e14862f
    @FXML
    public void showAlert(Alert.AlertType alertType, String title, String headerText, String contentText) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
    }

<<<<<<< HEAD
=======

>>>>>>> 6340eeb7b3df9b49a518d53dd8bc41491e14862f
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
        }
        if (userInput.contains("chkoun a7ssen we7ed fi groupe pidev")) {
            return "akiid majd a7sen we7ed tw heda klem aziiz mta3 souha w rayen mte3 oumayma 7keyethom fer8a te3bon toul hhh";
        } else if (userInput.contains("reservation existante")) {
            return "Pour consulter vos réservations existantes, veuillez vous connecter à votre compte et accéder à la section réservations.";
        } else {
            return "Je suis là pour vous aider . N'hésitez pas à me poser des questions !";
        }
    }

    @FXML
<<<<<<< HEAD
    private void likeRestaurant(ActionEvent event, Restaurant restaurant) {
        // Handle like action (e.g., update database, UI, etc.)
        System.out.println("Liked restaurant: " + restaurant.getNom());
    }

    @FXML
    private void dislikeRestaurant(ActionEvent event, Restaurant restaurant) {
        // Handle dislike action (e.g., update database, UI, etc.)
        System.out.println("Disliked restaurant: " + restaurant.getNom());
    }
=======
    private void showVideo(ActionEvent event) {
        try {
            // Load the video file (replace "video.mp4" with the actual path to your video file)
            String videoFile = getClass().getResource("/majd.mp4").toExternalForm();

            javafx.scene.media.Media media = new javafx.scene.media.Media(videoFile);

            // Create a media player
            MediaPlayer mediaPlayer = new MediaPlayer(media);
            mediaPlayer.setAutoPlay(true); // Auto play the video

            // Create a media view to display the video
            MediaView mediaView = new MediaView(mediaPlayer);
            mediaView.setFitWidth(600); // Set the width of the media view
            mediaView.setFitHeight(400); // Set the height of the media view

            // Create a new stage for the video window
            Stage stage = new Stage();
            stage.setScene(new Scene(new Group(mediaView)));

            // Show the video window
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


>>>>>>> 6340eeb7b3df9b49a518d53dd8bc41491e14862f
}
