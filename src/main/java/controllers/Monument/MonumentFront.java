package controllers.Monument;

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
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import models.Monument;
import models.Ville;
import services.ServiceMonument;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MonumentFront {
    @FXML
    private TextField searchBar;
    @FXML
    private GridPane cartesMonumentGrid;
    private ServiceMonument serviceMonument;

    public MonumentFront() {
        this.serviceMonument = new ServiceMonument();
    }

    @FXML
    private void initialize() {
        loadMonumentData();
    }

    private void loadMonumentData() {
        try {
            List<Monument> MonumentList = serviceMonument.Afficher();
            afficherCartesMonument(MonumentList);
        } catch (SQLException e) {
            e.printStackTrace();
            // Gérer les erreurs
        }
    }

    public void afficherCartesMonument(List<Monument> MonumentList) {
        int col = 0;
        int row = 0;

        for (Monument Monument : MonumentList) {
            Node carteMonument = createMonumentCard(Monument);
            cartesMonumentGrid.add(carteMonument, col, row);
            col++;
            if (col == 3) { // 3 cartes par ligne, vous pouvez ajuster cela selon vos préférences
                col = 0;
                row++;
            }
        }
    }

    private Node createMonumentCard(Monument Monument) {
        StackPane carteMonument = new StackPane();
        carteMonument.getStyleClass().add("Monument-card");

        String imageName = Monument.getImg_monument();
        URL imageUrl = getClass().getResource("/Upload/" + imageName);

        if (imageUrl != null) {
            ImageView imageView = new ImageView(new Image(imageUrl.toExternalForm()));
            imageView.setFitWidth(300);
            imageView.setFitHeight(200);
            carteMonument.getChildren().add(imageView);
        } else {
            System.out.println("L'image n'a pas été trouvée : " + imageName);
        }
    // Création des labels pour les informations du monuments
    //nom
        Label nomMonumentLabel = new Label("Nom: ");
        nomMonumentLabel.setStyle("-fx-text-fill: black;");

        Label nomMonumentValueLabel = new Label(Monument.getNom_monument());
        nomMonumentValueLabel.setStyle("-fx-text-fill: blue; -fx-font-weight: bold;");

        HBox nomMonumentBox = new HBox(nomMonumentLabel, nomMonumentValueLabel);
    // Description de la monumrnt
        Label desMonumentLabel = new Label("Description: ");
        desMonumentLabel.setStyle("-fx-text-fill: black;");

        Label desMonumentValueLabel = new Label("(Cliquer poor scanner le Qr code)");
        desMonumentValueLabel.setStyle("-fx-text-fill: blue;");

        HBox desMonumentBox = new HBox(desMonumentLabel, desMonumentValueLabel);
    // Ajouter le lien au label de description
        desMonumentLabel.setOnMouseClicked(event -> {
            try {
                displayQRCode(Monument.getDesc_monument());
            } catch (WriterException | IOException e) {
                e.printStackTrace();
                System.err.println("Error displaying QR code: " + e.getMessage());
            }
        });


        //Label latitudeLabel = new Label("Latitude: " + Monument.getLatitude());
        //Label longitudeLabel = new Label("Longitude: " + Monument.getLongitude());

        // Création d'une VBox pour organiser les informations verticalement
        VBox infosMonumentVBox = new VBox(5); // Espace vertical entre les informations
        infosMonumentVBox.getChildren().addAll(
                nomMonumentBox,
                desMonumentBox

        );
        // Création d'un bouton pour accéder à la page des villes
        Button voirVillesButton = new Button("Voir les villes");
        voirVillesButton.setOnAction(event -> {
            // Ici, vous pouvez ajouter le code pour rediriger vers la page des villes liées à ce Monument
            System.out.println("Redirection vers la page des villes pour le Monument: " + Monument.getNom_monument());
        });

        // Ajout de marges entre les cartes
        VBox.setMargin(carteMonument, new Insets(10, 10, 10, 10));
        VBox.setMargin(infosMonumentVBox, new Insets(0, 10, 10, 10));

        // Création d'une VBox pour organiser l'image et les informations verticalement
        VBox carteContentVBox = new VBox(10); // Espace vertical entre l'image et les informations
        carteContentVBox.getChildren().addAll(
                carteMonument, infosMonumentVBox
        );

        return carteContentVBox;
    }
    @FXML
    void returnVilles(ActionEvent event) {
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
    public void handleSearch(javafx.scene.input.KeyEvent keyEvent) {
        try {
            // Récupérer le texte de recherche
            String searchTerm = searchBar.getText();

            // Rechercher les pays par nom
            List<Monument> searchResult = serviceMonument.rechercherParNom(searchTerm);

            // Effacer les cartes existantes
            cartesMonumentGrid.getChildren().clear();

            // Afficher les nouveaux résultats de recherche
            afficherCartesMonument(searchResult);
        } catch (SQLException e) {
            e.printStackTrace();
            // Gérer les erreurs
        }
    }
}
