package controllers.Pays;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import models.Pays;
import models.Ville;
import services.ServicePays;
import services.ServiceVille;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class StatPays {

    @FXML
    private ImageView Background;

    @FXML
    private HBox gestion_Evenement;

    @FXML
    private Button returnToListPays1;

    @FXML
    private Label paysPlusVillesLabel;

    @FXML
    private Label paysMoinsVillesLabel;
    @FXML
    private Label villePlusMonumentsLabel;

    @FXML
    private Label villeMoinsMonumentsLabel;


    @FXML
    private Label continentPlusPaysLabel;

    @FXML
    private Label continentMoinsPaysLabel;

    @FXML
    private BarChart<String, Number> barChartPaysNbVilles;

    @FXML
    private BarChart<String, Number> barChartContinentNbPays;

    private ServicePays servicePays;
    private ServiceVille serviceVille;
    private Pays paysWithMostVilles;
    private Pays paysWithLeastVilles;

    private Ville villeWithMostMonuments;
    private Ville villeWithLeastMonuments;


    public void initialize() throws SQLException {
        servicePays = new ServicePays();
        serviceVille = new ServiceVille();
        loadStats();
        afficherStatistiques();
    }

    private void loadStats() throws SQLException {
        // Récupérer les données pour les statistiques depuis le service des pays
        paysWithMostVilles = servicePays.getPaysWithMostVilles();
        paysWithLeastVilles = servicePays.getPaysWithLeastVilles();
        villeWithMostMonuments= serviceVille.getVilleWithMostMonuments();
        villeWithLeastMonuments= serviceVille.getVilleWithLeastMonuments();

    }

    private void afficherStatistiques() {
    // Afficher les statistiques récupérées

    // Nom du pays avec le plus de villes
        if (paysWithMostVilles != null) {
            paysPlusVillesLabel.setText("Nom du pays avec le plus de villes : " + "\n" + paysWithMostVilles.getNom_pays());
        } else {
            paysPlusVillesLabel.setText("Aucun pays trouvé avec le plus de villes.");
        }

    // Nom du pays avec le moins de villes
        if (paysWithLeastVilles != null) {
            paysMoinsVillesLabel.setText("Nom du pays avec le moins de villes : " + "\n" + paysWithLeastVilles.getNom_pays());
        } else {
            paysMoinsVillesLabel.setText("Aucun pays trouvé avec le moins de villes.");
        }

    // Continent avec le plus de pays
        String continentWithMostPays = null;
        try {
            continentWithMostPays = servicePays.getContinentWithMostPays();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (continentWithMostPays != null) {
            continentPlusPaysLabel.setText("Continent avec le plus de pays : " + "\n" + continentWithMostPays);
        } else {
            continentPlusPaysLabel.setText("Aucun continent trouvé avec le plus de pays.");
        }

    // Continent avec le moins de pays
        String continentWithLeastPays = null;
        try {
            continentWithLeastPays = servicePays.getContinentWithLeastPays();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (continentWithLeastPays != null) {
            continentMoinsPaysLabel.setText("Continent avec le moins de pays : " + "\n" + continentWithLeastPays);
        } else {
            continentMoinsPaysLabel.setText("Aucun continent trouvé avec le moins de pays.");
        }
    // Nom du ville avec le plus de monuments
        if (villeWithMostMonuments != null) {
            villePlusMonumentsLabel.setText("Nom de la ville avec le plus de monuments : " + "\n" + villeWithMostMonuments.getNom_ville());
        } else {
            villePlusMonumentsLabel.setText("Aucun ville trouvé avec le plus de monuments.");
        }

    // Nom du ville avec le moins de monuments
        if (villeWithLeastMonuments != null) {
            villeMoinsMonumentsLabel.setText("Nom de la ville avec le moins de monuments : " + "\n" + villeWithLeastMonuments.getNom_ville());
        } else {
            villeMoinsMonumentsLabel.setText("Aucun ville trouvé avec le moins de monuments.");
        }
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
    @FXML
    void Charts(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Pays/Charts.fxml"));
        Parent root = loader.load();

        // Après avoir chargé le contrôleur, récupérez une référence au contrôleur de la page des pays
        Charts controller = loader.getController();

        // Appelez la méthode de rafraîchissement des données
        //controller.rafraichirDonnees();

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}


