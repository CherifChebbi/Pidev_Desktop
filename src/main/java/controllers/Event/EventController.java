package controllers.Event;

import models.Category;
import models.Event;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import services.ServiceCategory;
import services.ServiceEvent;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

public class EventController implements Initializable {

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

    private ServiceEvent serviceEvent;
    private ServiceCategory serviceCategory;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            serviceEvent = new ServiceEvent();
            serviceCategory = new ServiceCategory();
        } catch (SQLException e) {
            e.printStackTrace();
            // Gérer l'exception
        }

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
        colCategEvent.setCellValueFactory(cellData -> {
            int categoryId = cellData.getValue().getIdCategory();
            Category category = categories.stream().filter(c -> c.getId() == categoryId).findFirst().orElse(null);
            return new SimpleObjectProperty<>(category);
        });

        colCategEvent.setCellFactory(column -> new TableCell<Event, Category>() {
            @Override
            protected void updateItem(Category category, boolean empty) {
                super.updateItem(category, empty);
                if (category == null || empty) {
                    setText(null);
                } else {
                    setText(category.getNom());
                }
            }
        });

        afficherEvents();
    }

    @FXML
    void AjoutEvent(ActionEvent event) {
        String titre = titreEvent.getText();
        String description = descEvent.getText();
        LocalDate localDateDebut = debEvent.getValue();
        LocalDate localDateFin = finEvent.getValue();
        String lieu = lieuEvent.getText();
        String prixStr = prixEvent.getText();

        if (champsNonRemplis(titre, description, localDateDebut, localDateFin, lieu, prixStr)) return;

        if (!champsValides(titre, lieu, description, prixStr)) return;

        if (!dateFinApresDateDebut(localDateDebut, localDateFin)) return;

        double prix = Double.parseDouble(prixStr);
       // String imageEvent = ImgEventAffiche.getImage().getUrl();
        Category selectedCategory = categorieEvent.getValue();

        Date dateDebut = Date.valueOf(localDateDebut);
        Date dateFin = Date.valueOf(localDateFin);

        Event newEvent = new Event(0, titre, description, dateDebut, dateFin, lieu, prix, file.getName(), selectedCategory.getId());
        serviceEvent.ajouter(newEvent);

        afficherEvents();

        showAlert(Alert.AlertType.INFORMATION, "Événement ajouté", "L'événement a été ajouté avec succès.");
    }

    private boolean champsNonRemplis(String titre, String description, LocalDate localDateDebut, LocalDate localDateFin, String lieu, String prixStr) {
        if (titre.isEmpty() || description.isEmpty() || localDateDebut == null || localDateFin == null || lieu.isEmpty() || prixStr.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Champs vides", "Veuillez remplir tous les champs.");
            return true;
        }
        return false;
    }

    private boolean champsValides(String titre, String lieu, String description, String prixStr) {

        if (!lieu.matches("[a-zA-Z]+")) {
            showAlert(Alert.AlertType.ERROR, "Lieu invalide", "Le lieu ne peut contenir que des lettres.");
            return false;
        }

        if (description.length() > 255) {
            showAlert(Alert.AlertType.ERROR, "Description trop longue", "La description ne peut pas dépasser 255 caractères.");
            return false;
        }

        if (!prixStr.matches("\\d*\\.?\\d+")) {
            showAlert(Alert.AlertType.ERROR, "Prix invalide", "Le prix doit contenir uniquement des chiffres.");
            return false;
        }

        return true;
    }

    private boolean dateFinApresDateDebut(LocalDate dateDebut, LocalDate dateFin) {
        if (dateFin.isBefore(dateDebut)) {
            showAlert(Alert.AlertType.ERROR, "Date de fin invalide", "La date de fin doit être après la date de début.");
            return false;
        }
        return true;
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

    private File file;

    @FXML
    private void selectImageAction() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir une image");
        file = fileChooser.showOpenDialog(null);
        if (file != null) {
            // Copier l'image dans le dossier d'upload de votre projet
            String destinationPath = "C:/Users/rayen/IdeaProjects/JAVAPI/src/main/resources/upload/" + file.getName();
            try {
                Files.copy(file.toPath(), new File(destinationPath).toPath(), StandardCopyOption.REPLACE_EXISTING);
                //tf_imgPays.setText(destinationPath);
            } catch (IOException e) {
                e.printStackTrace(); // Gérer l'erreur d'écriture du fichier
            }
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/CategoryEvent/BackCategory.fxml"));
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ReservationEvent/ReservationBack.fxml"));
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Event/BackEvent.fxml"));
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Event/HomeBack.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @FXML
    public void DelEvent(ActionEvent actionEvent) {
        // Récupérer l'événement sélectionné dans le TableView
        Event selectedEvent = eventTable.getSelectionModel().getSelectedItem();

        if (selectedEvent != null) {
            // Appeler le service pour supprimer l'événement
            serviceEvent.supprimer(selectedEvent);

            // Mettre à jour l'affichage des événements dans le TableView
            afficherEvents();

            // Afficher une notification de succès
            showAlert(Alert.AlertType.INFORMATION, "Événement supprimé", "L'événement a été supprimé avec succès.");
        } else {
            // Si aucun événement n'est sélectionné, afficher une alerte d'erreur
            showAlert(Alert.AlertType.ERROR, "Aucun événement sélectionné", "Veuillez sélectionner un événement à supprimer.");
        }
    }


    @FXML
    public void UpdEvent(ActionEvent actionEvent) {
        // Récupérer l'événement sélectionné dans le TableView
        Event selectedEvent = eventTable.getSelectionModel().getSelectedItem();

        if (selectedEvent != null) {
            // Vous pouvez ouvrir une nouvelle fenêtre de dialogue pour permettre à l'utilisateur de mettre à jour les détails de l'événement
            // Vous pouvez utiliser les mêmes champs et contrôles que ceux utilisés pour l'ajout d'un événement, pré-remplis avec les détails de l'événement sélectionné

            // Une fois que l'utilisateur a effectué ses modifications et a confirmé, vous pouvez appeler le service pour mettre à jour l'événement
            // serviceEvent.mettreAJour(selectedEvent);

            // Mettre à jour l'affichage des événements dans le TableView
            afficherEvents();

            // Afficher une notification de succès
            showAlert(Alert.AlertType.INFORMATION, "Événement mis à jour", "L'événement a été mis à jour avec succès.");
        } else {
            // Si aucun événement n'est sélectionné, afficher une alerte d'erreur
            showAlert(Alert.AlertType.ERROR, "Aucun événement sélectionné", "Veuillez sélectionner un événement à mettre à jour.");
        }
    }

    public void frontEvent(ActionEvent actionEvent) {
        try {
            // Charger le fichier FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Event/indexEvent.fxml"));
            Parent root = loader.load();

            // Créer une nouvelle scène
            Scene scene = new Scene(root);

            // Obtenir la fenêtre actuelle
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();

            // Définir la scène sur la fenêtre
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            // Gérer les erreurs de chargement du FXML
        }
    }
}

