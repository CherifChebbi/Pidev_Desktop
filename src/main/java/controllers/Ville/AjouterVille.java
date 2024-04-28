package controllers.Ville;

import controllers.Pays.AfficherPays;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import models.Pays;
import models.Ville;
import services.ServicePays;
import services.ServiceVille;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class AjouterVille implements Initializable {

    @FXML
    private Button AjouterVille_Button1;

    @FXML
    private Button btnImporterImage;

    @FXML
    private ComboBox<String> comboPays;
    private ServicePays servicePays;



    @FXML
    private Button returnToList;

    @FXML
    private TextField tf_descVille;

    @FXML
    private TextField tf_latitude;

    @FXML
    private TextField tf_longitude;

    @FXML
    private TextField tf_nomVille;
    private File file;
    @FXML
    private TextField tf_id_pays;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        servicePays = new ServicePays();
        List<Pays> paysList = null;
        try {
            paysList = servicePays.getAll();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        for (Pays pays : paysList) {
            comboPays.getItems().add(pays.getNom_pays());
        }
    }

    @FXML
    void AjouterVille(ActionEvent event) {
        try {
            String nomPaysSelectionne = comboPays.getValue();
            Pays paysSelectionne = servicePays.getPaysByName(nomPaysSelectionne);

            String nomVille = tf_nomVille.getText().trim();

            if (!Character.isUpperCase(nomVille.charAt(0))) {
                throw new IllegalArgumentException("Le nom de la ville doit commencer par une majuscule.");
            }

            if (nomVille.isEmpty() || tf_descVille.getText().isEmpty()
                    || tf_latitude.getText().isEmpty() || tf_longitude.getText().isEmpty()) {
                throw new IllegalArgumentException("Tous les champs doivent être remplis.");
            }

            if (!tf_latitude.getText().matches("^-?\\d*\\.?\\d+$") || !tf_longitude.getText().matches("^-?\\d*\\.?\\d+$")) {
                throw new IllegalArgumentException("Les champs de latitude et longitude doivent être des nombres valides.");
            }

            Ville p = new Ville(
                    paysSelectionne.getId_pays(),
                    nomVille,
                    file.getName(),
                    tf_descVille.getText(),
                    Double.parseDouble(tf_latitude.getText()),
                    Double.parseDouble(tf_longitude.getText())
            );

            ServiceVille sp = new ServiceVille();
            sp.Add(p);
            System.out.println(paysSelectionne.getNb_villes());
            // Incrémenter le nombre de villes pour ce pays
            paysSelectionne.setNb_villes(paysSelectionne.getNb_villes() + 1);
            System.out.println(paysSelectionne.getNb_villes());
            // Mettre à jour le nombre de villes dans la base de données
            servicePays.updateNbVilles(paysSelectionne);

            switchToAfficherVille();
        } catch (SQLException e) {
            // Imprimer la trace complète de l'exception SQL
            e.printStackTrace();
            String errorMessage = "Erreur lors de l'ajout de la ville dans la base de données : " + e.getMessage();

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur SQL");
            alert.setContentText(errorMessage);
            alert.show();

        } catch (NumberFormatException e) {
            // Affichage d'un message d'erreur pour les erreurs de format de nombre
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur de format");
            alert.setContentText("Les champs de latitude et longitude doivent être des nombres valides.");
            alert.show();
        } catch (IllegalArgumentException e) {
            // Affichez un message d'erreur si une validation de saisie échoue
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur de saisie");
            alert.setContentText(e.getMessage());
            alert.show();
        } catch (IOException e) {
            // Affichage d'un message d'erreur pour les erreurs d'entrée/sortie
            throw new RuntimeException(e);
        }
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
    void returnToList(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Ville/AfficherVille.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }
    private void switchToAfficherVille() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Ville/AfficherVille.fxml"));
        Parent root = loader.load();

        // Après avoir chargé le contrôleur, rafraîchissez les données d'affichage
        //AfficherPays controller = loader.getController();
        //controller.rafraichirDonnees();

        Stage stage = (Stage) tf_nomVille.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();


    }
    @FXML
    void returnToListPays(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Pays/AfficherPays.fxml"));
        Parent root = loader.load();

        // Après avoir chargé le contrôleur, récupérez une référence au contrôleur de la page des pays
        AfficherPays controller = loader.getController();

        // Appelez la méthode de rafraîchissement des données
        //controller.rafraichirDonnees();

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }


}
