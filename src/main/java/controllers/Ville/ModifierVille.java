package controllers.Ville;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.Ville;
import services.ServiceVille;

import java.io.IOException;
import java.sql.SQLException;

public class ModifierVille {

    @FXML
    private Button ModifierVille_Button;

    @FXML
    private TextField tf_continentVille;

    @FXML
    private TextField tf_descVille;

    @FXML
    private TextField tf_imgVille;

    @FXML
    private TextField tf_langueVille;

    @FXML
    private TextField tf_latitude;

    @FXML
    private TextField tf_longitude;

    @FXML
    private TextField tf_nbrVillesVille;

    @FXML
    private TextField tf_nomVille;

    private Ville selectedVille;
    @FXML
    private ChoiceBox<String> choiceContinent;


    @FXML
    public void initialize() {
        choiceContinent.getItems().addAll(
                "Europe",
                "Asie",
                "Afrique",
                "Amérique du Nord",
                "Amérique du Sud",
                "Océanie"
        );
    }

    @FXML
    void ModifierVille(ActionEvent event) {
        // Vérifiez si le Ville sélectionné n'est pas nul
        if (selectedVille != null) {
            // Mettez à jour les propriétés du Ville avec les valeurs des champs de texte
            selectedVille.setNom_Ville(tf_nomVille.getText());
            selectedVille.setImg_Ville(tf_imgVille.getText());
            selectedVille.setDesc_Ville(tf_descVille.getText());
            selectedVille.setLangue(tf_langueVille.getText());
            selectedVille.setContinent(choiceContinent.getValue());

            // Vérifiez si les champs de latitude et de longitude ne sont pas vides avant de les mettre à jour
            if (!tf_latitude.getText().isEmpty()) {
                selectedVille.setLatitude(Double.parseDouble(tf_latitude.getText()));
            }
            if (!tf_longitude.getText().isEmpty()) {
                selectedVille.setLongitude(Double.parseDouble(tf_longitude.getText()));
            }

            // Mettez à jour le nombre de villes si le champ n'est pas vide
            if (!tf_nbrVillesVille.getText().isEmpty()) {
                selectedVille.setNb_villes(Integer.parseInt(tf_nbrVillesVille.getText()));
            }

            try {
                // Appelez la méthode Update du serviceVille pour mettre à jour le Ville dans la base de données
                ServiceVille sp = new ServiceVille();
                sp.Update(selectedVille);

                // Affichez un message de confirmation de la mise à jour
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("Ville mis à jour avec succès !");
                alert.show();

                // Redirigez vers la page AfficherVille après la mise à jour
                switchToAfficherVille();
            } catch (SQLException | IOException e) {
                e.printStackTrace();
                // Affichez un message d'erreur en cas d'échec de la mise à jour
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Erreur lors de la mise à jour du Ville.");
                alert.show();
            }
        } else {
            // Affichez un message d'erreur si aucun Ville n'est sélectionné
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Veuillez sélectionner un Ville à mettre à jour.");
            alert.show();
        }
    }
    public void initData(Ville selectedVille) {
        this.selectedVille = selectedVille;
        // Initialisez les champs de texte avec les données du Ville sélectionné
        tf_nomVille.setText(selectedVille.getNom_Ville());
        tf_imgVille.setText(selectedVille.getImg_Ville());
        tf_descVille.setText(selectedVille.getDesc_Ville());
        tf_langueVille.setText(selectedVille.getLangue());
        choiceContinent.setValue(selectedVille.getContinent());
        tf_nbrMonuments.setText(String.valueOf(selectedVille.getNb_monuments()));
        tf_latitude.setText(String.valueOf(selectedVille.getLatitude()));
        tf_longitude.setText(String.valueOf(selectedVille.getLongitude()));
        choiceContinent.setValue(selectedVille.getId_pays());

    }
    private void switchToAfficherVille() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Ville/AfficherVille.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) tf_nomVille.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    void returnToList(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Ville/AfficherVille.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) tf_nomVille.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }

}
