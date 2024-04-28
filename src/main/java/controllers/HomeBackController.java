package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import java.io.IOException;
import javafx.scene.Node;
import java.sql.SQLException;

import services.ServiceCategory;
import services.ServiceEvent;
import services.ServiceReservationEvent;
import utils.DB;


public class HomeBackController  {

    @FXML
    private Label totalCategoriesLabel;

    @FXML
    private Label totalEventsLabel;

    @FXML
    private Label totalReservationLabel;

    @FXML
    public void dashboardCategories(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/BackCategory.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void dashboardReservations(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ReservationBack.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void dashboardEvents(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/BackEvent.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void homeDashboard(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/HomeBack.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void initialize() {
        updateLabels();
    }

    private void updateLabels() {
        try {
            int totalCategories = ServiceCategory.countCategories();
            int totalEvents = ServiceEvent.countEvents();
            int totalReservations = ServiceReservationEvent.countReservations();

            totalCategoriesLabel.setText(String.valueOf(totalCategories));
            totalEventsLabel.setText(String.valueOf(totalEvents));
            totalReservationLabel.setText(String.valueOf(totalReservations));
        } catch (SQLException e) {
            e.printStackTrace();
            // Gérer l'exception : afficher un message d'erreur convivial à l'utilisateur
        }
    }



}
