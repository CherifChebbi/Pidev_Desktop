package tn.esprit.crud.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import tn.esprit.crud.models.User;
import tn.esprit.crud.services.UserService;
import javafx.fxml.FXMLLoader;
import tn.esprit.crud.utils.UserSession;

import java.io.IOException;
import java.sql.SQLException;

import static tn.esprit.crud.utils.UserSession.setCurrentUser;

public class EditProfile {

    @FXML
    private TextField nomNouv;

    @FXML
    private TextField prenomNouv;

    @FXML
    private TextField nationnaliteNouv;

    @FXML
    private TextField emailNouv;

    @FXML
    private TextField numtelNouv;

    @FXML
    private TextField passNouv;

    @FXML
    private Button Enregistrer;

    @FXML
    private Button logout;

    @FXML
    private Button index;

    private UserService userService = new UserService();

    User currentUser = UserSession.getCurrentUser();
    @FXML
    private void initialize() {
        // Set the current user when the profile is loaded

        if (currentUser != null) {
            populateFields(currentUser);        } else {
            // Handle the case where no user is logged in
        }
    }
    private void populateFields(User currentUser) {
        if (currentUser != null) {
            nomNouv.setText(currentUser.getNom());
            prenomNouv.setText(currentUser.getPrenom());
            nationnaliteNouv.setText(currentUser.getNationnalite());
            emailNouv.setText(currentUser.getEmail());
            numtelNouv.setText(String.valueOf(currentUser.getNumtel()));
            // Vous pouvez également gérer d'autres champs ici si nécessaire
        }
    }

    @FXML
    private void editProfile(ActionEvent event) {
        try {
            if (isValidInput()) {
                // Check if currentUser is null
                if (currentUser == null) {
                    showAlert(Alert.AlertType.ERROR, "Error", "No User Selected", "Please select a user to update their profile.");
                    return; // Exit the method early
                }

                String newNom = nomNouv.getText();
                String newPrenom = prenomNouv.getText();
                String newNationnalite = nationnaliteNouv.getText();
                String newEmail = emailNouv.getText();
                String newNumTel = numtelNouv.getText();
                String newPassword = passNouv.getText();

                // Update user's information
                currentUser.setNom(newNom);
                currentUser.setPrenom(newPrenom);
                currentUser.setNationnalite(newNationnalite);
                currentUser.setEmail(newEmail);
                currentUser.setNumtel(Integer.parseInt(newNumTel));
                // Hash the new password
                String hashedPassword = hashPassword(newPassword);
                currentUser.setPassword(hashedPassword);

                // Call service to update user profile
                userService.editProfile(currentUser);

                // Display success message
                showAlert(Alert.AlertType.INFORMATION, "Success", "Profile Updated", "Your profile has been updated successfully.");
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Profile Update Failed", "An error occurred while updating your profile: " + e.getMessage());
        }
    }


    @FXML
    void Enregistrer(ActionEvent event) {
        editProfile(event);
    }

    @FXML
    private void logout(ActionEvent event) {
        // Implement logic to log out the user
        // Close the current window or return to login screen
        try {
            // Load the login.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/tn/esprit/crud/login.fxml"));
            Parent root = loader.load();

            // Create a new scene with the login layout
            Scene scene = new Scene(root);

            // Get the current stage and set the new scene
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void index(ActionEvent event) {
        // Implement logic to go back to the previous screen
        try {
            // Load the index.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/front/index.fxml"));
            Parent profileInterface = loader.load();
            Scene profileScene = new Scene(profileInterface);
            Stage profileStage = new Stage();
            profileStage.setScene(profileScene);

            // Close the current stage (assuming indexButton is accessible from here)
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();

            // Show the index stage
            profileStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean isValidInput() {
        String newNom = nomNouv.getText();
        String newPrenom = prenomNouv.getText();
        String newNationnalite = nationnaliteNouv.getText();
        String newEmail = emailNouv.getText();
        String newNumTel = numtelNouv.getText();

        if (newNom.isEmpty() || newPrenom.isEmpty() || newNationnalite.isEmpty() || newEmail.isEmpty() || newNumTel.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Invalid Input", "Please fill in all fields.");
            return false;
        }

        if (!newEmail.matches("\\b[\\w.%-]+@[-.\\w]+\\.[A-Za-z]{2,4}\\b")) {
            showAlert(Alert.AlertType.ERROR, "Error", "Invalid Email", "Please enter a valid email address.");
            return false;
        }

        if (!newNumTel.matches("\\d{8}")) {
            showAlert(Alert.AlertType.ERROR, "Error", "Invalid Phone Number", "Please enter a valid phone number (8 digits).");
            return false;
        }

        return true;
    }

    private String hashPassword(String password) {
        // Implement password hashing logic here
        return password; // For demonstration purposes, return the password as is
    }

    private void showAlert(Alert.AlertType alertType, String title, String headerText, String contentText) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
    }
}
