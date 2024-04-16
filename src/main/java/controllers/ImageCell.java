package controllers;

import javafx.scene.control.TableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import models.Pays;

import java.io.File;

public class ImageCell extends TableCell<Pays, String> {

    private final ImageView imageView;

    public ImageCell() {
        imageView = new ImageView();
        imageView.setFitWidth(100); // Ajustez la largeur de l'image selon vos besoins
        imageView.setFitHeight(100); // Ajustez la hauteur de l'image selon vos besoins
        setGraphic(imageView);
    }

    @Override
    protected void updateItem(String imagePath, boolean empty) {
        super.updateItem(imagePath, empty);

        if (empty || imagePath == null) {
            imageView.setImage(null);
        } else {
            // Chargez l'image à partir du dossier spécifié
            File file = new File("resources/upload/" + imagePath);
            if (file.exists()) {
                Image image = new Image(file.toURI().toString());
                imageView.setImage(image);
            } else {
                // Gérez le cas où l'image n'est pas trouvée
                // Vous pouvez également afficher une image par défaut à la place
                imageView.setImage(null);
            }
        }
    }
}
