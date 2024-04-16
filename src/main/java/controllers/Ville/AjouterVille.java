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
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import models.Ville;
import services.ServiceVille;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;

public class AjouterVille {

    @FXML
    private TextField tf_descVille;

    @FXML
    private TextField tf_imgVille;


    @FXML
    private TextField tf_latitude;

    @FXML
    private TextField tf_longitude;

    @FXML
    private TextField tf_nbrMonuments;

    @FXML
    private TextField tf_nomVille;

    @FXML
    private Button AjouterVille_Button;
    private String imagePath;
    private File file;
    @FXML
    private ChoiceBox<String> choicePays;


    @FXML
    public void initialize() {
        choicePays.getItems().
    }
    @FXML
    void importerImage(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir une image");
        file = fileChooser.showOpenDialog(null);
        if (file != null) {
            // Copier l'image dans le dossier d'upload de votre projet
            String destinationPath = "C:/Users/cheri/Documents/-- ESPRIT --/3eme/--- SEMESTRE  2 ----/-- PI_Java --/Gest_Ville/src/main/resources/upload/" + file.getName();
            try {
                Files.copy(file.toPath(), new File(destinationPath).toPath(), StandardCopyOption.REPLACE_EXISTING);
                //tf_imgVille.setText(destinationPath);
            } catch (IOException e) {
                e.printStackTrace(); // Gérer l'erreur d'écriture du fichier
            }
        }
    }


    @FXML
    void AjouterVille(ActionEvent event) {
        try {

            Ville p = new Ville(tf_nomVille.getText(),
                    file.getName(),
                    tf_descVille.getText(),

                    Integer.parseInt(tf_nbrMonuments.getText()),
                    Double.parseDouble(tf_latitude.getText()),
                    Double.parseDouble(tf_longitude.getText()),
                    choicePays.getValue()
            );

            ServiceVille sp = new ServiceVille();
            sp.Add(p);

            switchToAfficherVille();

        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur de saisie");
            alert.setContentText("Vous avez une erreur dans la saisie de vos données!");
            alert.show();
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur de saisie");
            alert.setContentText("Vous avez une erreur dans la saisie de vos données!");
            alert.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
