package tn.esprit.crud.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import tn.esprit.crud.models.User;
import tn.esprit.crud.services.UserService;
import tn.esprit.crud.test.HelloApplication;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class AfficherUsers {

    @FXML
    private TableView<User> tableView;

    @FXML
    private TableColumn<User, Integer> idCol;

    @FXML
    private TableColumn<User, String> emailCol;

    @FXML
    private TableColumn<User, String> nomCol;

    @FXML
    private TableColumn<User, String> prenomCol;

    @FXML
    private TableColumn<User, String> nationnalieCol;

    @FXML
    private TableColumn<User, Integer> numtelCol;

    @FXML
    private TableColumn<User, Boolean> banCol;

    @FXML
    private Button auth;

    @FXML
    private RadioButton ban;

    private UserService userService;

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
        User selectedUser = tableView.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            try {
                userService.supprimer(selectedUser.getId());
                loadUsers(); // refresh the table view
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            System.err.println("Error: No user selected.");
        }
    }

    @FXML
    private void banUser() {
        User selectedUser = tableView.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            try {
                userService.toggleUserBanStatus(selectedUser.getId(), true);
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Ban ");
                alert.setContentText("Ban ");
                alert.showAndWait();
                loadUsers(); // refresh the table view
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            System.err.println("Error: No user selected.");
        }
    }

    private void loadUsers() {
        try {
            List<User> users = userService.recuperer();
            ObservableList<User> usersList = FXCollections.observableArrayList(users);

            idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
            emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
            nomCol.setCellValueFactory(new PropertyValueFactory<>("nom"));
            prenomCol.setCellValueFactory(new PropertyValueFactory<>("prenom"));
            nationnalieCol.setCellValueFactory(new PropertyValueFactory<>("nationnalite"));
            numtelCol.setCellValueFactory(new PropertyValueFactory<>("numtel"));
            banCol.setCellValueFactory(new PropertyValueFactory<>("banned")); // Set cell value factory for "Banned" column

            tableView.setItems(usersList);
        } catch (SQLException e) {
            System.err.println("Error loading users: " + e.getMessage());
        }
    }

    @FXML
    void PageModifier(ActionEvent event) {
        // Get the selected user
        User selectedUser = tableView.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            // Load the ModifierUser.fxml file
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/tn/esprit/crud/ModifierUser.fxml"));
            try {
                // Load the root node
                Parent root = fxmlLoader.load();
                // Get the controller instance
                ModifierUser modifierUserController = fxmlLoader.getController();
                // Pass the selected user to the controller
                modifierUserController.setSelectedUser(selectedUser);
                // Set the scene with the modified root
                tableView.getScene().setRoot(root);
            } catch (IOException e) {
                System.err.println(e.getMessage());
                throw new RuntimeException(e);
            }
        } else {
            System.err.println("Error: No user selected.");
        }
    }


    @FXML
    void ReturnToAjouter(ActionEvent event) {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/tn/esprit/crud/AjouterUser.fxml"));
        try {
            tableView.getScene().setRoot(fxmlLoader.load());
        } catch (IOException e) {
            System.err.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @FXML
    void initialize() {
        userService = new UserService();
        loadUsers();
    }
}
