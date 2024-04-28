package tn.esprit.crud.services;

import tn.esprit.crud.models.User;
import tn.esprit.crud.utils.MyDatabase;

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
        String req = "INSERT INTO user(nom , prenom , adresse , email , mdp,role) VALUES( '" + user.getNom() + "' , '" + user.getPrenom() + "' , '" + user.getNationnalite() + "' , '" + user.getEmail() + "' , '" + user.getPassword() + "','" + user.getRoles() + "')";
        Statement st = connection.createStatement();
        st.executeUpdate(req);
    }

    @Override
    public void modifier(User user) throws SQLException {
        String req = "UPDATE user SET nom = ?, prenom = ?, adresse = ?, email = ? , mdp = ? , role= ? WHERE id = ?";
        PreparedStatement us = connection.prepareStatement(req);
        us.setString(1, user.getNom());
        us.setString(2, user.getPrenom());
        us.setString(3, user.getNationnalite());
        us.setString(4, user.getEmail());
        us.setString(5, user.getPassword());
        us.setString(6, user.getRoles());
        us.setInt(7, user.getId());
        us.executeUpdate();
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
            user.setPassword(rs.getString("mdp")); // Use the "mdp" column instead of "nationnalite"
            user.setRoles(rs.getString("role"));

            users.add(user);
        }
        return users;
    }

    public boolean authenticateUser(String name, String pass) {
        String query = "SELECT * FROM user WHERE nom = ? AND mdp = ?";
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

            PreparedStatement stmt1 = connection.prepareStatement("SELECT role FROM user where id=?");
            stmt1.setInt(1, id);

            ResultSet rs = stmt1.executeQuery();

            while (rs.next()) {
                return rs.getString("role");
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
                String adresse = resultSet.getString("adresse");
                String email = resultSet.getString("email");
                String password = resultSet.getString("password"); // Update attribute name
                String roles = resultSet.getString("roles"); // Update attribute name
                int numtel = resultSet.getInt("numtel"); // Added for the "numtel" attribute

                // Create a new User object with retrieved data
                User user = new User(id, nom, prenom, adresse, email, password, roles, numtel);
                list.add(user);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    @Override
    public void inscription(User user) throws SQLException {
        String req = "INSERT INTO user(nom , prenom , adresse , email , mdp) VALUES( '" + user.getNom() + "' , '" + user.getPrenom() + "' , '" + user.getNationnalite() + "' , '" + user.getEmail() + "' , '" + user.getPassword() + "')";
        Statement st = connection.createStatement();
        st.executeUpdate(req);
    }



}
