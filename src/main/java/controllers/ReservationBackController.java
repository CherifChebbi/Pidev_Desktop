package controllers;

import entities.ReservationEvent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import services.ServiceReservationEvent;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class ReservationBackController {

    @FXML
    private DatePicker datePicker;

    @FXML
    private TableColumn<ReservationEvent, Integer> idColumn;

    @FXML
    private TableColumn<ReservationEvent, Integer> eventIdColumn;

    @FXML
    private TableColumn<ReservationEvent, String> nomColumn;

    @FXML
    private TableColumn<ReservationEvent, String> emailColumn;

    @FXML
    private TableColumn<ReservationEvent, String> numTelColumn;

    @FXML
    private TableColumn<ReservationEvent, java.util.Date> dateReservationColumn;

    @FXML
    private TextField nomRechercheTextField;

    @FXML
    private TableView<ReservationEvent> reservationTableView;

    private final ServiceReservationEvent serviceReservationEvent;

    {
        try {
            serviceReservationEvent = new ServiceReservationEvent();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void initialize() {
        // Liaison des colonnes avec les propriétés de la classe ReservationEvent
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        eventIdColumn.setCellValueFactory(new PropertyValueFactory<>("idEvent"));
        nomColumn.setCellValueFactory(new PropertyValueFactory<>("nom"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        numTelColumn.setCellValueFactory(new PropertyValueFactory<>("numTel"));
        dateReservationColumn.setCellValueFactory(new PropertyValueFactory<>("dateReservation"));

        // Charger les données depuis la base de données et les afficher dans TableView
        afficherReservations();

        // Mettre en place la fonctionnalité de recherche par nom
        nomRechercheTextField.textProperty().addListener((observable, oldValue, newValue) -> rechercherReservationParNom(newValue));

        // Mettre en place la fonctionnalité de filtrage par date
        datePicker.valueProperty().addListener((observable, oldValue, newValue) -> filtrerReservationParDate(newValue));
    }

    // Méthode pour charger les réservations depuis la base de données
    private void afficherReservations() {
        List<ReservationEvent> reservations = serviceReservationEvent.afficher();
        reservationTableView.getItems().addAll(reservations);
    }

    // Méthode pour rechercher les réservations par nom
    private void rechercherReservationParNom(String nom) {
        List<ReservationEvent> reservations = serviceReservationEvent.rechercherParNom(nom);
        reservationTableView.getItems().clear();
        reservationTableView.getItems().addAll(reservations);
    }

    // Méthode pour filtrer les réservations par date
    private void filtrerReservationParDate(java.time.LocalDate date) {
        List<ReservationEvent> reservations = serviceReservationEvent.filtrerParDate(date);
        reservationTableView.getItems().clear();
        reservationTableView.getItems().addAll(reservations);
    }


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

}
