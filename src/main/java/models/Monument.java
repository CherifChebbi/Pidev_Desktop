package models;

public class Monument {

    private int id_ville;
    private int id_monument;
    private String nom_monument;
    private String img_monument;
    private String desc_monument;
    private double latitude;
    private double longitude;

    @Override
    public String toString() {
        return "Monument{" +
                "nom_monument='" + nom_monument + '\'' +
                ", img_monument='" + img_monument + '\'' +
                ", desc_monument='" + desc_monument + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }

    public Monument() {
    }

    public Monument(int id_ville, int id_monument, String nom_monument, String img_monument, String desc_monument, double latitude, double longitude) {
        this.id_ville = id_ville;
        this.id_monument = id_monument;
        this.nom_monument = nom_monument;
        this.img_monument = img_monument;
        this.desc_monument = desc_monument;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Monument(String nom_monument, String img_monument, String desc_monument, double latitude, double longitude) {
        this.nom_monument = nom_monument;
        this.img_monument = img_monument;
        this.desc_monument = desc_monument;
        this.latitude = latitude;
        this.longitude = longitude;
    }
    public int getId_ville() {
        return id_ville;
    }

    public void setId_ville(int id_ville) {
        this.id_ville = id_ville;
    }

    public int getId_monument() {
        return id_monument;
    }

    public void setId_monument(int id_monument) {
        this.id_monument = id_monument;
    }

    public String getNom_monument() {
        return nom_monument;
    }

    public void setNom_monument(String nom_monument) {
        this.nom_monument = nom_monument;
    }

    public String getImg_monument() {
        return img_monument;
    }

    public void setImg_monument(String img_monument) {
        this.img_monument = img_monument;
    }

    public String getDesc_monument() {
        return desc_monument;
    }

    public void setDesc_monument(String desc_monument) {
        this.desc_monument = desc_monument;
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
