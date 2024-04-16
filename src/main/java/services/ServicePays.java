package services;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import models.Pays;
import utils.DBConnexion;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServicePays implements CRUD<Pays> {
    private Connection cnx;
    @FXML
    private TableView<Pays> paysTable;

    public ServicePays() {
        cnx = DBConnexion.getInstance().getCnx();
    }

    @Override
    public void Add(Pays pays) throws SQLException {
        String req = "INSERT INTO pays(nom_pays, img_pays, desc_pays, langue, continent, nb_villes, latitude, longitude) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = cnx.prepareStatement(req);

        ps.setString(1, pays.getNom_pays());
        ps.setString(2, pays.getImg_pays());
        ps.setString(3, pays.getDesc_pays());
        ps.setString(4, pays.getLangue());
        ps.setString(5, pays.getContinent());
        ps.setInt(6, pays.getNb_villes());
        ps.setDouble(7, pays.getLatitude());
        ps.setDouble(8, pays.getLongitude());

        ps.executeUpdate();
        ps.close();
    }

    @Override
    public void Update(Pays pays) throws SQLException {
        String req = "UPDATE pays SET nom_pays=?, img_pays=?, desc_pays=?, langue=?, continent=?, nb_villes=?, latitude=?, longitude=? WHERE id_pays=?";
        PreparedStatement ps = cnx.prepareStatement(req);
        ps.setString(1, pays.getNom_pays());
        ps.setString(2, pays.getImg_pays());
        ps.setString(3, pays.getDesc_pays());
        ps.setString(4, pays.getLangue());
        ps.setString(5, pays.getContinent());
        ps.setInt(6, pays.getNb_villes());
        ps.setDouble(7, pays.getLatitude());
        ps.setDouble(8, pays.getLongitude());
        ps.setInt(9, pays.getId_pays());

        ps.executeUpdate();
        ps.close();
    }

    @Override
    public void Delete(Pays pays) throws SQLException {
        String req = "DELETE FROM pays WHERE id_pays=?";
        PreparedStatement ps = cnx.prepareStatement(req);
        ps.setInt(1, pays.getId_pays());
        ps.executeUpdate();
        ps.close();
    }

    @Override
    public List<Pays> Afficher() throws SQLException {
        List<Pays> PaysList = new ArrayList<>();

        String req = "SELECT * FROM pays";
        Statement st = cnx.createStatement();

        ResultSet rs = st.executeQuery(req);
        while (rs.next()) {
            Pays p = new Pays();

            p.setId_pays(rs.getInt("id_pays"));
            p.setNom_pays(rs.getString("nom_pays"));
            p.setImg_pays(rs.getString("img_pays"));
            p.setDesc_pays(rs.getString("desc_pays"));
            p.setLangue(rs.getString("langue"));
            p.setContinent(rs.getString("continent"));
            p.setNb_villes(rs.getInt("nb_villes"));
            p.setLatitude(rs.getDouble("latitude"));
            p.setLongitude(rs.getDouble("longitude"));

            PaysList.add(p);
        }

        rs.close();
        st.close();

        return PaysList;
    }
    public List<Pays> rechercherParNom(String nom) throws SQLException {
        List<Pays> PaysList = new ArrayList<>();

        String req = "SELECT * FROM pays WHERE nom_pays LIKE ?";
        PreparedStatement ps = cnx.prepareStatement(req);
        ps.setString(1, "%" + nom + "%");

        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Pays p = new Pays();

            p.setId_pays(rs.getInt("id_pays"));
            p.setNom_pays(rs.getString("nom_pays"));
            p.setImg_pays(rs.getString("img_pays"));
            p.setDesc_pays(rs.getString("desc_pays"));
            p.setLangue(rs.getString("langue"));
            p.setContinent(rs.getString("continent"));
            p.setNb_villes(rs.getInt("nb_villes"));
            p.setLatitude(rs.getDouble("latitude"));
            p.setLongitude(rs.getDouble("longitude"));

            PaysList.add(p);
        }

        rs.close();
        ps.close();

        return PaysList;
    }
    public List<Pays> filterByVilles(int minVilles, int maxVilles) throws SQLException {
        List<Pays> filteredPays = new ArrayList<>();

        String req = "SELECT * FROM pays WHERE nb_villes >= ? AND nb_villes <= ?";
        PreparedStatement ps = cnx.prepareStatement(req);
        ps.setInt(1, minVilles);
        ps.setInt(2, maxVilles);

        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Pays p = new Pays();

            p.setId_pays(rs.getInt("id_pays"));
            p.setNom_pays(rs.getString("nom_pays"));
            p.setImg_pays(rs.getString("img_pays"));
            p.setDesc_pays(rs.getString("desc_pays"));
            p.setLangue(rs.getString("langue"));
            p.setContinent(rs.getString("continent"));
            p.setNb_villes(rs.getInt("nb_villes"));
            p.setLatitude(rs.getDouble("latitude"));
            p.setLongitude(rs.getDouble("longitude"));

            filteredPays.add(p);
        }

        rs.close();
        ps.close();

        return filteredPays;
    }






}
