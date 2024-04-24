
package Controller;
import Entity.Reservation;
import Services.ServiceReservation;
import com.itextpdf.kernel.pdf.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import javax.swing.text.Document;
import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.SQLException;
import java.util.List;

import com.itextpdf.layout.element.Paragraph;


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

    private Reservation selectedReservation; // Store the selected reservation

    private void handleReservationSelection(Reservation reservation) {
        selectedReservation = reservation;
    }

    // Method to add reservations to the reservationScrollPane
    private void addReservationToScrollPane(Reservation reservation) {
        Text reservationText = new Text("Nom: " + reservation.getNom() + ", Email: " + reservation.getEmail() +
                ", Date: " + reservation.getDate() + ", Nombre Personne: " + reservation.getNbrPersonne() +
                ", Restaurant: " + selectedRestaurantName);

        // Add event handler to the reservation text
        reservationText.setOnMouseClicked(event -> handleReservationSelection(reservation));

        // Add reservation text to the scroll pane
        GridPane gridPane = (GridPane) reservationScrollPane.getContent();
        if (gridPane == null) {
            gridPane = new GridPane();
            reservationScrollPane.setContent(gridPane);
        }
        int row = gridPane.getRowCount();
        gridPane.add(reservationText, 0, row);
    }

    // Modify ajouter() method to use addReservationToScrollPane
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

            // Add reservation to the scroll pane
            addReservationToScrollPane(newReservation);

        } catch (SQLException | NumberFormatException e) {
            e.printStackTrace();
            // Handle SQLException or NumberFormatException
        }
    }

    // Modify printReservationToPDF method to use selectedReservation
    @FXML
    void printReservationToPDF(ActionEvent event) {
        try {
            if (selectedReservation == null) {
                showAlert("No Reservation Selected", "Please select a reservation to print.");
                return;
            }

            // File name for the PDF
            String fileName = "reservation.pdf";

            // Create a new PDF document
            PdfDocument pdfDocument = new PdfDocument(new PdfWriter(fileName));

            // Create a Document instance
            com.itextpdf.layout.Document document = new com.itextpdf.layout.Document(pdfDocument);

            // Add reservation details to the PDF
            document.add(new com.itextpdf.layout.element.Paragraph("Reservation Details"));
            document.add(new com.itextpdf.layout.element.Paragraph("Nom: " + selectedReservation.getNom()));
            document.add(new com.itextpdf.layout.element.Paragraph("Email: " + selectedReservation.getEmail()));
            document.add(new com.itextpdf.layout.element.Paragraph("Date: " + selectedReservation.getDate()));
            document.add(new com.itextpdf.layout.element.Paragraph("Nombre Personne: " + selectedReservation.getNbrPersonne()));

            // Close the document
            document.close();

            showAlert("PDF Created", "Reservation details printed to PDF successfully.");

            // Open the PDF file using the default PDF viewer
            File file = new File(fileName);
            Desktop desktop = Desktop.getDesktop();
            if (file.exists()) {
                desktop.open(file);
            } else {
                showAlert("Error", "The generated PDF file does not exist.");
            }

        } catch (Exception e) {
            showAlert("Error", "An error occurred while printing the reservation to PDF.");
            e.printStackTrace();
        }
    }



    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public void supprimer(ActionEvent actionEvent) {
    }



    private void displayReservations() throws SQLException {
        // Get reservations for the selected restaurant from the service
        List<Reservation> reservations = serviceReservation.getAllReservationsForRestaurant(selectedRestaurantId);

        // Clear existing content in the reservationScrollPane
        GridPane gridPane = new GridPane();
        reservationScrollPane.setContent(gridPane);

        // Add reservations to the reservationScrollPane
        for (Reservation reservation : reservations) {
            addReservationToScrollPane(reservation);
        }
    }

    public void modifier(ActionEvent actionEvent) {
    }

    @FXML
    void ajouterEtActualiser(ActionEvent actionEvent) {
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

            // Refresh display by fetching and displaying all reservations again
            displayReservations();

        } catch (SQLException | NumberFormatException e) {
            e.printStackTrace();
            // Handle SQLException or NumberFormatException
        }
    }

    public void initData(int restaurantId, String restaurantName) {
        this.selectedRestaurantId = restaurantId;
        this.selectedRestaurantName = restaurantName;
        restaurantLabel.setText(selectedRestaurantName);
        try {
            // Display existing reservations when initializing the controller
            displayReservations();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
