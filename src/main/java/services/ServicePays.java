package services;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import models.Pays;
import utils.DBConnexion;

import javax.swing.text.html.ImageView;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.image.Image;


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

        // Récupérer le nom de l'image du pays
        String imageName = pays.getImg_pays();

        // Construire le chemin d'accès complet de l'image
        String imagePath = "C:/Users/cheri/Documents/-- ESPRIT --/3eme/--- SEMESTRE  2 ----/-- PI_Java --/Gest_Pays/src/main/resources/upload/" + imageName;

        // Supprimer le fichier image
        File imageFile = new File(imagePath);
        if (imageFile.exists()) {
            if (imageFile.delete()) {
                System.out.println("L'image a été supprimée avec succès.");
            } else {
                System.out.println("Impossible de supprimer l'image.");
            }
        } else {
            System.out.println("L'image n'a pas été trouvée.");
        }

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

    // Méthode pour récupérer tous les pays depuis la base de données
    public List<Pays> getAll() throws SQLException {
        List<Pays> paysList = new ArrayList<>();

        cnx = DBConnexion.getInstance().getCnx();
        String query = "SELECT * FROM pays";
        try (PreparedStatement statement = cnx.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            // Parcourir le résultat de la requête
            while (resultSet.next()) {
                // Créer un objet Pays à partir des données du résultat
                Pays pays = new Pays(resultSet.getString("nom_pays"),
                        resultSet.getString("img_pays"),
                        resultSet.getString("desc_pays"),
                        resultSet.getString("langue"),
                        resultSet.getString("continent"),
                        resultSet.getDouble("latitude"),
                        resultSet.getDouble("longitude"));
                // Ajouter le pays à la liste
                paysList.add(pays);
            }
            return paysList;
        }
    }
    public Pays getPaysByName(String nomPays) throws SQLException {
        cnx = DBConnexion.getInstance().getCnx();
                PreparedStatement ps = null;
        ResultSet rs = null;
        Pays pays = null;


            String query = "SELECT * FROM pays WHERE nom_pays = ?";
            ps = cnx.prepareStatement(query);
            ps.setString(1, nomPays);
            rs = ps.executeQuery();

            if (rs.next()) {
                // Si un pays avec le nom donné est trouvé, créer un objet Pays correspondant
                pays = new Pays(
                        rs.getInt("id_pays"),
                        rs.getString("nom_pays"),
                        rs.getString("img_pays"),
                        rs.getString("desc_pays"),
                        rs.getString("langue"),
                        rs.getString("continent"),
                        rs.getDouble("latitude"),
                        rs.getDouble("longitude"));
            }

        return pays;
    }
    public void updateNbVilles(Pays pays) throws SQLException {
        // Préparez votre requête SQL
        String query = "UPDATE pays SET nb_villes = ? WHERE id_pays = ?";

        // Obtenez une connexion à la base de données
        Connection cnx = DBConnexion.getInstance().getCnx();

        try (PreparedStatement ps = cnx.prepareStatement(query)) {
            // Définissez les paramètres de la requête
            ps.setInt(1, pays.getNb_villes());
            ps.setInt(2, pays.getId_pays());

            // Exécutez la requête
            ps.executeUpdate();
        } catch (SQLException e) {
            // Gérez les exceptions ou renvoyez-les à l'appelant
            throw new SQLException("Erreur lors de la mise à jour du nombre de villes pour le pays avec l'ID : " + pays.getId_pays(), e);
        }
    }
    public Pays getPaysById(int id) throws SQLException {
        // Initialisez la variable de pays à null
        Pays pays = null;

        // Préparez votre requête SQL
        String query = "SELECT * FROM pays WHERE id_pays = ?";

        // Obtenez une connexion à la base de données
        Connection cnx = DBConnexion.getInstance().getCnx();

        try (PreparedStatement ps = cnx.prepareStatement(query)) {
            // Définissez le paramètre de la requête
            ps.setInt(1, id);

            // Exécutez la requête
            try (ResultSet rs = ps.executeQuery()) {
                // Si un pays avec l'ID donné est trouvé, créez un objet Pays correspondant
                if (rs.next()) {
                    pays = new Pays(
                            rs.getInt("id_pays"),
                            rs.getString("nom_pays"),
                            rs.getString("img_pays"),
                            rs.getString("desc_pays"),
                            rs.getString("langue"),
                            rs.getString("continent"),
                            rs.getDouble("latitude"),
                            rs.getDouble("longitude")
                    );
                }
            }
        } catch (SQLException e) {
            // Gérez les exceptions ou renvoyez-les à l'appelant
            throw new SQLException("Erreur lors de la récupération du pays avec l'ID : " + id, e);
        }

        return pays;
    }
}
