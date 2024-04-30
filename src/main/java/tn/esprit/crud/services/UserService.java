package tn.esprit.crud.services;

import tn.esprit.crud.models.User;
import tn.esprit.crud.utils.MyDatabase;
import org.mindrot.jbcrypt.BCrypt;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserService implements IServices<User> {

    private Connection connection;
    private Statement ste;



    public UserService(){
        connection = MyDatabase.getInstance().getConnection();
    }


    @Override
    public void ajouter(User user) throws SQLException {
        String req = "INSERT INTO user(nom , prenom , nationnalite , email , password,roles,numtel) VALUES(?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement st = connection.prepareStatement(req);
        st.setString(1, user.getNom());
        st.setString(2, user.getPrenom());
        st.setString(3, user.getNationnalite());
        st.setString(4, user.getEmail());
        st.setString(5, user.getPassword());
        st.setString(6, user.getRoles());
        st.setInt(7, user.getNumtel());
        st.executeUpdate();
    }


    @Override
    public void modifier(User user) throws SQLException {
        String req = "UPDATE user SET nom = ?, prenom = ?, nationnalite = ?, email = ?, numtel = ?, roles = ? WHERE id = ?";
        try (PreparedStatement us = connection.prepareStatement(req)) {
            us.setString(1, user.getNom());
            us.setString(2, user.getPrenom());
            us.setString(3, user.getNationnalite());
            us.setString(4, user.getEmail());
            us.setInt(5, user.getNumtel());
            us.setString(6, user.getRoles());
            us.setInt(7, user.getId());

            int rowsAffected = us.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("User updated successfully");
            } else {
                System.out.println("Failed to update user");
            }
            System.out.println("Rows affected: " + rowsAffected);
        } catch (SQLException e) {
            e.printStackTrace();
            throw e; // Rethrow the exception to handle it in the caller method
        }
    }






    @Override
    public void supprimer(User user) throws SQLException {
        String req = "DELETE FROM user WHERE id = ?";
        PreparedStatement pre = connection.prepareStatement(req);
        pre.setInt(1, user.getId());
        pre.executeUpdate();
        System.out.println("User deleted successfully!");
    }

    @Override
    public void supprimer(int id) throws SQLException {
        String req = "DELETE FROM user WHERE id = ?";
        PreparedStatement pre = connection.prepareStatement(req);
        pre.setInt(1, id);
        pre.executeUpdate();
        System.out.println("User deleted successfully!");
    }
    @Override
    public List<User> recuperer() throws SQLException {
        List<User> users = new ArrayList<>();
        String req = "SELECT * FROM user";
        Statement us = connection.createStatement();
        ResultSet rs = us.executeQuery(req);

        while (rs.next()) {
            User user = new User();
            user.setId(rs.getInt("id"));
            user.setNom(rs.getString("nom"));
            user.setPrenom(rs.getString("prenom"));
            user.setNationnalite(rs.getString("nationnalite")); // Use the "nationnalite" column instead of "adresse"
            user.setEmail(rs.getString("email"));
            user.setPassword(rs.getString("password")); // Use the "mdp" column instead of "nationnalite"
            user.setRoles(rs.getString("roles"));
            user.setNumtel(rs.getInt("numtel"));

            users.add(user);
        }
        return users;
    }

    public boolean authenticateUser(String name, String pass) {
        String query = "SELECT * FROM user WHERE nom = ? AND password = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, name);
            statement.setString(2, pass);

            ResultSet resultSet = statement.executeQuery();

            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

    }

    public String roles (int id) {
        try {

            PreparedStatement stmt1 = connection.prepareStatement("SELECT roles FROM user where id=?");
            stmt1.setInt(1, id);

            ResultSet rs = stmt1.executeQuery();

            while (rs.next()) {
                return rs.getString("roles");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "not found";
    }

    public List<User> displayAll() {
        String requete = "SELECT * FROM user";
        List<User> list = new ArrayList<>();

        try {
            ste = connection.createStatement();
            ResultSet resultSet = ste.executeQuery(requete);
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String nom = resultSet.getString("nom");
                String prenom = resultSet.getString("prenom");
                String nationnalite = resultSet.getString("nationnalite"); // Update to use "nationnalite" column
                String email = resultSet.getString("email");
                String password = resultSet.getString("password"); // Update attribute name
                String roles = resultSet.getString("roles"); // Update attribute name
                int numtel = resultSet.getInt("numtel"); // Added for the "numtel" attribute

                // Create a new User object with retrieved data
                User user = new User(id, nom, prenom, nationnalite, email, password, roles, numtel);
                list.add(user);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }


    public void inscription(User user) throws SQLException {
        // Hash the password using bcrypt
        String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
        user.setPassword(hashedPassword);

        // Proceed with user insertion
        String req = "INSERT INTO user(nom, prenom, nationnalite, email, password,numtel) VALUES (?, ?, ?, ?, ?,?)";
        PreparedStatement st = connection.prepareStatement(req);
        st.setString(1, user.getNom());
        st.setString(2, user.getPrenom());
        st.setString(3, user.getNationnalite());
        st.setString(4, user.getEmail());
        st.setString(5, user.getPassword());
        st.setInt(6,user.getNumtel());
        st.executeUpdate();
    }



}
