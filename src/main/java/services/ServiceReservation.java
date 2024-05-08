package services;

import models.Reservation;
import utils.MyDB;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.LocalDate;

public class ServiceReservation {

    private Connection connection;

    public ServiceReservation() {
        connection = MyDB.getCon();
    }

    public void ajouter(Reservation reservation) throws SQLException {
        String req = "INSERT INTO reservation (hebergement, nom, email, date, nbr_personne) VALUES (70, ?, ?, ?, ?)";

        try (PreparedStatement pre = connection.prepareStatement(req)) {
            pre.setString(1, reservation.getNom());
            pre.setString(2, reservation.getEmail());

            if (reservation.getDate() != null) {
                java.sql.Date sqlDate = java.sql.Date.valueOf(reservation.getDate());
                pre.setDate(3, sqlDate);
            } else {
                throw new IllegalArgumentException("Reservation date cannot be null");
            }

            pre.setInt(4, reservation.getNbrPersonne());
            pre.executeUpdate();
            System.out.println("Reservation added successfully!");
        }
    }

    public ObservableList<Reservation> afficher() throws SQLException {
        String req = "SELECT * FROM reservation WHERE hebergement=5";
        ObservableList<Reservation> list = FXCollections.observableArrayList();
        try (PreparedStatement pre = connection.prepareStatement(req)) {
            ResultSet res = pre.executeQuery();
            while (res.next()) {
                Reservation r = new Reservation();
                r.setId(res.getInt("id"));
                r.setNom(res.getString("nom"));
                r.setEmail(res.getString("email"));
                r.setDate(res.getDate("date").toLocalDate());
                list.add(r);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public void modifier(Reservation reservation) throws SQLException {
        // Implementation for updating a reservation
    }

    public void supprimer(int reservationId) throws SQLException {
        // Implementation for deleting a reservation
    }

    public static void ajouterReservation(String nom, String email, LocalDate date, int nbrPersonne) throws SQLException {
        // Implementation for adding a reservation
    }

}
