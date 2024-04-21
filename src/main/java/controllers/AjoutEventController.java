package controllers;

import entities.Category;
import entities.Event;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import services.ServiceCategory;
import services.ServiceEvent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

import java.io.File;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

public class AjoutEventController implements Initializable {

    @FXML
    private ChoiceBox<Category> categorieEvent;

    @FXML
    private TableColumn<Event, Category> colCategEvent;

    @FXML
    private TableColumn<Event, Date> colDebEvent;

    @FXML
    private TableColumn<Event, String> colDescEvent;

    @FXML
    private TableColumn<Event, Date> colFinEvent;

    @FXML
    private TableColumn<Event, String> colLieuEvent;

    @FXML
    private TableColumn<Event, Double> colPrixEvent;

    @FXML
    private TableColumn<Event, String> colTitreEvent;

    @FXML
    private TableColumn<Event, Integer> colidEvent;

    @FXML
    private TableColumn<Event, String> colimgEvent;

    @FXML
    private DatePicker debEvent;

    @FXML
    private TextArea descEvent;

    @FXML
    private TableView<Event> eventTable;

    @FXML
    private DatePicker finEvent;

    @FXML
    private TextField imgPath;

    @FXML
    private TextField lieuEvent;

    @FXML
    private TextField prixEvent;

    @FXML
    private TextField rechercheTitreEvent;

    @FXML
    private TextField titreEvent;

    @FXML
    private ImageView ImgEventAffiche;

    @FXML
    private Button parcourirButton;

    private ServiceEvent serviceEvent;
    private ServiceCategory serviceCategory;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        serviceEvent = new ServiceEvent();
        serviceCategory = new ServiceCategory();

        List<Category> categories = serviceCategory.afficher();
        ObservableList<Category> observableCategories = FXCollections.observableArrayList(categories);
        categorieEvent.setItems(observableCategories);

        debEvent.setValue(LocalDate.now());

        colidEvent.setCellValueFactory(new PropertyValueFactory<>("id"));
        colTitreEvent.setCellValueFactory(new PropertyValueFactory<>("titre"));
        colDescEvent.setCellValueFactory(new PropertyValueFactory<>("description"));
        colDebEvent.setCellValueFactory(new PropertyValueFactory<>("dateDebut"));
        colFinEvent.setCellValueFactory(new PropertyValueFactory<>("dateFin"));
        colLieuEvent.setCellValueFactory(new PropertyValueFactory<>("lieu"));
        colPrixEvent.setCellValueFactory(new PropertyValueFactory<>("prix"));
        colimgEvent.setCellValueFactory(new PropertyValueFactory<>("imageEvent"));
        colCategEvent.setCellValueFactory(new PropertyValueFactory<>("idCategory"));

        afficherEvents();
    }

    @FXML
    void AjoutEvent(ActionEvent event) {
        String titre = titreEvent.getText();
        String description = descEvent.getText();
        LocalDate localDateDebut = debEvent.getValue();
        LocalDate localDateFin = finEvent.getValue();
        String lieu = lieuEvent.getText();
        double prix = Double.parseDouble(prixEvent.getText());
        Image image = ImgEventAffiche.getImage();
        String imageEvent = image.getUrl();
        Category selectedCategory = categorieEvent.getValue();

        if (localDateDebut != null && localDateFin != null) {
            Date dateDebut = Date.valueOf(localDateDebut);
            Date dateFin = Date.valueOf(localDateFin);

            Event newEvent = new Event(0, titre, description, dateDebut, dateFin, lieu, prix, imageEvent, selectedCategory.getId());
            serviceEvent.ajouter(newEvent);

            afficherEvents();

            showAlert(Alert.AlertType.INFORMATION, "Événement ajouté", "L'événement a été ajouté avec succès.");
        } else {
            System.err.println("Les champs de date ne peuvent pas être vides.");
        }
    }

    @FXML
    void ClearEvent(ActionEvent event) {
        titreEvent.clear();
        descEvent.clear();
        debEvent.setValue(null);
        finEvent.setValue(null);
        lieuEvent.clear();
        prixEvent.clear();
        imgPath.clear();
        categorieEvent.getSelectionModel().clearSelection();
    }

    @FXML
    void DelEvent(ActionEvent event) {
        Event selectedEvent = eventTable.getSelectionModel().getSelectedItem();
        serviceEvent.supprimer(selectedEvent);
        afficherEvents();
        showAlert(Alert.AlertType.INFORMATION, "Événement supprimé", "L'événement a été supprimé avec succès.");
    }

    @FXML
    void UpdEvent(ActionEvent event) {
        Event selectedEvent = eventTable.getSelectionModel().getSelectedItem();

        if (selectedEvent == null) {
            System.err.println("Veuillez sélectionner un événement à mettre à jour.");
            return;
        }

        String newTitre = titreEvent.getText();
        String newDescription = descEvent.getText();
        LocalDate newLocalDateDebut = debEvent.getValue();
        LocalDate newLocalDateFin = finEvent.getValue();
        String newLieu = lieuEvent.getText();
        double newPrix = Double.parseDouble(prixEvent.getText());
        Image image = ImgEventAffiche.getImage();
        String imageEvent = image.getUrl();        Category newSelectedCategory = categorieEvent.getValue();

        if (newLocalDateDebut != null && newLocalDateFin != null) {
            Date newDateDebut = Date.valueOf(newLocalDateDebut);
            Date newDateFin = Date.valueOf(newLocalDateFin);

            selectedEvent.setTitre(newTitre);
            selectedEvent.setDescription(newDescription);
            selectedEvent.setDateDebut(newDateDebut);
            selectedEvent.setDateFin(newDateFin);
            selectedEvent.setLieu(newLieu);
            selectedEvent.setPrix(newPrix);
            selectedEvent.setImageEvent(imageEvent);
            selectedEvent.setIdCategory(newSelectedCategory.getId());

            serviceEvent.modifier(selectedEvent);

            afficherEvents();

            showAlert(Alert.AlertType.INFORMATION, "Événement mis à jour", "L'événement a été mis à jour avec succès.");
        } else {
            System.err.println("Les champs de date ne peuvent pas être vides.");
        }
    }

    @FXML
    private void selectImageAction() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir une image");
        // Filtrer les fichiers pour afficher uniquement les images
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Images", "*.jpg", "*.jpeg", "*.png", "*.gif");
        fileChooser.getExtensionFilters().add(extFilter);
        // Afficher la boîte de dialogue de sélection de fichier
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            // Charger l'image sélectionnée dans l'ImageView
            Image image = new Image(file.toURI().toString());
            ImgEventAffiche.setImage(image);
        }
    }

    @FXML
    void rechercherParTitre(ActionEvent event) {
        String titre = rechercheTitreEvent.getText();
        if (!titre.isEmpty()) {
            List<Event> events = serviceEvent.rechercherParTitre(titre);
            eventTable.getItems().clear();
            eventTable.getItems().addAll(events);
        } else {
            // Si le champ de recherche est vide, afficher tous les événements
            afficherEvents();
        }
    }



    private void afficherEvents() {
        eventTable.getItems().clear();
        List<Event> events = serviceEvent.afficher();
        eventTable.getItems().addAll(events);
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    void dashboardCategories(ActionEvent event) {
        try {
            // Charger le fichier FXML de la page AjoutCategory.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjoutCategory.fxml"));
            Parent root = loader.load();

            // Créer une nouvelle scène avec le contenu de la page AjoutCategory.fxml
            Scene scene = new Scene(root);

            // Accéder à la fenêtre principale de votre application
            Stage stage = (Stage) ((javafx.scene.control.Button) event.getSource()).getScene().getWindow();

            // Afficher la nouvelle scène dans la fenêtre principale
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void dashboardReservations(ActionEvent event) {
        try {
            // Charger le fichier FXML de la page des réservations
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ReservationBack.fxml"));
            Parent root = loader.load();

            // Accéder à la fenêtre principale de votre application
            Stage stage = (Stage) ((javafx.scene.control.Button) event.getSource()).getScene().getWindow();

            // Changer la scène actuelle pour afficher la nouvelle vue
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    void dashboardEvents(ActionEvent event) {
        try {
            // Charger le fichier FXML de la page du tableau de bord des événements
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjoutEvent.fxml"));
            Parent root = loader.load();

            // Créer une nouvelle scène avec le contenu de la page du tableau de bord des événements
            Scene scene = new Scene(root);

            // Accéder à la fenêtre principale de votre application
            Stage stage = (Stage) ((javafx.scene.control.Button) event.getSource()).getScene().getWindow();

            // Afficher la nouvelle scène dans la fenêtre principale
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void homeDashboard(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/HomeBack.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

