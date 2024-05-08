package services;

import models.CategoryH;
import utils.MyDB;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServiceCategoryH implements ICategoryH<CategoryH> {

    private ObservableList<CategoryH> categoryHList = FXCollections.observableArrayList();




    private Connection connection;
    public ServiceCategoryH(){connection= MyDB.getCon();}
    @Override
    public void ajouter(CategoryH categoryH) throws SQLException {
        String req ="INSERT INTO category (nom, image, description) VALUES (?,?,?)";

        try (PreparedStatement pre = connection.prepareStatement(req)) {
            pre.setString(1, categoryH.getNom());
            pre.setString(2, categoryH.getImage());
            pre.setString(3, categoryH.getDescription());
            pre.executeUpdate();
            System.out.println("Category added successfully!");
        }
    }


    @Override
    public void modifier(CategoryH categoryH, int id) throws SQLException {
        String req = "UPDATE category SET nom=?, image=?, description=? WHERE id=?";
        try (PreparedStatement pre = connection.prepareStatement(req)) {
            pre.setString(1, categoryH.getNom());
            pre.setString(2, categoryH.getImage());
            pre.setString(3, categoryH.getDescription());
            pre.setInt(4, id); // Use the original category id for updating
            pre.executeUpdate();
            System.out.println("Category modified successfully!");

            // Create a copy of the current list
            ObservableList<CategoryH> newList = FXCollections.observableArrayList(categoryHList);

            // Update the copy with the modified category
            for (CategoryH c : newList) {
                if (c.getId() == id) {
                    c.setNom(categoryH.getNom());
                    c.setImage(categoryH.getImage());
                    c.setDescription(categoryH.getDescription());
                    break;
                }
            }

            // Replace the old list with the new list
            categoryHList.setAll(newList);
        }
    }

    @Override
    public ObservableList<CategoryH> afficher() throws SQLException {
        String req ="SELECT * FROM category";
        ObservableList<CategoryH> list = FXCollections.observableArrayList();
        PreparedStatement pre= connection.prepareStatement(req);
        ResultSet res = pre.executeQuery();
        while (res.next()) {
            CategoryH c= new CategoryH();
            c.setId(res.getInt("id"));
            c.setNom(res.getString("nom"));
            c.setImage(res.getString("image"));
            c.setDescription(res.getString("description"));
            list.add(c);
        }
        return list;
    }


    @Override
    public void supprimer(int categorie_id) throws SQLException {
        String req = "DELETE FROM category WHERE id=?";
        try (PreparedStatement pre = connection.prepareStatement(req)) {
            pre.setInt(1, categorie_id);
            pre.executeUpdate();
            System.out.println("Category deleted");
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
                plat.setDescription(resultSet.getString("Description"));
                plat.setImage(resultSet.getString("image"));
                // Assuming the "idR" column is the foreign key referencing the Restaurant table
                // You may need to set the corresponding Restaurant object here if needed
                plats.add(plat);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return plats;

    }

    public Map<String, Integer> getReservationsParJour() {
        String query = "SELECT date AS jour, COUNT(*) AS nb_reservations FROM reservation GROUP BY jour";
        Map<String, Integer> reservationsParJour = new HashMap<>();

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String jour = resultSet.getString("jour");
                int nbReservations = resultSet.getInt("nb_reservations");
                reservationsParJour.put(jour, nbReservations);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Gérer l'exception selon vos besoins
        }

        return reservationsParJour;
    }
}
