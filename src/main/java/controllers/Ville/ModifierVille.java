package controllers.Ville;

import controllers.Pays.AfficherPays;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import models.Pays;
import models.Ville;
import services.ServicePays;
import services.ServiceVille;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class ModifierVille implements Initializable {

    @FXML
    private TableView<Ville> VilleTable;

    @FXML
    private Button btnImporterImage;

    @FXML
    private Button ModifierVille_Button;

    @FXML
    private TextField tf_continentVille;

    @FXML
    private TextField tf_descVille;



    @FXML
    private TextField tf_langueVille;

    @FXML
    private TextField tf_latitude;

    @FXML
    private TextField tf_longitude;

    @FXML
    private TextField tf_nbrMonuments;

    @FXML
    private TextField tf_nomVille;

    private Ville selectedVille;
    @FXML
    private ComboBox<String> comboPays2;
    private File file;
    private ServicePays servicePays;
    private ServiceVille serviceVille;


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
            comboPays2.getItems().add(pays.getNom_pays());
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
    void ModifierVille(ActionEvent event) {
        // Vérifiez si le pays sélectionné n'est pas nul
        if (selectedVille != null) {
            try {
                // Vérification du nom du pays commençant par une majuscule
                String nomVille= tf_nomVille.getText().trim();
                if (!Character.isUpperCase(nomVille.charAt(0))) {
                    throw new IllegalArgumentException("Le nom de la ville doit commencer par une majuscule.");
                }

                // Vérification de tous les champs remplis
                if (nomVille.isEmpty() || tf_descVille.getText().isEmpty()
                        || tf_latitude.getText().isEmpty()
                        || tf_longitude.getText().isEmpty()) {
                    throw new IllegalArgumentException("Tous les champs doivent être remplis.");
                }

                // Vérification des caractères spéciaux
                if (!nomVille.matches("[A-Za-z0-9_]+") || !tf_descVille.getText().matches("[A-Za-z0-9_]+")) {
                    throw new IllegalArgumentException("Les champs ne doivent contenir que des lettres, des chiffres et '_'.");
                }

                // Mettez à jour les propriétés de la ville avec les valeurs des champs de texte
                selectedVille.setNom_ville(nomVille);
                selectedVille.setImg_ville(file.getName());
                selectedVille.setDesc_ville(tf_descVille.getText());

                int id=servicePays.getPaysByName(comboPays2.getValue()).getId_pays();
                selectedVille.setId_pays(id);

                // Vérifiez si les champs de latitude et de longitude ne sont pas vides avant de les mettre à jour
                if (!tf_latitude.getText().isEmpty()) {
                    selectedVille.setLatitude(Double.parseDouble(tf_latitude.getText()));
                }
                if (!tf_longitude.getText().isEmpty()) {
                    selectedVille.setLongitude(Double.parseDouble(tf_longitude.getText()));
                }

                // Appelez la méthode Update du serviceVille pour mettre à jour le pays dans la base de données
                ServiceVille sp = new ServiceVille();
                sp.Update(selectedVille);

                // Affichez un message de confirmation de la mise à jour
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("Pays mis à jour avec succès !");
                alert.show();

                // Redirigez vers la page AfficherPays après la mise à jour
                switchToAfficherVille();
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
            alert.setContentText("Veuillez sélectionner  la ville à mettre à jour.");
            alert.show();
        }

    }


    public void initData(Ville selectedVille) throws SQLException {
        this.selectedVille = selectedVille;
        // Initialisez les champs de texte avec les données du Ville sélectionné
        tf_nomVille.setText(selectedVille.getNom_ville());
        //tf_imgVille.setText(selectedVille.getImg_ville());
        tf_descVille.setText(selectedVille.getDesc_ville());
        tf_latitude.setText(String.valueOf(selectedVille.getLatitude()));
        tf_longitude.setText(String.valueOf(selectedVille.getLongitude()));
        String nom =servicePays.getPaysById(selectedVille.getId_pays()).getNom_pays();
        comboPays2.setValue(nom);


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
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
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
