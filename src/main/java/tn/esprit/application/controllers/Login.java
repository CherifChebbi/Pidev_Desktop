package tn.esprit.application.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.mindrot.jbcrypt.BCrypt;
import tn.esprit.application.models.Role;
import tn.esprit.application.models.User;
import tn.esprit.application.services.UserService;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Login {

    @FXML
    private Button loginButton;
    @FXML
    private TextField emailTextfield;
    @FXML
    private PasswordField passwordTextfield;
    @FXML
    private Hyperlink signupLink;
    @FXML
    private Label invalidText;
    private User connectedUser;
    public void loginButtonOnAction(ActionEvent e) throws IOException {
        if (emailTextfield.getText().isEmpty() || passwordTextfield.getText().isEmpty()) {
            invalidText.setText("Veuillez remplir tous les champs");
            return;
        } else {
            UserService us = new UserService();
            User u = us.getUserByEmail(emailTextfield.getText());
            if(u == null){
                invalidText.setText("Utilisateur introuvable");
                return;
            }
            //System.out.println(u);
            if(!u.getBanned()){
                invalidText.setText("Compte inactif veuillez contacter l'administrateur");
                return;
            }
            String enteredPassword = passwordTextfield.getText();
            boolean passwordMatch = BCrypt.checkpw(enteredPassword, u.getPassword());
            if (passwordMatch) {
                this.connectedUser = u;
                User ui = us.getUserById(u.getId());
                invalidText.setText("");

                if (ui.getRole() == Role.ADMIN) {
                    goToUserList();
                }

                else {
                    if (ui.getRole() == Role.ROLE_THERAPEUTE) {
                        System.out.println("go to THERAPEUTE");

                    }else{
                        System.out.println("go to PATIENT");
                    }
                }


            }else{
                invalidText.setText("Mot de passe incorrect");
            }


        }
    }


/*
    public void goToProfile(User user) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/profile.fxml"));
        Parent profileInterface = loader.load();

        // Get the controller instance
        Profile profileController = loader.getController();

        // Initialize data using the controller's method
        profileController.initData(user);

        Scene profileScene = new Scene(profileInterface);
        Stage profileStage = new Stage();
        profileStage.setScene(profileScene);

        // Close the current stage (assuming loginButton is accessible from here)
        Stage currentStage = (Stage) loginButton.getScene().getWindow();
        currentStage.close();

        // Show the profile stage
        profileStage.show();
    }
*/
    @FXML
    private void forgetPassword(ActionEvent event){
        UserService userService = new UserService();
        Stage resetPasswordStage = new Stage();
        Parent resetPasswordInterface;
        try {
            resetPasswordInterface = FXMLLoader.load(getClass().getResource("/gui/forgetPassword.fxml"));
            Scene resetPasswordScene = new Scene(resetPasswordInterface);
            resetPasswordStage.setScene(resetPasswordScene);
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();

            // Show the UserInterface stage
            resetPasswordStage.show();

        } catch (IOException ex) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    public void goToSignup() throws IOException {
        Stage signupStage = new Stage();
        Parent signupInterface = FXMLLoader.load(getClass().getResource("/gui/signup.fxml"));
        Scene signupScene = new Scene(signupInterface);
        signupStage.setScene(signupScene);
        Stage currentStage = (Stage) loginButton.getScene().getWindow();
        currentStage.close();
        signupStage.show();
    }
    public void goToUserList() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/DashboardAdmin.fxml"));
        Parent profileInterface = loader.load();

        // Get the controller instance
        DashboardAdmin profileController = loader.getController();

        // Initialize data using the controller's method


        Scene profileScene = new Scene(profileInterface);
        Stage profileStage = new Stage();
        profileStage.setScene(profileScene);

        // Close the current stage (assuming loginButton is accessible from here)
        Stage currentStage = (Stage) loginButton.getScene().getWindow();
        currentStage.close();

        // Show the profile stage
        profileStage.show();
    }
}
