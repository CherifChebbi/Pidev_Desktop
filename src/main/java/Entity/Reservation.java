package Entity;

import Services.ServiceReservation;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Reservation {
    private int id;
    private int restaurantId;
    private String nom;
    private String email;
    private String date;
    private int nbrPersonne;

    public Reservation(int id, int restaurantId, String nom, String email, String date, int nbrPersonne) {
        this.id = id;
        this.restaurantId = restaurantId;
        this.nom = nom;
        this.email = email;
        this.date = date;
        this.nbrPersonne = nbrPersonne;
    }

    public int getId() {
        return id;
    }

    public int getRestaurantId() {
        return restaurantId;
    }

    public String getNom() {
        return nom;
    }

    public String getEmail() {
        return email;
    }

    public String getDate() {
        return date;
    }

    public int getNbrPersonne() {
        return nbrPersonne;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setRestaurantId(int restaurantId) {
        this.restaurantId = restaurantId;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setNbrPersonne(int nbrPersonne) {
        this.nbrPersonne = nbrPersonne;
    }
    public Reservation(int restaurantId, String nom, String email, String date, int nbrPersonne) {
        this.restaurantId = restaurantId;
        this.nom = nom;
        this.email = email;
        this.date = date;
        this.nbrPersonne = nbrPersonne;
    }

    public String getRestaurantName(Connection connection, int restaurantId) {
        try {
            String query = "SELECT nom FROM restaurant WHERE id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, restaurantId);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getString("nom");
                    }
                }
            }
            throw new SQLException("Restaurant not found with ID: " + restaurantId);
        } catch (SQLException e) {
            e.printStackTrace();
            return null; // Handle the exception appropriately
        }
    }





}
