package controllers.Pays;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.common.BitMatrix;


import com.google.zxing.EncodeHintType;

import com.google.zxing.client.j2se.MatrixToImageWriter;

import com.itextpdf.text.pdf.qrcode.ErrorCorrectionLevel;

import controllers.Ville.PaysVille;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import models.Pays;
import services.ServicePays;

import java.awt.ScrollPane;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PaysFront {

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private GridPane cartesPaysGrid;


    private ServicePays servicePays;


    @FXML
    private TextField maxVillesField;

    @FXML
    private TextField minVillesField;

    @FXML
    private TextField searchBar;

    public PaysFront() {
        this.servicePays = new ServicePays();
    }

    @FXML
    private void initialize() {
        loadPaysData();
    }

    private void loadPaysData() {
        try {
            List<Pays> paysList = servicePays.Afficher();
            afficherCartesPays(paysList);
        } catch (SQLException e) {
            e.printStackTrace();
            // Gérer les erreurs
        }
    }

    public void afficherCartesPays(List<Pays> paysList) {
        int col = 0;
        int row = 0;

        for (Pays pays : paysList) {
            Node cartePays = createPaysCard(pays);
            cartesPaysGrid.add(cartePays, col, row);
            col++;
            if (col == 3) { // 3 cartes par ligne, vous pouvez ajuster cela selon vos préférences
                col = 0;
                row++;
            }
        }
    }

    private Node createPaysCard(Pays pays) {
        StackPane cartePays = new StackPane();
        cartePays.getStyleClass().add("pays-card");

        String imageName = pays.getImg_pays();
        URL imageUrl = getClass().getResource("/Upload/" + imageName);

        if (imageUrl != null) {
            ImageView imageView = new ImageView(new Image(imageUrl.toExternalForm()));
            imageView.setFitWidth(300);
            imageView.setFitHeight(200);
            cartePays.getChildren().add(imageView);
        } else {
            System.out.println("L'image n'a pas été trouvée : " + imageName);
        }

        // Création des labels pour les informations du pays
        //nom
        Label nomPaysLabel = new Label("Nom: ");
        nomPaysLabel.setStyle("-fx-text-fill: black;");

        Label nomPaysValueLabel = new Label(pays.getNom_pays());
        nomPaysValueLabel.setStyle("-fx-text-fill: blue; -fx-font-weight: bold;");

        HBox nomPaysBox = new HBox(nomPaysLabel, nomPaysValueLabel);
        // Langue
        Label langueLabel = new Label("Langue: ");
        langueLabel.setStyle("-fx-text-fill: black;");

        Label langueValueLabel = new Label(pays.getLangue());
        langueValueLabel.setStyle("-fx-text-fill: blue; -fx-font-weight: bold;");

        HBox langueBox = new HBox(langueLabel, langueValueLabel);

        // Continent
        Label continentLabel = new Label("Continent: ");
        continentLabel.setStyle("-fx-text-fill: black;");

        Label continentValueLabel = new Label(pays.getContinent());
        continentValueLabel.setStyle("-fx-text-fill: blue; -fx-font-weight: bold;");

        HBox continentBox = new HBox(continentLabel, continentValueLabel);

        // Nombre de villes
        Label nbVilleLabel = new Label("Nombre de villes: ");
        nbVilleLabel.setStyle("-fx-text-fill: black;");

        Label nbVilleValueLabel = new Label(String.valueOf(pays.getNb_villes()));
        nbVilleValueLabel.setStyle("-fx-text-fill: blue; -fx-font-weight: bold;");

        HBox nbVilleBox = new HBox(nbVilleLabel, nbVilleValueLabel);

        //desc
        Label desPaysLabel = new Label("Description: ");
        desPaysLabel.setStyle("-fx-font-weight: bold;");

        Label desPaysValueLabel = new Label("(Cliquer poor scanner le Qr code)");
        desPaysValueLabel.setStyle("-fx-text-fill: blue;");

        HBox descBox = new HBox(desPaysLabel, desPaysValueLabel);

        // Ajouter le lien au label de description
        desPaysLabel.setOnMouseClicked(event -> {
            try {
                displayQRCode(pays.getDesc_pays());
            } catch (WriterException | IOException e) {
                e.printStackTrace();
                System.err.println("Error displaying QR code: " + e.getMessage());
            }
        });
        //Label langueLabel = new Label("Langue: " + pays.getLangue());
        //Label continentLabel = new Label("Continent: " + pays.getContinent());
        //Label latitudeLabel = new Label("Latitude: " + pays.getLatitude());
        //Label longitudeLabel = new Label("Longitude: " + pays.getLongitude());
        //Label nbVilleLabel = new Label("Nombre de villes: " + pays.getNb_villes());


    // Création d'un bouton pour accéder à la page des villes
        Button voirVillesButton = new Button("Voir les villes");
        voirVillesButton.setStyle("-fx-background-color: rgb(160, 21, 62); -fx-text-fill: white;-fx-font-weight: bold;");
        voirVillesButton.setOnAction(event -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Ville/PaysVille.fxml"));
                Parent root = loader.load();
                PaysVille controller = loader.getController();
                controller.initData(pays.getId_pays()); // Passer l'ID du pays à la page des villes
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
                System.err.println("Error loading FXML file: " + e.getMessage());
            }
        });
        // Création d'un logo pour Maps
        ImageView mapLogo = new ImageView(getClass().getResource("/Pays/map-icon.png").toExternalForm());
        mapLogo.setPreserveRatio(true);
        mapLogo.setFitWidth(50);

        mapLogo.setOnMouseClicked(event -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Pays/MapPays.fxml"));
                Parent root = loader.load();
                MapPays controller =loader.getController();
                // Passez les coordonnées du pays au contrôleur
                controller.setCountryCoordinates(pays.getLatitude(), pays.getLongitude());
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
                System.err.println("Error loading FXML file: " + e.getMessage());
            }
        });


        // Création d'une VBox pour organiser les informations verticalement
        VBox infosPaysVBox = new VBox(5); // Espace vertical entre les informations
        infosPaysVBox.getChildren().addAll(
                nomPaysBox,
                langueBox,
                continentBox,
                nbVilleBox,
                descBox,
                voirVillesButton,
                mapLogo
        );

        // Ajout de marges entre les cartes
        VBox.setMargin(cartePays, new Insets(10, 10, 10, 10));
        VBox.setMargin(infosPaysVBox, new Insets(0, 10, 10, 10));

        // Création d'une VBox pour organiser l'image et les informations verticalement
        VBox carteContentVBox = new VBox(10); // Espace vertical entre l'image et les informations
        carteContentVBox.getChildren().addAll(
                cartePays, infosPaysVBox
        );

        return carteContentVBox;
    }

    @FXML
    void listVille(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Ville/VilleFront.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading FXML file: " + e.getMessage());
        }
    }

    @FXML
    void Front_Pays(ActionEvent event) {
        try {
            // Charger le fichier FXML de la page d'ajout de pays
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Pays/PaysFront.fxml"));
            Parent root = loader.load();

            // Créer une nouvelle scène avec le contenu de la page d'ajout de pays
            Scene scene = new Scene(root);

            // Obtenir la fenêtre principale à partir de l'événement du bouton ou d'une autre manière
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Définir la nouvelle scène sur la fenêtre principale
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @FXML
    void listMonument(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Monument/MonumentFront.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading FXML file: " + e.getMessage());
        }
    }

    // Fonction pour afficher le QR code
    public void displayQRCode(String content) throws WriterException, IOException {
        // Configuration du QR code
        int size = 200;
        String fileType = "png";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Map<EncodeHintType, Object> hintMap = new HashMap<>();
        hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);

        // Génération du QR code
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, size, size, hintMap);
        MatrixToImageWriter.writeToStream(bitMatrix, fileType, outputStream);

        // Affichage du QR code
        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        Image qrImage = new Image(inputStream);
        ImageView imageView = new ImageView(qrImage);
        StackPane qrPane = new StackPane();
        qrPane.getChildren().add(imageView);
        Stage qrStage = new Stage();
        qrStage.setScene(new Scene(qrPane, size, size));
        qrStage.show();
    }
    @FXML
    void filterByVilles(ActionEvent event) {
        try {
            // Récupérer les valeurs minimale et maximale
            int minVilles = Integer.parseInt(minVillesField.getText());
            int maxVilles = Integer.parseInt(maxVillesField.getText());

            // Filtrer les pays par nombre de villes
            List<Pays> filteredPays = servicePays.filterByVilles(minVilles, maxVilles);

            // Effacer les cartes existantes
            cartesPaysGrid.getChildren().clear();

            // Afficher les nouvelles cartes filtrées
            afficherCartesPays(filteredPays);
        } catch (NumberFormatException e) {
            // Gérer les erreurs de conversion
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please enter valid numbers for min and max cities.");
            alert.show();
        } catch (SQLException e) {
            e.printStackTrace();
            // Gérer les erreurs
        }
    }

    @FXML
    public void handleSearch(javafx.scene.input.KeyEvent keyEvent) {
        try {
            // Récupérer le texte de recherche
            String searchTerm = searchBar.getText();

            // Rechercher les pays par nom
            List<Pays> searchResult = servicePays.rechercherParNom(searchTerm);

            // Effacer les cartes existantes
            cartesPaysGrid.getChildren().clear();

            // Afficher les nouveaux résultats de recherche
            afficherCartesPays(searchResult);
        } catch (SQLException e) {
            e.printStackTrace();
            // Gérer les erreurs
        }
    }
    @FXML
    private void editProfile (ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/User/EditProfile.fxml"));
        Parent profileInterface = loader.load();
        Scene profileScene = new Scene(profileInterface);
        Stage profileStage = new Stage();
        profileStage.setScene(profileScene);

        // Close the current stage (assuming verifierButton is accessible from here)
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.close();

        // Show the login stage
        profileStage.show();
    }
}





