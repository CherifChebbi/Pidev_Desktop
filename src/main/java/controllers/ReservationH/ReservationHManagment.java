package controllers.ReservationH;

import models.ReservationH;
import services.ServiceReservation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Optional;

import javafx.stage.Stage;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import javafx.stage.FileChooser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;




public class ReservationHManagment {

    @FXML
    private TableView<ReservationH> afficher;

    @FXML
    private TextField nom;

    @FXML
    private TextField email;

    @FXML
    private DatePicker date;

    @FXML
    private TextField nbr;
    @FXML
    private Button excel;
    private ObservableList<ReservationH> reservationHS = FXCollections.observableArrayList();

    private ServiceReservation SR = new ServiceReservation();


    // Other methods...

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
        reservationHS.clear();
        reservationHS.addAll(SR.afficher());
        afficher.setItems(reservationHS);
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
                ReservationH reservationH = new ReservationH(70, i, y, x, z);
                SR.ajouter(reservationH);

                // Send email
                sendEmail(y, reservationH); // Pass the email address and reservation object to the sendEmail method

                // Add the new reservation to the TableView
                afficher.getItems().add(reservationH);

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


    private void sendEmail(String recipientEmail, ReservationH reservationH) {
        // Sender's email address and password
        final String senderEmail = "nsiriaziz009@gmail.com";
        final String senderPassword = "uoqpcqvcrrxqnbce";

        // SMTP server configuration
        final String smtpHost = "smtp.gmail.com"; // Change this to your SMTP server
        final String smtpPort = "587"; // Change this to the SMTP port

        // Email content
        String subject = "Confirmation de réservation";
        String body = "Bonjour " + reservationH.getNom() + ",\n\n"
                + "Votre réservation a été effectuée avec succès.\n"
                + "Date: " + reservationH.getDate() + "\n"
                + "Nombre de personnes: " + reservationH.getNbrPersonne() + "\n\n"
                + "Cordialement,\n"
                + "Votre équipe de réservation";

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
                return new PasswordAuthentication(senderEmail, senderPassword);
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
            e.printStackTrace();
        }
    }



    @FXML
    void modifier(ActionEvent event) throws SQLException {
        ReservationH selectedReservationH = afficher.getSelectionModel().getSelectedItem();
        if (selectedReservationH != null) {
            // Show a dialog or form to edit the reservation details
            // After editing, update the reservation in the database
            SR.modifier(selectedReservationH); // You need to implement the modifier method in ServiceReservation
            // Refresh the TableView
            afficher();
        } else {
            System.out.println("Veuillez sélectionner une réservation à modifier.");
        }
    }

    @FXML
    void supprimer(ActionEvent event) throws SQLException {
        ReservationH selectedReservationH = afficher.getSelectionModel().getSelectedItem();
        if (selectedReservationH != null) {
            // Show a confirmation dialog
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText("Supprimer la réservation?");
            alert.setContentText("Êtes-vous sûr de vouloir supprimer cette réservation?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                // User confirmed, delete the reservation
                SR.supprimer(selectedReservationH.getId()); // You need to implement the supprimer method in ServiceReservation
                reservationHS.remove(selectedReservationH);
                // Refresh the TableView
                afficher();
            }
        } else {
            System.out.println("Veuillez sélectionner une réservation à supprimer.");
        }
    }
    @FXML
    void generatePDF(ActionEvent event) {
        try {
            // Create a new PDF document
            PDDocument document = new PDDocument();

            // Add a page to the document
            PDPage page = new PDPage();
            document.addPage(page);

            // Create a content stream for the page
            PDPageContentStream contentStream = new PDPageContentStream(document, page);

            // Set font and font size
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);

            // Define the starting y-coordinate for the content
            float y = page.getMediaBox().getHeight() - 50;

            // Write header row
            contentStream.beginText();
            contentStream.newLineAtOffset(50, y);
            contentStream.showText("Nom");
            contentStream.newLineAtOffset(100, 0);
            contentStream.showText("Email");
            contentStream.newLineAtOffset(100, 0);
            contentStream.showText("Date");
            contentStream.newLineAtOffset(100, 0);
            contentStream.showText("Nombre de personnes");
            contentStream.endText();

            // Set font and font size for data rows
            contentStream.setFont(PDType1Font.HELVETICA, 12);

            // Write data rows
            y -= 20;
            for (ReservationH reservationH : reservationHS) {
                contentStream.beginText();
                contentStream.newLineAtOffset(50, y);
                contentStream.showText(reservationH.getNom());
                contentStream.newLineAtOffset(100, 0);
                contentStream.showText(reservationH.getEmail());
                contentStream.newLineAtOffset(100, 0);
                contentStream.showText(reservationH.getDate().toString());
                contentStream.newLineAtOffset(100, 0);
                contentStream.showText(Integer.toString(reservationH.getNbrPersonne()));
                contentStream.endText();
                y -= 20;
            }

            // Close content stream
            contentStream.close();

            // Create a file chooser dialog
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save PDF File");
            fileChooser.setInitialFileName("reservations.pdf");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));

            // Get the stage to show the file chooser dialog
            Stage stage = (Stage) excel.getScene().getWindow();
            // Show the save file dialog
            java.io.File file = fileChooser.showSaveDialog(stage);

            if (file != null) {
                // Save the PDF document to the selected file
                document.save(file);
                document.close();
                System.out.println("PDF file generated successfully.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void generateExcel(ActionEvent event) {
        // Create a new workbook
        try (Workbook workbook = new XSSFWorkbook()) {
            // Create a new sheet
            Sheet sheet = workbook.createSheet("Reservations");

            // Create header row
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("Nom");
            headerRow.createCell(1).setCellValue("Email");
            headerRow.createCell(2).setCellValue("Date");
            headerRow.createCell(3).setCellValue("Nombre de personnes");

            // Populate data rows
            int rowNum = 1;
            for (ReservationH reservationH : reservationHS) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(reservationH.getNom());
                row.createCell(1).setCellValue(reservationH.getEmail());
                row.createCell(2).setCellValue(reservationH.getDate().toString());
                row.createCell(3).setCellValue(reservationH.getNbrPersonne());
            }

            // Create a file chooser dialog
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save Excel File");
            fileChooser.setInitialFileName("reservations.xlsx");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel Files", "*.xlsx"));

            // Get the stage to show the file chooser dialog
            Stage stage = (Stage) excel.getScene().getWindow();
            // Show the save file dialog
              java.io.File file = fileChooser.showSaveDialog(stage);

            if (file != null) {
                // Write the workbook content to the selected file
                try (FileOutputStream fileOut = new FileOutputStream(file)) {
                    workbook.write(fileOut);
                    System.out.println("Excel file generated successfully.");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void categoryback(ActionEvent event) {
    }

    public void heberback(ActionEvent event) {
    }
}
