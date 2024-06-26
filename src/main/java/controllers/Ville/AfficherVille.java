package controllers.Ville;



import controllers.Pays.AfficherPays;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import models.Pays;
import models.Ville;
import services.ServicePays;
import services.ServiceVille;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class AfficherVille {
    @FXML
    private TableColumn<Ville, Double>  id_Pays_Column;

    public TextField searchBar;
    @FXML
    private Button AjouterVille_Button;

    @FXML
    private Button ModifierVille_Button;

    @FXML
    private TableView<Ville> VilleTable;


    @FXML
    private TableColumn<Ville, String> desc_Column;

    @FXML
    private TableColumn<Ville, Integer> id_Ville_Column;
    @FXML
    private TableColumn<Ville, Integer> id_Monument_Column;

    @FXML
    private TableColumn<Ville, String> img_Column;

    @FXML
    private TableColumn<Ville, Double> latitude_Column;

    @FXML
    private TableColumn<Ville, Double> longitude_Column;

    @FXML
    private TableColumn<Ville, Integer> nbrMonuments_Column;

    @FXML
    private TableColumn<Ville, String> nom_Column;
    private ServiceVille serviceVille;
    private TextField tf_recherche;


    @FXML
    private TextField maxMonumentsField;
    @FXML
    private TextField minMonumentsField;
    @FXML
    private TextField tf_nomPays;
    private ServicePays servicePays;

    @FXML
    private ImageView imageView_ville;


    @FXML
    private void initialize() {
        servicePays = new ServicePays();
        serviceVille = new ServiceVille();
        initializeTable();
        loadVilleData();
    }
    private void initializeTable() {
        id_Ville_Column.setCellValueFactory(new PropertyValueFactory<>("id_ville"));
        id_Pays_Column.setCellValueFactory(new PropertyValueFactory<>("id_pays"));
        nom_Column.setCellValueFactory(new PropertyValueFactory<>("nom_ville"));
        //img_Column.setCellValueFactory(new PropertyValueFactory<>("img_ville"));
        desc_Column.setCellValueFactory(new PropertyValueFactory<>("desc_ville"));
        nbrMonuments_Column.setCellValueFactory(new PropertyValueFactory<>("nb_monuments"));
        latitude_Column.setCellValueFactory(new PropertyValueFactory<>("latitude"));
        longitude_Column.setCellValueFactory(new PropertyValueFactory<>("longitude"));

    }

    private void loadVilleData() {
        try {
            List<Ville> VilleList = serviceVille.Afficher();
            ObservableList<Ville> observableVille = FXCollections.observableArrayList(VilleList);
            VilleTable.setItems(observableVille);
        } catch (SQLException e) {
            e.printStackTrace();
            // Gérer les erreurs
        }
    }

    @FXML
    public void onTableRowClickedVille(javafx.scene.input.MouseEvent mouseEvent) {
        Ville selectedVille = VilleTable.getSelectionModel().getSelectedItem();
        if (selectedVille != null) {
            String imageName = selectedVille.getImg_ville();
            if (imageName != null && !imageName.isEmpty()) {
                try {
                    // Construire l'URL de l'image en utilisant le chemin relatif
                    URL imageUrl = getClass().getResource("/Upload/" + imageName);
                    if (imageUrl != null) {
                        // Charger l'image à partir de l'URL
                        Image image = new Image(imageUrl.toExternalForm());
                        imageView_ville.setImage(image);
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
    private void AjouterVille() {
        try {
            // Charger le fichier FXML de la page d'ajout de Ville
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Ville/AjouterVille.fxml"));
            Parent root = loader.load();

            // Créer une nouvelle scène avec le contenu de la page d'ajout de Ville
            Scene scene = new Scene(root);

            // Obtenir la fenêtre principale à partir de l'événement du bouton ou d'une autre manière
            Stage stage = (Stage) AjouterVille_Button.getScene().getWindow();

            // Définir la nouvelle scène sur la fenêtre principale
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void ModifierVille(ActionEvent event) {
        Ville selectedVille = VilleTable.getSelectionModel().getSelectedItem();

        if (selectedVille != null) {
            try {
                // Charge la vue de modification avec le Ville sélectionné
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Ville/ModifierVille.fxml"));
                Parent root = loader.load();

                // Passe le Ville sélectionné au contrôleur de la page de modification
                ModifierVille controller = loader.getController();
                controller.initData(selectedVille);

                // Affiche la vue de modification
                Scene scene = new Scene(root);
                Stage stage = (Stage) AjouterVille_Button.getScene().getWindow();
                stage.setScene(scene);
                stage.show();
            } catch (IOException | SQLException e) {
                e.printStackTrace();
            }
        } else {
            // Affiche un message d'erreur si aucun Ville n'est sélectionné
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Veuillez sélectionner une Ville à modifier.");
            alert.show();
        }
    }


    @FXML
    void SupprimerVille(ActionEvent event) throws SQLException {
        // Get the selected Ville object from the table
        Ville selectedVille = VilleTable.getSelectionModel().getSelectedItem();

        if (selectedVille != null) {
            // Confirm deletion with the user
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setContentText("Are you sure you want to delete this record?");
            Optional<ButtonType> result = alert.showAndWait();



            if (result.get() == ButtonType.OK) {
                try {
                    // Récupérer le pays correspondant à la ville
                    Pays pays = servicePays.getPaysById(selectedVille.getId_pays());
                    // Decrement the number of cities for this country
                    if (pays.getNb_villes() > 1) {
                        pays.setNb_villes(pays.getNb_villes() - 1);
                    } else {
                        // Set the number of cities to 0 directly
                        pays.setNb_villes(0);
                    }
                        // Update the number of cities in the database
                        servicePays.updateNbVilles(pays);
                    // Delete the selected Ville object
                    serviceVille.Delete(selectedVille);


                    loadVilleData();
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

            // Rechercher les Ville par nom
            List<Ville> searchResult = serviceVille.rechercherParNom(searchTerm);

            // Mettre à jour la table avec les résultats de la recherche
            ObservableList<Ville> observableVille = FXCollections.observableArrayList(searchResult);
            VilleTable.setItems(observableVille);
        } catch (SQLException e) {
            e.printStackTrace();
            // Gérer les erreurs
        }

    }
    @FXML
    private void filterByMonuments(ActionEvent event) {
        try {
            // Récupérer les valeurs minimale et maximale
            int minMonuments = Integer.parseInt(minMonumentsField.getText());
            int maxMonuments = Integer.parseInt(maxMonumentsField.getText());

            // Filtrer les Ville par nombre de villes
            List<Ville> filteredVille = serviceVille.filterByMonument(minMonuments, maxMonuments);

            // Mettre à jour la table avec les résultats filtrés
            ObservableList<Ville> observableVille = FXCollections.observableArrayList(filteredVille);
            VilleTable.setItems(observableVille);
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
    void returnToListPays(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Pays/AfficherPays.fxml"));
        Parent root = loader.load();

        // Après avoir chargé le contrôleur, récupérez une référence au contrôleur de la page des pays
        AfficherPays controller = loader.getController();

        // Appelez la méthode de rafraîchissement des données
        //controller.rafraichirDonnees();

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void rafraichirDonnees() {
        // Clear existing items in the TableView
        VilleTable.getItems().clear();

        // Fetch updated data from the database
        List<Ville> VilleList = null;
        try {
            VilleList = serviceVille.getAll();
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exception
        }

        // Add fetched data to the TableView
        if (VilleList != null) {
            VilleTable.getItems().addAll(VilleList);
        }
    }

}
