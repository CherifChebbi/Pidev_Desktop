package models.ReservationEntity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Reservation {
    private int id;
    private int restaurant_id;
    private String nom;
    private String email;
    private String date;
    private int nbrPersonne;

    public Reservation(int id, int restaurant_id, String nom, String email, String date, int nbrPersonne) {
        this.id = id;
        this.restaurant_id = restaurant_id;
        this.nom = nom;
        this.email = email;
        this.date = date;
        this.nbrPersonne = nbrPersonne;
    }

    public int getId() {
        return id;
    }

    public int getrestaurant_id() {
        return restaurant_id;
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

    public void setrestaurant_id(int restaurant_id) {
        this.restaurant_id = restaurant_id;
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
    public Reservation(int restaurant_id, String nom, String email, String date, int nbrPersonne) {
        this.restaurant_id = restaurant_id;
        this.nom = nom;
        this.email = email;
        this.date = date;
        this.nbrPersonne = nbrPersonne;
    }

    public String getRestaurantName(Connection connection, int restaurant_id) {
        try {
            String query = "SELECT nom FROM restaurant WHERE id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1, restaurant_id);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getString("nom");
                    }
                }
            }
            throw new SQLException("Restaurant not found with ID: " + restaurant_id);
        } catch (SQLException e) {
            e.printStackTrace();
            return null; // Handle the exception appropriately
        }
    }





}
