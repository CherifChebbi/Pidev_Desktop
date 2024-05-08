package controllers;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import models.Event;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import services.ServiceEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import java.awt.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class FrontController {

    @FXML
    private ScrollPane eventScrollPane;

    @FXML
    private TextField rechEventFront;

    @FXML
    private ChoiceBox<String> choiceFiltreEvent;

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
        // Configurez votre ChoiceBox avec les options de filtrage
        choiceFiltreEvent.getItems().addAll(
                "Prix croissant", "Prix décroissant", "Date la plus proche", "A-Z", "Z-A"
        );

        // Ajoutez un gestionnaire d'événements pour réagir aux sélections de l'utilisateur
        choiceFiltreEvent.setOnAction(this::filtrerEvenements);

        // Affichez tous les événements initialement
        afficherEvenements(serviceEvent.afficher());

        // Ajoutez une marge supérieure et des marges sur les côtés pour le ScrollPane
        eventScrollPane.setPadding(new Insets(20, 20, 20, 20));
    }

    private void afficherEvenements(List<Event> events) {
        VBox eventVBox = new VBox(); // Utilisez un VBox pour stocker les événements
        eventVBox.setSpacing(20); // Espacement vertical entre les conteneurs d'événements

        HBox eventLine = new HBox(); // Utilisez un HBox pour chaque ligne d'événements
        eventLine.setSpacing(20); // Espacement horizontal entre les conteneurs d'événements

        for (int i = 0; i < events.size(); i++) {
            Event event = events.get(i);

            ImageView imageView = createEventView(event);
            imageView.setFitWidth(200); // Largeur de l'image
            imageView.setFitHeight(180); // Hauteur de l'image
            Label titleLabel = new Label("Titre: " + event.getTitre());
            Label descriptionLabel = new Label("Description: " + event.getDescription());
            Label dateLabel = new Label("Date: " + event.getDateDebut());
            Label priceLabel = new Label("Prix: " + event.getPrix());
            Button reserveButton = new Button("Réserver");
            reserveButton.setStyle("-fx-background-color: #FF0000;");
            reserveButton.setOnAction(this::reserveButtonClicked);
            reserveButton.setUserData(event);

            VBox eventDetails = new VBox(); // Utilisez VBox pour aligner les éléments verticalement
            eventDetails.getChildren().addAll(titleLabel, descriptionLabel, dateLabel, priceLabel, reserveButton);
            eventDetails.setSpacing(10);
            eventDetails.setAlignment(Pos.CENTER);

            VBox container = new VBox();
            container.getChildren().addAll(imageView, eventDetails);
            container.setSpacing(50); // Ajouter un espacement vertical entre chaque image
            container.setAlignment(Pos.CENTER);

            eventLine.getChildren().add(container); // Ajoutez chaque conteneur d'événement à la ligne

            // Si nous avons atteint le nombre maximal d'événements par ligne ou si c'est le dernier événement, ajoutez la ligne actuelle à eventVBox
            if ((i + 1) % 4 == 0 || i == events.size() - 1) {
                eventVBox.getChildren().add(eventLine); // Ajoutez la ligne d'événements à eventVBox
                eventLine = new HBox(); // Créez une nouvelle ligne pour les événements suivants
                eventLine.setSpacing(20); // Réinitialisez l'espacement horizontal pour la nouvelle ligne
            }
        }

        eventScrollPane.setContent(eventVBox); // Définissez le contenu du ScrollPane comme le VBox contenant tous les événements
    }





    private ImageView createEventView(Event event) {
        ImageView imageView = new ImageView();
        Image image = new Image(event.getImageEvent());
        imageView.setImage(image);

        // Ajoutez une marge autour de l'imageView
        StackPane stackPane = new StackPane(imageView);
        stackPane.setPadding(new Insets(5)); // Définissez la marge sur le StackPane
        return imageView;
    }

    private void filtrerEvenements(ActionEvent event) {
        // Obtenez le critère de filtrage sélectionné
        String filtreSelectionne = choiceFiltreEvent.getValue();

        // Vérifiez si un filtre est sélectionné
        if (filtreSelectionne != null) {
            // Appelez la méthode de filtrage correspondante dans votre service
            switch (filtreSelectionne) {
                case "Prix croissant":
                    afficherEvenements(serviceEvent.filtrerParPrixCroissant());
                    break;
                case "Prix décroissant":
                    afficherEvenements(serviceEvent.filtrerParPrixDécroissant());
                    break;
                case "Date la plus proche":
                    afficherEvenements(serviceEvent.filtrerParDateLaPlusProche());
                    break;
                case "A-Z":
                    afficherEvenements(serviceEvent.filtrerParTitreAZ());
                    break;
                case "Z-A":
                    afficherEvenements(serviceEvent.filtrerParTitreZA());
                    break;
                default:
                    break;
            }
        } else {
            // Si aucun filtre n'est sélectionné, affichez tous les événements
            afficherEvenements(serviceEvent.afficher());
        }
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

    @FXML
    public void rechercherEventFront(ActionEvent actionEvent) {
        // Récupérez le texte saisi dans le TextField
        String titreRecherche = rechEventFront.getText();

        // Si le champ de recherche est vide, affichez tous les événements
        if (titreRecherche.isEmpty()) {
            afficherEvenements(serviceEvent.afficher());
        } else {
            // Sinon, appelez la méthode de recherche par titre dans votre service
            List<Event> events = serviceEvent.rechercherParTitre(titreRecherche);

            // Affichez les événements correspondants
            afficherEvenements(events);
        }
    }
}
