package tn.esprit.crud.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Pagination;
import javafx.stage.Stage;

import java.io.IOException;

public class Home {

    public Pagination pagination;
    @FXML
    private Button Explore;
    @FXML
    private Button loginButton;


    @FXML
    void Explore(ActionEvent event) throws IOException {       FXMLLoader loader = new FXMLLoader(getClass().getResource("/front/Explore.fxml"));
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

    @FXML
    private void auth(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/tn/esprit/crud/login.fxml"));
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