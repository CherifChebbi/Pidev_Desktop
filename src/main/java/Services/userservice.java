package Services;

import Entity.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import Util.MyDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.mindrot.jbcrypt.BCrypt;
import Entity.User;
import Util.MyDB;

import java.sql.*;
import java.sql.Date;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
public class userservice implements Iuser<User> {
    public int getUserIdByName(String name) throws SQLException {
        int userId = -1; // Default value if not found
        String query = "SELECT id FROM user WHERE nom = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, name);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    userId = resultSet.getInt("id");
                }
            }
        }

        return userId;
    }




    private final Connection connection;

    public userservice() {
        connection = MyDB.getInstance().getConnection();
    }
//int id, String email, String roles, String password, String nom, String prenom, String numtel, String nationnalite
    @Override
    public void ajouter(User user) throws SQLException {
        String req = "INSERT INTO user (email, password, nom, prenom, numtel, nationnalite) VALUES (?,?,?,?,?,?)";
        try (PreparedStatement pre = connection.prepareStatement(req)) {
            pre.setString(1, user.getEmail());
            pre.setString(2, user.getPassword());
            pre.setString(3, user.getNom());
            pre.setString(4, user.getPrenom());
            pre.setString(5, user.getNumtel());
            pre.setString(6, user.getNationnalite());

            pre.executeUpdate();
            System.out.println("User added successfully!");
        }
    }


    @Override
    public void modifier(User user) throws SQLException {
        String req = "UPDATE user SET nom=?, prenom=?, email=?, password=?, nationnalite=?, numtel=? WHERE id=?";
        try (PreparedStatement pre = connection.prepareStatement(req)) {
            pre.setString(1, user.getNom());
            pre.setString(2, user.getPrenom());
            pre.setString(3, user.getEmail());
            pre.setString(4, user.getPassword());
            pre.setString(5, user.getNationnalite());
            pre.setString(6, user.getNumtel()); // Set numtel as string
            pre.setInt(7, user.getId()); // Assuming id is the primary key

            int affectedRows = pre.executeUpdate();
            if (affectedRows == 1) {
                System.out.println("User updated successfully!");
            } else {
                System.out.println("Failed to update user. No rows affected.");
            }
        }
    }



    @Override
    public void supprimer(User user) throws SQLException {
        String req = "DELETE FROM user WHERE id=?";
        try (PreparedStatement pre = connection.prepareStatement(req)) {
            pre.setInt(1, user.getId()); // Assuming id is the primary key

            int affectedRows = pre.executeUpdate();
            if (affectedRows == 1) {
                System.out.println("User deleted successfully!");
            } else {
                System.out.println("Failed to delete user. No rows affected.");
            }
        }
    }




    public ObservableList<User> afficher() throws SQLException {
        String req = "SELECT * FROM user";
        System.out.println("mataadetsh");
        ObservableList<User> list = FXCollections.observableArrayList();
        PreparedStatement pre= connection.prepareStatement(req);
        ResultSet res = pre.executeQuery();

        while (res.next()) {
            User user = new User();
            user.setId(res.getInt("id"));
            // res.getInt("IDformation"),
            // res.getInt("iduser"),
            user.setNom( res.getString("nom"));
            user.setPrenom( res.getString("Prenom"));
            user.setPassword( res.getString("password"));
            user.setEmail( res.getString("email"));
            user.setNationnalite( res.getString("nationnalite"));
            user.setNumtel( res.getString("numtel"));
            list.add(user);

        }

        return list ;
    }


    public List<User> getAllUsers() throws SQLException {
        String query = "SELECT * FROM user";
        List<User> users = new ArrayList<>();

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getInt("id"));
                user.setNom(resultSet.getString("nom"));
                user.setPrenom(resultSet.getString("prenom"));
                user.setEmail(resultSet.getString("email"));
                user.setPassword(resultSet.getString("password"));
                user.setNationnalite(resultSet.getString("nationnalite"));
                user.setNumtel(resultSet.getString("numtel"));
                users.add(user);
            }
        }
        return users;
    }

    public List<String> getAllUsersNames() throws SQLException {
        List<String> userNames = new ArrayList<>();

        // Logic to retrieve users names from the database
        // For example:
        // ResultSet resultSet = databaseConnection.createStatement().executeQuery("SELECT name FROM restaurants");
        // while (resultSet.next()) {
        //     restaurantNames.add(resultSet.getString("name"));
        // }

        // Dummy data for demonstration
        userNames.add("User 1");
        userNames.add("User 2");
        userNames.add("User 3");

        return userNames;
    }
    public User getUserById(int userId) {
        User user = null;
        String query = "SELECT * FROM user WHERE Id = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                user = new User();
                user.setId(resultSet.getInt("Id"));
                user.setEmail(resultSet.getString("email"));
                user.setPassword(resultSet.getString("password"));
                user.setNumtel(resultSet.getString("num_tel"));
                //  user.setRole(Role.valueOf(resultSet.getString("roles")));
                // Update the code that sets roles from a string to use the enum type
                String roleString = resultSet.getString("roles");
                roleString = roleString.replaceAll("[\\[\\]\"]", ""); // Remove square brackets and quotes
                Role role = Role.valueOf(roleString);
                user.setRoles(role);

                user.setNom(resultSet.getString("nom"));
                user.setPrenom(resultSet.getString("prenom"));
                user.setNationnalite(resultSet.getString("nationnalite"));

                user.setIs_banned(resultSet.getBoolean("is_banned"));

            }
        } catch (SQLException ex) {
            Logger.getLogger(userservice.class.getName()).log(Level.SEVERE, null, ex);
        }
        return user;
    }
    public User getUserByEmail(String text) {
        User user = null;
        String query = "SELECT * FROM user WHERE email = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, text);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                user = new User();
                user.setId(resultSet.getInt("id"));
                user.setEmail(resultSet.getString("email"));
                user.setPassword(resultSet.getString("password"));
                user.setNumtel(resultSet.getString("num_tel"));
                // user.setRole(Role.valueOf(resultSet.getString("roles")));
                String roleString = resultSet.getString("roles");
                roleString = roleString.replaceAll("[\\[\\]\"]", ""); // Remove square brackets and quotes
                Role role = Role.valueOf(roleString);
                user.setRoles(role);

                user.setIs_banned(resultSet.getBoolean("is_banned"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(userservice.class.getName()).log(Level.SEVERE, null, ex);
        }
        return user;
    }
    public User goToUserList(){

    }
}