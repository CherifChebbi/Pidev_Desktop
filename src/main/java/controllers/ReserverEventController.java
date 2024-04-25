package controllers;

import entities.Event;
import entities.ReservationEvent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import services.ServiceReservationEvent;

import java.io.IOException;
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
            showAlert(Alert.AlertType.WARNING, "Champs manquants", "Veuillez remplir tous les champs.");
        } else if (!isValidName(nom)) { // Vérifier si le nom est valide
            showAlert(Alert.AlertType.WARNING, "Nom invalide", "Veuillez saisir un nom valide (lettres uniquement).");
        } else if (!isValidEmail(email)) { // Vérifier si l'email est valide
            showAlert(Alert.AlertType.WARNING, "Email invalide", "Veuillez saisir une adresse email valide.");
        } else if (!isValidPhoneNumber(telephone)) { // Vérifier si le numéro de téléphone est valide
            showAlert(Alert.AlertType.WARNING, "Numéro de téléphone invalide", "Veuillez saisir un numéro de téléphone valide.");
        } else {
            // Tous les champs sont remplis et valides, enregistrer la réservation dans la base de données
            ReservationEvent reservation = new ReservationEvent(this.eventId, nom, email, telephone, dateReservation);
            ServiceReservationEvent service = null;
            try {
                service = new ServiceReservationEvent();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            service.ajouter(reservation);

            // Afficher une confirmation de réservation
            showAlert(Alert.AlertType.INFORMATION, "Réservation confirmée", "Votre réservation a été confirmée avec succès!");

            // Fermer la fenêtre de réservation
            Stage stage = (Stage) nomField.getScene().getWindow();
            stage.close();
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private boolean isValidName(String name) {
        // Utilisez une expression régulière pour valider que le nom/prénom contient uniquement des lettres
        return name.matches("[a-zA-Z]+");
    }
    private boolean isValidEmail(String email) {
        // Utilisez ici une expression régulière pour valider l'email
        // Cette expression régulière est simple et ne couvre pas tous les cas possibles
        return email.matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}");
    }

    // Méthode pour valider un numéro de téléphone
    private boolean isValidPhoneNumber(String phoneNumber) {
        // Utilisez ici une expression régulière pour valider le numéro de téléphone
        // Cette expression régulière est simple et ne couvre pas tous les cas possibles
        return phoneNumber.matches("\\d{8}"); // Exemple: 1234567890
    }


    @FXML
    void retourFront(ActionEvent actionEvent) {
        // Fermer la fenêtre de réservation
        Stage stage = (Stage) nomField.getScene().getWindow();
        stage.close();

        // Ouvrir la page front des événements
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Front.fxml"));
        Parent root;
        try {
            root = loader.load();
            Stage frontStage = new Stage();
            frontStage.setScene(new Scene(root));
            frontStage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}
