package controllers;

import entities.Event;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import services.ServiceEvent;

import java.sql.SQLException;
import java.util.List;

public class ReservationFront3Controller {

    @FXML
    private GridPane eventGrid;

    @FXML
    private ScrollPane scrollPane;

    private ServiceEvent serviceEvent;

    public ReservationFront3Controller() {
        try {
            serviceEvent = new ServiceEvent();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void initialize() {
        List<Event> events = serviceEvent.afficher();

        int column = 0;
        int row = 0;

        for (Event event : events) {
            ImageView imageView = createEventView(event);
            GridPane.setMargin(imageView, new Insets(10)); // Ajout de marge autour de l'image
            Label titleLabel = new Label("Titre: " + event.getTitre());
            Label descriptionLabel = new Label("Description: " + event.getDescription());
            Label priceLabel = new Label("Prix: " + event.getPrix());
            Button reserveButton = new Button("Réserver");

            GridPane eventInfoGrid = new GridPane();
            eventInfoGrid.addRow(0, titleLabel);
            eventInfoGrid.addRow(1, descriptionLabel);
            eventInfoGrid.addRow(2, priceLabel);
            eventInfoGrid.addRow(3, reserveButton);
            GridPane.setMargin(eventInfoGrid, new Insets(10, 0, 0, 0)); // Ajout de marge en haut des détails des événements

            eventGrid.add(imageView, column, row);
            eventGrid.add(eventInfoGrid, column, row + 1);

            column++;
            if (column == 3) {
                column = 0;
                row += 2; // Increase row by 2 to leave space for event info
            }
        }
    }

    private ImageView createEventView(Event event) {
        ImageView imageView = new ImageView();
        // Récupérez l'image de l'événement depuis la classe Event et chargez-la dans ImageView
        Image image = new Image(event.getImageEvent());
        imageView.setImage(image);
        // Définissez la taille de l'image
        imageView.setFitWidth(150);
        imageView.setFitHeight(150);
        // Autres configurations de l'ImageView si nécessaire
        // ...

        return imageView;
    }
}
