package Services;

import Entity.Reservation;
import Util.MyDB;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ServiceReservation {
    private final Connection connection;

    public ServiceReservation() {
        this.connection = MyDB.getInstance().getConnection();
    }



    public void ajouter(Reservation reservation) throws SQLException {
        String query = "INSERT INTO reservation (idR, nom, email, date, nbr_personne) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, reservation.getRestaurantId());
            preparedStatement.setString(2, reservation.getNom());
            preparedStatement.setString(3, reservation.getEmail());
            preparedStatement.setString(4, reservation.getDate());
            preparedStatement.setInt(5, reservation.getNbrPersonne());
            preparedStatement.executeUpdate();
        }
    }

    public List<Reservation> getAllReservationsForRestaurant(int restaurantId) throws SQLException {
        List<Reservation> reservations = new ArrayList<>();
        String query = "SELECT * FROM reservation WHERE idR = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, restaurantId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    int userId = resultSet.getInt("idR");
                    String nom = resultSet.getString("nom");
                    String email = resultSet.getString("email");
                    String date = resultSet.getString("date");
                    int nbrPersonne = resultSet.getInt("nbr_personne");
                    reservations.add(new Reservation(id, userId, nom, email, date, nbrPersonne));
                }
            }
        }
        return reservations;
    }

    public ObservableList<Reservation> afficher(int restaurantId) throws SQLException {
        List<Reservation> reservations = getAllReservationsForRestaurant(restaurantId);
        return FXCollections.observableArrayList(reservations);
    }

    public String getRestaurantName(int restaurantId) {
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
