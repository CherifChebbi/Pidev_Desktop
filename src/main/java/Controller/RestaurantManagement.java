package Controller;

import Entity.Restaurant;
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
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

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

    @FXML
    void ajouter(ActionEvent event) throws SQLException {
        String i = String.valueOf(nom.getText());
        String j = String.valueOf(localisation.getText());
        String y = String.valueOf(image.getText());
        String z = String.valueOf(description.getText());
        SR.ajouter(new Restaurant(i, j, y, z));
        afficher();
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
            String newNom = nom.getText();
            String newLocalisation = localisation.getText();
            String newImage = image.getText();
            String newDescription = description.getText();

            // Update the selected Restaurant object
            selectedRestaurant.setNom(newNom);
            selectedRestaurant.setLocalisataion(newLocalisation);
            selectedRestaurant.setImage(newImage);
            selectedRestaurant.setDescription(newDescription);

            // Call the modifier method in ServiceRestaurant
            SR.modifier(selectedRestaurant);

            // Refresh TableView
            afficher();
        } else {
            // Display an error message or prompt the user to select a row
        }
    }


    @FXML
    void supprimer(ActionEvent event) throws SQLException {
        Restaurant selectedRestaurant = afficher.getSelectionModel().getSelectedItem();
        if (selectedRestaurant != null) {
            SR.supprimer(selectedRestaurant);
            refreshTableView();
            System.out.println("Restaurant deleted successfully!");
        } else {
            System.out.println("Please select a restaurant to delete.");
        }
    }

    private void refreshTableView() {try {
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
            Stage stage = (Stage) managePlatButton.getScene().getWindow();
            stage.close();

            // Load the PlatManagement view from FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Plat.fxml"));
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