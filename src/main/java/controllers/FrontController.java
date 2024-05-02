package controllers;

import entities.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import services.ServiceEvent;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class FrontController {

    @FXML
    private FlowPane eventFlowPane;

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

        for (Event event : events) {
            ImageView imageView = createEventView(event);
            Label titleLabel = new Label("Titre: " + event.getTitre());
            Label descriptionLabel = new Label("Description: " + event.getDescription());
            Label dateLabel = new Label("Date: " + event.getDateDebut());
            Label priceLabel = new Label("Prix: " + event.getPrix());
            Button reserveButton = new Button("Réserver");
            reserveButton.setOnAction(this::reserveButtonClicked);
            reserveButton.setUserData(event);

            VBox eventDetails = new VBox(); // Utilisez VBox pour aligner les éléments verticalement
            eventDetails.getChildren().addAll(titleLabel, descriptionLabel, dateLabel, priceLabel, reserveButton);
            eventDetails.setSpacing(10);
            eventDetails.setAlignment(Pos.CENTER);

            VBox container = new VBox();
            container.getChildren().addAll(imageView, eventDetails);
            container.setSpacing(10);
            container.setAlignment(Pos.CENTER);

            HBox eventContainer = new HBox();
            eventContainer.getChildren().add(container);
            eventContainer.setAlignment(Pos.CENTER);
            eventContainer.setPadding(new Insets(10));

            eventFlowPane.getChildren().add(eventContainer);
        }
    }


    private ImageView createEventView(Event event) {
        ImageView imageView = new ImageView();
        Image image = new Image(event.getImageEvent());
        imageView.setImage(image);
        imageView.setFitWidth(150);
        imageView.setFitHeight(150);

        return imageView;
    }

    @FXML
    public void reserveButtonClicked(ActionEvent event) {
        Button buttonClicked = (Button) event.getSource();
        Event selectedEvent = (Event) buttonClicked.getUserData();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ReserverEvent.fxml"));
        Parent root;
        try {
            root = loader.load();
            ReserverEventController controller = loader.getController();
            controller.initData(selectedEvent);
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
