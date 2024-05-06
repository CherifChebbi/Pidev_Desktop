package Services;

import Entity.Notification;
import Entity.Plat;
import Entity.Restaurant;
import Util.MyDB;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ServiceRestaurant implements Irestaurant<Restaurant> {

    private Connection connection;

    private NotificationService notificationService;




    public ServiceRestaurant() {
        connection = MyDB.getInstance().getConnection();
        notificationService = new NotificationService();
    }


    public List<Restaurant> getRecommendedRestaurants() {
        List<Restaurant> allRestaurants = getAllRestaurants();
        List<Restaurant> recommendedRestaurants = new ArrayList<>();

        for (Restaurant restaurant : allRestaurants) {
            // Check if the restaurant has many plates (e.g., more than 5)
            if (getPlatsForRestaurant(restaurant.getIdR()).size() > 5) {
                recommendedRestaurants.add(restaurant);
            }
        }

        return recommendedRestaurants;
    }




    @Override
    public void ajouter(Restaurant restaurant) {
        String req = "INSERT INTO restaurant (nom, localisation, image, description) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pre = connection.prepareStatement(req)) {
            pre.setString(1, restaurant.getNom());
            pre.setString(2, restaurant.getLocalisataion());
            pre.setString(3, restaurant.getImage());
            pre.setString(4, restaurant.getDescription());

            pre.executeUpdate();
            System.out.println("Restaurant added successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle SQL exception
        }
        Notification notification = new Notification("Restaurant added: " + restaurant.getNom(), LocalDateTime.now());
        notificationService.addNotification(notification);
    }

    @Override
    public void modifier(Restaurant restaurant) {
        String req = "UPDATE restaurant SET nom=?, localisation=?, image=?, description=? WHERE idR=?";
        try (Connection connection = MyDB.getInstance().getConnection();
             PreparedStatement pre = connection.prepareStatement(req)) {
            pre.setString(1, restaurant.getNom());
            pre.setString(2, restaurant.getLocalisataion());
            pre.setString(3, restaurant.getImage());
            pre.setString(4, restaurant.getDescription());
            pre.setInt(5, restaurant.getIdR());

            int affectedRows = pre.executeUpdate();
            if (affectedRows == 1) {
                System.out.println("Restaurant updated successfully!");
            } else {
                System.out.println("Failed to update restaurant. No rows affected.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle SQL exception
        }
    }

    @Override
    public void supprimer(Restaurant restaurant) {
        String deleteReservationsQuery = "DELETE FROM reservation WHERE idR = ?";
        String deleteRestaurantQuery = "DELETE FROM restaurant WHERE idR = ?";

        try (Connection connection = MyDB.getInstance().getConnection();
             PreparedStatement deleteReservationsStatement = connection.prepareStatement(deleteReservationsQuery);
             PreparedStatement deleteRestaurantStatement = connection.prepareStatement(deleteRestaurantQuery)) {

            deleteReservationsStatement.setInt(1, restaurant.getIdR());
            deleteReservationsStatement.executeUpdate();

            deleteRestaurantStatement.setInt(1, restaurant.getIdR());
            int affectedRows = deleteRestaurantStatement.executeUpdate();

            if (affectedRows == 1) {
                System.out.println("Restaurant deleted successfully!");
            } else {
                System.out.println("Failed to delete restaurant. No rows affected.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle SQL exception
        }
    }


    public ObservableList<Restaurant> afficher(String nameFilter, String locationFilter) {
        String query = "SELECT * FROM restaurant";
        List<Restaurant> restaurants = new ArrayList<>();

        if (nameFilter != null && !nameFilter.isEmpty()) {
            query += " WHERE nom LIKE '%" + nameFilter + "%'";
        }
        if (locationFilter != null && !locationFilter.isEmpty()) {
            if (nameFilter != null && !nameFilter.isEmpty()) {
                query += " AND";
            } else {
                query += " WHERE";
            }
            query += " localisation LIKE '%" + locationFilter + "%'";
        }

        try (Connection connection = MyDB.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                Restaurant restaurant = new Restaurant();
                restaurant.setIdR(resultSet.getInt("idR"));
                restaurant.setNom(resultSet.getString("nom"));
                restaurant.setLocalisataion(resultSet.getString("localisation"));
                restaurant.setImage(resultSet.getString("image"));
                restaurant.setDescription(resultSet.getString("description"));
                restaurants.add(restaurant);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle SQL exception
        }

        return FXCollections.observableArrayList(restaurants);
    }

    public ObservableList<Restaurant> afficher() {
        return afficher(null, null);
    }

    public List<Restaurant> getAllRestaurants() {
        String query = "SELECT * FROM restaurant";
        List<Restaurant> restaurants = new ArrayList<>();

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Restaurant restaurant = new Restaurant();
                restaurant.setIdR(resultSet.getInt("idR"));
                restaurant.setNom(resultSet.getString("nom"));
                restaurant.setLocalisataion(resultSet.getString("localisation"));
                restaurant.setImage(resultSet.getString("image"));
                restaurant.setDescription(resultSet.getString("description"));
                restaurants.add(restaurant);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle SQL exception
        }

        return restaurants;
    }

    public List<String> getAllRestaurantNames() {
        List<String> restaurantNames = new ArrayList<>();
        // Dummy data for demonstration
        restaurantNames.add("Restaurant 1");
        restaurantNames.add("Restaurant 2");
        restaurantNames.add("Restaurant 3");
        return restaurantNames;
    }

    public int getRestaurantIdByName(String name) {
        int restaurantId = -1;
        String query = "SELECT idR FROM restaurant WHERE nom = ?";
        try (Connection connection = MyDB.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, name);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    restaurantId = resultSet.getInt("idR");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle SQL exception
        }
        return restaurantId;
    }


    public List<Restaurant> afficher(String nameFilter, String locationFilter, float minPrice, float maxPrice) {
        String query = "SELECT DISTINCT r.* FROM restaurant r JOIN plat p ON r.idR = p.idR WHERE p.prix BETWEEN ? AND ?";
        List<Restaurant> restaurants = new ArrayList<>();

        if (nameFilter != null && !nameFilter.isEmpty()) {
            query += " AND r.nom LIKE '%" + nameFilter + "%'";
        }
        if (locationFilter != null && !locationFilter.isEmpty()) {
            query += " AND r.localisation LIKE '%" + locationFilter + "%'";
        }

        try (Connection connection = MyDB.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setFloat(1, minPrice);
            preparedStatement.setFloat(2, maxPrice);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Restaurant restaurant = new Restaurant();
                restaurant.setIdR(resultSet.getInt("idR"));
                restaurant.setNom(resultSet.getString("nom"));
                restaurant.setLocalisataion(resultSet.getString("localisation"));
                restaurant.setImage(resultSet.getString("image"));
                restaurant.setDescription(resultSet.getString("description"));
                restaurants.add(restaurant);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle SQL exception
        }

        return restaurants;
    }




    public void insertPlat(Plat plat) {
        try (Connection connection = MyDB.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO plat (id, nom, image, prix) VALUES (?, ?, ?, ?)")) {
            preparedStatement.setInt(1, plat.getId());
            preparedStatement.setString(2, plat.getNom());
            preparedStatement.setString(3, plat.getImage());
            preparedStatement.setFloat(4, plat.getPrix());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle SQL exception
        }
    }
    public List<Plat> getPlatsForRestaurant(int restaurantId) {
        String query = "SELECT * FROM plat WHERE idR = ?";
        List<Plat> plats = new ArrayList<>();

        try (Connection connection = MyDB.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, restaurantId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Plat plat = new Plat();
                plat.setId(resultSet.getInt("id"));
                plat.setNom(resultSet.getString("nom"));
                plat.setImage(resultSet.getString("image"));
                plat.setPrix(resultSet.getFloat("prix"));
                plats.add(plat);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle SQL exception
        }

        return plats;
    }




}
