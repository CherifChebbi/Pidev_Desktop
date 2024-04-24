package controllers;

import entities.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import services.ServiceEvent;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;
import javafx.scene.Scene;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class FrontController {

    @FXML
    private GridPane eventGrid;

    @FXML
    private ScrollPane scrollPane;

    private ServiceEvent serviceEvent;

    public FrontController() {
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
            Button reserveButton = new Button("Réserver");
            reserveButton.setOnAction(this::reserveButtonClicked);
            reserveButton.setUserData(event); // Attribuer l'événement au bouton pour le récupérer plus tard lors du clic

            eventGrid.add(imageView, column, row);
            eventGrid.add(reserveButton, column, row + 1);

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

    @FXML
    public void reserveButtonClicked(ActionEvent event) {
        Button buttonClicked = (Button) event.getSource();
        Event selectedEvent = (Event) buttonClicked.getUserData(); // Récupérer l'événement correspondant au bouton cliqué
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ReserverEvent.fxml"));
        Parent root;
        try {
            root = loader.load();
            ReserverEventController controller = loader.getController();
            controller.initData(selectedEvent); // Passer les données de l'événement au contrôleur de la page de réservation
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}
