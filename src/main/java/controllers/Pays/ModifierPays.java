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
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import models.Pays;
import services.ServicePays;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
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
    private File file;


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
    void importerImage(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir une image");
        file = fileChooser.showOpenDialog(null);
        if (file != null) {
            // Copier l'image dans le dossier d'upload de votre projet
            String destinationPath = "C:/Users/cheri/Documents/-- ESPRIT --/3eme/--- SEMESTRE  2 ----/-- PI_Java --/Gest_Pays/src/main/resources/upload/" + file.getName();
            try {
                Files.copy(file.toPath(), new File(destinationPath).toPath(), StandardCopyOption.REPLACE_EXISTING);
                //tf_imgPays.setText(destinationPath);
            } catch (IOException e) {
                e.printStackTrace(); // Gérer l'erreur d'écriture du fichier
            }
        }
    }
    @FXML
    void ModifierPays(ActionEvent event) {
        // Vérifiez si le pays sélectionné n'est pas nul
        if (selectedPays != null) {
            try {
                // Vérification du nom du pays commençant par une majuscule
                String nomPays = tf_nomPays.getText().trim();
                if (!Character.isUpperCase(nomPays.charAt(0))) {
                    throw new IllegalArgumentException("Le nom du pays doit commencer par une majuscule.");
                }

                // Vérification de tous les champs remplis
                if (nomPays.isEmpty() || tf_descPays.getText().isEmpty() || tf_languePays.getText().isEmpty()
                        || choiceContinent.getValue() == null || tf_latitude.getText().isEmpty()
                        || tf_longitude.getText().isEmpty()) {
                    throw new IllegalArgumentException("Tous les champs doivent être remplis.");
                }

                // Vérification des caractères spéciaux
                if (!nomPays.matches("[A-Za-z0-9_]+")
                        || !tf_languePays.getText().matches("[A-Za-z0-9_]+")) {
                    throw new IllegalArgumentException("Les champs ne doivent contenir que des lettres, des chiffres et '_'.");
                }

                // Mettez à jour les propriétés du pays avec les valeurs des champs de texte
                selectedPays.setNom_pays(nomPays);
                selectedPays.setImg_pays(file.getName());
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

                // Appelez la méthode Update du servicePays pour mettre à jour le pays dans la base de données
                ServicePays sp = new ServicePays();
                sp.Update(selectedPays);

                // Affichez un message de confirmation de la mise à jour
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("Pays mis à jour avec succès !");
                alert.show();

                // Redirigez vers la page AfficherPays après la mise à jour
                switchToAfficherPays();
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
        //tf_imgPays.setText(selectedPays.getImg_pays());
        tf_descPays.setText(selectedPays.getDesc_pays());
        tf_languePays.setText(selectedPays.getLangue());
        choiceContinent.setValue(selectedPays.getContinent());
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

    @FXML
    void returnToListPays(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Pays/AfficherPays.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) tf_nomPays.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }

}
