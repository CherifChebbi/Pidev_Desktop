
package controllers.Reservation;
import models.Reservation;
import services.ReservationService.ServiceReservation;
import com.itextpdf.kernel.pdf.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.scene.image.Image;

import java.awt.*;
import java.io.File;

import java.sql.SQLException;
import java.util.List;
import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;

import javax.mail.*;
import javax.mail.internet.*;


import javafx.util.Duration;
import org.controlsfx.control.Notifications;


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
    private ImageView imagerest;

    @FXML
    private Label restaurantLabel; // Label to display the restaurant name

    private final ServiceReservation serviceReservation = new ServiceReservation();
    private int selectedrestaurant_id; // Store the selected restaurant ID
    private String selectedRestaurantName; // Store the selected restaurant name

    private Reservation selectedReservation; // Store the selected reservation

    private void handleReservationSelection(Reservation reservation) {
        selectedReservation = reservation;
    }


    private static final String EMAIL_USERNAME = "your_email@example.com"; // Your email address
    private static final String EMAIL_PASSWORD = "your_email_password";

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

    @FXML
    void supprimer(ActionEvent actionEvent) {
        try {
            if (selectedReservation != null) {
                // Delete the selected reservation from the database
                serviceReservation.deleteReservation(selectedReservation.getId());

                // Refresh display by fetching and displaying all reservations again
                displayReservations();

                showAlert("Reservation Deleted", "Reservation has been deleted successfully.");
            } else {
                showAlert("No Reservation Selected", "Please select a reservation to delete.");
            }
        } catch (SQLException e) {
            showAlert("Error", "An error occurred while deleting the reservation.");
            e.printStackTrace();
        }
    }

    @FXML
    void modifier(ActionEvent actionEvent) {
        try {
            if (selectedReservation != null) {
                // Get data from fields
                String reservationNom = nom.getText();
                String reservationEmail = email.getText();
                String reservationDate = datePicker.getValue().toString();
                int reservationNbrPersonne = Integer.parseInt(nbrpersonne.getText());

                // Update the selected reservation object
                selectedReservation.setNom(reservationNom);
                selectedReservation.setEmail(reservationEmail);
                selectedReservation.setDate(reservationDate);
                selectedReservation.setNbrPersonne(reservationNbrPersonne);

                // Update the reservation in the database
                serviceReservation.modifier(selectedReservation);

                // Refresh display by fetching and displaying all reservations again
                displayReservations();

                showAlert("Reservation Updated", "Reservation has been updated successfully.");
            } else {
                showAlert("No Reservation Selected", "Please select a reservation to modify.");
            }
        } catch (SQLException | NumberFormatException e) {
            showAlert("Error", "An error occurred while modifying the reservation.");
            e.printStackTrace();
        }
    }







    private void displayReservations() throws SQLException {
        // Get reservations for the selected restaurant from the service
        List<Reservation> reservations = serviceReservation.getAllReservationsForRestaurant(selectedrestaurant_id);

        // Clear existing content in the reservationScrollPane
        GridPane gridPane = new GridPane();
        reservationScrollPane.setContent(gridPane);

        // Add reservations to the reservationScrollPane
        for (Reservation reservation : reservations) {
            addReservationToScrollPane(reservation);
        }
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
            Reservation newReservation = new Reservation(selectedrestaurant_id, reservationNom, reservationEmail, reservationDate, reservationNbrPersonne);

            // Add reservation to the database
            serviceReservation.ajouter(newReservation);

            // Send email to the user
            sendEmail(reservationEmail, newReservation);
            showNotification("Reservation Added", "Reservation has been added successfully!");


            // Refresh display by fetching and displaying all reservations again
            displayReservations();

        } catch (SQLException | NumberFormatException e) {
            e.printStackTrace();
            // Handle SQLException or NumberFormatException
        }
    }
    private void showNotification(String title, String message) {
        Notifications.create()
                .title(title)
                .text(message)
                .hideAfter(Duration.seconds(5))  // Set the duration for how long the notification should be displayed
                .position(Pos.BOTTOM_RIGHT)
                .show();
    }
    private void sendEmail(String recipientEmail, Reservation reservation) {
        // Sender's email address and password
        final String senderEmail = "majdzari2@gmail.com";
        final String password = "uqagqqooyxceanti";

        // SMTP server configuration
        final String smtpHost = "smtp.gmail.com"; // Assuming you're using Gmail SMTP
        final String smtpPort = "587"; // Gmail SMTP port

        // Email content
        String subject = "Your Reservation Details";
        String body = "Dear " + reservation.getNom() + ",\n\n"
                + "Thank you for making a reservation with us.\n"
                + "Here are the details of your reservation:\n\n"
                + "Date: " + reservation.getDate() + "\n"
                + "Nombre Personne: " + reservation.getNbrPersonne() + "\n"
                + "Restaurant: " + selectedRestaurantName + "\n\n"
                + "We look forward to seeing you!\n\n"
                + "Best regards,\n"
                + "Your Restaurant Team";

        // Set properties for SMTP server
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", smtpHost);
        props.put("mail.smtp.port", smtpPort);

        // Create a session with authentication
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, password);
            }
        });

        try {
            // Create a message
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(senderEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject(subject);
            message.setText(body);

            // Send the message
            Transport.send(message);

            System.out.println("Email sent successfully to " + recipientEmail);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    // Import Image class from JavaFX

    public void initData(int restaurant_id, String restaurantName) {
        this.selectedrestaurant_id = restaurant_id;
        this.selectedRestaurantName = restaurantName;
        restaurantLabel.setText(selectedRestaurantName);
        try {
            // Display existing reservations when initializing the controller
            displayReservations();

            // Get the image path for the selected restaurant from your data source
            String imagePath = serviceReservation.getRestaurantImage(selectedrestaurant_id);
            if (imagePath != null && !imagePath.isEmpty()) {
                // Load and set the image of the selected restaurant
                try {
                    // Load and set the image of the selected restaurant
                    Image image = new Image(new File(imagePath).toURI().toString());
                    imagerest.setImage(image);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {
                // If no image path is found, display a default image or hide the ImageView
                // For example, you can set a default image like this:
                // Image defaultImage = new Image("default_image_path/default.png");
                // imagerest.setImage(defaultImage);
                // Or you can hide the ImageView if no image is available:
                // imagerest.setVisible(false);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
