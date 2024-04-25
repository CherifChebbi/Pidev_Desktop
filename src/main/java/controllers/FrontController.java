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

        int columnCount = 3;
        int rowCount = (int) Math.ceil((double) events.size() / columnCount); // Calculer le nombre de lignes nécessaires

        for (int i = 0; i < rowCount; i++) {
            for (int j = 0; j < columnCount; j++) {
                int index = i * columnCount + j;
                if (index < events.size()) {
                    Event event = events.get(index);
                    ImageView imageView = createEventView(event);
                    Label titleLabel = new Label(event.getTitre());
                    Label priceLabel = new Label("Prix : " + event.getPrix());
                    Button reserveButton = new Button("Réserver");
                    reserveButton.setOnAction(this::reserveButtonClicked);
                    reserveButton.setUserData(event);

                    VBox eventDetails = new VBox(); // Utilisez VBox pour aligner les éléments verticalement
                    eventDetails.getChildren().addAll(titleLabel, priceLabel, reserveButton);
                    eventDetails.setSpacing(10);
                    eventDetails.setPadding(new Insets(10)); // Ajoutez un remplissage aux détails de l'événement
                    eventDetails.setAlignment(javafx.geometry.Pos.CENTER); // Alignez les éléments au centre horizontalement

                    // Ajustez la largeur préférée de chaque élément pour occuper environ un tiers de la largeur totale du FlowPane
                    imageView.setFitWidth(150);
                    imageView.setFitHeight(150);
                    eventDetails.setPrefWidth(150);

                    // Créez un conteneur pour chaque élément (image et détails de l'événement) et ajoutez-le au FlowPane
                    HBox eventContainer = new HBox(imageView, eventDetails);
                    eventContainer.setAlignment(Pos.TOP_LEFT); // Alignez le conteneur en haut à gauche
                    eventContainer.setSpacing(10); // Ajoutez un espacement horizontal entre l'image et les détails de l'événement

                    eventFlowPane.getChildren().add(eventContainer);
                }
            }
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
