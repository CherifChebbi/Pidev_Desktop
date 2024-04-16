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

public class AjouterPays {
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

    @FXML
    private Button AjouterPays_Button;
    private String imagePath;
    private File file;
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
    void AjouterPays(ActionEvent event) {
        try {
            
            Pays p = new Pays(tf_nomPays.getText(),
                    file.getName(),
                    tf_descPays.getText(),
                    tf_languePays.getText(),
                    choiceContinent.getValue(),
                    Integer.parseInt(tf_nbrVillesPays.getText()),
                    Double.parseDouble(tf_latitude.getText()),
                    Double.parseDouble(tf_longitude.getText())
            );

            ServicePays sp = new ServicePays();
            sp.Add(p);

            switchToAfficherPays();

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
