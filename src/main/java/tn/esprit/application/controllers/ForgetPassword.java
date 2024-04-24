package tn.esprit.application.controllers;

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
import tn.esprit.application.services.UserService;
import tn.esprit.application.utils.MailUtil;

import java.io.IOException;

public class ForgetPassword {
    @FXML
    private TextField codeField;
    @FXML
    private Label codeLabel;
    @FXML
    private Button loginButton;
    @FXML
    private TextField emailTextfield;
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
        emailTextfield.setDisable(true);
        UserService us = new UserService();
        if(us.UserExistsByEmail(emailTextfield.getText()) == false){

            invalidText.setText("User does not exist");
            invalidText.setVisible(true);
            emailTextfield.setDisable(false);

            return;
        }
        if( MailUtil.sendPasswordResetMail(emailTextfield.getText(), us.getVerificationCodeByEmail(emailTextfield.getText()))) {
            codeField.setVisible(true);
            codeLabel.setVisible(true);
            loginButton.setVisible(false);
            verifierButton.setVisible(true);


        }
    }
    public void changePassword(ActionEvent event){
        UserService us = new UserService();
        String code = us.getVerificationCodeByEmail(emailTextfield.getText());
        if(codeField.getText().equals(code)){
            System.out.println("ok");
            codeField.setDisable(true);
            loginButton.setVisible(false);
            verifierButton.setVisible(false);
            password.setVisible(true);
            confirmpassword.setVisible(true);
            passwordL.setVisible(true);
            confirmL.setVisible(true);
            changeButton.setVisible(true);
        }else{
            invalidText.setText("Invalid code");
            invalidText.setVisible(true);
        }
    }
    public void updatePassword(ActionEvent event){
        UserService us = new UserService();
        String code = us.getVerificationCodeByEmail(emailTextfield.getText());
        if(codeField.getText().equals(code)){

            if(!password.getText().equals(confirmpassword.getText())){
                invalidText.setText("Passwords do not match");
                invalidText.setVisible(true);
                return;
            }

            String hashedPassword = BCrypt.hashpw(password.getText(), BCrypt.gensalt());
            us.changePasswordByEmail(emailTextfield.getText(), hashedPassword);
        }
    }
    public void goBack() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/login.fxml"));
        Parent profileInterface = loader.load();
        Scene profileScene = new Scene(profileInterface);
        Stage profileStage = new Stage();
        profileStage.setScene(profileScene);

        // Close the current stage (assuming loginButton is accessible from here)
        Stage currentStage = (Stage) verifierButton.getScene().getWindow();
        currentStage.close();

        // Show the profile stage
        profileStage.show();
    }
}
