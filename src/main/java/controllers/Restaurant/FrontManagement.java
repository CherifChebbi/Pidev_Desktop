package controllers.Restaurant;

import controllers.Reservation.ReservationManagement;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import models.Notification;
import models.Plat;
import models.Reservation;
import models.Restaurant;
import services.ReservationService.ServiceReservation;
import services.RestaurantService.NotificationService;
import services.RestaurantService.ServiceRestaurant;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;

public class FrontManagement {
    @FXML
    private VBox restaurantsContainer;

    @FXML
    private TextField searchNameField;
    @FXML
    private Button calendar;

    @FXML
    private TextField searchLocationField;

    @FXML
    private ChoiceBox<String> questionChoiceBox;

    @FXML
    private TextArea chatArea;

    @FXML
    private TextArea notificationArea;

    @FXML
    private VBox recommendedRestaurantsContainer;

    private ServiceRestaurant serviceRestaurant = new ServiceRestaurant();
    private ServiceReservation serviceReservation = new ServiceReservation();
    private NotificationService notificationService;


    public FrontManagement() {
        notificationService = new NotificationService();
    }


    @FXML
    private void openCalendar(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Calendar/Calendar.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void watchVideo(ActionEvent event) {
        try {
            File file = new File(getClass().getResource("/majd.mp4").toURI());
            Media media = new Media(file.toURI().toString());
            MediaPlayer mediaPlayer = new MediaPlayer(media);

            MediaView mediaView = new MediaView(mediaPlayer);
            mediaView.setFitWidth(600);
            mediaView.setFitHeight(400);

            Stage stage = new Stage();
            stage.setScene(new Scene(new Group(mediaView), 600, 400));
            stage.show();

            mediaPlayer.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void initialize() {
        try {
            displayRestaurants();
            displayNotifications();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de l'affichage des restaurants", "Une erreur s'est produite lors de l'affichage des restaurants. Veuillez réessayer.");
        }
    }

    private void displayNotifications() {
        List<Notification> notifications = notificationService.getNotifications();
        for (Notification notification : notifications) {
            notificationArea.appendText(notification.getMessage() + "\n");
        }
    }

    private void displayRestaurants() throws SQLException {
        List<Restaurant> restaurantList = serviceRestaurant.afficher();
        populateRestaurantsContainer(restaurantList);
    }

    private void populateRestaurantsContainer(List<Restaurant> restaurants) {
        restaurantsContainer.getChildren().clear();

        for (int i = 0; i < restaurants.size(); i += 6) {
            HBox row = createRow(restaurants.subList(i, Math.min(i + 6, restaurants.size())));
            restaurantsContainer.getChildren().add(row);
        }
    }



    private HBox createRow(List<Restaurant> restaurants) {
        HBox row = new HBox(20);
        row.setStyle("-fx-background-color: transparent;");

        for (Restaurant restaurant : restaurants) {
            String imageName = restaurant.getImage();
            if (imageName != null && !imageName.isEmpty()) {
                URL imageUrl = getClass().getResource("/upload/" + imageName);
                if (imageUrl != null) {
                    ImageView imageView = new ImageView(new Image(imageUrl.toExternalForm()));
                    imageView.setFitWidth(200);
                    imageView.setFitHeight(150);

                    Label nameLabel = createStyledLabel(restaurant.getNom(), 18, FontWeight.BOLD, Color.BLACK);
                    nameLabel.setWrapText(true);
                    nameLabel.setMaxWidth(200);

                    Label descriptionLabel = createStyledLabel(truncateDescription(restaurant.getDescription(), 3), 14, FontWeight.NORMAL, Color.GRAY);
                    descriptionLabel.setWrapText(true);
                    descriptionLabel.setMaxWidth(200);

                    Label locationLabel = createStyledLabel(restaurant.getLocalisataion(), 14, FontWeight.NORMAL, Color.GRAY);
                    locationLabel.setWrapText(true);
                    locationLabel.setMaxWidth(200);

                    Button reserveButton = createStyledButton("Réserver");
                    reserveButton.setOnAction(event -> reserveForRestaurant(event, restaurant));
                    Button viewPlatButton = createStyledButton("Voir Plat");
                    viewPlatButton.setOnAction(event -> viewPlatsForRestaurant(event, restaurant));

                    VBox restaurantInfo = new VBox(5);
                    restaurantInfo.getChildren().addAll(nameLabel, descriptionLabel, locationLabel, reserveButton, viewPlatButton);

                    VBox restaurantBox = new VBox(5);
                    restaurantBox.getChildren().addAll(imageView, restaurantInfo);

                    row.getChildren().add(restaurantBox);
                }
            }
        }

        return row;
    }

    private String truncateDescription(String description, int maxLines) {
        String[] lines = description.split("\\r?\\n");
        StringBuilder truncatedDescription = new StringBuilder();
        for (int i = 0; i < Math.min(maxLines, lines.length); i++) {
            truncatedDescription.append(lines[i]);
            if (i < maxLines - 1) {
                truncatedDescription.append("\n");
            }
        }
        return truncatedDescription.toString();
    }

    private Label createStyledLabel(String text, double fontSize, FontWeight fontWeight, Color textColor) {
        Label label = new Label(text);
        label.setFont(Font.font("Arial", fontWeight, fontSize));
        label.setTextFill(textColor);
        return label;
    }

    private Button createStyledButton(String text) {
        Button button = new Button(text);
        button.setStyle("-fx-background-color:  #E1FFEF; -fx-border-color: red; -fx-background-radius: 20; -fx-border-radius: 20; -fx-position-shape: 10;");
        button.setTextFill(Color.BLACK);
        button.setFont(new Font("Century Schoolbook", 14.0));
        return button;
    }

    @FXML
    public void viewPlatsForRestaurant(ActionEvent event, Restaurant restaurant) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Restaurant/RestaurantDetails.fxml"));
            Parent root = loader.load();

            RestaurantDetailsController controller = loader.getController();
            List<Plat> plats = serviceRestaurant.getPlatsForRestaurant(restaurant.getid());

            String imageName = restaurant.getImage();
            URL imageUrl = getClass().getResource("/upload/" + imageName);


            controller.initData(restaurant.getNom(), restaurant.getDescription(), plats,imageUrl);

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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Reservation/Reservation.fxml"));
            Parent root = loader.load();

            ReservationManagement controller = loader.getController();
            controller.initData(restaurant.getid(), restaurant.getNom());

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

    private String generateResponse(String selectedQuestion) {
        if (selectedQuestion.contains("Bonjour")) {
            return "Bonjour! Est-ce que je peux vous aider?";
        } else if (selectedQuestion.contains("Comment puis-je effectuer une réservation")) {
            return "Pour effectuer une réservation, veuillez fournir le nom du restaurant et la date/heure de la réservation";
        } else if (selectedQuestion.contains("Comment utiliser cette application ")) {
            return "Notre Application est simple a utiliser il suffit de chercher un restaurant est le reserver ";
        } else {
            return "Je suis là pour vous aider. N'hésitez pas à me poser des questions!";
        }
    }

    @FXML
    private void likeRestaurant(ActionEvent event, Restaurant restaurant) {
        System.out.println("Liked restaurant: " + restaurant.getNom());
    }

    @FXML
    private void dislikeRestaurant(ActionEvent event, Restaurant restaurant) {
        System.out.println("Disliked restaurant: " + restaurant.getNom());
    }

    public void handleNotificationClick(javafx.scene.input.MouseEvent mouseEvent) {
    }


    public void displayReservationsCalendar(ActionEvent actionEvent, Restaurant restaurant) {

        try {
            List<Reservation> reservations = serviceReservation.getReservationsForRestaurant(restaurant.getid());

            // Create and populate a GridPane with reservation details
            GridPane gridPane = new GridPane();
            gridPane.setHgap(10);
            gridPane.setVgap(10);

            int row = 0;
            for (Reservation reservation : reservations) {
                gridPane.addRow(row++, new Label(reservation.getNom()), new Label(reservation.getDate()));
            }

            // Create a new VBox to contain the GridPane
            VBox vBox = new VBox(10, new Label("Reservations for " + restaurant.getNom()), gridPane);

            // Create a new Stage to display the reservations calendar
            Stage stage = new Stage();
            stage.setScene(new Scene(vBox));
            stage.show();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Error fetching reservations", "An error occurred while fetching reservations.");
        }
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
}
