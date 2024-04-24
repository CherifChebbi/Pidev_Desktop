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
    private TextField email;
    @FXML
    private TextField fname;
    @FXML
    private TextField lname;
    @FXML
    private TextField phone;
    @FXML
    private TextField nationnalite;
    @FXML
    private TextField role;
    @FXML
    private Label invalidText;

    public void signupButtonOnAction(ActionEvent event) throws IOException {
        if (email.getText().isEmpty() || fname.getText().isEmpty() || lname.getText().isEmpty() || phone.getText().isEmpty() || nationnalite.getText().isEmpty() || role.getText().isEmpty()) {
            invalidText.setText("Please fill in all fields");
            invalidText.setVisible(true);
            return;
        }

        UserService us = new UserService();
        if (us.getUserByEmail(email.getText()) == null) {
            int leftLimit = 97; // letter 'a'
            int rightLimit = 122; // letter 'z'
            int targetStringLength = 6;
            Random random = new Random();
            String generatedString = random.ints(leftLimit, rightLimit + 1)
                    .limit(targetStringLength)
                    .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                    .toString();
            User u = new User(email.getText(), generatedString, "avatar.png", Role.valueOf(role.getText()), true, fname.getText(), lname.getText(), Integer.parseInt(phone.getText()));
            us.ajouterUser(u);
        } else {
            invalidText.setText("User already exists");
            invalidText.setVisible(true);
            return;
        }
    }

    public void goToProfile(User user) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/profile.fxml"));
        Parent profileInterface = loader.load();

        // Get the controller instance
        Profile profileController = loader.getController();

        // Initialize data using the controller's method
        profileController.initData(user);

        // Now, show the signup scene
        Scene profileScene = new Scene(profileInterface);
        Stage profileStage = new Stage();
        profileStage.setScene(profileScene);

        // Close the current stage (assuming signupButton is accessible from here)
        Stage currentStage = (Stage) signupButton.getScene().getWindow();
        currentStage.close();

        // Show the profile stage
        profileStage.show();
    }

    public void goBack() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/login.fxml"));
        Parent profileInterface = loader.load();
        Scene profileScene = new Scene(profileInterface);
        Stage profileStage = new Stage();
        profileStage.setScene(profileScene);

        // Close the current stage (assuming signupButton is accessible from here)
        Stage currentStage = (Stage) signupButton.getScene().getWindow();
        currentStage.close();

        // Show the profile stage
        profileStage.show();
    }
}
