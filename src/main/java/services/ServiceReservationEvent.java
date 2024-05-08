package services;

import models.ReservationEvent;
import utils.DBConnexion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServiceReservationEvent implements IService<ReservationEvent> {
    private Connection con;

    public ServiceReservationEvent() throws SQLException {
        // Créer une instance de la classe DB et obtenir la connexion
        con = DBConnexion.getInstance().getCnx();
    }

    @Override
    public void ajouter(ReservationEvent reservationEvent) {
        String query = "INSERT INTO reservation_event (nom, email, num_tel, date_reservation, id_event_id) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setString(1, reservationEvent.getNom());
            stmt.setString(2, reservationEvent.getEmail());
            stmt.setString(3, reservationEvent.getNumTel());
            stmt.setDate(4, new java.sql.Date(reservationEvent.getDateReservation().getTime()));
            stmt.setInt(5, reservationEvent.getIdEvent());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void modifier(ReservationEvent reservationEvent) {
        String query = "UPDATE reservation_event SET nom=?, email=?, num_tel=?, date_reservation=?, id_event_id=? WHERE id=?";
        try (PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setString(1, reservationEvent.getNom());
            stmt.setString(2, reservationEvent.getEmail());
            stmt.setString(3, reservationEvent.getNumTel());
            stmt.setDate(4, new java.sql.Date(reservationEvent.getDateReservation().getTime()));
            stmt.setInt(5, reservationEvent.getIdEvent());
            stmt.setInt(6, reservationEvent.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void supprimer(ReservationEvent reservationEvent) {
        String query = "DELETE FROM reservation_event WHERE id=?";
        try (PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setInt(1, reservationEvent.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<ReservationEvent> afficher() {
        List<ReservationEvent> reservationEvents = new ArrayList<>();
        String query = "SELECT * FROM reservation_event";
        try (PreparedStatement stmt = con.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                int id = rs.getInt("id");
                String nom = rs.getString("nom");
                String email = rs.getString("email");
                String numTel = rs.getString("num_tel");
                java.util.Date dateReservation = rs.getDate("date_reservation");
                int idEvent = rs.getInt("id_event_id");

                ReservationEvent reservationEvent = new ReservationEvent(id, idEvent, nom, email, numTel, dateReservation);
                reservationEvents.add(reservationEvent);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reservationEvents;
    }

    // Méthode pour récupérer le titre de l'événement associé à une réservation par son ID
    public String getEventTitleByReservationId(int reservationId) {
        String title = null;
        String query = "SELECT e.titre FROM event e " +
                "INNER JOIN reservation_event r ON e.id = r.id_event_id " +
                "WHERE r.id = ?";
        try (PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setInt(1, reservationId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    title = rs.getString("titre");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return title;
    }

    public List<ReservationEvent> rechercherParNom(String nom) {
        List<ReservationEvent> reservationEvents = new ArrayList<>();
        String query = "SELECT * FROM reservation_event WHERE nom LIKE ?";
        try (PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setString(1, "%" + nom + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    int idEvent = rs.getInt("id_event_id");
                    String email = rs.getString("email");
                    String numTel = rs.getString("num_tel");
                    java.util.Date dateReservation = rs.getDate("date_reservation");

                    ReservationEvent reservationEvent = new ReservationEvent(id, idEvent, nom, email, numTel, dateReservation);
                    reservationEvents.add(reservationEvent);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reservationEvents;
    }

    // Méthode pour filtrer les réservations par date
    public List<ReservationEvent> filtrerParDate(java.time.LocalDate date) {
        List<ReservationEvent> reservationEvents = new ArrayList<>();
        String query = "SELECT * FROM reservation_event WHERE date_reservation = ?";
        try (PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setDate(1, java.sql.Date.valueOf(date));
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    int idEvent = rs.getInt("id_event_id");
                    String nom = rs.getString("nom");
                    String email = rs.getString("email");
                    String numTel = rs.getString("num_tel");
                    java.util.Date dateReservation = rs.getDate("date_reservation");

                    ReservationEvent reservationEvent = new ReservationEvent(id, idEvent, nom, email, numTel, dateReservation);
                    reservationEvents.add(reservationEvent);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reservationEvents;
    }


    public static int countReservations() throws SQLException {
        int count = 0;
        String query = "SELECT COUNT(*) FROM reservation_event"; // Remplacez 'reservation_event' par le nom de votre table de réservations

        try (Connection conn = DBConnexion.getInstance().getCnx();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                count = rs.getInt(1);
            }
        }

        return count;
    }


    public static Map<String, Integer> countReservationsByEvent() throws SQLException {
        Map<String, Integer> reservationsByEvent = new HashMap<>();
        String query = "SELECT e.titre, COUNT(r.id) AS count " +
                "FROM event e " +
                "LEFT JOIN reservation_event r ON e.id = r.id_event_id " +
                "GROUP BY e.id, e.titre";

        try (Connection conn = DBConnexion.getInstance().getCnx();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String eventTitle = rs.getString("titre");
                int reservationCount = rs.getInt("count");
                reservationsByEvent.put(eventTitle, reservationCount);
            }
        }

        return reservationsByEvent;

    }
}