package services;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import models.Pays;
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
    private ServiceMonument serviceMonument;


    public ServiceVille() {
        cnx = DBConnexion.getInstance().getCnx();
    }
    public void initServiceMonument(ServiceMonument serviceMonument) {
        this.serviceMonument= serviceMonument;
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

    }

    @Override
    public void Delete(Ville ville) throws SQLException {

        ServiceVille serviceVille = new ServiceVille();
        ServiceMonument serviceMonument = new ServiceMonument();
        serviceVille.initServiceMonument(serviceMonument); // Initialise serviceVille
        // Supprimer toutes les villes associées à ce pays
        serviceMonument.DeleteByVilleId(ville.getId_ville());

        String req = "DELETE FROM ville WHERE id_ville=?";
        PreparedStatement ps = cnx.prepareStatement(req);
        ps.setInt(1, ville.getId_ville());
        ps.executeUpdate();

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



        return villeList;
    }

    public List<Ville> PaysVille(int id_pays) throws SQLException {
        List<Ville> filteredville = new ArrayList<>();

        String req = "SELECT * FROM ville WHERE id_pays LIKE ?";
        PreparedStatement ps = cnx.prepareStatement(req);
        ps.setInt(1, id_pays);

        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Ville p = new Ville();

            p.setId_pays(rs.getInt("id_pays"));
            p.setId_ville(rs.getInt("id_ville"));
            p.setNom_ville(rs.getString("nom_ville"));
            p.setImg_ville(rs.getString("img_ville"));
            p.setDesc_ville(rs.getString("desc_ville"));
            p.setNb_monuments(rs.getInt("nb_monuments"));
            p.setLatitude(rs.getDouble("latitude"));
            p.setLongitude(rs.getDouble("longitude"));

            filteredville.add(p);
        }



        return filteredville;
    }
    public List<Ville> rechercherParNom(String nom) throws SQLException {
        List<Ville> VilleList = new ArrayList<>();

        String req = "SELECT * FROM ville WHERE nom_ville LIKE ?";
        PreparedStatement ps = cnx.prepareStatement(req);
        ps.setString(1, "%" + nom + "%");

        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Ville p = new Ville();


            p.setId_pays(rs.getInt("id_pays"));
            p.setId_ville(rs.getInt("id_ville"));
            p.setNom_ville(rs.getString("nom_ville"));
            p.setImg_ville(rs.getString("img_ville"));
            p.setDesc_ville(rs.getString("desc_ville"));
            p.setNb_monuments(rs.getInt("nb_monuments"));
            p.setLatitude(rs.getDouble("latitude"));
            p.setLongitude(rs.getDouble("longitude"));

            VilleList.add(p);
        }



        return VilleList;
    }
    public List<Ville> filterByMonument(int minMonuments, int maxMonuments) throws SQLException {
        List<Ville> filteredville = new ArrayList<>();

        String req = "SELECT * FROM ville WHERE nb_monuments >= ? AND nb_monuments <= ?";
        PreparedStatement ps = cnx.prepareStatement(req);
        ps.setInt(1, minMonuments);
        ps.setInt(2, maxMonuments);

        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Ville p = new Ville();

            p.setId_pays(rs.getInt("id_pays"));
            p.setId_ville(rs.getInt("id_ville"));
            p.setNom_ville(rs.getString("nom_ville"));
            p.setImg_ville(rs.getString("img_ville"));
            p.setDesc_ville(rs.getString("desc_ville"));
            p.setNb_monuments(rs.getInt("nb_monuments"));
            p.setLatitude(rs.getDouble("latitude"));
            p.setLongitude(rs.getDouble("longitude"));

            filteredville.add(p);
        }



        return filteredville;
    }
    public Ville getVilleById(int id) throws SQLException {
        // Initialisez la variable de pays à null
        Ville ville = null;

        // Préparez votre requête SQL
        String query = "SELECT * FROM ville WHERE id_ville = ?";

        // Obtenez une connexion à la base de données
        Connection cnx = DBConnexion.getInstance().getCnx();

        try (PreparedStatement ps = cnx.prepareStatement(query)) {
            // Définissez le paramètre de la requête
            ps.setInt(1, id);

            // Exécutez la requête
            try (ResultSet rs = ps.executeQuery()) {
                // Si un pays avec l'ID donné est trouvé, créez un objet Pays correspondant
                if (rs.next()) {
                    ville = new Ville(
                            rs.getInt("id_ville"),
                            rs.getString("nom_ville"),
                            rs.getString("img_ville"),
                            rs.getString("desc_ville"),
                            rs.getInt("nb_monuments"),
                            rs.getDouble("latitude"),
                            rs.getDouble("longitude")
                    );
                }
            }
        } catch (SQLException e) {
            // Gérez les exceptions ou renvoyez-les à l'appelant
            throw new SQLException("Erreur lors de la récupération ville avec l'ID : " + id, e);
        }

        return ville;
    }
    public void updateNbMonuments(Ville ville) throws SQLException {
        // Préparez votre requête SQL
        String query = "UPDATE ville SET nb_monuments = ? WHERE id_ville = ?";

        // Obtenez une connexion à la base de données
        Connection cnx = DBConnexion.getInstance().getCnx();

        try (PreparedStatement ps = cnx.prepareStatement(query)) {
            // Définissez les paramètres de la requête
            ps.setInt(1, ville.getNb_monuments());
            ps.setInt(2, ville.getId_ville());

            // Exécutez la requête
            ps.executeUpdate();
        } catch (SQLException e) {
            // Gérez les exceptions ou renvoyez-les à l'appelant
            throw new SQLException("Erreur lors de la mise à jour du nombre de monuments pour le pays avec l'ID : " + ville.getId_ville(), e);
        }
    }
    public List<Ville> getAll() throws SQLException {
        List<Ville> VilleList = new ArrayList<>();

        cnx = DBConnexion.getInstance().getCnx();
        String query = "SELECT * FROM ville";
        try (PreparedStatement statement = cnx.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            // Parcourir le résultat de la requête
            while (resultSet.next()) {
                // Créer un objet Pays à partir des données du résultat
                Ville ville = new Ville(
                        resultSet.getInt("id_ville"),
                        resultSet.getInt("id_pays"),
                        resultSet.getString("nom_ville"),
                        resultSet.getString("img_ville"),
                        resultSet.getString("desc_ville"),
                        resultSet.getInt("nb_monuments"),
                        resultSet.getDouble("latitude"),
                        resultSet.getDouble("longitude"));
                // Ajouter le pays à la liste
                VilleList.add(ville);
            }
            return VilleList;
        }
    }
    public Ville getVilleByName(String nomVille) throws SQLException {
        cnx = DBConnexion.getInstance().getCnx();
        PreparedStatement ps = null;
        ResultSet rs = null;
        Ville ville = null;


        String query = "SELECT * FROM ville WHERE nom_ville = ?";
        ps = cnx.prepareStatement(query);
        ps.setString(1, nomVille);
        rs = ps.executeQuery();

        if (rs.next()) {
            // Si un pays avec le nom donné est trouvé, créer un objet Pays correspondant
            ville = new Ville(
                    rs.getInt("id_ville"),
                    rs.getInt("id_pays"),
                    rs.getString("nom_ville"),
                    rs.getString("img_ville"),
                    rs.getString("desc_ville"),
                    rs.getInt("nb_monuments"),
                    rs.getDouble("latitude"),
                    rs.getDouble("longitude"));
        }

        return ville;
    }
    public void DeleteByPaysId(int paysId) throws SQLException {
        // Supprimer les monuments liés aux villes associées à ce pays
        String reqDeleteMonuments = "DELETE FROM monument WHERE id_ville IN (SELECT id_ville FROM ville WHERE id_pays=?)";
        PreparedStatement psDeleteMonuments = cnx.prepareStatement(reqDeleteMonuments);
        psDeleteMonuments.setInt(1, paysId);
        psDeleteMonuments.executeUpdate();


        // Supprimer les villes associées à ce pays
        String reqDeleteVilles = "DELETE FROM ville WHERE id_pays=?";
        PreparedStatement psDeleteVilles = cnx.prepareStatement(reqDeleteVilles);
        psDeleteVilles.setInt(1, paysId);
        psDeleteVilles.executeUpdate();

    }
    public Ville getVilleWithMostMonuments() throws SQLException {
        String query = "SELECT * FROM ville ORDER BY nb_monuments DESC LIMIT 1";
        try (PreparedStatement ps = cnx.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                Ville ville = new Ville(
                        rs.getInt("id_ville"),
                        rs.getString("nom_ville"),
                        rs.getString("img_ville"),
                        rs.getString("desc_ville"),
                        rs.getInt("nb_monuments"),
                        rs.getDouble("latitude"),
                        rs.getDouble("longitude")
                );
                return ville;
            }
        }
        return null;
    }

    public Ville getVilleWithLeastMonuments() throws SQLException {
        String query = "SELECT * FROM ville ORDER BY nb_monuments ASC LIMIT 1";
        try (PreparedStatement ps = cnx.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                // Créer un objet Pays à partir des données du résultat
                Ville ville = new Ville(
                        rs.getInt("id_ville"),
                        rs.getString("nom_ville"),
                        rs.getString("img_ville"),
                        rs.getString("desc_ville"),
                        rs.getInt("nb_monuments"),
                        rs.getDouble("latitude"),
                        rs.getDouble("longitude")
                );
                return ville;
            }
        }
        return null;
    }

    public boolean isNomVilleExistant(String nomVille) throws SQLException {
        String query = "SELECT COUNT(*) FROM ville WHERE nom_ville = ?";
        try (PreparedStatement stmt = cnx.prepareStatement(query)) {
            stmt.setString(1, nomVille);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int count = rs.getInt(1);
                return count > 0;
            }
        }
        return false;
    }



}
