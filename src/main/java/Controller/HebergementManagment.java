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
    ComboBox<Category> comboxid;
    @FXML
    private ListView<Hebergement> afficher;
    @FXML
    private TextField image;
    @FXML
    private Button ajouter;


    ServiceHebergement SH = new ServiceHebergement();


    ServiceCategory SC = new ServiceCategory();

    private void selection(){
        Hebergement h=afficher.getItems().get(afficher.getSelectionModel().getSelectedIndex());

        Nom.setText(String.valueOf(h.getNom()));
        Description.setText(String.valueOf(h.getDescription()));
    }


    @FXML
    void ajouter(ActionEvent event) throws SQLException {
        String i = String.valueOf(Nom.getText());
        String j = String.valueOf(Description.getText());
        Category selectedRestaurant = comboxid.getValue();

        // Vérifier si les champs requis sont remplis
        if (i.isEmpty() || j.isEmpty()) {
            // Afficher un message d'alerte
            showAlert("Veuillez remplir tous les champs.");
        } else {
            SH.ajouter(new Hebergement(i, j));
            // Afficher un message de succès
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

        // Vérifier si les champs requis sont remplis
        if (i.isEmpty() || mo.isEmpty()) {
            // Afficher un message d'alerte
            showAlert("Veuillez remplir tous les champs.");
        } else {
            Hebergement hb = new Hebergement(mo, i);
            SH.modifier(hb, h.getId());
            // Afficher un message de succès
            showAlert("Hébergement modifié avec succès.");
            afficher();
        }
    }


    public void supp(ActionEvent actionEvent) throws SQLException {
        Hebergement h = afficher.getSelectionModel().getSelectedItem();
        System.out.println(h.getCategorie_id());
        SH.supprimer(h.getCategorie_id());
        afficher();
afficher.refresh();
    }


    @FXML
    void moove(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/test/Category.fxml"));
        Parent root = loader.load();
        Stage st = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        st.setScene(scene);
        st.show();
    }


    public void selectrestaurant(ActionEvent actionEvent) {
    }
}
