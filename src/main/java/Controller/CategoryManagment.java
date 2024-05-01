package Controller;

import Entity.Category;
import Services.ServiceCategory;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

public class CategoryManagment {


    @FXML
    private TextField description;

    @FXML
    private TextField image;

    @FXML
    private Button selectimage;


    @FXML
    private TextField nom;

    @FXML
    private TableColumn<Category, String> imagecategory;

    @FXML
    private TableColumn<Category, String> nomcategory;

    @FXML
    private TableView<Category> afficher;
    @FXML
    private TableColumn<Category, String> desccategory;



    ServiceCategory sc = new ServiceCategory();

    private void selection(){
        Category c=afficher.getItems().get(afficher.getSelectionModel().getSelectedIndex());

        nom.setText(String.valueOf(c.getNom()));
        image.setText(String.valueOf(c.getImage()));
        description.setText(String.valueOf(c.getDescription()));
    }

    public void initialize() {
        nomcategory.setCellValueFactory(new PropertyValueFactory<>("nom"));
        imagecategory.setCellValueFactory(new PropertyValueFactory<>("image"));
        desccategory.setCellValueFactory(new PropertyValueFactory<>("description"));
        try {
            afficher();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @FXML
    void Ajouter(ActionEvent event) throws SQLException, IOException {
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
            // Add the new category label to the scroll pane in FrontManagment
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/test/pane.fxml"));
            Parent root = loader.load();
            FrontManagment frontController = loader.getController();

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

    @FXML
    void goheberg(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/test/hello-view.fxml"));
        Parent root = loader.load();
        Stage st = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        st.setScene(scene);
        st.show();

    }
    @FXML
    void goreserv(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/test/reservation.fxml"));
        Parent root = loader.load();
        Stage st = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        st.setScene(scene);
        st.show();

    }
    @FXML
    void backcateg(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/test/Category.fxml"));
        Parent root = loader.load();
        Stage st = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        st.setScene(scene);
        st.show();

    }




}
