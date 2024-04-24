package tn.esprit.application.services;

import org.mindrot.jbcrypt.BCrypt;
import tn.esprit.application.models.User;
import tn.esprit.application.utils.myDatabase;

import java.util.*;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserService implements IUser<User> {
    private myDatabase myConnection = myDatabase.getInstance();
    private Connection connection = myConnection.getConnection();

    @Override
    public boolean UserExistsById(int userId) {
        String query = "SELECT COUNT(*) FROM User WHERE id = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, userId);
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

    @Override
    public boolean UserExistsByEmail(String email) {
        String query = "SELECT COUNT(*) FROM User WHERE email = ?";
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

    public String getVerificationCodeByEmail(String email) {
        String query = "SELECT verification_code FROM User WHERE email = ?";
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

    public boolean ajouterUser(User user) {
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 6;
        Random random = new Random();

        String generatedString = random.ints(leftLimit, rightLimit + 1)
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
        String query = "INSERT INTO User(`email`, `password`, `profile_picture`, `roles` ,`nom`,`prenom`,`numtel`,`nationnalite`,`is_banned`,`is_verified`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        if (UserExistsByEmail(user.getEmail())) {
            System.out.println("User already exists!");
            return false;
        }
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, user.getEmail());
            String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
            preparedStatement.setString(2, hashedPassword);
            preparedStatement.setString(3, user.getProfilePicture());
            preparedStatement.setString(4, user.getRole());
            preparedStatement.setString(5, user.getNom());
            preparedStatement.setString(6, user.getPrenom());
            preparedStatement.setInt(7, user.getNumtel());
            preparedStatement.setString(8, user.getNationnalite());
            preparedStatement.setBoolean(9, user.isBanned());
            preparedStatement.setBoolean(10, user.isVerified());
            preparedStatement.executeUpdate();
            System.out.println("User added successfully!");
        } catch (SQLException ex) {
            Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }


    public List<User> afficherUser() {
        List<User> users = new ArrayList<>();
        String query = "SELECT * FROM User";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getInt("id"));
                user.setEmail(resultSet.getString("email"));
                user.setPassword(resultSet.getString("password"));
                user.setProfilePicture(resultSet.getString("profile_picture"));
                user.setRole(resultSet.getString("roles"));
                user.setNom(resultSet.getString("nom"));
                user.setPrenom(resultSet.getString("prenom"));
                user.setNumtel(resultSet.getInt("numtel"));
                user.setNationnalite(resultSet.getString("nationnalite"));
                user.setBanned(resultSet.getBoolean("is_banned"));
                user.setVerified(resultSet.getBoolean("is_verified"));
                users.add(user);
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return users;
    }


    public void changePasswordById(int userId, String newPassword) {
        String query = "UPDATE `User` SET `password`=? WHERE `id`=? ";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            String hashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());
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

    public void modifierUser(User user) {
        String query = "UPDATE `User` SET `email`=?, `password`=?, `profile_picture`=?, `roles`=?, `is_banned`=? WHERE `id`=?";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, user.getEmail());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setString(3, user.getProfilePicture());
            preparedStatement.setString(4, user.getRole());
            preparedStatement.setBoolean(5, user.isBanned());
            preparedStatement.setInt(6, user.getId());

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


    public void deleteUser(User user) {
        String query = "DELETE FROM User WHERE id=?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, user.getId());

            int rowsDeleted = preparedStatement.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("User deleted successfully!");
            } else {
                System.out.println("No user found with the given ID.");
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public User getUserById(int userId) {
        User user = null;
        String query = "SELECT * FROM User WHERE id = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                user = new User();
                user.setId(resultSet.getInt("id"));
                user.setEmail(resultSet.getString("email"));
                user.setPassword(resultSet.getString("password"));
                user.setProfilePicture(resultSet.getString("profile_picture"));
                user.setRole(resultSet.getString("roles"));
                user.setNom(resultSet.getString("nom"));
                user.setPrenom(resultSet.getString("prenom"));
                user.setBanned(resultSet.getBoolean("is_banned"));
                user.setVerified(resultSet.getBoolean("is_verified"));
                user.setNationnalite(resultSet.getString("nationnalite"));
                user.setNumtel(resultSet.getInt("numtel"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return user;
    }




    public int getIdByEmail(String email) {
        int userId = -1; // Default value indicating no user found with the provided email

        String query = "SELECT id FROM User WHERE email = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                userId = resultSet.getInt("id");
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return userId;
    }

    public List<User> getAll() {
        List<User> list = new ArrayList<>();
        try {
            String req = "SELECT * FROM User";
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(req);

            while (rs.next()) {
                User u = new User();
                u.setId(rs.getInt("id"));
                u.setEmail(rs.getString("email"));
                u.setPassword(rs.getString("password"));
                u.setProfilePicture(rs.getString("profile_picture"));
                u.setRole(rs.getString("roles"));
                u.setBanned(rs.getBoolean("is_banned"));
                u.setNom(rs.getString("nom"));
                u.setPrenom(rs.getString("prenom"));
                u.setNumtel(rs.getInt("numtel"));
                u.setNationnalite(rs.getString("nationnalite"));
                u.setVerified(rs.getBoolean("is_verified"));
                list.add(u);
            }

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return list;
    }










    public String getRoleByEmail(String email) {
        String role = "Unknown"; // Default value

        String query = "SELECT `roles` FROM User WHERE email = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, email);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                role = resultSet.getString("roles");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the exception as needed
        }

        return role;
    }




    public User authenticateUser(String email, String password) {
        User user = null;
        try {
            String query = "SELECT * FROM User WHERE email = ? AND password = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, email);
            statement.setString(2, password);
            ResultSet result = statement.executeQuery();

            if (result.next()) {
                user = new User();
                user.setId(result.getInt("id"));
                user.setEmail(result.getString("email"));
                user.setPassword(result.getString("password"));
                // Set other user attributes from the database
                user.setProfilePicture(result.getString("profile_picture"));
                user.setRole(result.getString("roles"));
                user.setBanned(result.getBoolean("is_banned"));
                user.setVerified(result.getBoolean("is_verified"));
                user.setNom(result.getString("nom"));
                user.setPrenom(result.getString("prenom"));
                user.setNumtel(result.getInt("numtel"));
                user.setNationnalite(result.getString("nationnalite"));
                System.out.println("User Retrieved: " + user.toString());
            }
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }


    public boolean verifyCode(String code) {
        boolean isValid = false;
        String query = "SELECT * FROM User WHERE verification_code = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, code);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                isValid = true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return isValid;
    }


    public User getUserByEmail(String email) {
        User user = null;
        String query = "SELECT * FROM User WHERE email = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                user = new User();
                user.setId(resultSet.getInt("id"));
                user.setEmail(resultSet.getString("email"));
                user.setPassword(resultSet.getString("password"));
                user.setProfilePicture(resultSet.getString("profile_picture"));
                user.setRole(resultSet.getString("roles"));
                user.setBanned(resultSet.getBoolean("is_banned"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return user;
    }


    public void changePasswordByEmail(String email, String hashedPassword) {
        String query = "UPDATE User SET password = ? WHERE email = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, hashedPassword);
            preparedStatement.setString(2, email);
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void updateImage(User user, String image) {
        String query = "UPDATE User SET profile_picture = ? WHERE id = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, image);
            preparedStatement.setInt(2, user.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void updateProfile(User user) {
        try {
            // Update user information
            String updateUserQuery = "UPDATE User SET email=?, profile_picture=?, roles=? WHERE id=?";
            PreparedStatement updateUserStatement = connection.prepareStatement(updateUserQuery);
            updateUserStatement.setString(1, user.getEmail());
            updateUserStatement.setString(2, user.getProfilePicture());
            updateUserStatement.setString(3, user.getRole());
            updateUserStatement.setInt(4, user.getId());
            int userRowsUpdated = updateUserStatement.executeUpdate();
            updateUserStatement.close();

            if (userRowsUpdated > 0) {
                System.out.println("Profile updated successfully!");
            } else {
                System.out.println("Failed to update profile.");
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    public void changeUserActive(User user, boolean active) {
        String query = "UPDATE User SET is_banned = ? WHERE id = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setBoolean(1, active);
            preparedStatement.setInt(2, user.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

/*
    //stat/////////
    public Map<Date, Integer> getUserDataByDate() {
        Map<Date, Integer> userDataByDate = new HashMap<>();
        String query = "SELECT date, COUNT(*) AS user_count FROM user GROUP BY date";

        try (
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Date dateRegistered = resultSet.getDate("date");
                int userCount = resultSet.getInt("user_count");
                userDataByDate.put(dateRegistered, userCount);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle SQLException appropriately
        }

        return userDataByDate;
    }
*/


    public Map<String, Integer> getUserDataByStatus() {
        Map<String, Integer> userDataByStatus = new HashMap<>();
        String query = "SELECT is_banned, COUNT(*) AS status_count FROM User GROUP BY is_banned";

        try (
                PreparedStatement statement = connection.prepareStatement(query);
                ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                int isBanned = resultSet.getInt("is_banned");
                String status = (isBanned == 1) ? "Active" : "Inactive";
                int statusCount = resultSet.getInt("status_count");
                userDataByStatus.put(status, statusCount);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle SQLException appropriately
        }

        return userDataByStatus;
    }

}
