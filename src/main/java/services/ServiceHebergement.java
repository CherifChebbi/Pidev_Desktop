package services;

import models.CategoryH;
import models.Hebergement;
import utils.MyDB;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ServiceHebergement implements IHebergement<Hebergement> {

    private Connection connection;
    public ServiceHebergement(){connection= MyDB.getCon();}
    public void ajouter(Hebergement hebergement) throws SQLException {
        String req ="INSERT INTO hebergement (categorie_id, nom, description) VALUES (58,?,?)";

        try (PreparedStatement pre = connection.prepareStatement(req)) {
            pre.setString(1, hebergement.getNom());
            pre.setString(2, hebergement.getDescription());
            pre.executeUpdate();
            System.out.println("hebergement added successfully!");
        }
    }

    @Override
    public void modifier(Hebergement hebergement, int id) throws SQLException {
        String req = "UPDATE hebergement SET categorie_id=?, nom=?, description=? WHERE id=?";
        try (PreparedStatement pre = connection.prepareStatement(req)) {
            pre.setInt(1, 9); // Assuming 1 is the new category id
            pre.setString(2, hebergement.getNom());
            pre.setString(3, hebergement.getDescription());
            pre.setInt(4, id);
            pre.executeUpdate();
            System.out.println("hebergement modified successfully!");
        }
    }


    public ObservableList<Hebergement> afficher() throws SQLException{
        String req ="SELECT * FROM hebergement WHERE categorie_id=58";
        ObservableList<Hebergement> list = FXCollections.observableArrayList();
        PreparedStatement pre= connection.prepareStatement(req);
        ResultSet res = pre.executeQuery();
        while (res.next()) {
            Hebergement h= new Hebergement();
            h.setId(res.getInt("id"));
            h.setCategorie_id(res.getInt(1));
            h.setNom(res.getString("nom"));
            h.setDescription(res.getString("description"));
            list.add(h);
        }
        return list;
    }

    @Override
    public void supprimer(int categorie_id) throws SQLException {
        String req = "DELETE FROM hebergement WHERE id=?";
        try (PreparedStatement pre = connection.prepareStatement(req)) {
            pre.setInt(1, categorie_id);
            pre.executeUpdate();
            System.out.println("Hebergement deleted");
        }
    }

    public List<CategoryH> getAllCategories() {
        String query = "SELECT * FROM category";
        List<CategoryH> plats = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                CategoryH plat = new CategoryH();
                plat.setId(resultSet.getInt("id"));
                plat.setNom(resultSet.getString("nom"));
                plat.setDescription(resultSet.getString("image"));
                plat.setImage(resultSet.getString("prix"));
                // Assuming the "idR" column is the foreign key referencing the Restaurant table
                // You may need to set the corresponding Restaurant object here if needed
                plats.add(plat);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return plats;

    }




}
