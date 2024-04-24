package Entity;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.List;

public class Restaurant {

    int idR;
    String nom, localisataion, image, description;
    ImageView imageView;
    List<Plat> plats; // Add this attribute to store the list of plats

    public Restaurant(int idR, String nom, String localisataion, String image, String description, List<Plat> plats) {
        this.idR = idR;
        this.nom = nom;
        this.localisataion = localisataion;
        this.image = image;
        this.description = description;
        this.imageView = new ImageView(new Image("file:" + image)); // Initialize the ImageView with the image file
        this.imageView.setFitWidth(100); // Set the width of the image (adjust as needed)
        this.imageView.setFitHeight(100);
        this.plats = plats; // Assign the list of plats
    }


    public Restaurant(String nom, String localisataion, String image, String description) {
        this.nom = nom;
        this.localisataion = localisataion;
        this.image = image;
        this.description = description;
    }

    public Restaurant() {

    }

    public Restaurant(String restaurantName) {
    }

    public int getIdR() {
        return idR;
    }

    public void setIdR(int idR) {
        this.idR = idR;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getLocalisataion() {
        return localisataion;
    }

    public void setLocalisataion(String localisataion) {
        this.localisataion = localisataion;
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

    public List<Plat> getPlats() {
        return plats;
    }

    public void setPlats(List<Plat> plats) {
        this.plats = plats;
    }

    @Override
    public String toString() {
        return "Restaurant{" +
                "idR=" + idR +
                ", nom='" + nom + '\'' +
                ", localisataion='" + localisataion + '\'' +
                ", image='" + image + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
