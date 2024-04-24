package Controller;

import Entity.Reservation;
import Entity.Restaurant;
import Services.ServiceReservation;
import Services.ServiceRestaurant;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label; // Add this import for Label
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

import java.sql.SQLException;
import java.util.List;

public class ReservationManagement {

    @FXML
    private ComboBox<Restaurant> idres;

    @FXML
    private TextField nom;

    @FXML
    private TextField email;

    @FXML
    private DatePicker datePicker;

    @FXML
    private TextField nbrpersonne;

    @FXML
    private GridPane reservationGridPane;

    private final ServiceReservation serviceReservation = new ServiceReservation();
    private final ServiceRestaurant serviceRestaurant = new ServiceRestaurant();
    private Restaurant selectedRestaurant;

    @FXML
    void ajouter(ActionEvent event) {
        try {
            // Get data from fields
            Restaurant selectedRestaurant = idres.getValue();
            String selectedRestaurantName = selectedRestaurant.getNom();
            int selectedRestaurantId = selectedRestaurant.getIdR();
            String reservationNom = nom.getText();
            String reservationEmail = email.getText();
            String reservationDate = datePicker.getValue().toString();
            int reservationNbrPersonne = Integer.parseInt(nbrpersonne.getText());

            // Create a new reservation object
            Reservation newReservation = new Reservation(0, selectedRestaurantId, reservationNom, reservationEmail, reservationDate, reservationNbrPersonne);
            newReservation.setSelectedRestaurant(selectedRestaurant);

            // Add reservation to the database
            serviceReservation.ajouter(newReservation);

            // Refresh the grid view
            refreshGridView();

        } catch (SQLException | NumberFormatException e) {
            e.printStackTrace();
            // Handle SQLException or NumberFormatException
        }
    }

    @FXML
    void modifier(ActionEvent event) {
        // Your code for modifying a reservation goes here
    }

    @FXML
    void supprimer(ActionEvent event) {
        // Your code for deleting a reservation goes here
    }

    // Add other methods here...

    private void refreshGridView() {
        try {
            // Clear existing grid
            reservationGridPane.getChildren().clear();

            // Retrieve all reservations
            List<Reservation> reservations = serviceReservation.afficher();

            // Add each reservation to the grid
            int row = 0;
            int col = 0;
            for (Reservation reservation : reservations) {
                // Create a new reservation grid
                GridPane reservationGrid = new GridPane();
                reservationGrid.setPrefSize(200, 100); // Set preferred size

                // Add reservation details to the grid using labels
                Label idLabel = new Label("Reservation ID: " + reservation.getId());
                Label nameLabel = new Label("Nom: " + reservation.getNom());
                Label emailLabel = new Label("Email: " + reservation.getEmail());
                Label dateLabel = new Label("Date: " + reservation.getDate());
                Label nbrPersonneLabel = new Label("Nombre Personne: " + reservation.getNbrPersonne());

                // Retrieve the restaurant name from the reservation's associated restaurant
                String restaurantName = (reservation.getSelectedRestaurant() != null) ? reservation.getSelectedRestaurant().getNom() : "N/A";

                Label restaurantLabel = new Label("Restaurant: " + restaurantName);

                // Add labels to the reservation grid
                reservationGrid.add(idLabel, 0, 0);
                reservationGrid.add(nameLabel, 0, 1);
                reservationGrid.add(emailLabel, 0, 2);
                reservationGrid.add(dateLabel, 0, 3);
                reservationGrid.add(nbrPersonneLabel, 0, 4);
                reservationGrid.add(restaurantLabel, 0, 5); // Add the restaurant label

                // Add reservation grid to the main grid pane
                reservationGridPane.add(reservationGrid, col, row);

                // Update selectedRestaurant for this reservation
                reservation.setRestaurant(selectedRestaurant);

                // Update row and col counters
                col++;
                if (col == 3) {
                    col = 0;
                    row++;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    @FXML
    void initialize() {
        try {
            List<Restaurant> restaurants = serviceRestaurant.getAllRestaurants();
            idres.setItems(FXCollections.observableArrayList(restaurants));

            // Add a listener to ComboBox selection
            idres.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                selectedRestaurant = newValue; // Update selectedRestaurant when ComboBox selection changes
                refreshGridView(); // Refresh the grid view when selection changes
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Refresh the grid view
        refreshGridView();
    }


}
