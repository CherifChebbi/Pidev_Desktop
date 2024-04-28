package Controller;

import Services.ServicePlat;
import Services.ServiceRestaurant;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class AdminHome {

    @FXML
    private ImageView front;


    @FXML
    private ImageView plats;

    @FXML
    private ImageView restauran;


    @FXML
    private Label plat;

    @FXML
    private Label restaurant;


    private ServicePlat servicePlat;
    private ServiceRestaurant serviceRestaurant;


    private void initialize() {
        servicePlat = new ServicePlat();
        serviceRestaurant = new ServiceRestaurant();

    }


    public void navigateBack(MouseEvent mouseEvent) {
        try {
            // Load the FXML file for the restaurant view
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Front.fxml"));
            Parent root = loader.load();

            // Create a new scene with the restaurant view
            Scene scene = new Scene(root);

            // Get the stage from the ImageView
            Stage stage = (Stage) front.getScene().getWindow();

            // Set the new scene on the stage
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void moove (MouseEvent mouseEvent) {
        try {
            // Load the FXML file for the restaurant view
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Plat.fxml"));
            Parent root = loader.load();

            // Create a new scene with the restaurant view
            Scene scene = new Scene(root);

            // Get the stage from the ImageView
            Stage stage = (Stage) plats.getScene().getWindow();

            // Set the new scene on the stage
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }





    public void navigate(MouseEvent mouseEvent) {
        try {
            // Load the FXML file for the restaurant view
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Restaurant.fxml"));
            Parent root = loader.load();

            // Create a new scene with the restaurant view
            Scene scene = new Scene(root);

            // Get the stage from the Label
            Stage stage = (Stage) restauran.getScene().getWindow();

            // Set the new scene on the stage
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
