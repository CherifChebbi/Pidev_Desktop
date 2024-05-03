package tn.esprit.crud.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.mindrot.jbcrypt.BCrypt;
import tn.esprit.crud.services.UserService;
import tn.esprit.crud.utils.MailUtil;

import java.io.IOException;

public class ForgetPassword {

    @FXML
    private TextField codeField;
    @FXML
    private Label codeLabel;
    @FXML
    private TextField emailTF;
    @FXML
    private Label invalidText;
    @FXML
    private PasswordField password;
    @FXML
    private PasswordField confirmpassword;
    @FXML
    private Button verifierButton;
    @FXML
    private Label passwordL;
    @FXML
    private Label confirmL;
    @FXML
    private Button changeButton;

    public void getVerificationCode(ActionEvent event){
        emailTF.setDisable(true); // Corrected reference to emailTF
        UserService us = new UserService();
        if(us.userExistsByEmail(emailTF.getText()) == false){
            invalidText.setText("User does not exist");
            invalidText.setVisible(true);
            emailTF.setDisable(false);
            return;
        }
        String code = us.getVerificationCodeByEmail(emailTF.getText()); // Retrieve verification code from the database
        if (MailUtil.sendPasswordResetMail(emailTF.getText(), code)) {
            codeField.setVisible(true);
            codeLabel.setVisible(true);
            verifierButton.setVisible(true);
        }
    }


    public void changePassword(ActionEvent event) {
        UserService us = new UserService();
        String code = us.getVerificationCodeByEmail(emailTF.getText());
        if (codeField.getText().equals(code)) {
            codeField.setDisable(true);
            verifierButton.setVisible(false);
            password.setVisible(true);
            confirmpassword.setVisible(true);
            passwordL.setVisible(true);
            confirmL.setVisible(true);
            changeButton.setVisible(true);
        } else {
            invalidText.setText("Invalid code");
            invalidText.setVisible(true);
        }
    }

    public void updatePassword(ActionEvent event) {
        UserService us = new UserService();
        String code = us.getVerificationCodeByEmail(emailTF.getText());
        if (codeField.getText().equals(code)) {
            if (!password.getText().equals(confirmpassword.getText())) {
                invalidText.setText("Passwords do not match");
                invalidText.setVisible(true);
                return;
            }
            String hashedPassword = BCrypt.hashpw(password.getText(), BCrypt.gensalt());
            us.changePasswordByEmail(emailTF.getText(), hashedPassword);
        }
    }

    public void goBack() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/tn/esprit/crud/login.fxml"));
        Parent profileInterface = loader.load();
        Scene profileScene = new Scene(profileInterface);
        Stage profileStage = new Stage();
        profileStage.setScene(profileScene);

        // Close the current stage (assuming verifierButton is accessible from here)
        Stage currentStage = (Stage) verifierButton.getScene().getWindow();
        currentStage.close();

        // Show the login stage
        profileStage.show();
    }
}
