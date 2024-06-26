package services;

import models.Category;
import utils.DBConnexion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ServiceCategory implements IService<Category> {
    private Connection con;

    public ServiceCategory() throws SQLException {
        // Créer une instance de la classe DB et obtenir la connexion
        con = DBConnexion.getInstance().getCnx();
    }

    @Override
    public void ajouter(Category category) {
        String query = "INSERT INTO category (nom) VALUES (?)";
        try (PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setString(1, category.getNom());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void modifier(Category category) {
        String query = "UPDATE category SET nom=? WHERE id=?";
        try (PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setString(1, category.getNom());
            stmt.setInt(2, category.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void supprimer(Category category) {
        String query = "DELETE FROM category WHERE id=?";
        try (PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setInt(1, category.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void viderCategories() {
        String query = "DELETE FROM category";
        try (PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Category> afficher() {
        List<Category> categories = new ArrayList<>();
        String query = "SELECT * FROM category"; // Correction ici : "categorie" au lieu de "category"
        try (PreparedStatement stmt = con.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                int id = rs.getInt("id");
                String nom = rs.getString("nom");

                Category category = new Category(id, nom);
                categories.add(category);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categories;
    }


    public Category getCategorieById(int id) {
        String query = "SELECT * FROM category WHERE id=?";
        try (PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String nom = rs.getString("nom");
                return new Category(id, nom);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Retourne null si aucune catégorie trouvée pour l'ID donné
    }

    public static int countCategories() throws SQLException {
        int count = 0;
        String query = "SELECT COUNT(*) FROM category"; // Remplacez 'category' par le nom de votre table de catégories

        try (Connection conn = DBConnexion.getInstance().getCnx();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                count = rs.getInt(1);
            }
        }

        return count;
    }

}
