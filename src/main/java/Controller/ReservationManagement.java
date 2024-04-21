package Controller;

import Entity.Reservation;
import Services.ServiceReservation;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.time.LocalDate;
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

    private final ServiceReservation serviceReservation = new ServiceReservation();
    private int selectedRestaurantId; // Store the selected restaurant ID

    public void initData(int restaurantId) {
        this.selectedRestaurantId = restaurantId; // Initialize selectedRestaurantId with the passed ID
    }

    @FXML
    void ajouter() {
        try {
            // Get data from fields
            String reservationNom = nom.getText();
            String reservationEmail = email.getText();
            String reservationDate = datePicker.getValue().toString();
            int reservationNbrPersonne = Integer.parseInt(nbrpersonne.getText());

            // Create a new reservation object with the selected restaurant ID
            Reservation newReservation = new Reservation(0, selectedRestaurantId, reservationNom, reservationEmail, reservationDate, reservationNbrPersonne);

            // Add reservation to the database
            serviceReservation.ajouter(newReservation);

            // Fetch the list of reservations for the selected restaurant
            List<Reservation> reservations = serviceReservation.getAllReservationsForRestaurant(selectedRestaurantId);

            // Clear existing content in the reservationScrollPane
            reservationScrollPane.setContent(new GridPane());

            // Add reservations to the reservationScrollPane
            GridPane gridPane = (GridPane) reservationScrollPane.getContent();
            gridPane.getChildren().clear(); // Clear existing content if needed

            int row = 0;
            for (Reservation reservation : reservations) {
                Text reservationText = new Text(reservation.toString());
                gridPane.add(reservationText, 0, row++);
            }

            // Close the reservation form after adding the reservation
            closeForm();

        } catch (SQLException | NumberFormatException e) {
            e.printStackTrace();
            // Handle SQLException or NumberFormatException
        }
    }


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

    // Add other methods...
}
