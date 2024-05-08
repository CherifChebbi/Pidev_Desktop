package models;

import java.time.LocalDate;

public class ReservationH {
    private int id;
    private int hebergmenet;
    private String nom;
    private String email;
    private LocalDate date;
    private int nbrPersonne;

    public ReservationH(int id, int hebergmenet, String nom, String email, LocalDate date, int nbrPersonne) {
        this.id = id;
        this.hebergmenet = hebergmenet;
        this.nom = nom;
        this.email = email;
        this.date = date;
        this.nbrPersonne = nbrPersonne;
    }

    public ReservationH(int hebergmenet, String nom, String email, LocalDate date, int nbrPersonne) {
        this.hebergmenet = hebergmenet;
        this.nom = nom;
        this.email = email;
        this.date = date;
        this.nbrPersonne = nbrPersonne;
    }

    public ReservationH(String nom, String email, LocalDate date, int nbrPersonne) {
        this.nom = nom;
        this.email = email;
        this.date = date;
        this.nbrPersonne = nbrPersonne;
    }
    public ReservationH() {
        // You can initialize default values or leave it empty
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getHebergmenet() {
        return hebergmenet;
    }

    public void setHebergmenet(int hebergmenet) {
        this.hebergmenet = hebergmenet;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public int getNbrPersonne() {
        return nbrPersonne;
    }

    public void setNbrPersonne(int nbrPersonne) {
        this.nbrPersonne = nbrPersonne;
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "id=" + id +
                ", hebergmenet=" + hebergmenet +
                ", nom='" + nom + '\'' +
                ", email='" + email + '\'' +
                ", date=" + date +
                ", nbrPersonne=" + nbrPersonne +
                '}';
    }
}
