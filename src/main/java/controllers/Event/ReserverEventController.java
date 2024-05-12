package controllers.Event;

import controllers.ReservationEvent.PaiementController;
import models.Event;
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
import java.time.LocalDate;

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
        prixevent.setText(String.valueOf(event.getPrix())); // Afficher le prix de l'événement
        Image image = new Image(event.getImageEvent());
        imgRes.setImage(image);
    }

    @FXML
    private void validerReservEvent(ActionEvent actionEvent) {
        String nom = nomField.getText();
        String telephone = telephoneField.getText();
        String email = emailField.getText();
        LocalDate dateReservation = datePicker.getValue();

        if (nom.isEmpty() || !nom.matches("[a-zA-Z\\s]+")) {
            showAlert("Erreur de saisie", "Veuillez saisir un nom valide (lettres seulement).");
            return;
        }

        if (telephone.length() != 8 || !telephone.matches("\\d+")) {
            showAlert("Erreur de saisie", "Veuillez saisir un numéro de téléphone valide (10 chiffres).");
            return;
        }

        if (!email.contains("@")) {
            showAlert("Erreur de saisie", "Veuillez saisir une adresse email valide.");
            return;
        }

        try {
            // Charger le fichier FXML de la page de paiement
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ReservationEvent/Paiement.fxml"));
            Parent root = loader.load();

            // Récupérer la scène de la fenêtre actuelle
            Scene scene = nomField.getScene();

            // Remplacer la racine de la scène par la nouvelle vue chargée
            scene.setRoot(root);

            // Initialiser les données du contrôleur de la nouvelle vue
            PaiementController paiementController = loader.getController();
            ServiceReservationEvent serviceReservationEvent = new ServiceReservationEvent(); // Initialisez ServiceReservationEvent
            paiementController.setServiceReservationEvent(serviceReservationEvent); // Injectez l'instance dans PaiementController
            paiementController.initData(event, nom, email, telephone, dateReservation);
        } catch (IOException | SQLException ex) {
            ex.printStackTrace();
        }
    }


    @FXML
    void retourFront(ActionEvent actionEvent) {
        try {
            // Charger le fichier FXML de la page front des événements
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Event/indexEvent.fxml"));
            Parent root = loader.load();

            // Récupérer la scène de la fenêtre actuelle
            Scene scene = nomField.getScene();

            // Remplacer la racine de la scène par la nouvelle vue chargée
            scene.setRoot(root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    public void initialize() {
        // Définir la date du DatePicker sur la date d'aujourd'hui
        datePicker.setValue(LocalDate.now());
    }
}
