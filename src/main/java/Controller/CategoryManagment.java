package Controller;

import Entity.Category;
import Entity.Hebergement;
import Services.ServiceCategory;
import Services.ServiceHebergement;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class CategoryManagment {
    @FXML
    private ListView<Category> afficher;

    @FXML
    private TextField description;

    @FXML
    private TextField image;

    @FXML
    private Button selectimage;


    @FXML
    private TextField nom;



    ServiceCategory sc = new ServiceCategory();

    private void selection(){
        Category c=afficher.getItems().get(afficher.getSelectionModel().getSelectedIndex());

        nom.setText(String.valueOf(c.getNom()));
        image.setText(String.valueOf(c.getImage()));
        description.setText(String.valueOf(c.getDescription()));
    }

    @FXML
    void Ajouter(ActionEvent event) throws SQLException {
        String i = String.valueOf(nom.getText());
        String j = String.valueOf(image.getText());
        String y = String.valueOf(description.getText());

        // Vérifier si les champs requis sont remplis
        if (i.isEmpty() || j.isEmpty() || y.isEmpty()) {
            // Afficher un message d'alerte
            showAlert("Veuillez remplir tous les champs.");
        } else {
            // Ajouter la catégorie
            sc.ajouter(new Category(i, j, y));
            // Afficher un message de succès
            showAlert("Catégorie ajoutée avec succès.");
            // Rafraîchir la liste des catégories
            afficher();
        }
    }

    @FXML
    void selectImage(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir une image");
        // Set extension filters if needed
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            image.setText(selectedFile.getAbsolutePath());
        }
    }



    // Méthode pour afficher un message d'alerte
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }




    private void afficher() throws SQLException {
        afficher.setItems(sc.afficher());
    }

    @FXML
    void modifier(ActionEvent event) throws SQLException {
        Category c = afficher.getSelectionModel().getSelectedItem();
        String i = String.valueOf(nom.getText());
        String mo = String.valueOf(image.getText());
        String z = String.valueOf(description.getText());

        // Vérifier si les champs requis sont remplis
        if (i.isEmpty() || mo.isEmpty() || z.isEmpty()) {
            // Afficher un message d'alerte
            showAlert("Veuillez remplir tous les champs.");
        } else {
            Category ct = new Category(i, mo, z);
            sc.modifier(ct, c.getId());

            // Clear the ListView and then re-populate it with updated data
            afficher.getItems().clear();
            afficher.getItems().addAll(sc.afficher());
        }
    }


    @FXML
    void supprimer(ActionEvent event) throws SQLException {
        Category c = afficher.getSelectionModel().getSelectedItem();
        System.out.println(c.getId());
        sc.supprimer(c.getId());
        afficher();
        afficher.refresh();
    }

    @FXML
    void take(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/test/hello-view.fxml"));
        Parent root = loader.load();
        Stage st = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        st.setScene(scene);
        st.show();
    }


}
