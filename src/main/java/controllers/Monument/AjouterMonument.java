package controllers.Monument;

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
import models.Ville;
import models.Monument;
import services.ServiceVille;
import services.ServiceMonument;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class AjouterMonument implements Initializable {

    @FXML
    private Button AjouterMonument_Button;

    @FXML
    private Button btnImporterImage;

    @FXML
    private ComboBox<String> comboVille_Monument;
    private ServiceVille serviceVille;



    @FXML
    private Button returnToList;

    @FXML
    private TextField tf_descMonument;

    @FXML
    private TextField tf_latitude;

    @FXML
    private TextField tf_longitude;

    @FXML
    private TextField tf_nomMonument;
    private File file;
    @FXML
    private TextField tf_id_Ville;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        serviceVille = new ServiceVille();
        List<Ville> VilleList = null;
        try {
            VilleList = serviceVille.getAll();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        for (Ville Ville : VilleList) {
            comboVille_Monument.getItems().add(Ville.getNom_ville());
        }
    }
    @FXML
    void importerImage(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir une image");
        file = fileChooser.showOpenDialog(null);
        if (file != null) {
            // Copier l'image dans le dossier d'upload de votre projet
            String destinationPath = "C:/Users/cheri/Documents/-- ESPRIT --/3eme/--- SEMESTRE  2 ----/-- PI_Java --/------- PI_JAVA_Finale -------/Integration/src/main/resources/Upload/" + file.getName();
            try {
                Files.copy(file.toPath(), new File(destinationPath).toPath(), StandardCopyOption.REPLACE_EXISTING);
                //tf_imgVille.setText(destinationPath);
            } catch (IOException e) {
                e.printStackTrace(); // Gérer l'erreur d'écriture du fichier
            }
        }

    }

    @FXML
    void AjouterMonument(ActionEvent event) {
        try {
            String nomVilleSelectionne = comboVille_Monument.getValue();
            Ville VilleSelectionne = serviceVille.getVilleByName(nomVilleSelectionne);


            System.out.println(nomVilleSelectionne);
            System.out.println(VilleSelectionne.getId_ville());

            String nomMonument = tf_nomMonument.getText().trim();
            if (!Character.isUpperCase(nomMonument.charAt(0))) {
                throw new IllegalArgumentException("Le nom de la Monument doit commencer par une majuscule.");
            }

            if (nomMonument.isEmpty() || tf_descMonument.getText().isEmpty()
                    || tf_latitude.getText().isEmpty() || tf_longitude.getText().isEmpty()) {
                throw new IllegalArgumentException("Tous les champs doivent être remplis.");
            }

            if (!tf_latitude.getText().matches("^-?\\d*\\.?\\d+$") || !tf_longitude.getText().matches("^-?\\d*\\.?\\d+$")) {
                throw new IllegalArgumentException("Les champs de latitude et longitude doivent être des nombres valides.");
            }

            Monument p = new Monument(
                    VilleSelectionne.getId_ville(),
                    nomMonument,
                    file.getName(),
                    tf_descMonument.getText(),
                    Double.parseDouble(tf_latitude.getText()),
                    Double.parseDouble(tf_longitude.getText())
            );

            ServiceMonument sp = new ServiceMonument();
            sp.Add(p);
            System.out.println(p);
            // Incrémenter le nombre de Monuments pour ce Ville
            VilleSelectionne.setNb_monuments(VilleSelectionne.getNb_monuments() + 1);
            // Mettre à jour le nombre de Monuments dans la base de données
            serviceVille.updateNbMonuments(VilleSelectionne);

            switchToAfficherMonument();
        } catch (SQLException e) {
            // Imprimer la trace complète de l'exception SQL
            e.printStackTrace();
            String errorMessage = "Erreur lors de l'ajout de la Monument dans la base de données : " + e.getMessage();

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
    void returnToList(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Monument/AfficherMonument.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }
    private void switchToAfficherMonument() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Monument/AfficherMonument.fxml"));
        Parent root = loader.load();

        // Après avoir chargé le contrôleur, rafraîchissez les données d'affichage
        //AfficherVille controller = loader.getController();
        //controller.rafraichirDonnees();

        Stage stage = (Stage) tf_nomMonument.getScene().getWindow();
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
