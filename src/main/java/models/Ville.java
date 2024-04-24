package models;

public class Ville {

    private int id_pays;
    private int id_ville;
    private String nom_ville;
    private String img_ville;
    private String desc_ville;
    private int nb_monuments;
    private double latitude;
    private double longitude;

    @Override
    public String toString() {
        return "Ville{" +
                "nom_ville='" + nom_ville + '\'' +
                ", img_ville='" + img_ville + '\'' +
                ", desc_ville='" + desc_ville + '\'' +
                ", nb_monuments=" + nb_monuments +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }

    public Ville() {
    }

    public Ville(int id_ville,int id_pays,  String nom_ville, String img_ville, String desc_ville,int nb_monuments, double latitude, double longitude) {

        this.id_ville = id_ville;
        this.id_pays = id_pays;
        this.nom_ville = nom_ville;
        this.img_ville = img_ville;
        this.desc_ville = desc_ville;
        this.nb_monuments = nb_monuments;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Ville(int id_ville,String nom_ville, String img_ville, String desc_ville,int nb_monuments,  double latitude, double longitude) {
        this.id_ville = id_ville;
        this.nom_ville = nom_ville;
        this.img_ville = img_ville;
        this.desc_ville = desc_ville;
        this.nb_monuments = nb_monuments;
        this.latitude = latitude;
        this.longitude = longitude;
    }
    public Ville(int id_pays,String nom_ville, String img_ville, String desc_ville, double latitude, double longitude) {
        this.id_pays = id_pays;
        this.nom_ville = nom_ville;
        this.img_ville = img_ville;
        this.desc_ville = desc_ville;
        this.latitude = latitude;
        this.longitude = longitude;
    }


    public int getId_pays() {
        return id_pays;
    }

    public void setId_pays(int id_pays) {
        this.id_pays = id_pays;
    }

    public int getId_ville() {
        return id_ville;
    }

    public void setId_ville(int id_ville) {
        this.id_ville = id_ville;
    }

    public String getNom_ville() {
        return nom_ville;
    }

    public void setNom_ville(String nom_ville) {
        this.nom_ville = nom_ville;
    }

    public String getImg_ville() {
        return img_ville;
    }

    public void setImg_ville(String img_ville) {
        this.img_ville = img_ville;
    }

    public String getDesc_ville() {
        return desc_ville;
    }

    public void setDesc_ville(String desc_ville) {
        this.desc_ville = desc_ville;
    }

    public int getNb_monuments() {
        return nb_monuments;
    }

    public void setNb_monuments(int nb_monuments) {
        this.nb_monuments = nb_monuments;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
