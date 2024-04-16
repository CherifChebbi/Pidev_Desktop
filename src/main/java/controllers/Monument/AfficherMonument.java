package controllers.Monument;


import controllers.*;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import models.Monument;
import services.ServiceMonument;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class AfficherMonument {
    @FXML
    private TableColumn<Monument, Double>  id_Pays_Column;

    public TextField searchBar;
    @FXML
    private Button AjouterMonument_Button;

    @FXML
    private Button ModifierMonument_Button;

    @FXML
    private TableView<Monument> MonumentTable;


    @FXML
    private TableColumn<Monument, String> desc_Column;

    @FXML
    private TableColumn<Monument, Integer> id_Ville_Column;
    @FXML
    private TableColumn<Monument, Integer> id_Monument_Column;

    @FXML
    private TableColumn<Monument, String> img_Column;

    @FXML
    private TableColumn<Monument, Double> latitude_Column;

    @FXML
    private TableColumn<Monument, Double> longitude_Column;

    @FXML
    private TableColumn<Monument, String> nom_Column;
    private ServiceMonument serviceMonument;
    private TextField tf_recherche;


    private TextField tf_nomMonument;

    @FXML

    private TextField tf_nomVille;


    @FXML
    private void initialize() {
        serviceMonument = new ServiceMonument();
        initializeTable();
        loadMonumentData();
    }
    private void initializeTable() {
        id_Monument_Column.setCellValueFactory(new PropertyValueFactory<>("id_monument"));
        id_Ville_Column.setCellValueFactory(new PropertyValueFactory<>("id_ville"));
        nom_Column.setCellValueFactory(new PropertyValueFactory<>("nom_monument"));
        img_Column.setCellValueFactory(new PropertyValueFactory<>("img_monument"));
        desc_Column.setCellValueFactory(new PropertyValueFactory<>("desc_monument"));
        latitude_Column.setCellValueFactory(new PropertyValueFactory<>("latitude"));
        longitude_Column.setCellValueFactory(new PropertyValueFactory<>("longitude"));

    }

    private void loadMonumentData() {
        try {
            List<Monument> MonumentList = serviceMonument.Afficher();
            ObservableList<Monument> observableMonument = FXCollections.observableArrayList(MonumentList);
            MonumentTable.setItems(observableMonument);
        } catch (SQLException e) {
            e.printStackTrace();
            // Gérer les erreurs
        }
    }
    @FXML
    private void AjouterMonument() {
        try {
            // Charger le fichier FXML de la page d'ajout de Monument
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Monument/AjouterMonument.fxml"));
            Parent root = loader.load();

            // Créer une nouvelle scène avec le contenu de la page d'ajout de Monument
            Scene scene = new Scene(root);

            // Obtenir la fenêtre principale à partir de l'événement du bouton ou d'une autre manière
            Stage stage = (Stage) AjouterMonument_Button.getScene().getWindow();

            // Définir la nouvelle scène sur la fenêtre principale
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void ModifierMonument(ActionEvent event) {
        Monument selectedMonument = MonumentTable.getSelectionModel().getSelectedItem();

        if (selectedMonument != null) {
            try {
                // Charge la vue de modification avec le Monument sélectionné
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Monument/ModifierMonument.fxml"));
                Parent root = loader.load();

                // Passe le Monument sélectionné au contrôleur de la page de modification
                ModifierMonument controller = loader.getController();
                controller.initData(selectedMonument);

                // Affiche la vue de modification
                Scene scene = new Scene(root);
                Stage stage = new Stage(); // Crée une nouvelle fenêtre pour la page de modification
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            // Affiche un message d'erreur si aucun Monument n'est sélectionné
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Veuillez sélectionner un Monument à modifier.");
            alert.show();
        }
    }


    @FXML
    void SupprimerMonument(ActionEvent event) {
        // Get the selected Monument object from the table
        Monument selectedMonument = MonumentTable.getSelectionModel().getSelectedItem();

        if (selectedMonument != null) {
            // Confirm deletion with the user
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setContentText("Are you sure you want to delete this record?");
            Optional<ButtonType> result = alert.showAndWait();

            if (result.get() == ButtonType.OK) {
                try {
                    // Delete the selected Monument object
                    serviceMonument.Delete(selectedMonument);
                    loadMonumentData();
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

            // Rechercher les Monument par nom
            List<Monument> searchResult = serviceMonument.rechercherParNom(searchTerm);

            // Mettre à jour la table avec les résultats de la recherche
            ObservableList<Monument> observableMonument = FXCollections.observableArrayList(searchResult);
            MonumentTable.setItems(observableMonument);
        } catch (SQLException e) {
            e.printStackTrace();
            // Gérer les erreurs
        }

    }

    @FXML
    void listeMonuments(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Monument/AfficherMonument.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }
    @FXML
    void returnToListVille(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Ville/AfficherVille.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }
}
