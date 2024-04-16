package models;

import com.mysql.cj.conf.StringProperty;
import javafx.beans.property.SimpleStringProperty;

public class Pays {


    private int id_pays;
    private String nom_pays;
    private String img_pays;
    private String desc_pays;
    private String langue;
    private String continent;
    private int nb_villes;
    private double latitude;
    private double longitude;
    private final SimpleStringProperty  imagePath = new SimpleStringProperty();
    public Pays(String imagePath) {
        setImagePath(imagePath);
    }

    public String getImagePath() {
        return imagePath.get();
    }

    public SimpleStringProperty imagePathProperty() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath.set(imagePath);
    }


    public Pays() {}
    public Pays(int id_pays, String nom_pays, String img_pays, String desc_pays, String langue, String continent, int nb_villes, double latitude, double longitude) {
        this.id_pays = id_pays;
        this.nom_pays = nom_pays;
        this.img_pays = img_pays;
        this.desc_pays = desc_pays;
        this.langue = langue;
        this.continent = continent;
        this.nb_villes = nb_villes;
        this.latitude = latitude;
        this.longitude = longitude;
    }
    public Pays( String nom_pays, String img_pays, String desc_pays, String langue, String continent, int nb_villes, double latitude, double longitude) {
        this.nom_pays = nom_pays;
        this.img_pays = img_pays;
        this.desc_pays = desc_pays;
        this.langue = langue;
        this.continent = continent;
        this.nb_villes = nb_villes;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return "Pays{" +
                "id_pays=" + id_pays +
                ", nom_pays='" + nom_pays + '\'' +
                ", desc_pays='" + desc_pays + '\'' +
                ", langue='" + langue + '\'' +
                ", continent='" + continent + '\'' +
                ", nb_villes=" + nb_villes +
                '}';
    }

    public int getId_pays() {
        return id_pays;
    }

    public void setId_pays(int id_pays) {
        this.id_pays = id_pays;
    }

    public String getNom_pays() {
        return nom_pays;
    }

    public void setNom_pays(String nom_pays) {
        this.nom_pays = nom_pays;
    }

    public String getImg_pays() {
        return img_pays;
    }

    public void setImg_pays(String img_pays) {
        this.img_pays = img_pays;
    }

    public String getDesc_pays() {
        return desc_pays;
    }

    public void setDesc_pays(String desc_pays) {
        this.desc_pays = desc_pays;
    }

    public String getLangue() {
        return langue;
    }

    public void setLangue(String langue) {
        this.langue = langue;
    }

    public String getContinent() {
        return continent;
    }

    public void setContinent(String continent) {
        this.continent = continent;
    }

    public int getNb_villes() {
        return nb_villes;
    }

    public void setNb_villes(int nb_villes) {
        this.nb_villes = nb_villes;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
