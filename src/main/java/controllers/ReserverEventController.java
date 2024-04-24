package controllers;

import entities.Event;
import entities.ReservationEvent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import services.ServiceReservationEvent;

import java.sql.SQLException;

public class ReserverEventController {

    @FXML
    private DatePicker datePicker;

    @FXML
    private TextField emailField;

    @FXML
    private ImageView imgRes;

    @FXML
    private TextField nomField;

    @FXML
    private TextField telephoneField;

    @FXML
    private Label titreevent;

    @FXML
    private Label descripevent;

    @FXML
    private Label ddebutevent;

    @FXML
    private Label dfinevent;

    @FXML
    private Label lieuevent;

    @FXML
    private Label prixevent;

    private Event event;
    private int eventId;

    public void initData(Event event) {
        this.event = event;
        this.eventId = event.getId(); // Récupérer l'ID de l'événement
        titreevent.setText(event.getTitre());
        descripevent.setText(event.getDescription());
        ddebutevent.setText(event.getDateDebut().toString());
        dfinevent.setText(event.getDateFin().toString());
        lieuevent.setText(event.getLieu());
        prixevent.setText(String.valueOf(event.getPrix()));
        Image image = new Image(event.getImageEvent());
        imgRes.setImage(image);
    }

    @FXML
    void validerReservEvent(ActionEvent event) {
        // Récupérer les données du formulaire
        String nom = nomField.getText();
        String email = emailField.getText();
        String telephone = telephoneField.getText();
        java.util.Date dateReservation = java.sql.Date.valueOf(datePicker.getValue());

        // Vérifier si les champs obligatoires sont vides
        if (nom.isEmpty() || email.isEmpty() || telephone.isEmpty() || datePicker.getValue() == null) {
            // Afficher une alerte pour informer l'utilisateur de remplir tous les champs
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Champs manquants");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez remplir tous les champs.");
            alert.showAndWait();
        } else {
            // Tous les champs sont remplis, enregistrer la réservation dans la base de données
            ReservationEvent reservation = new ReservationEvent(this.eventId, nom, email, telephone, dateReservation);
            ServiceReservationEvent service = null;
            try {
                service = new ServiceReservationEvent();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            service.ajouter(reservation);

            // Afficher une confirmation de réservation
            Alert confirmation = new Alert(Alert.AlertType.INFORMATION);
            confirmation.setTitle("Réservation confirmée");
            confirmation.setHeaderText(null);
            confirmation.setContentText("Votre réservation a été confirmée avec succès!");
            confirmation.showAndWait();

            // Fermer la fenêtre de réservation
            Stage stage = (Stage) nomField.getScene().getWindow();
            stage.close();
        }
    }


    @FXML
    void retourner(ActionEvent event) {
        // Fermer la fenêtre de réservation sans effectuer d'autres actions
        Stage stage = (Stage) nomField.getScene().getWindow();
        stage.close();
    }
}
