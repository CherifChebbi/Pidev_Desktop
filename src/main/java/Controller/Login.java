package Controller;

import Entity.roles;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.mindrot.jbcrypt.BCrypt;
import Entity.User;
import Services.userservice;

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
            userservice us = new userservice();
            User u = us.getUserByEmail(emailTextfield.getText());
            if (u == null) {
                invalidText.setText("Utilisateur introuvable");
                return;
            }
            //System.out.println(u);
            if (!u.isIs_banned()) {
                invalidText.setText("Compte inactif veuillez contacter l'administrateur");
                return;
            }
            String enteredPassword = passwordTextfield.getText();
            boolean passwordMatch = BCrypt.checkpw(enteredPassword, u.getPassword());
            if (passwordMatch) {
                this.connectedUser = u;
                User ui = us.getUserById(u.getId());
                invalidText.setText("");

                if (ui.getRoles() == u.getRoles()) {
                    us.goToUserList();
                } else {
                    if (ui.getRoles().equals(roles.ROLE_USER)) {
                        System.out.println("go to THERAPEUTE");

                    } else {
                        System.out.println("go to PATIENT");
                    }
                }


            } else {
                invalidText.setText("Mot de passe incorrect");
            }


        }
    }
}

