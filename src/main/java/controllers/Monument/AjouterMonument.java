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
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import models.Monument;
import services.ServiceMonument;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;

public class AjouterMonument {

    @FXML
    private TextField tf_descMonument;

    @FXML
    private TextField tf_imgMonument;


    @FXML
    private TextField tf_latitude;

    @FXML
    private TextField tf_longitude;

    @FXML
    private TextField tf_nomMonument;

    @FXML
    private Button AjouterMonument_Button;
    private String imagePath;
    private File file;
    @FXML
    private ChoiceBox<String> choicePays;


    @FXML
    public void initialize() {
        //choice
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
    void AjouterMonument(ActionEvent event) {
        try {

            Monument p = new Monument(tf_nomMonument.getText(),
                    file.getName(),
                    tf_descMonument.getText(),
                    Double.parseDouble(tf_latitude.getText()),
                    Double.parseDouble(tf_longitude.getText())
            );

            ServiceMonument sp = new ServiceMonument();
            sp.Add(p);

            switchToAfficherMonument();

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
