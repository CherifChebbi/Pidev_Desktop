package controllers.User;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.mindrot.jbcrypt.BCrypt;
import services.User.UserService;
import utils.MailUtil;

import java.io.IOException;
import java.util.UUID;

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

    String code;
    // Method to validate email format
    private boolean isValidEmail(String email) {
        return email.matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}");
    }

    // Method to validate password length
    private boolean isValidPassword(String password) {
        return password.length() >= 6; // Change the length as needed
    }

    public void getVerificationCode(ActionEvent event){
        emailTF.setDisable(true); // Corrected reference to emailTF
        UserService us = new UserService();
        String email = emailTF.getText();

        if (!isValidEmail(email)) {
            showAlert("Invalid Email", "Please enter a valid email address.");
            emailTF.setDisable(false);
            return;
        }

        if (!us.userExistsByEmail(email)) {
            showAlert("User Not Found", "The provided email does not exist.");
            emailTF.setDisable(false);
            return;
        }
         code = UUID.randomUUID().toString();
        if (MailUtil.sendPasswordResetMail(email, code)) {
            codeField.setVisible(true);
            codeLabel.setVisible(true);
            verifierButton.setVisible(true);
        }
    }
    @FXML
    public void changePassword(ActionEvent event) {
        UserService us = new UserService();
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
    @FXML
    public void updatePassword(ActionEvent event) {
        UserService us = new UserService();
        System.out.println("yesss");
        if (!isValidPassword(password.getText())) {
            showAlert("Invalid Password", "Password must be at least 6 characters long.");
            return;
        }

        if (!password.getText().equals(confirmpassword.getText())) {
            showAlert("Password Mismatch", "Passwords do not match.");
            return;
        }

        String hashedPassword = BCrypt.hashpw(password.getText(), BCrypt.gensalt());
        boolean success = us.changePasswordByEmail(emailTF.getText(), hashedPassword);

        if (success) {
            showAlert("Password Updated", "Password has been updated successfully.");
        } else {
            showAlert("Error", "Failed to update password.");
        }
    }


    public void goBack() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/User/login.fxml"));
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

    // Method to show an alert dialog
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
