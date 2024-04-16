package controllers.Pays;
import controllers.*;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import models.Pays;
import services.ServicePays;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class AfficherPays {

    public TextField searchBar;
    @FXML
    private Button AjouterPays_Button;

    @FXML
    private Button ModifierPays_Button;


    @FXML
    private TableView<Pays> paysTable;


    @FXML
    private TableColumn<Pays, String> desc_Column;

    @FXML
    private TableColumn<Pays, Integer> id_Column;

    @FXML
    private TableColumn<Pays, String> img_Column;
    @FXML
    private TableColumn<Pays, String> langue_Column;

    @FXML
    private TableColumn<Pays, String> continent_Column;

    @FXML
    private TableColumn<Pays, Double> latitude_Column;

    @FXML
    private TableColumn<Pays, Double> longitude_Column;

    @FXML
    private TableColumn<Pays, Integer> nbrVilles_Column;

    @FXML
    private TableColumn<Pays, String> nom_Column;
    private ServicePays servicePays;
    private TextField tf_recherche;

    @FXML
    private TextField minVillesField;

    @FXML
    private TextField maxVillesField;
    @FXML
    private void initialize() {
        servicePays = new ServicePays();
        initializeTable();
        loadPaysData();
    }
    private void initializeTable() {
        id_Column.setCellValueFactory(new PropertyValueFactory<>("id_pays"));
        nom_Column.setCellValueFactory(new PropertyValueFactory<>("nom_pays"));
        img_Column.setCellFactory(param -> new ImageTableCell());

       // img_Column.setCellValueFactory(new PropertyValueFactory<>("img_pays"));
        desc_Column.setCellValueFactory(new PropertyValueFactory<>("desc_pays"));
        langue_Column.setCellValueFactory(new PropertyValueFactory<>("langue"));
        continent_Column.setCellValueFactory(new PropertyValueFactory<>("continent"));
        nbrVilles_Column.setCellValueFactory(new PropertyValueFactory<>("nb_villes"));
        latitude_Column.setCellValueFactory(new PropertyValueFactory<>("latitude"));
        longitude_Column.setCellValueFactory(new PropertyValueFactory<>("longitude"));

    }

    private void loadPaysData() {
        try {
            List<Pays> paysList = servicePays.Afficher();
            ObservableList<Pays> observablePays = FXCollections.observableArrayList(paysList);
            paysTable.setItems(observablePays);
        } catch (SQLException e) {
            e.printStackTrace();
            // Gérer les erreurs
        }
    }
    @FXML
    private void AjouterPays() {
        try {
            // Charger le fichier FXML de la page d'ajout de pays
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Pays/AjouterPays.fxml"));
            Parent root = loader.load();

            // Créer une nouvelle scène avec le contenu de la page d'ajout de pays
            Scene scene = new Scene(root);

            // Obtenir la fenêtre principale à partir de l'événement du bouton ou d'une autre manière
            Stage stage = (Stage) AjouterPays_Button.getScene().getWindow();

            // Définir la nouvelle scène sur la fenêtre principale
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void ModifierPays(ActionEvent event) {
        Pays selectedPays = paysTable.getSelectionModel().getSelectedItem();

        if (selectedPays != null) {
            try {
                // Charge la vue de modification avec le pays sélectionné
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Pays/ModifierPays.fxml"));
                Parent root = loader.load();

                // Passe le pays sélectionné au contrôleur de la page de modification
                ModifierPays controller = loader.getController();
                controller.initData(selectedPays);

                // Affiche la vue de modification
                Scene scene = new Scene(root);
                Stage stage = new Stage(); // Crée une nouvelle fenêtre pour la page de modification
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            // Affiche un message d'erreur si aucun pays n'est sélectionné
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Veuillez sélectionner un pays à modifier.");
            alert.show();
        }
    }


    @FXML
    void SupprimerPays(ActionEvent event) {
        // Get the selected Pays object from the table
        Pays selectedPays = paysTable.getSelectionModel().getSelectedItem();

        if (selectedPays != null) {
            // Confirm deletion with the user
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setContentText("Are you sure you want to delete this record?");
            Optional<ButtonType> result = alert.showAndWait();

            if (result.get() == ButtonType.OK) {
                try {
                    // Delete the selected Pays object
                    servicePays.Delete(selectedPays);
                    loadPaysData();
                } catch (SQLException e) {
                    e.printStackTrace();
                    // Handle the exception
                }
            }
        } else {
            // Show an error message if no record is selected
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please select a record to delete.");
            alert.show();
        }
    }
    public void handleSearch(javafx.scene.input.KeyEvent keyEvent) {
        try {
            // Récupérer le texte de recherche
            String searchTerm = searchBar.getText();

            // Rechercher les pays par nom
            List<Pays> searchResult = servicePays.rechercherParNom(searchTerm);

            // Mettre à jour la table avec les résultats de la recherche
            ObservableList<Pays> observablePays = FXCollections.observableArrayList(searchResult);
            paysTable.setItems(observablePays);
        } catch (SQLException e) {
            e.printStackTrace();
            // Gérer les erreurs
        }

    }
    @FXML
    private void filterByVilles(ActionEvent event) {
        try {
            // Récupérer les valeurs minimale et maximale
            int minVilles = Integer.parseInt(minVillesField.getText());
            int maxVilles = Integer.parseInt(maxVillesField.getText());

            // Filtrer les pays par nombre de villes
            List<Pays> filteredPays = servicePays.filterByVilles(minVilles, maxVilles);

            // Mettre à jour la table avec les résultats filtrés
            ObservableList<Pays> observablePays = FXCollections.observableArrayList(filteredPays);
            paysTable.setItems(observablePays);
        } catch (NumberFormatException e) {
            // Gérer les erreurs de conversion
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please enter valid numbers for min and max cities.");
            alert.show();
        } catch (SQLException e) {
            e.printStackTrace();
            // Gérer les erreurs
        }
    }
}
