package models;

import java.util.Date;

public class ReservationEvent {
    private int id;
    private int idEvent;
    private String nom;
    private String email;
    private String numTel;
    private Date dateReservation;


    // Constructeur avec et sans ID
    public ReservationEvent(int idEvent, String nom, String email, String numTel, Date dateReservation) {
        this.idEvent = idEvent;
        this.nom = nom;
        this.email = email;
        this.numTel = numTel;
        this.dateReservation = dateReservation;
    }

    public ReservationEvent(int id, int idEvent, String nom, String email, String numTel, Date dateReservation) {
        this.id = id;
        this.idEvent = idEvent;
        this.nom = nom;
        this.email = email;
        this.numTel = numTel;
        this.dateReservation = dateReservation;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdEvent() {
        return idEvent;
    }

    public void setIdEvent(int idEvent) {
        this.idEvent = idEvent;
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

    public String getNumTel() {
        return numTel;
    }

    public void setNumTel(String numTel) {
        this.numTel = numTel;
    }

    public Date getDateReservation() {
        return dateReservation;
    }

    public void setDateReservation(Date dateReservation) {
        this.dateReservation = dateReservation;
    }

    @Override
    public String toString() {

        return "ReservationEvent{" +
                "id=" + id +
                ", idEvent=" + idEvent +
                ", nom='" + nom + '\'' +
                ", email='" + email + '\'' +
                ", numTel='" + numTel + '\'' +
                ", dateReservation=" + dateReservation +
                '}';
    }



}
