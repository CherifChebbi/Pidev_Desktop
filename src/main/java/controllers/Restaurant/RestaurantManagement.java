    package controllers.Restaurant;

    import models.RestaurantEntity.Notification;
    import models.RestaurantEntity.Restaurant;
    import services.RestaurantService.NotificationService;
    import services.RestaurantService.ServiceRestaurant;
    import javafx.collections.FXCollections;
    import javafx.collections.ObservableList;
    import javafx.event.ActionEvent;
    import javafx.fxml.FXML;
    import javafx.fxml.FXMLLoader;
    import javafx.geometry.Pos;
    import javafx.scene.Parent;
    import javafx.scene.Scene;
    import javafx.scene.control.*;
    import javafx.scene.control.cell.PropertyValueFactory;
    import javafx.scene.image.ImageView;
    import javafx.stage.FileChooser;
    import javafx.stage.Stage;
    import javafx.util.Duration;
    import org.controlsfx.control.Notifications;

    import java.io.File;
    import java.io.IOException;
    import java.sql.SQLException;
    import java.util.List;
    import java.util.regex.Pattern;


    public class RestaurantManagement {
        ServiceRestaurant SR = new ServiceRestaurant();

        @FXML
        private TableView<Restaurant> afficher;

        @FXML
        private TextArea description;

        @FXML
        private TableColumn<Restaurant, String> descriptioncol;

        @FXML
        private Button managePlatButton;


        @FXML
        private Button front;

        @FXML
        private TextField image;
        @FXML
        private TableColumn<Restaurant, ImageView> imagecol;

        @FXML
        private TextField localisation;

        @FXML
        private TableColumn<Restaurant, Integer> idcol;

        @FXML
        private TableColumn<Restaurant, String> locationcol;

        @FXML
        private TextField nom;

        @FXML
        private TableColumn<Restaurant, String> nomcol;

        @FXML
        private Button switchToPlatButton; // Button to switch to Plat view

        private static final Pattern STRING_PATTERN = Pattern.compile("^[a-zA-Zéèêçàâôûî\\s]+$");
        private static final Pattern INTEGER_PATTERN = Pattern.compile("^\\d+$");

        NotificationService notificationService = new NotificationService();

        @FXML
        void ajouter(ActionEvent event) throws SQLException {
            String restaurantName = nom.getText().trim();
            String restaurantLocation = localisation.getText().trim();
            String imagePath = image.getText().trim();
            String restaurantDescription = description.getText().trim();

            // Input validation...

            SR.ajouter(new Restaurant(restaurantName, restaurantLocation, imagePath, restaurantDescription));
            afficher();

            // Send notification to the front management
            String notificationMessage = "Nouveau restaurant ajouté: " + restaurantName;
            notificationService.addNotification(new Notification(notificationMessage)); // Use Notification constructor

            showAlert("Success", "Restaurant ajouté avec succès.");
            showNotification("Reservation Added", "Restaurant ajouter avec succes!");


        }

        private void showNotification(String title, String message) {
            Notifications.create()
                    .title(title)
                    .text(message)
                    .hideAfter(Duration.seconds(5))  // Set the duration for how long the notification should be displayed
                    .position(Pos.BOTTOM_RIGHT)
                    .show();
        }


        @FXML
        void selectImage(ActionEvent event) {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Choisir une image");
            // Set extension filters if needed
            File selectedFile = fileChooser.showOpenDialog(null);
            if (selectedFile != null) {
                image.setText(selectedFile.getAbsolutePath());
            }
        }

        @FXML
        public void afficher() throws SQLException {
            List<Restaurant> restaurantList = SR.afficher();
            ObservableList<Restaurant> observableList = FXCollections.observableArrayList(restaurantList);
            afficher.setItems(observableList);
        }

        @FXML
        void modifier(ActionEvent event) throws SQLException {
            // Check if a row is selected
            Restaurant selectedRestaurant = afficher.getSelectionModel().getSelectedItem();
            if (selectedRestaurant != null) {
                // Retrieve updated data from input fields
                String newNom = nom.getText().trim();
                String newLocalisation = localisation.getText().trim();
                String newImage = image.getText().trim();
                String newDescription = description.getText().trim();

                // Input validation
                if (!isValidInput(newNom, STRING_PATTERN)) {
                    showAlert("Error", "Le nom du restaurant ne peut pas contenir de chiffres ou de symboles.");
                    return;
                }

                if (!isValidInput(newLocalisation, STRING_PATTERN)) {
                    showAlert("Error", "La localisation du restaurant ne peut pas contenir de chiffres ou de symboles.");
                    return;
                }

                if (newDescription.isEmpty()) {
                    showAlert("Error", "Veuillez entrer une description pour le restaurant.");
                    return;
                }

                // Update the selected Restaurant object
                selectedRestaurant.setNom(newNom);
                selectedRestaurant.setLocalisataion(newLocalisation);
                selectedRestaurant.setImage(newImage);
                selectedRestaurant.setDescription(newDescription);

                // Call the modifier method in ServiceRestaurant
                SR.modifier(selectedRestaurant);

                // Refresh TableView
                afficher();
                showAlert("Success", "Restaurant modifié avec succès.");
            } else {
                showAlert("Error", "Veuillez sélectionner un restaurant à modifier.");
            }
        }

        @FXML
        void supprimer(ActionEvent event) throws SQLException {
            Restaurant selectedRestaurant = afficher.getSelectionModel().getSelectedItem();
            if (selectedRestaurant != null) {
                SR.supprimer(selectedRestaurant);
                refreshTableView();
                showAlert("Success", "Restaurant supprimé avec succès.");
            } else {
                showAlert("Error", "Veuillez sélectionner un restaurant à supprimer.");
            }
        }

        private void refreshTableView() {
            try {
                afficher();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        @FXML
        private void initialize() {
            idcol.setCellValueFactory(new PropertyValueFactory<>("id"));
            nomcol.setCellValueFactory(new PropertyValueFactory<>("nom"));
            locationcol.setCellValueFactory(new PropertyValueFactory<>("localisation"));
            imagecol.setCellValueFactory(new PropertyValueFactory<>("image"));
            descriptioncol.setCellValueFactory(new PropertyValueFactory<>("description"));
        }

        @FXML
        void switchToPlat(ActionEvent event) {
            try {
                // Close the current window
                Stage stage = (Stage) switchToPlatButton.getScene().getWindow();
                stage.close();

                // Load the dashboard view from FXML
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Plat/BackPlat.fxml"));
                Parent root = loader.load();

                // Show the dashboard view
                Stage dashboardStage = new Stage();
                dashboardStage.setTitle("Dashboard");
                dashboardStage.setScene(new Scene(root));
                dashboardStage.show();
            } catch (IOException e) {
                e.printStackTrace(); // Handle error loading the dashboard view
            }
        }


        private void showAlert(String title, String content) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(content);
            alert.showAndWait();
        }

        private boolean isValidInput(String input, Pattern pattern) {
            return pattern.matcher(input).matches();
        }


        public void navigateBack() {
            try {
                // Load the FXML file for the restaurant view
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Restaurant/index.fxml"));
                Parent root = loader.load();

                // Create a new scene with the restaurant view
                Scene scene = new Scene(root);

                // Get the stage from the button's scene
                Stage stage = (Stage) front.getScene().getWindow();

                // Set the new scene on the stage
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }
