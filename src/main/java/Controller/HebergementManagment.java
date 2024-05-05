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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class HebergementManagment {

    @FXML
    private TextField Description;

    @FXML
    private TextField Nom;

    @FXML
    private ComboBox<Category> comboxid;

    @FXML
    private TableView<Hebergement> afficher;

    @FXML
    private TableColumn<Hebergement, String> afficherNom;

    @FXML
    private TableColumn<Hebergement, String> afficherdesc;

    @FXML
    private TextField image;

    @FXML
    private Button ajouter;

    private ServiceHebergement SH = new ServiceHebergement();

    private ServiceCategory SC = new ServiceCategory();

    public void initialize() {
        afficherNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        afficherdesc.setCellValueFactory(new PropertyValueFactory<>("description"));
        try {
            afficher();
            populateComboBox();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private void populateComboBox() throws SQLException {
        List<Category> categories = SC.getAllCategories();
        comboxid.setItems(FXCollections.observableArrayList(categories));
    }


    @FXML
    void ajouter(ActionEvent event) throws SQLException {
        String i = Nom.getText();
        String j = Description.getText();
        Category selectedCategory = comboxid.getValue();

        if (i.isEmpty() || j.isEmpty() || selectedCategory == null) {
            showAlert("Veuillez remplir tous les champs.");
        } else {
            SH.ajouter(new Hebergement(i, j));
            showAlert("Hébergement ajouté avec succès.");
            afficher();
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
        afficher.setItems(SH.afficher());
    }

    @FXML
    void modifier(ActionEvent event) throws SQLException {
        Hebergement h = afficher.getSelectionModel().getSelectedItem();
        String i = String.valueOf(Nom.getText());
        String mo = String.valueOf(Description.getText());

        if (i.isEmpty() || mo.isEmpty()) {
            showAlert("Veuillez remplir tous les champs.");
        } else {
            Hebergement hb = new Hebergement(mo, i);
            SH.modifier(hb, h.getId());
            showAlert("Hébergement modifié avec succès.");
            afficher();
        }}

    @FXML
    void supp(ActionEvent event) throws SQLException {
        Hebergement h = afficher.getSelectionModel().getSelectedItem();
        if (h == null) {
            showAlert("Veuillez sélectionner un hébergement à supprimer.");
        } else {
            SH.supprimer(h.getId());
            showAlert("Hébergement supprimé avec succès.");
            afficher();
        }
    }


    public void selectrestaurant(ActionEvent actionEvent) {
    }

    @FXML
     void gocategory(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Category.fxml"));
        Parent root = loader.load();
        Stage st = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        st.setScene(scene);
        st.show();
    }

    @FXML
     void backheberg(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Hebergement.fxml"));
        Parent root = loader.load();
        Stage st = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        st.setScene(scene);
        st.show();
    }

    @FXML
    void goreserv(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/reservation.fxml"));
        Parent root = loader.load();
        Stage st = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        st.setScene(scene);
        st.show();
    }

}