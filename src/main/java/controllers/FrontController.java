package controllers;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
    private FlowPane eventFlowPane;

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

        // Ajoutez de l'espace autour de chaque événement dans le FlowPane
        eventFlowPane.setPadding(new Insets(20)); // Ajoutez 20 pixels d'espace autour de chaque événement
        eventFlowPane.setHgap(10); // Espacement horizontal entre les images
        eventFlowPane.setVgap(10); // Espacement vertical entre les images
        eventFlowPane.setAlignment(Pos.CENTER); // Centrer les événements dans le FlowPane

        // Calculez la largeur préférée à laquelle le FlowPane doit retourner à la ligne
        int preferredWidth = (int) (4 * 220 + 40); // 4 événements par ligne, 220 est la largeur des images + les marges
        eventFlowPane.setPrefWrapLength(preferredWidth);
    }



    private void afficherEvenements(List<Event> events) {
        eventFlowPane.getChildren().clear(); // Effacez les événements actuellement affichés

        for (Event event : events) {
            ImageView imageView = createEventView(event);
            imageView.setFitWidth(200); // Largeur de l'image
            imageView.setFitHeight(180); // Hauteur de l'image
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

            // Créer un rectangle noir pour le cadre autour de chaque événement
            Rectangle rectangle = new Rectangle();
            rectangle.setWidth(220); // Largeur du rectangle (ajustez selon vos besoins)
            rectangle.setHeight(250); // Hauteur du rectangle (ajustez selon vos besoins)
            rectangle.setFill(Color.ORANGERED); // Couleur du rectangle (noir)
            rectangle.setOpacity(0.5); // Opacité du rectangle

            // Créer un StackPane pour superposer le rectangle et le contenu de l'événement
            StackPane stackPane = new StackPane();
            stackPane.getChildren().addAll(rectangle, eventContainer);

            // Ajouter le StackPane au FlowPane
            eventFlowPane.getChildren().add(stackPane);
        }
    }

    private ImageView createEventView(Event event) {
        ImageView imageView = new ImageView();
        Image image = new Image(event.getImageEvent());
        imageView.setImage(image);

        // Ajoutez une marge autour de l'imageView
        StackPane stackPane = new StackPane(imageView);
        stackPane.setPadding(new Insets(15)); // Définissez la marge sur le StackPane
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
