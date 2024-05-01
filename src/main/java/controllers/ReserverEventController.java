package controllers;

import entities.Event;
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

import java.io.IOException;
import java.util.Date;

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
        // Récupérer le FXMLLoader
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Paiement.fxml"));
        Parent root;
        try {
            // Charger la page de paiement
            root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));

            // Passer les données de l'événement au contrôleur de paiement
            PaiementController paiementController = loader.getController();
            paiementController.initData(event);

            // Afficher la fenêtre de paiement
            stage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
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
