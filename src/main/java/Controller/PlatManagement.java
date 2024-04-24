package Controller;

import Entity.Plat;
import Entity.Restaurant;
import Services.ServicePlat;
import Services.ServiceRestaurant;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class PlatManagement {

    @FXML
    private TableView<Plat> afficher;

    @FXML
    private TableColumn<Plat, String> idcol;

    @FXML
    private TextField image;

    @FXML
    private TableColumn<Plat, String> imagecol;

    @FXML
    private TextField nom;

    @FXML
    private TableColumn<Plat, String> nomcol;

    @FXML
    private TextField prix;

    @FXML
    private TableColumn<Plat, Float> prixcol;

    @FXML
    private ComboBox<Restaurant> restaurantComboBox;

    @FXML
    private TableColumn<Plat, String> selectnamecol;

    @FXML
    private Button platbutton;

    private ServicePlat servicePlat;
    private ServiceRestaurant serviceRestaurant;

    @FXML
    void ajouter(ActionEvent event) {
        try {
            String nomPlat = nom.getText();
            String imagePlat = image.getText();
            float prixPlat = Float.parseFloat(prix.getText());
            Restaurant selectedRestaurant = restaurantComboBox.getValue();
            Plat plat = new Plat(nomPlat, imagePlat, prixPlat, selectedRestaurant);
            servicePlat.ajouter(plat);

            // Clear input fields after adding
            clearFields();

            // Refresh TableView
            refreshTableView(); // Add this line to refresh TableView after adding Plat
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle SQLException
        } catch (NumberFormatException e) {
            e.printStackTrace();
            // Handle NumberFormatException
        }
    }


    @FXML
    void modifier(ActionEvent event) {
        try {
            Plat selectedPlat = afficher.getSelectionModel().getSelectedItem();
            if (selectedPlat != null) {
                selectedPlat.setNom(nom.getText());
                selectedPlat.setImage(image.getText());

                // Check if the prix field is not empty before parsing
                if (!prix.getText().isEmpty()) {
                    float prixPlat = Float.parseFloat(prix.getText());
                    selectedPlat.setPrix(prixPlat);
                } else {
                    // Handle case where prix field is empty
                    System.out.println("Please enter a valid price.");
                    return; // Exit the method
                }

                selectedPlat.setRestaurant(restaurantComboBox.getValue());

                servicePlat.modifier(selectedPlat);

                // Clear input fields after modifying
                clearFields();

                // Refresh TableView
                refreshTableView(); // Add this line to refresh TableView after modifying Plat
            } else {
                // Handle case where no Plat is selected
                System.out.println("Please select a Plat to modify.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle SQLException
        } catch (NumberFormatException e) {
            e.printStackTrace();
            // Handle NumberFormatException
        }
    }


    @FXML
    void supprimer(ActionEvent event) {
        try {
            Plat selectedPlat = afficher.getSelectionModel().getSelectedItem();
            if (selectedPlat != null) {
                int id = selectedPlat.getId();
                servicePlat.supprimer(id);

                // Refresh TableView after deleting
                refreshTableView(); // Add this line to refresh TableView after deleting Plat
            } else {
                // Handle case where no Plat is selected
                System.out.println("Please select a Plat to delete.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle SQLException
        }
    }

    @FXML
    void selectrestaurant(ActionEvent event) {
        // Implement method to handle selection of restaurant
    }

    @FXML
    private void initialize() {
        servicePlat = new ServicePlat();
        serviceRestaurant = new ServiceRestaurant();
        populateRestaurantComboBox();

        // Call the afficher method to populate the TableView
        try {
            afficher();

            // Set up cell value factories for each TableColumn
            idcol.setCellValueFactory(new PropertyValueFactory<>("id"));
            nomcol.setCellValueFactory(new PropertyValueFactory<>("nom"));
            imagecol.setCellValueFactory(new PropertyValueFactory<>("image"));
            prixcol.setCellValueFactory(new PropertyValueFactory<>("prix"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private void populateRestaurantComboBox() {
        try {
            List<Restaurant> restaurants = serviceRestaurant.getAllRestaurants();
            restaurantComboBox.setItems(FXCollections.observableArrayList(restaurants));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void afficher() throws SQLException {
        List<Plat> plats = servicePlat.getAllPlats();
        ObservableList<Plat> observableList = FXCollections.observableArrayList(plats);
        afficher.setItems(observableList);
    }

    private void refreshTableView() {
        try {
            afficher.getItems().clear(); // Clear existing items
            afficher(); // Re-populate TableView
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private void clearFields() {
        nom.clear();
        image.clear();
        prix.clear();
    }

    @FXML
    void moove(ActionEvent event) {
        try {
            // Close the current window
            Stage stage = (Stage) platbutton.getScene().getWindow();
            stage.close();

            // Load the PlatManagement view from FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Restaurant.fxml"));
            Parent root = loader.load();

            // Show the PlatManagement view
            Stage platStage = new Stage();
            platStage.setTitle("Plat Management");
            platStage.setScene(new Scene(root));
            platStage.show();
        } catch (IOException e) {
            e.printStackTrace(); // Handle error loading the PlatManagement view
        }

    }
    }

