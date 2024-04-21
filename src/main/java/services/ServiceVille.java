package services;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import models.Ville;
import utils.DBConnexion;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceVille implements CRUD<Ville> {
    private Connection cnx;
    @FXML
    private TableView<Ville> VilleTable;
    private ServicePays servicePays;

    public ServiceVille() {
        cnx = DBConnexion.getInstance().getCnx();
    }

    @Override
    public void Add(Ville ville) throws SQLException {
        String req = "INSERT INTO ville(id_pays,nom_ville, img_ville, desc_ville, nb_monuments, latitude, longitude) VALUES (?, ?, ?, ?, ?, ?,?)";
        try (PreparedStatement ps = cnx.prepareStatement(req)) {
            ps.setInt(1, ville.getId_pays());
            ps.setString(2, ville.getNom_ville());
            ps.setString(3, ville.getImg_ville());
            ps.setString(4, ville.getDesc_ville());
            ps.setInt(5, ville.getNb_monuments());
            ps.setDouble(6, ville.getLatitude());
            ps.setDouble(7, ville.getLongitude());

            ps.executeUpdate();
        }
    }

    @Override
    public void Update(Ville ville) throws SQLException {
        String req = "UPDATE ville SET id_pays=?, nom_ville=?, img_ville=?, desc_ville=?,  nb_monuments=?, latitude=?, longitude=?  WHERE id_ville=?";
        PreparedStatement ps = cnx.prepareStatement(req);

        ps.setInt(1, ville.getId_pays());
        ps.setString(2, ville.getNom_ville());
        ps.setString(3, ville.getImg_ville());
        ps.setString(4, ville.getDesc_ville());
        ps.setInt(5, ville.getNb_monuments());
        ps.setDouble(6, ville.getLatitude());
        ps.setDouble(7, ville.getLongitude());
        ps.setInt(8, ville.getId_ville());


        ps.executeUpdate();
        ps.close();
    }

    @Override
    public void Delete(Ville ville) throws SQLException {
        String req = "DELETE FROM ville WHERE id_ville=?";
        PreparedStatement ps = cnx.prepareStatement(req);
        ps.setInt(1, ville.getId_ville());
        ps.executeUpdate();
        ps.close();
    }

    @Override
    public List<Ville> Afficher() throws SQLException {
        List<Ville> villeList = new ArrayList<>();

        String req = "SELECT * FROM ville";
        Statement st = cnx.createStatement();

        ResultSet rs = st.executeQuery(req);
        while (rs.next()) {
            Ville p = new Ville();

            p.setId_ville(rs.getInt("id_ville"));
            p.setId_pays(rs.getInt("id_pays"));
            p.setNom_ville(rs.getString("nom_ville"));
            p.setImg_ville(rs.getString("img_ville"));
            p.setDesc_ville(rs.getString("desc_ville"));
            p.setNb_monuments(rs.getInt("nb_monuments"));
            p.setLatitude(rs.getDouble("latitude"));
            p.setLongitude(rs.getDouble("longitude"));

            villeList.add(p);
        }

        rs.close();
        st.close();

        return villeList;
    }
    public List<Ville> rechercherParNom(String nom) throws SQLException {
        List<Ville> VilleList = new ArrayList<>();

        String req = "SELECT * FROM ville WHERE nom_ville LIKE ?";
        PreparedStatement ps = cnx.prepareStatement(req);
        ps.setString(1, "%" + nom + "%");

        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Ville p = new Ville();

            p.setId_ville(rs.getInt("id_ville"));
            p.setId_pays(rs.getInt("id_pays"));
            p.setNom_ville(rs.getString("nom_ville"));
            p.setImg_ville(rs.getString("img_ville"));
            p.setDesc_ville(rs.getString("desc_ville"));
            p.setNb_monuments(rs.getInt("nb_monuments"));
            p.setLatitude(rs.getDouble("latitude"));
            p.setLongitude(rs.getDouble("longitude"));

            VilleList.add(p);
        }

        rs.close();
        ps.close();

        return VilleList;
    }
    public List<Ville> filterByVilles(int minMonuments, int maxMonuments) throws SQLException {
        List<Ville> filteredville = new ArrayList<>();

        String req = "SELECT * FROM ville WHERE nb_monuments >= ? AND nb_monuments <= ?";
        PreparedStatement ps = cnx.prepareStatement(req);
        ps.setInt(1, minMonuments);
        ps.setInt(2, maxMonuments);

        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Ville p = new Ville();

            p.setId_ville(rs.getInt("id_ville"));
            p.setId_pays(rs.getInt("id_pays"));
            p.setNom_ville(rs.getString("nom_ville"));
            p.setImg_ville(rs.getString("img_ville"));
            p.setDesc_ville(rs.getString("desc_ville"));
            p.setNb_monuments(rs.getInt("nb_monuments"));
            p.setLatitude(rs.getDouble("latitude"));
            p.setLongitude(rs.getDouble("longitude"));

            filteredville.add(p);
        }

        rs.close();
        ps.close();

        return filteredville;
    }






}
