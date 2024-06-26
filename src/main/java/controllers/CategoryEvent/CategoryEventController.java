package controllers.CategoryEvent;

import models.Category;
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
import javafx.stage.Stage;
import services.ServiceCategory;
import javafx.collections.FXCollections;
import utils.DBConnexion;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

public class CategoryEventController implements Initializable {

    private final ServiceCategory sc;

    {
        try {
            sc = new ServiceCategory();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private Button btnClearCat;

    @FXML
    private Button btnDelCat;

    @FXML
    private Button btnUpdCat;

    @FXML
    private TextField nomCategory;

    @FXML
    private TableView<Category> categoryTable;

    @FXML
    private TableColumn<Category, Integer> colid;

    @FXML
    private TableColumn<Category, String> colnomcateg;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        showCategories();
    }

    @FXML
    void AjoutCateg(ActionEvent event) {
        String nom = nomCategory.getText();
        if (nom.matches("[a-zA-Z]+")) {
            sc.ajouter(new Category(0, nom));
            showCategories(); // Mettre à jour l'affichage du tableau avec la nouvelle catégorie
        } else {
            // Afficher une alerte si le champ de saisie du nom de la catégorie contient des caractères non autorisés
            showAlert("Caractères non autorisés", "Le nom de la catégorie ne peut contenir que des lettres.");
        }
    }

    @FXML
    void DelCateg(ActionEvent event) {
        Category selectedCategory = categoryTable.getSelectionModel().getSelectedItem();
        if (selectedCategory != null) {
            sc.supprimer(selectedCategory);
            showCategories(); // Mettre à jour l'affichage du tableau après la suppression
        } else {
            // Afficher une alerte si aucune catégorie n'est sélectionnée
            showAlert("Aucune sélection", "Veuillez sélectionner une catégorie à supprimer.");
        }
    }

    @FXML
    void UpdateCat(ActionEvent event) {
        Category selectedCategory = categoryTable.getSelectionModel().getSelectedItem();
        String newName = nomCategory.getText();
        if (selectedCategory != null && newName.matches("[a-zA-Z]+")) {
            selectedCategory.setNom(newName);
            sc.modifier(selectedCategory);
            showCategories(); // Mettre à jour l'affichage du tableau après la modification
        } else {
            // Afficher une alerte si aucune catégorie n'est sélectionnée ou si le champ de saisie du nom de la catégorie contient des caractères non autorisés
            showAlert("Champ vide ou caractères non autorisés", "Veuillez sélectionner une catégorie et saisir un nouveau nom contenant uniquement des lettres.");
        }
    }

    // Méthode utilitaire pour afficher une alerte
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public ObservableList<Category> getCategories() {
        ObservableList<Category> categories = FXCollections.observableArrayList();
        String query = "SELECT * FROM category";
        try {
            Connection con = DBConnexion.getInstance().getCnx();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                int id = rs.getInt("id");
                String nom = rs.getString("nom");
                Category category = new Category(id, nom);
                categories.add(category);
            }
            // Ne pas fermer la connexion ici
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categories;
    }

    public void showCategories() {
        ObservableList<Category> list = getCategories();
        categoryTable.setItems(list);
        colid.setCellValueFactory(new PropertyValueFactory<>("id"));
        colnomcateg.setCellValueFactory(new PropertyValueFactory<>("nom"));
    }

    @FXML
    public void dashboardCategories(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/CategoryEvent/BackCategory.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void dashboardReservations(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ReservationEvent/ReservationBack.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void dashboardEvents(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Event/BackEvent.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
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

    public void ClearCat(ActionEvent actionEvent) {
        categoryTable.getItems().clear();
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

    @FXML
    public void Back_Gestion_User(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/User/AfficherUsers.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    public void Back_Gestion_Pays(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Pays/AfficherPays.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    public void Back_Gestion_Restaurant(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Restaurant/Back.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    public void Back_Gestion_Hebergement(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/CategoryH/Category.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    public void Back_Gestion_Event(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Event/HomeBack.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
