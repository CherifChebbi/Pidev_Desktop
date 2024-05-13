package controllers.CategoryH;

import javafx.scene.image.Image;
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
import java.net.DatagramPacket;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;

import java.util.HashMap;
import java.util.Map;

public class CategoryHManagment {


    @FXML
    private TextField description;

    @FXML
    private TextField image;

    @FXML
    private Button selectimage;


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

    private void selection(){
        CategoryH c=afficher.getItems().get(afficher.getSelectionModel().getSelectedIndex());

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
        initializePieChart();
    }

    private void initializePieChart() {
        Map<String, Integer> data = getDataForStatistics();
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        for (Map.Entry<String, Integer> entry : data.entrySet()) {
            pieChartData.add(new PieChart.Data(entry.getKey(), entry.getValue()));
        }
        DatagramPacket pieChart;

    }

    private Map<String, Integer> getDataForStatistics() {
        Map<String, Integer> data = new HashMap<>();
        data.put("Hôtel 1", 25);
        data.put("Hôtel 2", 40);
        data.put("Hôtel 3", 30);
        return data;
    }

    // Other methods remain unchanged...

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
            sc.ajouter(new CategoryH(i, j, y));
            // Afficher un message de succès
            showAlert("Catégorie ajoutée avec succès.");
            // Rafraîchir la liste des catégories
            afficher();
            // Add the new category label to the scroll pane in FrontManagment
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ReservationH/ReservationFront.fxml"));
            Parent root = loader.load();
            FrontManagment frontController = loader.getController();

        }

    }

    private File file;
    @FXML
    private TextField imagePathTextField;
    @FXML
    private void selectImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir une image");
        file = fileChooser.showOpenDialog(null);
        if (file != null) {
            // Extraire le nom du fichier à partir du chemin complet
            String imageName = file.getName();
            // Enregistrer uniquement le nom de l'image dans le champ texte
            image.setText(imageName);

            // Définir le chemin de destination dans votre dossier upload
            String destinationPath = "C:/Users/cheri/Documents/-- ESPRIT --/3eme/--- SEMESTRE  2 ----/-- PI_Java --/------- PI_JAVA_Finale -------/Integration/src/main/resources/Upload/" + imageName;
            try {
                // Copier le fichier vers le dossier Upload
                Files.copy(file.toPath(), new File(destinationPath).toPath(), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace(); // Gérer les erreurs d'écriture de fichier
            }
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

