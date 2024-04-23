package controllers;

import entities.Category;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import services.ServiceCategory;
import javafx.collections.FXCollections;
import utils.DB;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class AjoutCategoryController implements Initializable {

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
        sc.ajouter(new Category(0, nom));
        showCategories(); // Mettre à jour l'affichage du tableau avec la nouvelle catégorie

    }

    @FXML
    void ClearCat(ActionEvent event) {
        // Supprimer toutes les catégories de la base de données
        sc.viderCategories();

        // Rafraîchir l'affichage de la table pour refléter les modifications
        showCategories();
    }

    @FXML
    void DelCateg(ActionEvent event) {
        Category selectedCategory = categoryTable.getSelectionModel().getSelectedItem();
        if (selectedCategory != null) {
            sc.supprimer(selectedCategory);
            showCategories(); // Mettre à jour l'affichage du tableau après la suppression
        }
    }

    @FXML
    void UpdateCat(ActionEvent event) {
        Category selectedCategory = categoryTable.getSelectionModel().getSelectedItem();
        if (selectedCategory != null) {
            String newName = nomCategory.getText();
            selectedCategory.setNom(newName);
            sc.modifier(selectedCategory);
            showCategories(); // Mettre à jour l'affichage du tableau après la modification
        }
    }

    public ObservableList<Category> getCategories() {
        ObservableList<Category> categories = FXCollections.observableArrayList();
        String query = "SELECT * FROM category";
        try {
            Connection con = DB.getInstance().getConnection();
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjoutCategory.fxml"));
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ReservationBack.fxml"));
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjoutEvent.fxml"));
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
