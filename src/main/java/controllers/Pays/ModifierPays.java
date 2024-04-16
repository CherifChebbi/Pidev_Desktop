package controllers.Pays;

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
import models.Pays;
import services.ServicePays;

import java.io.IOException;
import java.sql.SQLException;

public class ModifierPays {

    @FXML
    private Button ModifierPays_Button;

    @FXML
    private TextField tf_continentPays;

    @FXML
    private TextField tf_descPays;

    @FXML
    private TextField tf_imgPays;

    @FXML
    private TextField tf_languePays;

    @FXML
    private TextField tf_latitude;

    @FXML
    private TextField tf_longitude;

    @FXML
    private TextField tf_nbrVillesPays;

    @FXML
    private TextField tf_nomPays;

    private Pays selectedPays;
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
    void ModifierPays(ActionEvent event) {
        // Vérifiez si le pays sélectionné n'est pas nul
        if (selectedPays != null) {
            // Mettez à jour les propriétés du pays avec les valeurs des champs de texte
            selectedPays.setNom_pays(tf_nomPays.getText());
            selectedPays.setImg_pays(tf_imgPays.getText());
            selectedPays.setDesc_pays(tf_descPays.getText());
            selectedPays.setLangue(tf_languePays.getText());
            selectedPays.setContinent(choiceContinent.getValue());

            // Vérifiez si les champs de latitude et de longitude ne sont pas vides avant de les mettre à jour
            if (!tf_latitude.getText().isEmpty()) {
                selectedPays.setLatitude(Double.parseDouble(tf_latitude.getText()));
            }
            if (!tf_longitude.getText().isEmpty()) {
                selectedPays.setLongitude(Double.parseDouble(tf_longitude.getText()));
            }

            // Mettez à jour le nombre de villes si le champ n'est pas vide
            if (!tf_nbrVillesPays.getText().isEmpty()) {
                selectedPays.setNb_villes(Integer.parseInt(tf_nbrVillesPays.getText()));
            }

            try {
                // Appelez la méthode Update du servicePays pour mettre à jour le pays dans la base de données
                ServicePays sp = new ServicePays();
                sp.Update(selectedPays);

                // Affichez un message de confirmation de la mise à jour
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("Pays mis à jour avec succès !");
                alert.show();

                // Redirigez vers la page AfficherPays après la mise à jour
                switchToAfficherPays();
            } catch (SQLException | IOException e) {
                e.printStackTrace();
                // Affichez un message d'erreur en cas d'échec de la mise à jour
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Erreur lors de la mise à jour du pays.");
                alert.show();
            }
        } else {
            // Affichez un message d'erreur si aucun pays n'est sélectionné
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Veuillez sélectionner un pays à mettre à jour.");
            alert.show();
        }
    }
    public void initData(Pays selectedPays) {
        this.selectedPays = selectedPays;
        // Initialisez les champs de texte avec les données du pays sélectionné
        tf_nomPays.setText(selectedPays.getNom_pays());
        tf_imgPays.setText(selectedPays.getImg_pays());
        tf_descPays.setText(selectedPays.getDesc_pays());
        tf_languePays.setText(selectedPays.getLangue());
        choiceContinent.setValue(selectedPays.getContinent());
        tf_nbrVillesPays.setText(String.valueOf(selectedPays.getNb_villes()));
        tf_latitude.setText(String.valueOf(selectedPays.getLatitude()));
        tf_longitude.setText(String.valueOf(selectedPays.getLongitude()));
    }
    private void switchToAfficherPays() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Pays/AfficherPays.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) tf_nomPays.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    void returnToList(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Pays/AfficherPays.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) tf_nomPays.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }

}
