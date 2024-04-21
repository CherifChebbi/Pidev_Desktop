package controllers.Pays;

import javafx.scene.image.ImageView;
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
import models.Pays;
import services.ServicePays;
import javafx.scene.image.Image;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class AfficherPays {

    public TextField searchBar;
    @FXML
    private Button AjouterPays_Button;




    @FXML
    private TableView<Pays> paysTable;


    @FXML
    private TableColumn<Pays, String> desc_Column;

    @FXML
    private TableColumn<Pays, Integer> id_Pays_Column;

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


    @FXML
    private TextField minVillesField;

    @FXML
    private TextField maxVillesField;

    @FXML
    private ImageView imageView_pays;




    @FXML
    private void initialize() {
        servicePays = new ServicePays();
        initializeTable();
        loadPaysData();
    }
    private void initializeTable() {
        id_Pays_Column.setCellValueFactory(new PropertyValueFactory<>("id_pays"));
        nom_Column.setCellValueFactory(new PropertyValueFactory<>("nom_pays"));
        //img_Column.setCellValueFactory(new PropertyValueFactory<>("img_pays"));
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
    public void onTableRowPaysClicked(javafx.scene.input.MouseEvent mouseEvent) {
        Pays selectedPays = paysTable.getSelectionModel().getSelectedItem();
        if (selectedPays != null) {
            String imageName = selectedPays.getImg_pays();
            if (imageName != null && !imageName.isEmpty()) {
                try {
                    // Construire l'URL de l'image en utilisant le chemin relatif
                    URL imageUrl = getClass().getResource("/Upload/" + imageName);
                    if (imageUrl != null) {
                        // Charger l'image à partir de l'URL
                        Image image = new Image(imageUrl.toExternalForm());
                        imageView_pays.setImage(image);
                    } else {
                        System.out.println("L'image n'a pas été trouvée : " + imageName);
                    }
                } catch (Exception e) {
                    System.out.println("Erreur lors du chargement de l'image : " + e.getMessage());
                }
            }
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
                Stage stage = (Stage) AjouterPays_Button.getScene().getWindow();                stage.setScene(scene);
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
    @FXML
    void listeVille(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Ville/AfficherVille.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading FXML file: " + e.getMessage());
        }
    }

    public void rafraichirDonnees() {
        // Clear existing items in the TableView
        paysTable.getItems().clear();

        // Fetch updated data from the database
        List<Pays> paysList = null;
        try {
            paysList = servicePays.getAll();
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exception
        }

        // Add fetched data to the TableView
        if (paysList != null) {
            paysTable.getItems().addAll(paysList);
        }
    }
}
