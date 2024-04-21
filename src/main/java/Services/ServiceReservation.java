package Services;

import Entity.Hebergement;
import Entity.Reservation;
import Util.MyDB;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

public class ServiceReservation {

    private Connection connection;
    public ServiceReservation(){connection= MyDB.getCon();}
    public void ajouter(Reservation reservation) throws SQLException {
        String req = "INSERT INTO reservation (hebergement, nom, email, date, nbr_personne) VALUES (5, ?, ?, ?, ?)";

        try (PreparedStatement pre = connection.prepareStatement(req)) {
            pre.setString(1, reservation.getNom());
            pre.setString(2, reservation.getEmail());

            // Check if the reservation date is not null before converting it
            if (reservation.getDate() != null) {
                java.sql.Date sqlDate = java.sql.Date.valueOf(reservation.getDate());
                pre.setDate(3, sqlDate);
            } else {
                // Handle the case where the reservation date is null
                throw new IllegalArgumentException("Reservation date cannot be null");
            }

            pre.setInt(4, reservation.getNbrPersonne());
            pre.executeUpdate();
            System.out.println("Reservation added successfully!");
        }
    }


    public ObservableList<Reservation> afficher() throws SQLException{
        String req ="SELECT * FROM hebergement WHERE hebergement=5";
        ObservableList<Reservation> list = FXCollections.observableArrayList();
        PreparedStatement pre= connection.prepareStatement(req);
        ResultSet res = pre.executeQuery();
        while (res.next()) {
            Reservation r= new Reservation();
            r.setId(res.getInt("id"));
            r.setNom(res.getString("nom"));
            r.setEmail(res.getString("email"));
            r.setDate(res.getDate("date").toLocalDate());
            list.add(r);
        }
        return list;
    }



    public static void ajouterReservation(String nom, String email, LocalDate date, int nbrPersonne) throws SQLException {
        Connection con = MyDB.getCon();
        String query = "INSERT INTO reservation (nhebergement,om, email, date, nbr_personne) VALUES (5,?, ?, ?, ?)";
        PreparedStatement pst = con.prepareStatement(query);
        pst.setString(1, nom);
        pst.setString(2, email);
        pst.setObject(3, date);
        pst.setInt(4, nbrPersonne);
        pst.executeUpdate();
    }


}
