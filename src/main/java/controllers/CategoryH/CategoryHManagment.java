package controllers.CategoryH;

import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import models.CategoryH;
import services.ServiceCategoryH;
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
import java.util.Map;

public class CategoryHManagment {

    @FXML
    private TextField description;

    @FXML
    private TextField image;

    @FXML
    private Button selectimage;

    @FXML
    private BarChart<String, Integer> statistiquesRes;

    @FXML
    private TextField nom;

    @FXML
    private TableColumn<CategoryH, String> imagecategory;

    @FXML
    private TableColumn<CategoryH, String> nomcategory;

    @FXML
    private TableView<CategoryH> afficher;

    @FXML
    private TableColumn<CategoryH, String> desccategory;

    ServiceCategoryH sc = new ServiceCategoryH();

    public void initialize() {
        nomcategory.setCellValueFactory(new PropertyValueFactory<>("nom"));
        imagecategory.setCellValueFactory(new PropertyValueFactory<>("image"));
        desccategory.setCellValueFactory(new PropertyValueFactory<>("description"));
        try {
            afficher();
            afficherStatistiques();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @FXML
    void Ajouter(ActionEvent event) throws SQLException, IOException {
        String nomText = nom.getText();
        String imageText = image.getText();
        String descriptionText = description.getText();

        if (nomText.isEmpty() || imageText.isEmpty() || descriptionText.isEmpty()) {
            showAlert("Veuillez remplir tous les champs.");
        } else {
            sc.ajouter(new CategoryH(nomText, imageText, descriptionText));
            showAlert("Catégorie ajoutée avec succès.");
            afficher();
        }
    }

    @FXML
    void selectImage(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir une image");
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            image.setText(selectedFile.getAbsolutePath());
        }
    }

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

    private void afficherStatistiques() {
        Map<String, Integer> reservationsParJour = sc.getReservationsParJour();
        XYChart.Series<String, Integer> series = new XYChart.Series<>();
        series.setName("Nombre de réservations par jour");

        for (Map.Entry<String, Integer> entry : reservationsParJour.entrySet()) {
            series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }

        statistiquesRes.getData().add(series);
    }
    @FXML
    void modifier(ActionEvent event) throws SQLException {
        CategoryH c = afficher.getSelectionModel().getSelectedItem();
        String i = String.valueOf(nom.getText());
        String mo = String.valueOf(image.getText());
        String z = String.valueOf(description.getText());

        // Vérifier si les champs requis sont remplis
        if (i.isEmpty() || mo.isEmpty() || z.isEmpty()) {
            // Afficher un message d'alerte
            showAlert("Veuillez remplir tous les champs.");
        } else {
            CategoryH ct = new CategoryH(i, mo, z);
            sc.modifier(ct, c.getId());

            // Clear the ListView and then re-populate it with updated data
            afficher.getItems().clear();
            afficher.getItems().addAll(sc.afficher());
        }
    }


    @FXML
    void supprimer(ActionEvent event) throws SQLException {
        CategoryH c = afficher.getSelectionModel().getSelectedItem();
        System.out.println(c.getId());
        sc.supprimer(c.getId());
        afficher();
        afficher.refresh();
    }

    @FXML
    void take(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Hebergement/Hebergement.fxml"));
        Parent root = loader.load();
        Stage st = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        st.setScene(scene);
        st.show();
    }

    @FXML
    void goheberg(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Hebergement/Hebergement.fxml"));
        Parent root = loader.load();
        Stage st = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        st.setScene(scene);
        st.show();

    }
    @FXML
    void goreserv(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ReservationH/reservation.fxml"));
        Parent root = loader.load();
        Stage st = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        st.setScene(scene);
        st.show();

    }
    @FXML
    void backcateg(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/CategoryH/Category.fxml"));
        Parent root = loader.load();
        Stage st = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        st.setScene(scene);
        st.show();

    }

}