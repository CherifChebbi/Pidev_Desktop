package tn.esprit.application.controllers;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import tn.esprit.application.models.Role;
import tn.esprit.application.models.User;
import tn.esprit.application.services.UserService;

import java.io.IOException;
import java.util.Random;

public class Signup {
    @FXML
    private Button signupButton;
    @FXML
    private TextField nomTextField;
    @FXML
    private TextField prenomTextField;
    @FXML
    private TextField ageTextField;
    @FXML
    private TextField emailTextfield;
    @FXML
    private TextField emailTextfield1;
    @FXML
    private PasswordField passwordTextfield;
    @FXML
    private PasswordField ConfirmpasswordTextfield;
    @FXML
    private Label invalidText;
    @FXML
    private ComboBox<String> genderComboBox;
    @FXML
    private ComboBox<String> roleComboBox;
   public void signupButtonOnAction(ActionEvent event) throws IOException {

      // String gender = genderComboBox.getValue();

       //System.out.println(gender);
        if (emailTextfield.getText().isEmpty() || emailTextfield1.getText().isEmpty() || passwordTextfield.getText().isEmpty() || ConfirmpasswordTextfield.getText().isEmpty()|| roleComboBox.getValue() == null) {
            invalidText.setText("Please fill in all fields");
            invalidText.setVisible(true);
            return;
        }
        if(!emailTextfield.getText().equals(emailTextfield1.getText())){
            invalidText.setText("Emails do not match");
            invalidText.setVisible(true);
            return;
        }
        if(!passwordTextfield.getText().equals(ConfirmpasswordTextfield.getText())){
            invalidText.setText("Passwords do not match");
            invalidText.setVisible(true);
            return;
        }
       UserService us = new UserService();
       if(us.getUserByEmail(emailTextfield.getText()) == null){
           int leftLimit = 97; // letter 'a'
           int rightLimit = 122; // letter 'z'
           int targetStringLength = 6;
           Random random = new Random();
           String generatedString = random.ints(leftLimit, rightLimit + 1)
                   .limit(targetStringLength)
                   .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                   .toString();
           User u = new User(emailTextfield.getText(), generatedString, "avatar.png", Role.valueOf(roleComboBox.getValue()), true, nomTextField.getText(), prenomTextField.getText(), Integer.parseInt(ageTextField.getText()));
            us.ajouterUser(u);
       }else{
           invalidText.setText("User already exists");
           invalidText.setVisible(true);
           return;
       }
    }
    public void goBack() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/login.fxml"));
        Parent profileInterface = loader.load();
        Scene profileScene = new Scene(profileInterface);
        Stage profileStage = new Stage();
        profileStage.setScene(profileScene);

        // Close the current stage (assuming loginButton is accessible from here)
        Stage currentStage = (Stage) signupButton.getScene().getWindow();
        currentStage.close();

        // Show the profile stage
        profileStage.show();
    }
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
        Stage currentStage = (Stage) signupButton.getScene().getWindow();
        currentStage.close();

        // Show the profile stage
        profileStage.show();
    }
}
