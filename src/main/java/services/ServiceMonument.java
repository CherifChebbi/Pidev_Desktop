package services;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import models.Monument;
import utils.DBConnexion;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

    public class ServiceMonument implements CRUD<Monument> {
        private Connection cnx;
        @FXML
        private TableView<Monument> monumentTable;

        public ServiceMonument() {
            cnx = DBConnexion.getInstance().getCnx();
        }

        @Override
        public void Add(Monument Monument) throws SQLException {
            String req = "INSERT INTO monument(nom_monument, img_monument, desc_monument,latitude, longitude) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement ps = cnx.prepareStatement(req);

            ps.setString(1, Monument.getNom_monument());
            ps.setString(2, Monument.getImg_monument());
            ps.setString(3, Monument.getDesc_monument());
            ps.setDouble(4, Monument.getLatitude());
            ps.setDouble(5, Monument.getLongitude());

            ps.executeUpdate();
            ps.close();
        }

        @Override
        public void Update(Monument Monument) throws SQLException {
            String req = "UPDATE monument SET nom_monument=?, img_monument=?, desc_monument=?, latitude=?, longitude=? WHERE id_monument=?";
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setString(1, Monument.getNom_monument());
            ps.setString(2, Monument.getImg_monument());
            ps.setString(3, Monument.getDesc_monument());
            ps.setDouble(4, Monument.getLatitude());
            ps.setDouble(5, Monument.getLongitude());
            ps.setInt(6, Monument.getId_monument());

            ps.executeUpdate();
            ps.close();
        }

        @Override
        public void Delete(Monument Monument) throws SQLException {
            String req = "DELETE FROM monument WHERE id_monument=?";
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setInt(1, Monument.getId_monument());
            ps.executeUpdate();
            ps.close();
        }

        @Override
        public List<Monument> Afficher() throws SQLException {
            List<Monument> monumentList = new ArrayList<>();

            String req = "SELECT * FROM monument";
            Statement st = cnx.createStatement();

            ResultSet rs = st.executeQuery(req);
            while (rs.next()) {
                Monument p = new Monument();

                p.setId_monument(rs.getInt("id_monument"));
                p.setId_ville(rs.getInt("id_ville"));
                p.setNom_monument(rs.getString("nom_monument"));
                p.setImg_monument(rs.getString("img_monument"));
                p.setDesc_monument(rs.getString("desc_monument"));
                p.setLatitude(rs.getDouble("latitude"));
                p.setLongitude(rs.getDouble("longitude"));

                monumentList.add(p);
            }

            rs.close();
            st.close();

            return monumentList;
        }
        public List<Monument> rechercherParNom(String nom) throws SQLException {
            List<Monument> monumentList = new ArrayList<>();

            String req = "SELECT * FROM monument WHERE nom_monument LIKE ?";
            PreparedStatement ps = cnx.prepareStatement(req);
            ps.setString(1, "%" + nom + "%");

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Monument p = new Monument();

                p.setId_monument(rs.getInt("id_monument"));
                p.setId_ville(rs.getInt("id_ville"));
                p.setNom_monument(rs.getString("nom_monument"));
                p.setImg_monument(rs.getString("img_monument"));
                p.setDesc_monument(rs.getString("desc_monument"));
                p.setLatitude(rs.getDouble("latitude"));
                p.setLongitude(rs.getDouble("longitude"));

                monumentList.add(p);
            }

            rs.close();
            ps.close();

            return monumentList;
        }
}
