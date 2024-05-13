package controllers.Pays;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
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
    private TableView<Pays> paysTable;

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
    private Button AjouterPays_Button1;
    private String imagePath;
    private File file;
    @FXML
    private ChoiceBox<String> choiceContinent;
    // Déclarez une liste observable pour stocker les données affichées dans le tableau


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
                String destinationPath = "C:/Users/cheri/Documents/-- ESPRIT --/3eme/--- SEMESTRE  2 ----/-- PI_Java --/------- PI_JAVA_Finale -------/Integration/src/main/resources/Upload/" + file.getName();
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

            // Conversion des champs de latitude et longitude en double
            double latitude = Double.parseDouble(tf_latitude.getText());
            double longitude = Double.parseDouble(tf_longitude.getText());

            // Création de l'objet Pays et ajout dans la base de données
            Pays p = new Pays(nomPays, file.getName(), tf_descPays.getText(), tf_languePays.getText(),
                    choiceContinent.getValue(), latitude, longitude);

            ServicePays sp = new ServicePays();

            if (sp.isNomPaysExistant(nomPays)) {
                throw new IllegalArgumentException("Le nom du pays existe déjà.");
            } else {
                // Le nom du pays est unique, vous pouvez ajouter le pays à la base de données
                sp.Add(p);
                switchToAfficherPays();
            }

        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur de saisie");
            alert.setContentText("Vous avez une erreur dans la saisie de vos données!");
            alert.show();
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur de saisie");
            alert.setContentText("Les champs de latitude et longitude doivent être des nombres.");
            alert.show();
        } catch (IllegalArgumentException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur de saisie");
            alert.setContentText(e.getMessage());
            alert.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private void switchToAfficherPays() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Pays/AfficherPays.fxml"));
        Parent root = loader.load();

        // Après avoir chargé le contrôleur, rafraîchissez les données d'affichage
        //AfficherPays controller = loader.getController();
        //controller.rafraichirDonnees();

        Stage stage = (Stage) tf_nomPays.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();


    }

    @FXML
    void returnToList(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Pays/AfficherPays.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }

    @FXML
    void returnToListPays(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Pays/AfficherPays.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }





}
