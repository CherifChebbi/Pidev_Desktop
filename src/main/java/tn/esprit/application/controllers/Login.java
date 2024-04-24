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
            UserService userService = new UserService();
            User user = userService.getUserByEmail(emailTextfield.getText());
            if (user == null) {
                invalidText.setText("Utilisateur introuvable");
                return;
            }
            if (!user.isBanned()) {
                invalidText.setText("Compte inactif veuillez contacter l'administrateur");
                return;
            }
            String enteredPassword = passwordTextfield.getText();
            boolean passwordMatch = BCrypt.checkpw(enteredPassword, user.getPassword());
            if (passwordMatch) {
                this.connectedUser = user;
                invalidText.setText("");

                if (user.getRole().equals("admin")) {
                    goToDashboardAdmin();
                } else {
                    // Handle user login
                    System.out.println("User logged in");
                }
            } else {
                invalidText.setText("Mot de passe incorrect");
            }
        }
    }

    @FXML
    private void forgetPassword(ActionEvent event) {
        // Implementation for password reset
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

    public void goToDashboardAdmin() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/DashboardAdmin.fxml"));
        Parent dashboardInterface = loader.load();
        DashboardAdmin dashboardAdminController = loader.getController();
        // Pass the connected user to the dashboard controller if needed
        dashboardAdminController.initData(this.connectedUser);
        Scene dashboardScene = new Scene(dashboardInterface);
        Stage dashboardStage = new Stage();
        dashboardStage.setScene(dashboardScene);
        Stage currentStage = (Stage) loginButton.getScene().getWindow();
        currentStage.close();
        dashboardStage.show();
    }
}
