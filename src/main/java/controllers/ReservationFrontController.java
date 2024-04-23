package controllers;

import entities.Event;
import entities.ReservationEvent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import services.ServiceEvent;
import services.ServiceReservationEvent;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

public class ReservationFrontController implements Initializable {

    @FXML
    private DatePicker datePicker;

    @FXML
    private TextField emailField;

    @FXML
    private ChoiceBox<String> eventChoiceBox;

    @FXML
    private TextField nomField;

    @FXML
    private TextField telephoneField;

    private final ServiceEvent serviceEvent;

    {
        try {
            serviceEvent = new ServiceEvent();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialiser la ChoiceBox avec les titres des événements
        List<Event> events = serviceEvent.afficher();
        ObservableList<String> eventTitles = FXCollections.observableArrayList();
        for (Event event : events) {
            eventTitles.add(event.getTitre());
        }
        eventChoiceBox.setItems(eventTitles);
    }

    // Constructeur par défaut
    public ReservationFrontController() {
        // Constructeur par défaut nécessaire pour l'instanciation depuis le fichier FXML
    }

    @FXML
    private void validerReservEvent(ActionEvent event) {
        // Récupérer les valeurs des champs
        String nom = nomField.getText();
        String email = emailField.getText();
        String telephone = telephoneField.getText();
        String eventTitle = eventChoiceBox.getValue();
        LocalDate date = datePicker.getValue();

        // Vérifier si tous les champs sont remplis
        if (nom.isEmpty() || email.isEmpty() || telephone.isEmpty() || eventTitle == null || date == null) {
            // Afficher une alerte si des champs sont manquants
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Champs incomplets");
            alert.setContentText("Veuillez remplir tous les champs.");
            alert.showAndWait();
        } else {
            // Récupérer l'ID de l'événement par son titre
            int eventId = serviceEvent.getEventIdByTitle(eventTitle);

            // Créer une instance de la classe ReservationEvent avec les données récupérées
            ReservationEvent reservationEvent = new ReservationEvent(eventId, nom, email, telephone, java.sql.Date.valueOf(date));

            // Enregistrer la réservation dans la base de données en utilisant le service approprié
            ServiceReservationEvent serviceReservationEvent = null;
            try {
                serviceReservationEvent = new ServiceReservationEvent();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            serviceReservationEvent.ajouter(reservationEvent);

            // Afficher un message de succès
            Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
            successAlert.setTitle("Succès");
            successAlert.setHeaderText(null);
            successAlert.setContentText("Réservation enregistrée avec succès.");
            successAlert.showAndWait();

            // Effacer les champs après l'enregistrement
            nomField.clear();
            emailField.clear();
            telephoneField.clear();
            eventChoiceBox.setValue(null);
            datePicker.setValue(null);
        }
    }
}
