package tn.esprit.crud.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import tn.esprit.crud.models.User;
import tn.esprit.crud.services.UserService;
import tn.esprit.crud.test.HelloApplication;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class AfficherUsers {

    @FXML
    private ListView<String> idCol;

    @FXML
    private ListView<String> emailCol;

    @FXML
    private ListView<String> nomCol;

    @FXML
    private ListView<String> prenomCol;

    @FXML
    private ListView<String> nationnalieCol; // Adjusted ListView name

    @FXML
    private ListView<String> numtelCol; // Added ListView for numtel

    @FXML
    private Button auth;

    private UserService userService; // Add this line

    @FXML
    void auth(ActionEvent event) {
        System.out.println("btn");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/tn/esprit/crud/login.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void supprimer() {
        String selectedUserId = idCol.getSelectionModel().getSelectedItem();
        if (selectedUserId != null) {
            try {
                userService.supprimer(Integer.parseInt(selectedUserId));

                loadUsers(); // refresh the table view
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            System.err.println("Error: No user selected.");
        }
    }

    private void loadUsers() throws SQLException {
        List<User> users = userService.recuperer();

        ObservableList<String> idList = FXCollections.observableArrayList();
        ObservableList<String> emailList = FXCollections.observableArrayList();
        ObservableList<String> nomList = FXCollections.observableArrayList();
        ObservableList<String> prenomList = FXCollections.observableArrayList();
        ObservableList<String> nationnaliteList = FXCollections.observableArrayList();
        ObservableList<String> numtelList = FXCollections.observableArrayList();

        for (User user : users) {
            idList.add(String.valueOf(user.getId()));
            emailList.add(user.getEmail());
            nomList.add(user.getNom());
            prenomList.add(user.getPrenom());
            nationnaliteList.add(user.getNationnalite());
            numtelList.add(String.valueOf(user.getNumtel()));
        }

        idCol.setItems(idList);
        emailCol.setItems(emailList);
        nomCol.setItems(nomList);
        prenomCol.setItems(prenomList);
        nationnalieCol.setItems(nationnaliteList);
        numtelCol.setItems(numtelList);
    }

    @FXML
    void PageModifier(ActionEvent event) {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/tn/esprit/crud/ModifierUser.fxml"));
        try {
            prenomCol.getScene().setRoot(fxmlLoader.load());
        } catch (IOException e) {
            System.err.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @FXML
    void ReturnToAjouter(ActionEvent event) {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/tn/esprit/crud/AjouterUser.fxml"));
        try {
            prenomCol.getScene().setRoot(fxmlLoader.load());
        } catch (IOException e) {
            System.err.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @FXML
    void initialize() {
        userService = new UserService(); // Initialize the userService variable here


        try {
            List<User> users = userService.recuperer();
            ObservableList<String> idList = FXCollections.observableArrayList();
            ObservableList<String> emailList = FXCollections.observableArrayList();
            ObservableList<String> nomList = FXCollections.observableArrayList();
            ObservableList<String> prenomList = FXCollections.observableArrayList();
            ObservableList<String> nationnaliteList = FXCollections.observableArrayList(); // Adjusted ObservableList name
            ObservableList<String> numtelList = FXCollections.observableArrayList(); // Added ObservableList for numtel

            for (User user : users) {
                idList.add(String.valueOf(user.getId()));
                emailList.add(user.getEmail());
                nomList.add(user.getNom());
                prenomList.add(user.getPrenom());
                nationnaliteList.add(user.getNationnalite()); // Adjusted attribute name
                numtelList.add(String.valueOf(user.getNumtel())); // Added attribute for numtel
            }

            idCol.setItems(idList);
            emailCol.setItems(emailList);
            nomCol.setItems(nomList);
            prenomCol.setItems(prenomList);
            nationnalieCol.setItems(nationnaliteList); // Adjusted ListView reference
            numtelCol.setItems(numtelList); // Added ListView reference

        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }
}