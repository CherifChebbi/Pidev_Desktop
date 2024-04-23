package controllers;

import entities.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import services.ServiceEvent;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.List;

public class ReservationFront2Controller {

    @FXML
    private GridPane eventGrid;

    @FXML
    private ScrollPane scrollPane;

    private final ServiceEvent serviceEvent;

    public ReservationFront2Controller() {
        try {
            serviceEvent = new ServiceEvent();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void initialize() {
        List<Event> events = serviceEvent.afficher();

        int col = 0;
        int row = 0;
        for (Event event : events) {
            addEvent(event, row, col);
            col++;
            if (col == 3) {
                col = 0;
                row++;
            }
        }
    }

    private void addEvent(Event event, int row, int col) {
        String imagePath = event.getImageEvent();

        if (imagePath.startsWith("file:/")) {
            imagePath = imagePath.substring(5);
            imagePath = imagePath.replace("/", "\\");
        }

        File file = new File(imagePath);

        try {
            if (file.exists()) {
                FileInputStream inputStream = new FileInputStream(file);
                Image image = new Image(inputStream);
                ImageView imageView = new ImageView(image);
                imageView.setFitWidth(200);
                imageView.setFitHeight(200);

                Label detailsLabel = new Label("Titre : " + event.getTitre() + "\nDescription : " + event.getDescription());

                Button reserveButton = new Button("Réserver");

                eventGrid.add(imageView, col, row);
                eventGrid.add(detailsLabel, col, row + 1);
                eventGrid.add(reserveButton, col, row + 2);

                col++;
                if (col == 3) {
                    col = 0;
                    row++;
                }
            } else {
                System.err.println("Le fichier d'image n'existe pas : " + file.getAbsolutePath());
            }
        } catch (FileNotFoundException e) {
            System.err.println("Erreur lors du chargement de l'image : " + e.getMessage());
            e.printStackTrace();
        }
    }
}
