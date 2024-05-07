package services;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.Plat;
import models.Restaurant;
import utils.DBConnexion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ServicePlat {
    private Connection connection;

    public ServicePlat() {
        // Retrieve the database connection
        connection = DBConnexion.getInstance().getCnx();
    }

    public ObservableList<Plat> afficher() throws SQLException {
        List<Plat> plats = getAllPlats();
        return FXCollections.observableArrayList(plats);
    }

    public List<Plat> getPlatsBySearchCriteria(String nom) throws SQLException {
        List<Plat> plats = new ArrayList<>();
        String sql = "SELECT * FROM plat WHERE nom LIKE ?";
        try (Connection connection = DBConnexion.getInstance().getCnx();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, "%" + nom + "%");
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Plat plat = new Plat();
                    plat.setNom(resultSet.getString("nom"));
                    plat.setImage(resultSet.getString("image"));
                    plat.setPrix(resultSet.getFloat("prix"));
                    plats.add(plat);
                }
            }
        }
        return plats;
    }


    public List<Plat> getAllPlats() throws SQLException {
        String query = "SELECT * FROM plat";
        List<Plat> plats = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Plat plat = new Plat();
                plat.setId(resultSet.getInt("id"));
                plat.setNom(resultSet.getString("nom"));
                plat.setImage(resultSet.getString("image"));
                plat.setPrix(resultSet.getFloat("prix"));

                // Fetch the Restaurant details by ID
                int restaurantId = resultSet.getInt("restaurant_id");
                Restaurant restaurant = fetchRestaurantById(restaurantId);
                plat.setRestaurant(restaurant);

                plats.add(plat);
            }
        }
        return plats;
    }

    private Restaurant fetchRestaurantById(int id) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM restaurant WHERE id = ?")) {
            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    Restaurant restaurant = new Restaurant();
                    restaurant.setid(resultSet.getInt("id"));
                    restaurant.setNom(resultSet.getString("nom"));
                    // Set other restaurant properties as needed
                    return restaurant;
                }
            }
        }
        return null; // Return null if no restaurant found with the given ID
    }

    public void ajouter(Plat plat) throws SQLException {
        String req = "INSERT INTO plat (nom, image, prix, restaurant_id) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pre = connection.prepareStatement(req)) {
            pre.setString(1, plat.getNom());
            pre.setString(2, plat.getImage());
            pre.setFloat(3, plat.getPrix());
            pre.setInt(4, plat.getRestaurant_id()); // Use restaurant_id instead of plat.getRestaurant().getid()
            pre.executeUpdate();
            System.out.println("Plat added successfully!");
        }
    }

    public void modifier(Plat plat) throws SQLException {
        String req = "UPDATE plat SET nom=?, image=?, prix=?, restaurant_id=? WHERE id=?";
        try (PreparedStatement pre = connection.prepareStatement(req)) {
            pre.setString(1, plat.getNom());
            pre.setString(2, plat.getImage());
            pre.setFloat(3, plat.getPrix());
            pre.setInt(4, plat.getRestaurant_id()); // Use restaurant_id instead of plat.getRestaurant().getid()
            pre.setInt(5, plat.getId());
            pre.executeUpdate();
            System.out.println("Plat updated successfully!");
        }
    }


    public void supprimer(int id) throws SQLException {
        String req = "DELETE FROM plat WHERE id=?";
        try (PreparedStatement pre = connection.prepareStatement(req)) {
            pre.setInt(1, id);
            pre.executeUpdate();
            System.out.println("Plat deleted successfully!");
        }
    }
}
