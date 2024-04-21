package Controller;

import Entity.Reservation;
import Services.ServiceReservation;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;

public class ReservationManagment {

    @FXML
    private Button reserver;

    @FXML
    private ListView<Reservation> afficher;

    @FXML
    private DatePicker date;

    @FXML
    private TextField email;

    @FXML
    private TextField nbr;

    @FXML
    private TextField nom;

    @FXML
    void moove(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/test/reservation.fxml"));
        Parent root = loader.load();
        Stage st = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        st.setScene(scene);
        st.show();
    }

    ServiceReservation SR = new ServiceReservation();


    @FXML
    void ajouter(ActionEvent event) throws SQLException {
        String i = nom.getText();
        String y = email.getText();
        LocalDate x = date.getValue(); // Retrieve selected date from DatePicker
        Integer z = Integer.parseInt(nbr.getText());

        if (x != null) {
            // Pass LocalDate object to Reservation constructor
            System.out.println("Selected date: " + x); // Add this line for debugging
            SR.ajouter(new Reservation(i, y, x, z));
            // Update the ListView
            try {
                afficher();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        } else {
            // Show an alert message if the date is null
            System.out.println("Veuillez sélectionner une date.");
        }

    }
    private void afficher() throws SQLException {
        afficher.setItems(SR.afficher());
    }

    @FXML
    void modifier(ActionEvent event) {

    }

    @FXML
    void supprimer(ActionEvent event) {

    }
}
