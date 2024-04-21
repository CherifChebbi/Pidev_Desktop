package entities;

import java.util.Date;

public class Event {
    private int id;
    private String titre;
    private String description;
    private Date dateDebut;
    private Date dateFin;
    private String lieu;
    private double prix;
    private String imageEvent;
    private int idCategory; // Nouvel attribut pour l'ID de la catégorie

    // Constructeur
    public Event(int id, String titre, String description, Date dateDebut, Date dateFin, String lieu, double prix, String imageEvent) {
        this.id = id;
        this.titre = titre;
        this.description = description;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.lieu = lieu;
        this.prix = prix;
        this.imageEvent = imageEvent;
    }

    // Constructeur avec l'ID de la catégorie
    public Event(int id, String titre, String description, Date dateDebut, Date dateFin, String lieu, double prix, String imageEvent, int idCategory) {
        this.id = id;
        this.titre = titre;
        this.description = description;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.lieu = lieu;
        this.prix = prix;
        this.imageEvent = imageEvent;
        this.idCategory = idCategory;
    }

    // Getters et setters pour l'ID de la catégorie
    public int getIdCategory() {
        return idCategory;
    }

    public void setIdCategory(int idCategory) {
        this.idCategory = idCategory;
    }

    // Getters et setters pour les autres attributs (générés automatiquement ou manuellement)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
    }

    public Date getDateFin() {
        return dateFin;
    }

    public void setDateFin(Date dateFin) {
        this.dateFin = dateFin;
    }

    public String getLieu() {
        return lieu;
    }

    public void setLieu(String lieu) {
        this.lieu = lieu;
    }

    public double getPrix() {
        return prix;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }

    public String getImageEvent() {
        return imageEvent;
    }

    public void setImageEvent(String imageEvent) {
        this.imageEvent = imageEvent;
    }


    @Override
    public String toString() {
        return "Event{" +
                "id=" + id +
                ", titre='" + titre + '\'' +
                ", description='" + description + '\'' +
                ", dateDebut=" + dateDebut +
                ", dateFin=" + dateFin +
                ", lieu='" + lieu + '\'' +
                ", prix=" + prix +
                ", imageEvent='" + imageEvent + '\'' +
                ", idCategory=" + idCategory +
                '}';
    }
}
