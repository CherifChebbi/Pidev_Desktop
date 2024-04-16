package controllers;

import javafx.scene.control.TableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import models.Pays;

public class ImageTableCell extends TableCell<Pays, String> {
    private final ImageView imageView = new ImageView();

    public ImageTableCell() {
        imageView.setFitWidth(100);
        imageView.setFitHeight(100);
        setGraphic(imageView);
    }

    @Override
    protected void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || item == null) {
            setGraphic(null);
        } else {
            Image image = new Image("C:/Users/cheri/Documents/-- ESPRIT --/3eme/--- SEMESTRE  2 ----/-- PI_Java --/Gest_Pays/src/main/resources/Upload/" + item);
            imageView.setImage(image);
        }
    }
}
