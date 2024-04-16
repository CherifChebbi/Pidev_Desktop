package controllers.Monument;

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
import models.Monument;
import services.ServiceMonument;

import java.io.IOException;
import java.sql.SQLException;

public class ModifierMonument {

    @FXML
    private Button ModifierMonument_Button;

    @FXML
    private TextField tf_continentMonument;

    @FXML
    private TextField tf_descMonument;

    @FXML
    private TextField tf_imgMonument;

    @FXML
    private TextField tf_langueMonument;

    @FXML
    private TextField tf_latitude;

    @FXML
    private TextField tf_longitude;

    @FXML
    private TextField tf_nomMonument;

    private Monument selectedMonument;
    @FXML
    private ChoiceBox<String> choiceContinent;


    @FXML
    public void initialize() {
    }

    @FXML
    void ModifierMonument(ActionEvent event) {
        // Vérifiez si le Monument sélectionné n'est pas nul
        if (selectedMonument != null) {
            // Mettez à jour les propriétés du Monument avec les valeurs des champs de texte
            selectedMonument.setNom_monument(tf_nomMonument.getText());
            selectedMonument.setImg_monument(tf_imgMonument.getText());
            selectedMonument.setDesc_monument(tf_descMonument.getText());

            // Vérifiez si les champs de latitude et de longitude ne sont pas vides avant de les mettre à jour
            if (!tf_latitude.getText().isEmpty()) {
                selectedMonument.setLatitude(Double.parseDouble(tf_latitude.getText()));
            }
            if (!tf_longitude.getText().isEmpty()) {
                selectedMonument.setLongitude(Double.parseDouble(tf_longitude.getText()));
            }

            try {
                // Appelez la méthode Update du serviceMonument pour mettre à jour le Monument dans la base de données
                ServiceMonument sp = new ServiceMonument();
                sp.Update(selectedMonument);

                // Affichez un message de confirmation de la mise à jour
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("Monument mis à jour avec succès !");
                alert.show();

                // Redirigez vers la page AfficherMonument après la mise à jour
                switchToAfficherMonument();
            } catch (SQLException | IOException e) {
                e.printStackTrace();
                // Affichez un message d'erreur en cas d'échec de la mise à jour
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Erreur lors de la mise à jour du Monument.");
                alert.show();
            }
        } else {
            // Affichez un message d'erreur si aucun Monument n'est sélectionné
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Veuillez sélectionner un Monument à mettre à jour.");
            alert.show();
        }
    }
    public void initData(Monument selectedMonument) {
        this.selectedMonument = selectedMonument;
        // Initialisez les champs de texte avec les données du Monument sélectionné
        tf_nomMonument.setText(selectedMonument.getNom_monument());
        tf_imgMonument.setText(selectedMonument.getImg_monument());
        tf_descMonument.setText(selectedMonument.getDesc_monument());
        tf_latitude.setText(String.valueOf(selectedMonument.getLatitude()));
        tf_longitude.setText(String.valueOf(selectedMonument.getLongitude()));
        //choicelabel

    }
    private void switchToAfficherMonument() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Monument/AfficherMonument.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) tf_nomMonument.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    void returnToList(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Monument/AfficherMonument.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) tf_nomMonument.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }

}
