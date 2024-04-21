package Services;

import Entity.Category;
import Entity.Hebergement;
import Util.MyDB;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ServiceCategory implements ICategory<Category>{

    private ObservableList<Category> categoryList = FXCollections.observableArrayList();




    private Connection connection;
    public ServiceCategory(){connection= MyDB.getCon();}
    @Override
    public void ajouter(Category category) throws SQLException {
        String req ="INSERT INTO category (nom, image, description) VALUES (?,?,?)";

        try (PreparedStatement pre = connection.prepareStatement(req)) {
            pre.setString(1, category.getNom());
            pre.setString(2, category.getImage());
            pre.setString(3, category.getDescription());
            pre.executeUpdate();
            System.out.println("Category added successfully!");
        }
    }


    @Override
    public void modifier(Category category, int id) throws SQLException {
        String req = "UPDATE category SET nom=?, image=?, description=? WHERE id=?";
        try (PreparedStatement pre = connection.prepareStatement(req)) {
            pre.setString(1, category.getNom());
            pre.setString(2, category.getImage());
            pre.setString(3, category.getDescription());
            pre.setInt(4, id); // Use the original category id for updating
            pre.executeUpdate();
            System.out.println("Category modified successfully!");

            // Create a copy of the current list
            ObservableList<Category> newList = FXCollections.observableArrayList(categoryList);

            // Update the copy with the modified category
            for (Category c : newList) {
                if (c.getId() == id) {
                    c.setNom(category.getNom());
                    c.setImage(category.getImage());
                    c.setDescription(category.getDescription());
                    break;
                }
            }

            // Replace the old list with the new list
            categoryList.setAll(newList);
        }
    }

    @Override
    public ObservableList<Category> afficher() throws SQLException {
        String req ="SELECT * FROM category";
        ObservableList<Category> list = FXCollections.observableArrayList();
        PreparedStatement pre= connection.prepareStatement(req);
        ResultSet res = pre.executeQuery();
        while (res.next()) {
            Category c= new Category();
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

    public List<Category> getAllCategories() {
        String query = "SELECT * FROM category";
        List<Category> plats = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Category plat = new Category();
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
}
