package services;

import entities.Event;
import utils.DB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.sql.SQLException;
import java.util.Date;

public class ServiceEvent implements IService<Event> {
    private Connection con;

    public ServiceEvent() throws SQLException {
        // Créer une instance de la classe DB et obtenir la connexion
        con = DB.getInstance().getConnection();
    }

    @Override
    public void ajouter(Event event) {
        String query = "INSERT INTO event (titre, description, date_debut, date_fin, lieu, prix, image_event, id_category_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setString(1, event.getTitre());
            stmt.setString(2, event.getDescription());
            stmt.setObject(3, event.getDateDebut()); // Utiliser setObject pour LocalDateTime
            stmt.setObject(4, event.getDateFin()); // Utiliser setObject pour LocalDateTime
            stmt.setString(5, event.getLieu());
            stmt.setDouble(6, event.getPrix());
            stmt.setString(7, event.getImageEvent());
            stmt.setInt(8, event.getIdCategory());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void modifier(Event event) {
        String query = "UPDATE event SET titre=?, description=?, date_debut=?, date_fin=?, lieu=?, prix=?, image_event=?, id_category_id=? WHERE id=?";
        try (PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setString(1, event.getTitre());
            stmt.setString(2, event.getDescription());
            stmt.setDate(3, new java.sql.Date(event.getDateDebut().getTime())); // Convertir Date en java.sql.Date
            stmt.setDate(4, new java.sql.Date(event.getDateFin().getTime())); // Convertir Date en java.sql.Date
            stmt.setString(5, event.getLieu());
            stmt.setDouble(6, event.getPrix());
            stmt.setString(7, event.getImageEvent());
            stmt.setInt(8, event.getIdCategory());
            stmt.setInt(9, event.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void supprimer(Event event) {
        String query = "DELETE FROM event WHERE id=?";
        try (PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setInt(1, event.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Event> afficher() {
        List<Event> events = new ArrayList<>();
        String query = "SELECT * FROM event";
        try (PreparedStatement stmt = con.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                int id = rs.getInt("id");
                String titre = rs.getString("titre");
                String description = rs.getString("description");
                Date dateDebut = rs.getDate("date_debut");
                Date dateFin = rs.getDate("date_fin");
                String lieu = rs.getString("lieu");
                double prix = rs.getDouble("prix");
                String imageEvent = rs.getString("image_event");
                int idCategory = rs.getInt("id_category_id");

                Event event = new Event(id, titre, description, dateDebut, dateFin, lieu, prix, imageEvent, idCategory);
                events.add(event);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Vérification des données récupérées
        System.out.println("Events retrieved from database: " + events);

        return events;
    }

    public int getEventIdByTitle(String eventTitle) {
        int eventId = 0;
        String query = "SELECT id FROM event WHERE titre = ?";
        try (PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setString(1, eventTitle);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                eventId = rs.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return eventId;
    }

    public Event getEventById(int eventId) {
        Event event = null;
        String query = "SELECT * FROM event WHERE id = ?";
        try (PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setInt(1, eventId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt("id");
                    String titre = rs.getString("titre");
                    String description = rs.getString("description");
                    Date dateDebut = rs.getDate("date_debut");
                    Date dateFin = rs.getDate("date_fin");
                    String lieu = rs.getString("lieu");
                    double prix = rs.getDouble("prix");
                    String imageEvent = rs.getString("image_event");
                    int idCategory = rs.getInt("id_category_id");

                    event = new Event(id, titre, description, dateDebut, dateFin, lieu, prix, imageEvent, idCategory);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return event;
    }

    public List<Event> rechercherParTitre(String titre) {
        List<Event> events = new ArrayList<>();
        if (titre == null || titre.isEmpty()) {
            // Si le titre est vide, retourner une liste vide
            return events;
        }

        String query = "SELECT * FROM event WHERE titre LIKE ?";
        try (PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setString(1, "%" + titre + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String eventTitre = rs.getString("titre");
                    String description = rs.getString("description");
                    Date dateDebut = rs.getDate("date_debut");
                    Date dateFin = rs.getDate("date_fin");
                    String lieu = rs.getString("lieu");
                    double prix = rs.getDouble("prix");
                    String imageEvent = rs.getString("image_event");
                    int idCategory = rs.getInt("id_category_id");

                    Event event = new Event(id, eventTitre, description, dateDebut, dateFin, lieu, prix, imageEvent, idCategory);
                    events.add(event);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return events;
    }


    public static int countEvents() throws SQLException {
        int count = 0;
        String query = "SELECT COUNT(*) FROM event"; // Remplacez 'event' par le nom de votre table d'événements

        try (Connection conn = DB.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                count = rs.getInt(1); 
            }
        }

        return count;
    }





}
