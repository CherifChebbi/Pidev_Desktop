package controllers.Pays;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;
import models.Pays;
import services.ServicePays;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Charts {
    @FXML
    private PieChart continentPieChart;

    @FXML
    private BarChart<String, Number> cityBarChart;

    private ServicePays servicePays = new ServicePays();

    @FXML
    public void initialize() throws SQLException {
        List<Pays> paysList = servicePays.getAll();

        // Remplir le PieChart avec les continents des pays
        paysList.stream()
                .collect(Collectors.groupingBy(Pays::getContinent, Collectors.counting()))
                .forEach((continent, count) -> continentPieChart.getData().add(new PieChart.Data(continent, count)));

        // Remplir le BarChart avec le nombre de villes par pays
        XYChart.Series<String, Number> citySeries = new XYChart.Series<>();

        // Liste de couleurs pour les barres
        List<String> colors = Arrays.asList(
                "#FF3333", "#FF3333", "#FF5733", "#FFBD33", "#33FF57",
                "#33FFBD", "#5733FF", "#BD33FF", "#3363FF", "#33FFBD"
        );

        // Index pour choisir une couleur différente à chaque itération
        final int[] colorIndex = {0};

        for (Pays pays : paysList) {
            XYChart.Data<String, Number> data = new XYChart.Data<>(pays.getNom_pays(), pays.getNb_villes());
            // Attribution d'une couleur différente à chaque barre
            data.nodeProperty().addListener((obs, oldNode, newNode) -> {
                if (newNode != null) {
                    newNode.setStyle("-fx-bar-fill: " + colors.get(colorIndex[0] % colors.size()) + ";");
                    colorIndex[0]++;
                }
            });
            citySeries.getData().add(data);
        }

        cityBarChart.getData().add(citySeries);
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

    @FXML
    void returnStat(ActionEvent event) throws IOException {
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
