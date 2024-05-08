package controllers.Event;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import java.io.IOException;
import javafx.scene.Node;
import java.sql.SQLException;
import java.util.Map;

import services.ServiceCategory;
import services.ServiceEvent;
import services.ServiceReservationEvent;


public class HomeBackController {

    @FXML
    private Label totalCategoriesLabel;

    @FXML
    private Label totalEventsLabel;

    @FXML
    private Label totalReservationLabel;

    @FXML
    private StackedBarChart<String, Number> statistiques;

    @FXML
    public void dashboardCategories(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/CategoryEvent/BackCategory.fxml"));
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ReservationEvent/ReservationBack.fxml"));
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Event/BackEvent.fxml"));
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Event/HomeBack.fxml"));
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
        updateStatistics();

        // Ajouter un titre au graphique
        statistiques.setTitle("Statistiques sur les réservations");

        // Ajouter un titre à l'axe des abscisses
        CategoryAxis xAxis = (CategoryAxis) statistiques.getXAxis();
        xAxis.setLabel("Événements");

        // Ajouter un titre à l'axe des ordonnées
        NumberAxis yAxis = (NumberAxis) statistiques.getYAxis();
        yAxis.setLabel("Nombre de réservations");

        // Définir l'unité de pas sur l'axe des ordonnées pour afficher des nombres entiers
        yAxis.setTickUnit(5); // Chaque pas représente un multiple de 5
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

    private void updateStatistics() {
        try {
            Map<String, Integer> reservationsByEvent = ServiceReservationEvent.countReservationsByEvent();

            XYChart.Series<String, Number> series = new XYChart.Series<>();
            for (String event : reservationsByEvent.keySet()) {
                XYChart.Data<String, Number> data = new XYChart.Data<>(event, reservationsByEvent.get(event));
                series.getData().add(data);

                // Ajuster la largeur de chaque barre individuellement
                data.nodeProperty().addListener((observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        newValue.setStyle("-fx-bar-width: 10;"); // Modifier la largeur de la barre ici
                    }
                });
            }

            statistiques.getData().clear();
            statistiques.getData().add(series);

            // Définir l'unité de pas sur l'axe des nombres pour afficher des nombres entiers
            NumberAxis yAxis = (NumberAxis) statistiques.getYAxis();
            yAxis.setTickUnit(5); // Chaque pas représente un multiple de 5
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void frontEvent(ActionEvent actionEvent) {
        try {
            // Charger le fichier FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/indexEvent.fxml"));
            Parent root = loader.load();

            // Créer une nouvelle scène
            Scene scene = new Scene(root);

            // Obtenir la fenêtre actuelle
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();

            // Définir la scène sur la fenêtre
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            // Gérer les erreurs de chargement du FXML
        }
    }
}