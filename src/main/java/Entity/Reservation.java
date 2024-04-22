package Entity;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.SimpleStringProperty;

public class Reservation {
    private int id;
    private IntegerProperty idR;
    private StringProperty nom;
    private StringProperty email;
    private StringProperty date;
    private int nbrPersonne;
    private final IntegerProperty reservationId;
    private StringProperty restaurantName;

    // Constructors
    public Reservation(int reservationId, int idR) {
        this.reservationId = new SimpleIntegerProperty(reservationId);
        this.idR = new SimpleIntegerProperty(idR);
        this.nom = new SimpleStringProperty("");
        this.email = new SimpleStringProperty("");
        this.date = new SimpleStringProperty("");
        this.restaurantName = new SimpleStringProperty(""); // Initialize restaurant name property
    }

    public Reservation(int reservationId, int idR, String nom, String email, String date, int nbrPersonne, String restaurantName) {
        this.reservationId = new SimpleIntegerProperty(reservationId);
        this.idR = new SimpleIntegerProperty(idR);
        this.nom = new SimpleStringProperty(nom);
        this.email = new SimpleStringProperty(email);
        this.date = new SimpleStringProperty(date);
        this.nbrPersonne = nbrPersonne;
        this.restaurantName = new SimpleStringProperty(restaurantName);
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdR() {
        return idR.get();
    }

    public void setIdR(int idR) {
        this.idR.set(idR);
    }

    public IntegerProperty idRProperty() {
        return idR;
    }

    public String getNom() {
        return nom.get();
    }

    public void setNom(String nom) {
        this.nom.set(nom);
    }

    public StringProperty nomProperty() {
        return nom;
    }

    public String getEmail() {
        return email.get();
    }

    public void setEmail(String email) {
        this.email.set(email);
    }

    public StringProperty emailProperty() {
        return email;
    }

    public String getDate() {
        return date.get();
    }

    public void setDate(String date) {
        this.date.set(date);
    }

    public StringProperty dateProperty() {
        return date;
    }

    public int getNbrPersonne() {
        return nbrPersonne;
    }

    public void setNbrPersonne(int nbrPersonne) {
        this.nbrPersonne = nbrPersonne;
    }

    public IntegerProperty NbrPersonneProperty() {
        return new SimpleIntegerProperty(nbrPersonne);
    }

    public int getReservationId() {
        return reservationId.get();
    }

    public void setReservationId(int reservationId) {
        this.reservationId.set(reservationId);
    }

    public IntegerProperty reservationIdProperty() {
        return reservationId;
    }

    public String getRestaurantName() {
        return restaurantName.get();
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName.set(restaurantName);
    }

    public StringProperty restaurantNameProperty() {
        return restaurantName;
    }

    public int getRestaurantId() {
        return idR.get();
    }
}
