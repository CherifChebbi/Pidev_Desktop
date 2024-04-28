package tn.esprit.crud.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import tn.esprit.crud.models.User;
import tn.esprit.crud.services.UserService;
import tn.esprit.crud.test.HelloApplication;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AjouterUser implements Initializable {
    private ToggleGroup toggleGroup;
    private RadioButton selectedRadioButton;

    private UserService userService = new UserService();

    @FXML
    private TextField adresseTF;

    @FXML
    private TextField emailTF;

    @FXML
    private TextField mdpTF;

    @FXML
    private TextField nomTF;

    @FXML
    private TextField prenomTF;

    @FXML
    private TextField roleTF;

    @FXML
    private TextField numtelTF; // Added TextField for numtel

    @FXML
    private RadioButton et;

    @FXML
    private RadioButton fo;

    @FXML
    private RadioButton ad;

    @FXML
    void afficherUsers(ActionEvent event) {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/tn/esprit/crud/AfficherUsers.fxml"));
        try {
            nomTF.getScene().setRoot(fxmlLoader.load());
        } catch (IOException e) {
            System.err.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @FXML
    void ajouterUser(ActionEvent event) throws SQLException, IOException {
        User user = new User();
        user.setNom(nomTF.getText());
        user.setPrenom(prenomTF.getText());
        user.setNationnalite(adresseTF.getText());
        user.setEmail(emailTF.getText());
        user.setPassword(mdpTF.getText());
        user.setRoles(selectedRadioButton.getText());
        user.setNumtel(Integer.parseInt(numtelTF.getText())); // Set numtel from TextField

        try {
            userService.ajouter(user);
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Success");
            alert.setContentText("Personne Ajouter");
            alert.showAndWait();
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            throw new RuntimeException(e);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        toggleGroup = new ToggleGroup();
        et.setToggleGroup(toggleGroup);
        fo.setToggleGroup(toggleGroup);

        toggleGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            selectedRadioButton = (RadioButton) newValue;
        });
    }




}
