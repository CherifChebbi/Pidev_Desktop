package models;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class CategoryH {
    int id;
    String nom;
    String image;
    String description;
    ImageView imageView;


    public CategoryH(int id, String nom, String image, String description) {
        this.id = id;
        this.nom = nom;
        this.image = image;
        this.description = description;
        this.imageView = new ImageView(new Image("file:" + image)); // Initialize the ImageView with the image file
        this.imageView.setFitWidth(100); // Set the width of the image (adjust as needed)
        this.imageView.setFitHeight(100);
    }

    public CategoryH(String nom, String image, String description) {
        this.nom = nom;
        this.image = image;
        this.description = description;
    }

    public CategoryH() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", image='" + image + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

}
