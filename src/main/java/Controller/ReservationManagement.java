package Controller;

import Entity.Reservation;
import Entity.Restaurant;
import Services.ServiceReservation;
import Util.MyDB;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.List;

public class ReservationManagement {
    @FXML
    private TextField nom;

    @FXML
    private TextField email;

    @FXML
    private DatePicker datePicker;

    @FXML
    private TextField nbrpersonne;

    @FXML
    private ScrollPane reservationScrollPane;

    @FXML
    private Label restaurantLabel; // Label to display the restaurant name

    private final ServiceReservation serviceReservation = new ServiceReservation();
    private int selectedRestaurantId; // Store the selected restaurant ID
    private String selectedRestaurantName; // Store the selected restaurant name

    @FXML
    void ajouter() {
        try {
            // Get data from fields
            String reservationNom = nom.getText();
            String reservationEmail = email.getText();
            String reservationDate = datePicker.getValue().toString();
            int reservationNbrPersonne = Integer.parseInt(nbrpersonne.getText());

            // Create a new reservation object with the selected restaurant ID
            Reservation newReservation = new Reservation(selectedRestaurantId, reservationNom, reservationEmail, reservationDate, reservationNbrPersonne);

            // Add reservation to the database
            serviceReservation.ajouter(newReservation);

            // Fetch all reservations for the selected restaurant
            List<Reservation> reservations = serviceReservation.getAllReservationsForRestaurant(selectedRestaurantId);

            // Clear existing content in the reservationScrollPane
            GridPane gridPane = new GridPane();
            reservationScrollPane.setContent(gridPane);

            // Add reservations to the reservationScrollPane
            int row = 0;
            for (Reservation reservation : reservations) {
                Text reservationText = new Text("Nom: " + reservation.getNom() + ", Email: " + reservation.getEmail() +
                        ", Date: " + reservation.getDate() + ", Nombre Personne: " + reservation.getNbrPersonne() +
                        ", Restaurant: " + selectedRestaurantName); // Use the selected restaurant name here
                gridPane.add(reservationText, 0, row++);
            }

        } catch (SQLException | NumberFormatException e) {
            e.printStackTrace();
            // Handle SQLException or NumberFormatException
        }
    }

    @FXML
    private void closeForm() {
        // Get the stage (window) associated with the current scene
        Stage stage = (Stage) nom.getScene().getWindow();

        // Close the stage
        stage.close();
    }

    public void modifier(ActionEvent actionEvent) {
    }

    public void supprimer(ActionEvent actionEvent) {
    }

    public void ajouterEtActualiser(ActionEvent actionEvent) {
        ajouter(); // Call the existing ajouter() method to add the reservation
    }

    public void initData(int restaurantId, String restaurantName) {
        this.selectedRestaurantId = restaurantId;
        this.selectedRestaurantName = restaurantName;
        restaurantLabel.setText(selectedRestaurantName); // Set the label text to the selected restaurant name
    }
}
