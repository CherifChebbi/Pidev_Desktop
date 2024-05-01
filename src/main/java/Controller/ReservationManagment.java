package Controller;

import Entity.Reservation;
import Services.ServiceReservation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Optional;

public class ReservationManagment {

    @FXML
    private TableView<Reservation> afficher;

    @FXML
    private TextField nom;

    @FXML
    private TextField email;

    @FXML
    private DatePicker date;

    @FXML
    private TextField nbr;

    private ObservableList<Reservation> reservations = FXCollections.observableArrayList();

    private ServiceReservation SR = new ServiceReservation();

    @FXML
    public void initialize() {
        // Initialize the TableView with the list of reservations
        try {
            afficher();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void afficher() throws SQLException {
        reservations.clear();
        reservations.addAll(SR.afficher());
        afficher.setItems(reservations);
    }
    private void refreshTableView() {
        try {
            afficher.getItems().clear(); // Clear existing items
            afficher(); // Re-populate TableView
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @FXML
    void ajouter(ActionEvent event) throws SQLException {
        String i = nom.getText();
        String y = email.getText();
        LocalDate x = date.getValue();
        Integer z = Integer.parseInt(nbr.getText());

        try {
            if (i != null && !i.isEmpty() && y != null && !y.isEmpty() && x != null && z != null) {
                Reservation reservation = new Reservation(5, i, y, x, z);
                SR.ajouter(reservation);

                // Add the new reservation to the TableView
                afficher.getItems().add(reservation);

                // Clear the input fields after adding the reservation
                nom.clear();
                email.clear();
                date.setValue(null);
                nbr.clear();
            } else {
                System.out.println("Veuillez remplir tous les champs.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Veuillez entrer un nombre valide pour le nombre de personnes.");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    void modifier(ActionEvent event) throws SQLException {
        Reservation selectedReservation = afficher.getSelectionModel().getSelectedItem();
        if (selectedReservation != null) {
            // Show a dialog or form to edit the reservation details
            // After editing, update the reservation in the database
            SR.modifier(selectedReservation); // You need to implement the modifier method in ServiceReservation
            // Refresh the TableView
            afficher();
        } else {
            System.out.println("Veuillez sélectionner une réservation à modifier.");
        }
    }

    @FXML
    void supprimer(ActionEvent event) throws SQLException {
        Reservation selectedReservation = afficher.getSelectionModel().getSelectedItem();
        if (selectedReservation != null) {
            // Show a confirmation dialog
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText("Supprimer la réservation?");
            alert.setContentText("Êtes-vous sûr de vouloir supprimer cette réservation?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                // User confirmed, delete the reservation
                SR.supprimer(selectedReservation.getId()); // You need to implement the supprimer method in ServiceReservation
                reservations.remove(selectedReservation);
                // Refresh the TableView
                afficher();
            }
        } else {
            System.out.println("Veuillez sélectionner une réservation à supprimer.");
        }
    }


}
