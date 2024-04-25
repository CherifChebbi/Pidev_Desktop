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
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import tn.esprit.crud.models.User;
import tn.esprit.crud.services.UserService;
import tn.esprit.crud.test.HelloApplication;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class AfficherUsers {



    @FXML
    private ListView<String> adresseCol;

    @FXML
    private ListView<String> emailCol;

    @FXML
    private ListView<String> mdpCol;

    @FXML
    private ListView<String> nomCol;

    @FXML
    private ListView<String> prenomCol;

    @FXML
    private ListView<String> idCol;
    @FXML
    private Button auth;

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
    void PageSupprimer(ActionEvent event) {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/tn/esprit/crud/SupprimerUser.fxml"));
        try {
            prenomCol.getScene().setRoot(fxmlLoader.load());
        } catch (IOException e) {
            System.err.println(e.getMessage());
            throw new RuntimeException(e);
        }

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
        UserService userService = new UserService();

        try {
            List<User> users = userService.recupperer();
            ObservableList<String> idList = FXCollections.observableArrayList();
            ObservableList<String> emailList = FXCollections.observableArrayList();
        ObservableList<String> mdpList = FXCollections.observableArrayList();
        ObservableList<String> nomList = FXCollections.observableArrayList();
        ObservableList<String> prenomList = FXCollections.observableArrayList();
        ObservableList<String> adresseList = FXCollections.observableArrayList();

        for (User user : users) {
            idList.add(String.valueOf(user.getId()));
            emailList.add(user.getEmail());
            mdpList.add(user.getMdp());
            nomList.add(user.getNom());
            prenomList.add(user.getPrenom());
            adresseList.add(user.getAdresse());
        }
        idCol.setItems(idList);
        emailCol.setItems(emailList);
        mdpCol.setItems(mdpList);
        nomCol.setItems(nomList);
        prenomCol.setItems(prenomList);
        adresseCol.setItems(adresseList);

    } catch (SQLException e) {
        System.err.println(e.getMessage());
    }
}

}
