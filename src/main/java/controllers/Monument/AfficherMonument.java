package controllers.Monument;

import controllers.Pays.AfficherPays;
import controllers.Ville.AfficherVille;
import controllers.Monument.ModifierMonument;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import models.Ville;
import models.Monument;
import services.ServiceVille;
import services.ServiceMonument;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class AfficherMonument {
    @FXML
    private TableColumn<Monument, Double> id_Ville_Column;

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
    private TableColumn<Monument, Integer> id_Monument_Column;


    @FXML
    private TableColumn<Monument, String> img_Column;

    @FXML
    private TableColumn<Monument, Double> latitude_Column;

    @FXML
    private TableColumn<Monument, Double> longitude_Column;

    @FXML
    private TableColumn<Monument, Integer> nbrMonuments_Column;

    @FXML
    private TableColumn<Monument, String> nom_Column;
    private ServiceMonument serviceMonument;
    private TextField tf_recherche;


    @FXML
    private TextField maxMonumentsField;
    @FXML
    private TextField minMonumentsField;
    @FXML
    private TextField tf_nomVille;
    private ServiceVille serviceVille;

    @FXML
    private ImageView imageView_Monument;


    @FXML
    private void initialize() {
        serviceVille = new ServiceVille();
        serviceMonument = new ServiceMonument();
        initializeTable();
        loadMonumentData();
    }
    private void initializeTable() {
        id_Monument_Column.setCellValueFactory(new PropertyValueFactory<>("id_monument"));
        id_Ville_Column.setCellValueFactory(new PropertyValueFactory<>("id_ville"));
        nom_Column.setCellValueFactory(new PropertyValueFactory<>("nom_monument"));
        //img_Column.setCellValueFactory(new PropertyValueFactory<>("img_Monument"));
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
    public void onTableRowClickedMonument(javafx.scene.input.MouseEvent mouseEvent) {
        Monument selectedMonument = MonumentTable.getSelectionModel().getSelectedItem();
        if (selectedMonument != null) {
            String imageName = selectedMonument.getImg_monument();
            if (imageName != null && !imageName.isEmpty()) {
                try {
                    // Construire l'URL de l'image en utilisant le chemin relatif
                    URL imageUrl = getClass().getResource("/Upload/" + imageName);
                    if (imageUrl != null) {
                        // Charger l'image à partir de l'URL
                        Image image = new Image(imageUrl.toExternalForm());
                        imageView_Monument.setImage(image);
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
                Stage stage = (Stage) AjouterMonument_Button.getScene().getWindow();
                stage.setScene(scene);
                stage.show();
            } catch (IOException | SQLException e) {
                e.printStackTrace();
            }
        } else {
            // Affiche un message d'erreur si aucun Monument n'est sélectionné
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Veuillez sélectionner une Monument à modifier.");
            alert.show();
        }
    }


    @FXML
    void SupprimerMonument(ActionEvent event) throws SQLException {
        // Get the selected Monument object from the table
        Monument selectedMonument = MonumentTable.getSelectionModel().getSelectedItem();

        if (selectedMonument != null) {
            // Confirm deletion with the user
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setContentText("Are you sure you want to delete this record?");
            Optional<ButtonType> result = alert.showAndWait();



            if (result.get() == ButtonType.OK) {
                try {
                    // Récupérer le Ville correspondant à la Monument
                    Ville Ville = serviceVille.getVilleById(selectedMonument.getId_ville());
                    // Decrement the number of cities for this country
                    if (Ville.getNb_monuments() > 1) {
                        Ville.setNb_monuments(Ville.getNb_monuments() - 1);
                    } else {
                        // Set the number of cities to 0 directly
                        Ville.setNb_monuments(0);
                    }
                    // Update the number of cities in the database
                    serviceVille.updateNbMonuments(Ville);
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
    void returnToListVille(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Ville/AfficherVille.fxml"));
        Parent root = loader.load();

        // Après avoir chargé le contrôleur, récupérez une référence au contrôleur de la page des Ville
        AfficherVille controller = loader.getController();

        // Appelez la méthode de rafraîchissement des données
        //controller.rafraichirDonnees();

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void rafraichirDonnees() {
        // Clear existing items in the TableView
        MonumentTable.getItems().clear();

        // Fetch updated data from the database
        List<Monument> MonumentList = null;
        try {
            MonumentList = serviceMonument.getAll();
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exception
        }

        // Add fetched data to the TableView
        if (MonumentList != null) {
            MonumentTable.getItems().addAll(MonumentList);
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
}