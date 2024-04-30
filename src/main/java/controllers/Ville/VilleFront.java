package controllers.Ville;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.itextpdf.text.pdf.qrcode.ErrorCorrectionLevel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import models.Ville;
import services.ServiceVille;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VilleFront {
    @FXML
    private GridPane cartesVilleGrid;
    private ServiceVille serviceVille;

    public VilleFront() {
        this.serviceVille = new ServiceVille();
    }

    @FXML
    private void initialize() {
        loadVilleData();
    }

    private void loadVilleData() {
        try {
            List<Ville> VilleList = serviceVille.Afficher();
            afficherCartesVille(VilleList);
        } catch (SQLException e) {
            e.printStackTrace();
            // Gérer les erreurs
        }
    }

    public void afficherCartesVille(List<Ville> VilleList) {
        int col = 0;
        int row = 0;

        for (Ville Ville : VilleList) {
            Node carteVille = createVilleCard(Ville);
            cartesVilleGrid.add(carteVille, col, row);
            col++;
            if (col == 3) { // 3 cartes par ligne, vous pouvez ajuster cela selon vos préférences
                col = 0;
                row++;
            }
        }
    }

    private Node createVilleCard(Ville Ville) {
        StackPane carteVille = new StackPane();
        carteVille.getStyleClass().add("Ville-card");

        String imageName = Ville.getImg_ville();
        URL imageUrl = getClass().getResource("/Upload/" + imageName);

        if (imageUrl != null) {
            ImageView imageView = new ImageView(new Image(imageUrl.toExternalForm()));
            imageView.setFitWidth(300);
            imageView.setFitHeight(200);
            carteVille.getChildren().add(imageView);
        } else {
            System.out.println("L'image n'a pas été trouvée : " + imageName);
        }

    // Création des labels pour les informations du Ville
    //nom
        Label nomVilleLabel = new Label("Nom: ");
        nomVilleLabel.setStyle("-fx-text-fill: black;");

        Label nomVilleValueLabel = new Label(Ville.getNom_ville());
        nomVilleValueLabel.setStyle("-fx-text-fill: blue; -fx-font-weight: bold;");

        HBox nomVilleBox = new HBox(nomVilleLabel, nomVilleValueLabel);
    // Nombre de monuments
        Label nbMonumentLabel = new Label("Nombre de villes: ");
        nbMonumentLabel.setStyle("-fx-text-fill: black;");

        Label nbMonumentValueLabel = new Label(String.valueOf(Ville.getNb_monuments()));
        nbMonumentValueLabel.setStyle("-fx-text-fill: blue; -fx-font-weight: bold;");

        HBox nbMonumentBox = new HBox(nbMonumentLabel, nbMonumentValueLabel);
    // Description de la ville
        Label desVilleLabel = new Label("Description: ");
        desVilleLabel.setStyle("-fx-text-fill: black;");

        Label desVilleValueLabel = new Label("(Cliquer poor scanner le Qr code)");
        desVilleValueLabel.setStyle("-fx-text-fill: blue;");

        HBox desVilleBox = new HBox(desVilleLabel, desVilleValueLabel);
        // Ajouter le lien au label de description
        desVilleLabel.setOnMouseClicked(event -> {
            try {
                displayQRCode(Ville.getDesc_ville());
            } catch (WriterException | IOException e) {
                e.printStackTrace();
                System.err.println("Error displaying QR code: " + e.getMessage());
            }
        });


        //Label latitudeLabel = new Label("Latitude: " + Ville.getLatitude());
        //Label longitudeLabel = new Label("Longitude: " + Ville.getLongitude());


        // Création d'une VBox pour organiser les informations verticalement
        VBox infosVilleVBox = new VBox(5); // Espace vertical entre les informations
        infosVilleVBox.getChildren().addAll(
                nomVilleBox,
                nbMonumentBox,
                desVilleBox

        );
        // Création d'un bouton pour accéder à la page des villes
        Button voirVillesButton = new Button("Voir les villes");
        voirVillesButton.setOnAction(event -> {
            // Ici, vous pouvez ajouter le code pour rediriger vers la page des villes liées à ce Ville
            System.out.println("Redirection vers la page des villes pour le Ville: " + Ville.getNom_ville());
        });

        // Ajout de marges entre les cartes
        VBox.setMargin(carteVille, new Insets(10, 10, 10, 10));
        VBox.setMargin(infosVilleVBox, new Insets(0, 10, 10, 10));

        // Création d'une VBox pour organiser l'image et les informations verticalement
        VBox carteContentVBox = new VBox(10); // Espace vertical entre l'image et les informations
        carteContentVBox.getChildren().addAll(
                carteVille, infosVilleVBox
        );

        return carteContentVBox;
    }

    @FXML
    void returnPays(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Pays/PaysFront.fxml"));
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
}
