package controllers;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import entities.Event;
import entities.ReservationEvent;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import services.ServiceEvent;
import services.ServiceReservationEvent;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class ReservationBackController {
    private PdfPTable eventTable;

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
    private TableColumn<ReservationEvent, LocalDate> dateReservationColumn;

    @FXML
    private TextField nomRechercheTextField;

    @FXML
    private TableView<ReservationEvent> reservationTableView;

    @FXML
    private TableColumn<ReservationEvent, Void> pdfReservation;

    private final ServiceReservationEvent serviceReservationEvent;
    private final ServiceEvent serviceEvent;

    {
        try {
            serviceReservationEvent = new ServiceReservationEvent();
            serviceEvent = new ServiceEvent();
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

        // Ajouter le bouton "Télécharger le reçu" dans la colonne "Reçu"
        pdfReservation.setCellFactory(column -> new TableCell<>() {
            private final Button downloadButton = new Button("Télécharger");

            {
                downloadButton.setOnAction(event -> {
                    ReservationEvent reservation = getTableView().getItems().get(getIndex());
                    genererEtTelechargerPDFReservation(reservation);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(downloadButton);
                }
            }
        });

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
    private void filtrerReservationParDate(LocalDate date) {
        List<ReservationEvent> reservations = serviceReservationEvent.filtrerParDate(date);
        reservationTableView.getItems().clear();
        reservationTableView.getItems().addAll(reservations);
    }

    // Méthode pour générer et télécharger le PDF du reçu de la réservation
    // Méthode pour générer et télécharger le PDF du reçu de la réservation
    private void genererEtTelechargerPDFReservation(ReservationEvent reservation) {
        try {
            // Récupérer l'événement correspondant à la réservation
            Event event = serviceEvent.getEventById(reservation.getIdEvent());
            if (event != null) {
                // Générer le nom du fichier PDF avec "recu_reservation" suivi de l'ID de la réservation
                String fileName = "recu_reservation_" + reservation.getId() + ".pdf";

                // Créer le contenu PDF avec iText
                Document document = new Document();
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Enregistrer le reçu de réservation");
                fileChooser.setInitialFileName(fileName);
                FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Fichiers PDF (*.pdf)", "*.pdf");
                fileChooser.getExtensionFilters().add(extFilter);
                File selectedFile = fileChooser.showSaveDialog(null);

                if (selectedFile != null) {
                    PdfWriter.getInstance(document, new FileOutputStream(selectedFile));
                    document.open();

                    // Titre du document
                    Paragraph title = new Paragraph("Reçu de Réservation", new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 24, com.itextpdf.text.Font.BOLD, BaseColor.BLUE));
                    title.setAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
                    document.add(title);

                    // Ajouter l'image de l'événement s'il y en a une
                    if (event.getImageEvent() != null && !event.getImageEvent().isEmpty()) {
                        // Créer l'objet Image avec l'URL de l'image
                        URL imageURL = new URL(event.getImageEvent());
                        Image img = Image.getInstance(imageURL);
                        // Redimensionner l'image
                        img.scaleToFit(200, 200);
                        img.setAlignment(Image.ALIGN_CENTER);
                        document.add(img);

                        // Espacement après l'image
                        document.add(new Paragraph("\n"));
                    }

                    // Informations sur l'événement
                    document.add(new Paragraph("Informations sur l'événement :", new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 18, com.itextpdf.text.Font.BOLD)));
                    document.add(new Paragraph(""));

                    document.add(new Paragraph("Nom de l'événement : " + event.getTitre()));
                    document.add(new Paragraph("Date de l'événement : " + event.getDateDebut().toString()));
                    document.add(new Paragraph("Lieu de l'événement : " + event.getLieu()));
                    document.add(new Paragraph(""));

                    // Informations sur la réservation
                    document.add(new Paragraph("Informations sur la réservation :", new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 18, com.itextpdf.text.Font.BOLD)));
                    document.add(new Paragraph(""));

                    document.add(new Paragraph("Nom du réservant : " + reservation.getNom()));
                    document.add(new Paragraph("Email du réservant : " + reservation.getEmail()));
                    document.add(new Paragraph("Numéro de téléphone du réservant : " + reservation.getNumTel()));
                    document.add(new Paragraph("Date de réservation : " + reservation.getDateReservation().toString()));

                    // Ajouter le logo de votre application en bas de la page
                    URL logoURL = getClass().getResource("/images/TourTravelBusinessLogo.png");
                    if (logoURL != null) {
                        Image logo = Image.getInstance(logoURL);
                        logo.scaleToFit(100, 100); // Ajuster la taille du logo si nécessaire
                        logo.setAlignment(Image.ALIGN_CENTER);
                        document.add(logo);
                    }

                    // Ajouter un espacement pour déplacer le logo vers le bas
                    document.add(new Paragraph("\n"));

                    document.close();
                    System.out.println("PDF généré avec succès !");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    @FXML
    public void dashboardCategories(ActionEvent event) {
        loadView("/BackCategory.fxml", event);
    }

    @FXML
    public void dashboardReservations(ActionEvent event) {
        loadView("/ReservationBack.fxml", event);
    }

    @FXML
    public void dashboardEvents(ActionEvent event) {
        loadView("/BackEvent.fxml", event);
    }

    @FXML
    public void homeDashboard(ActionEvent event) {
        loadView("/HomeBack.fxml", event);
    }

    private void loadView(String viewPath, ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(viewPath));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
