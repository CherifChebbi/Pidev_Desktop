package tn.esprit.crud.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import tn.esprit.crud.models.User;
import tn.esprit.crud.services.UserService;
import javafx.scene.Node;
import tn.esprit.crud.utils.UserSession;

import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class login {
    @FXML
    private Button insription;
    @FXML
    public Hyperlink forgetP;

    @FXML
    private Button login;

    @FXML
    private PasswordField pass;

    @FXML
    private TextField name;

    private final UserService userService = new UserService();
    @FXML
    private Button Home;

    @FXML
    void connecter(ActionEvent event) {
        String nom = name.getText();
        String mdp = pass.getText();

        // Attempt to login
        User user = userService.authenticateUser(nom, mdp);

        if (user != null) {
            if (!user.isBanned()) {
                System.out.println("Login successful");
                // Proceed with any actions after successful login
                UserSession.setCurrentUser(user);

                openAfficherUsers(user); // Call the method to open AfficherUsers
            } else {
                System.out.println("Login failed: User is banned");
                showAlert("Login Failed", "Your account has been banned. Please contact the administrator for further assistance.");
            }
        } else {
            System.out.println("Login failed");
            showAlert("Login Failed", "Incorrect username or password.");
        }
    }



    @FXML
    void inscription(ActionEvent event) {
        try {
            // Load the FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/tn/esprit/crud/inscription.fxml"));
            Parent root = loader.load();

            // Create a new scene
            Scene scene = new Scene(root);

            // Get the stage information
            Stage stage = new Stage();
            stage.setTitle("Inscription Page");
            stage.setScene(scene);

            // Show the stage
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to open AfficherUsers upon successful login
    private void openAfficherUsers(User user) {
        try {
            FXMLLoader loader = new FXMLLoader();

            // Check the role of the user
            if (user.getRoles().equals("ADMIN")) {
                // If the user is an ADMIN, load AfficherUsers.fxml
                loader.setLocation(getClass().getResource("/tn/esprit/crud/AfficherUsers.fxml"));
            } else {
                // If the user is a USER, load index.fxml
                loader.setLocation(getClass().getResource("/Front/index.fxml"));
            }

            Parent root = loader.load();

            // Create a new scene
            Scene scene = new Scene(root);

            // Get the stage information
            Stage stage = new Stage();
            stage.setTitle("Afficher Users");
            stage.setScene(scene);

            // Close the current stage (login stage)
            Stage currentStage = (Stage) login.getScene().getWindow();
            currentStage.close();

            // Show the stage
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "An error occurred while opening the appropriate page.");
        }
    }


    // Method to show an alert dialog
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void forgetPassword(ActionEvent event){
        UserService userService = new UserService();
        Stage resetPasswordStage = new Stage();
        Parent resetPasswordInterface;
        try {
            resetPasswordInterface = FXMLLoader.load(getClass().getResource("/tn/esprit/crud//forgetPassword.fxml"));
            Scene resetPasswordScene = new Scene(resetPasswordInterface);
            resetPasswordStage.setScene(resetPasswordScene);
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();

            // Show the UserInterface stage
            resetPasswordStage.show();

        } catch (IOException ex) {
            Logger.getLogger(login.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    @FXML
    private void home (ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/front/Home.fxml"));
        Parent profileInterface = loader.load();
        Scene profileScene = new Scene(profileInterface);
        Stage profileStage = new Stage();
        profileStage.setScene(profileScene);

        // Close the current stage (assuming verifierButton is accessible from here)
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.close();

        // Show the login stage
        profileStage.show();
    }


}
