package tn.esprit.crud.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import tn.esprit.crud.services.UserService;
import javafx.scene.control.Button;

import java.io.IOException;
import java.sql.SQLException;


public class login {
    @FXML
    private Button insription;

    @FXML
    private Button login;

    @FXML
    private PasswordField pass;

    @FXML
    private TextField name;
    @FXML
    private RadioButton et;

    @FXML
    private RadioButton fo;

    @FXML
    private RadioButton ad;


    private final UserService userService = new UserService();

    public login() throws SQLException {
    }

    @FXML
    void connecter(ActionEvent event) {
        String nom = name.getText();
        String mdp = pass.getText();
        if (userService.authenticateUser(nom, mdp)) {
            System.out.println("Login successful");
        } else {
            System.out.println("Login failed");
        }
        System.out.println("aa");

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

            // Close the current stage (login stage) if needed
            // ((Stage) insription.getScene().getWindow()).close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}