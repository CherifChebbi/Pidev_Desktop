package controllers.ReservationEvent;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import models.Event;
import models.ReservationEvent;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import services.ServiceEvent;
import services.ServiceReservationEvent;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
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
    private TableColumn<ReservationEvent, String> eventColumn;
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

        // Modification de la liaison pour afficher le nom de l'événement
        eventColumn.setCellValueFactory(cellData -> {
            int eventId = cellData.getValue().getIdEvent();
            Event event = serviceEvent.getEventById(eventId);
            return new SimpleStringProperty(event != null ? event.getTitre() : "");
        });

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
    private void genererEtTelechargerPDFReservation(ReservationEvent reservation) {
        try {
            // Récupérer l'événement correspondant à la réservation
            Event event = serviceEvent.getEventById(reservation.getIdEvent());
            if (event != null) {
                // Générer le nom du fichier PDF avec "recu_reservation" suivi de l'ID de la réservation
                String fileName = "recu_reservation_" + reservation.getId() + ".pdf";

                // Créer le contenu PDF avec iText
                Document document = new Document(PageSize.A4, 50, 50, 50, 50); // Ajouter des marges
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Enregistrer le reçu de réservation");
                fileChooser.setInitialFileName(fileName);
                FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Fichiers PDF (*.pdf)", "*.pdf");
                fileChooser.getExtensionFilters().add(extFilter);
                File selectedFile = fileChooser.showSaveDialog(null);

                if (selectedFile != null) {
                    PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(selectedFile));
                    writer.setBoxSize("art", new Rectangle(36, 54, 559, 788)); // Définir la zone d'impression

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

                    // Informations sur l'événement et la réservation sous forme de tableau
                    PdfPTable table = new PdfPTable(2); // 2 colonnes
                    table.setWidthPercentage(100); // Largeur du tableau en pourcentage de la page
                    table.setSpacingBefore(10f); // Espacement avant le tableau
                    table.setSpacingAfter(10f); // Espacement après le tableau

                    // Ajouter les cellules du tableau avec les informations sur l'événement
                    PdfPCell cell = new PdfPCell(new Paragraph("Informations sur l'événement", new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 18, com.itextpdf.text.Font.BOLD)));
                    cell.setColspan(2); // Fusionner les deux colonnes
                    cell.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
                    cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                    table.addCell(cell);

                    table.addCell("Titre de l'événement");
                    table.addCell(event.getTitre());

                    table.addCell("Date de l'événement");
                    table.addCell(event.getDateDebut().toString());

                    table.addCell("Lieu de l'événement");
                    table.addCell(event.getLieu());

                    table.addCell("Prix de l'événement");
                    table.addCell(String.valueOf(event.getPrix())); // Ajout du prix de l'événement

                    // Ajouter les cellules du tableau avec les informations sur la réservation
                    cell = new PdfPCell(new Paragraph("Informations sur la réservation", new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 18, com.itextpdf.text.Font.BOLD)));
                    cell.setColspan(2); // Fusionner les deux colonnes
                    cell.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
                    cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                    table.addCell(cell);

                    table.addCell("Nom du réservant");
                    table.addCell(reservation.getNom());

                    table.addCell("Email du client");
                    table.addCell(reservation.getEmail());

                    table.addCell("Numéro de téléphone");
                    table.addCell(reservation.getNumTel());

                    table.addCell("Date de réservation");
                    table.addCell(reservation.getDateReservation().toString());

                    document.add(table);

                    // Ajouter le QR code contenant l'ID de réservation avec une taille de 150x150
                    BarcodeQRCode qrCode = new BarcodeQRCode(String.valueOf(reservation.getId()), 150, 150, null);
                    Image qrCodeImage = qrCode.getImage();
                    qrCodeImage.setAlignment(Element.ALIGN_RIGHT); // Aligner à droite
                    document.add(qrCodeImage);

                    // Ajouter une phrase personnalisée sous le tableau
                    Paragraph thankYou = new Paragraph("Merci pour votre réservation et votre confiance.");
                    thankYou.setAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
                    document.add(thankYou);

                    // Ajouter le logo de votre application en bas de la page
                    URL logoURL = getClass().getResource("/images/TourTravelBusinessLogo.png");
                    if (logoURL != null) {
                        Image logo = Image.getInstance(logoURL);
                        logo.scaleToFit(100, 100); // Ajuster la taille du logo si nécessaire
                        logo.setAlignment(Image.ALIGN_CENTER);
                        document.add(logo);
                    }

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
        loadView("/CategoryEvent/BackCategory.fxml", event);
    }

    @FXML
    public void dashboardReservations(ActionEvent event) {
        loadView("/ReservationEvent/ReservationBack.fxml", event);
    }

    @FXML
    public void dashboardEvents(ActionEvent event) {
        loadView("/Event/BackEvent.fxml", event);
    }

    @FXML
    public void homeDashboard(ActionEvent event) {
        loadView("/Event/HomeBack.fxml", event);
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

    public void frontEvent(ActionEvent actionEvent) {
        try {
            // Charger le fichier FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Event/indexEvent.fxml"));
            Parent root = loader.load();

            // Créer une nouvelle scène
            Scene scene = new Scene(root);

            // Obtenir la fenêtre actuelle
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();

            // Définir la scène sur la fenêtre
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            // Gérer les erreurs de chargement du FXML
        }
    }
}
