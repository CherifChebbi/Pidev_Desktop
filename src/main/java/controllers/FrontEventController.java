package controllers;

import entities.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import services.ServiceEvent;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class FrontEventController implements Initializable {

    @FXML
    private VBox eventContainer;

    private ServiceEvent serviceEvent;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        serviceEvent = new ServiceEvent();
        afficherEvenements();
    }

    private void afficherEvenements() {
        List<Event> events = serviceEvent.afficher();
        for (Event event : events) {
            HBox eventBox = new HBox();
            eventBox.getStyleClass().add("event-box");

            Label titleLabel = new Label(event.getTitre());
            titleLabel.getStyleClass().add("event-title");

            Label descriptionLabel = new Label(event.getDescription());
            descriptionLabel.getStyleClass().add("event-description");

            Label dateDebutLabel = new Label("Début: " + event.getDateDebut());
            dateDebutLabel.getStyleClass().add("event-date");

            Label dateFinLabel = new Label("Fin: " + event.getDateFin());
            dateFinLabel.getStyleClass().add("event-date");

            Label lieuLabel = new Label("Lieu: " + event.getLieu());
            lieuLabel.getStyleClass().add("event-location");

            Label prixLabel = new Label("Prix: " + event.getPrix());
            prixLabel.getStyleClass().add("event-price");

            // Chargement de l'image
            ImageView imageView = new ImageView();
            File imageFile = new File(event.getImageEvent());
            if (imageFile.exists()) {
                String imageUrl = imageFile.toURI().toString();
                Image image = new Image(imageUrl);
                imageView.setImage(image);
                imageView.setFitWidth(100); // Ajustez la largeur de l'image selon vos besoins
                imageView.setPreserveRatio(true);
            } else {
                System.out.println("Fichier image non trouvé: " + event.getImageEvent());
            }

            Button reserveButton = new Button("Réserver");
            reserveButton.getStyleClass().add("reserve-button");
            reserveButton.setOnAction(e -> reserverEvent(event.getId()));

            eventBox.getChildren().addAll(titleLabel, descriptionLabel, dateDebutLabel, dateFinLabel, lieuLabel, prixLabel, imageView, reserveButton);
            eventContainer.getChildren().add(eventBox);
        }
    }

    private void reserverEvent(int eventId) {
        // Rediriger vers la page de réservation avec l'ID de l'événement
        // Vous pouvez implémenter cette fonctionnalité ici
        System.out.println("Réserver l'événement avec l'ID: " + eventId);
    }
}
