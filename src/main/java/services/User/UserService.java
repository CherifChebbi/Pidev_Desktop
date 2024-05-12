package services.User;

import models.User;
import utils.DBConnexion;
import org.mindrot.jbcrypt.BCrypt;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import java.util.Random;

public class UserService implements IServices<User> {

    private Connection connection;
    private Statement ste;

    public UserService(){
        connection = DBConnexion.getInstance().getCnx();
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
        String req = "UPDATE user SET nom = ?, prenom = ?, nationnalite = ?, email = ?, numtel = ?, roles = ?  WHERE id = ?";
        try (PreparedStatement us = connection.prepareStatement(req)) {
            us.setString(1, user.getNom());
            us.setString(2, user.getPrenom());
            us.setString(3, user.getNationnalite());
            us.setString(4, user.getEmail());
            us.setInt(5, user.getNumtel());
            us.setString(6, user.getRoles());
            //us.setBoolean(7,user.getisBanned());
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
            user.setIsBanned(rs.getBoolean("is_banned"));

            users.add(user);
        }
        return users;
    }

    public User authenticateUser(String email, String password) {
        String query = "SELECT * FROM user WHERE email = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, email);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                // User found, check password
                String storedPassword = resultSet.getString("password");
                try {
                    if (BCrypt.checkpw(password, storedPassword)) {
                        // Passwords match, create and return User object
                        User user = new User();
                        user.setId(resultSet.getInt("id"));
                        user.setNom(resultSet.getString("nom"));
                        user.setPrenom(resultSet.getString("prenom"));
                        user.setNationnalite(resultSet.getString("nationnalite"));
                        user.setEmail(resultSet.getString("email"));
                        user.setPassword(resultSet.getString("password"));
                        user.setRoles(resultSet.getString("roles"));
                        user.setNumtel(resultSet.getInt("numtel"));
                        user.setIsBanned(resultSet.getBoolean("is_banned"));
                        return user;
                    } else {
                        // Passwords don't match
                        System.out.println("Login failed: Incorrect password");
                        return null;
                    }
                } catch (IllegalArgumentException e) {
                    // Handle invalid salt version
                    System.out.println("Invalid salt version");
                    return null;
                }
            } else {
                // No user found with the provided email address
                System.out.println("Login failed: User not found");
                return null;
            }
        } catch (SQLException e) {
            System.err.println("Error during login: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public boolean changePasswordByEmail(String email, String hashedPassword) {
        String query = "UPDATE user SET password = ? WHERE email = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, hashedPassword);
            preparedStatement.setString(2, email);
            int rowsUpdated = preparedStatement.executeUpdate();
            return rowsUpdated > 0; // Renvoie vrai si une ligne a été mise à jour
        } catch (SQLException ex) {
            Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, null, ex);
            return false; // En cas d'erreur, renvoie false
        }
    }


    public boolean userExistsByEmail(String email) {
        String query = "SELECT COUNT(*) FROM user WHERE email = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                return count > 0;
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }


    public void changePasswordById(int userId, String newPassword) {
        String query = "UPDATE user SET password=? WHERE id=?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            String hashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt(13));
            preparedStatement.setString(1, hashedPassword);
            preparedStatement.setInt(2, userId);
            int rowsUpdated = preparedStatement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("User updated successfully!");
            } else {
                System.out.println("No user found with the given ID.");
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String getVerificationCodeByEmail(String email) {
        String query = "SELECT verification_code FROM user WHERE email = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String verificationCode = resultSet.getString("verification_code");
                return verificationCode;
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
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
                boolean isBanned = resultSet.getBoolean("is_banned"); // Fetch is_banned attribute

                // Create a new User object with retrieved data
                User user = new User(id, nom, prenom, nationnalite, email, password, roles, numtel,isBanned);
                user.setIsBanned(isBanned); // Set is_banned attribute
                list.add(user);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }


    public void inscription(User user) throws SQLException {
        // Hash the password using bcrypt
        String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt(13));
        user.setPassword(hashedPassword);

        // Proceed with user insertion
        String req = "INSERT INTO user(nom, prenom, nationnalite, email, password, roles, numtel) VALUES (?, ?, ?, ?, ?, ?, ?)";
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

    public static String generateCode() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder code = new StringBuilder();
        int codeLength = 4;
        Random random = new Random();

        for (int i = 0; i < codeLength; i++) {
            int randomIndex = random.nextInt(characters.length());
            code.append(characters.charAt(randomIndex));
        }

        return code.toString();
    }
   // Method to generate a random verification code
   public void editProfile(User user) throws SQLException {
       String req = "UPDATE user SET nom = ?, prenom = ?, nationnalite = ?, email = ?, password = ?, numtel = ? WHERE id = ?";
       try (PreparedStatement us = connection.prepareStatement(req)) {
           us.setString(1, user.getNom());
           us.setString(2, user.getPrenom());
           us.setString(3, user.getNationnalite());
           us.setString(4, user.getEmail());
           us.setString(5, user.getPassword());
           us.setInt(6, user.getNumtel());
           us.setInt(7, user.getId());

           // Log SQL query and user data
           System.out.println("SQL Query: " + us.toString());
           System.out.println("User Data: " + user.toString());

           int rowsAffected = us.executeUpdate();
           if (rowsAffected > 0) {
               System.out.println("User profile updated successfully");
           } else {
               System.out.println("Failed to update user profile");
           }
           System.out.println("Rows affected: " + rowsAffected);
       } catch (SQLException e) {
           e.printStackTrace();
           throw e; // Rethrow the exception to handle it in the caller method
       }
   }

    // New method to retrieve authenticated user's information
    public User getUserById(int userId) {
        String query = "SELECT * FROM user WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, userId);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                // User found, create and return User object
                User user = new User();
                user.setId(resultSet.getInt("id"));
                user.setNom(resultSet.getString("nom"));
                user.setPrenom(resultSet.getString("prenom"));
                user.setNationnalite(resultSet.getString("nationnalite"));
                user.setEmail(resultSet.getString("email"));
                user.setPassword(resultSet.getString("password"));
                user.setRoles(resultSet.getString("roles"));
                user.setNumtel(resultSet.getInt("numtel"));
                user.setIsBanned(resultSet.getBoolean("is_banned"));
                return user;
            } else {
                // No user found with the provided ID
                System.out.println("User not found with ID: " + userId);
                return null;
            }
        } catch (SQLException e) {
            System.err.println("Error fetching user information: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public void banUser(int userId) throws SQLException {
        String req = "UPDATE user SET is_banned = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(req)) {
            statement.setBoolean(1, true);
            statement.setInt(2, userId);

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("User banned successfully");
            } else {
                System.out.println("Failed to ban user");
            }
            System.out.println("Rows affected: " + rowsAffected);
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public void toggleUserBanStatus(int userId, boolean isBanned) throws SQLException {
        String req = "UPDATE user SET is_banned = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(req)) {
            statement.setBoolean(1, isBanned);
            statement.setInt(2, userId);

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                if (isBanned) {
                    System.out.println("User banned successfully");
                } else {
                    System.out.println("User unbanned successfully");
                }
            } else {
                System.out.println("Failed to update user ban status");
            }
            System.out.println("Rows affected: " + rowsAffected);
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public boolean isUserBanned(int userId) {
        String req = "SELECT is_banned FROM user WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(req)) {
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getBoolean("is_banned");
            } else {
                System.out.println("User not found");
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }




}
