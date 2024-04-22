package controllers.Monument;

import com.google.protobuf.Any;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import models.Monument;
import models.Monument;
import models.Ville;
import services.ServiceMonument;
import services.ServiceMonument;
import services.ServiceVille;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class ModifierMonument  implements Initializable {

    @FXML
    private TableView<Monument> MonumentTable;

    @FXML
    private Button btnImporterImage;

    @FXML
    private Button ModifierMonument_Button;

    @FXML
    private TextField tf_continentMonument;

    @FXML
    private TextField tf_descMonument;



    @FXML
    private TextField tf_langueMonument;

    @FXML
    private TextField tf_latitude;

    @FXML
    private TextField tf_longitude;

    @FXML
    private TextField tf_nbrMonuments;

    @FXML
    private TextField tf_nomMonument;

    private Monument selectedMonument;
    @FXML
    private ComboBox<String> comboMonument2;
    private File file;
    private ServiceMonument serviceMonument;

    private ServiceVille serviceVille;

    @FXML
    private  ComboBox<String>comboVille_Monument;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        serviceVille = new ServiceVille();
        List<Ville> VilleList = null;
        try {
            VilleList = serviceVille.getAll();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        for (Ville ville : VilleList) {
            comboVille_Monument.getItems().add(ville.getNom_ville());
        }
    }
    @FXML
    void importerImage(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir une image");
        file = fileChooser.showOpenDialog(null);
        if (file != null) {
            // Copier l'image dans le dossier d'upload de votre projet
            String destinationPath = "C:/Users/cheri/Documents/-- ESPRIT --/3eme/--- SEMESTRE  2 ----/-- PI_Java --/Gest_Monument/src/main/resources/upload/" + file.getName();
            try {
                Files.copy(file.toPath(), new File(destinationPath).toPath(), StandardCopyOption.REPLACE_EXISTING);
                //tf_imgMonument.setText(destinationPath);
            } catch (IOException e) {
                e.printStackTrace(); // Gérer l'erreur d'écriture du fichier
            }
        }
    }

    @FXML
    void ModifierMonument(ActionEvent event) {
        // Vérifiez si le Monument sélectionné n'est pas nul
        if (selectedMonument != null) {
            try {
                // Vérification du nom du Monument commençant par une majuscule
                String nomMonument= tf_nomMonument.getText().trim();
                if (!Character.isUpperCase(nomMonument.charAt(0))) {
                    throw new IllegalArgumentException("Le nom de la Monument doit commencer par une majuscule.");
                }

                // Vérification de tous les champs remplis
                if (nomMonument.isEmpty() || tf_descMonument.getText().isEmpty()
                        || tf_latitude.getText().isEmpty()
                        || tf_longitude.getText().isEmpty()) {
                    throw new IllegalArgumentException("Tous les champs doivent être remplis.");
                }

                // Vérification des caractères spéciaux
                if (!nomMonument.matches("[A-Za-z0-9_]+") || !tf_descMonument.getText().matches("[A-Za-z0-9_]+")) {
                    throw new IllegalArgumentException("Les champs ne doivent contenir que des lettres, des chiffres et '_'.");
                }

                // Mettez à jour les propriétés de la Monument avec les valeurs des champs de texte
                selectedMonument.setNom_monument(nomMonument);
                selectedMonument.setImg_monument(file.getName());
                selectedMonument.setDesc_monument(tf_descMonument.getText());
                int id= serviceVille.getVilleByName(comboVille_Monument.getValue()).getId_ville();
                selectedMonument.setId_ville(id);

                // Vérifiez si les champs de latitude et de longitude ne sont pas vides avant de les mettre à jour
                if (!tf_latitude.getText().isEmpty()) {
                    selectedMonument.setLatitude(Double.parseDouble(tf_latitude.getText()));
                }
                if (!tf_longitude.getText().isEmpty()) {
                    selectedMonument.setLongitude(Double.parseDouble(tf_longitude.getText()));
                }

                // Appelez la méthode Update du serviceMonument pour mettre à jour le Monument dans la base de données
                ServiceMonument sp = new ServiceMonument();
                sp.Update(selectedMonument);

                // Affichez un message de confirmation de la mise à jour
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("Monument mis à jour avec succès !");
                alert.show();

                // Redirigez vers la page AfficherMonument après la mise à jour
                switchToAfficherMonument();
            } catch (NumberFormatException e) {
                // Affichez un message d'erreur si les champs de latitude et de longitude ne sont pas des nombres
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Les champs de latitude et longitude doivent être des nombres.");
                alert.show();
            } catch (IllegalArgumentException e) {
                // Affichez un message d'erreur si une validation de saisie échoue
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText(e.getMessage());
                alert.show();
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
            alert.setContentText("Veuillez sélectionner  la Monument à mettre à jour.");
            alert.show();
        }

    }


    public void initData(Monument selectedMonument) throws SQLException {
        this.selectedMonument = selectedMonument;
        // Initialisez les champs de texte avec les données du Monument sélectionné
        tf_nomMonument.setText(selectedMonument.getNom_monument());
        //tf_imgMonument.setText(selectedMonument.getImg_Monument());
        tf_descMonument.setText(selectedMonument.getDesc_monument());
        tf_latitude.setText(String.valueOf(selectedMonument.getLatitude()));
        tf_longitude.setText(String.valueOf(selectedMonument.getLongitude()));
        String nom =serviceVille.getVilleById(selectedMonument.getId_ville()).getNom_ville();
        comboVille_Monument.setValue(nom);


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
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }
}
