package tn.esprit.crud.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import tn.esprit.crud.models.User;
import tn.esprit.crud.services.UserService;
import tn.esprit.crud.test.HelloApplication;
import javafx.scene.control.ToggleGroup;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ModifierUser implements Initializable {
    private ToggleGroup toggleGroup;
    private RadioButton selectedRadioButton;

    @FXML
    private TextField nomNouv;

    @FXML
    private TextField prenomNouv;

    @FXML
    private TextField nationnaliteNouv;

    @FXML
    private TextField emailNouv;

    @FXML
    private TextField numtelNouv;

    @FXML
    private Button modifier;

    @FXML
    private RadioButton fo;

    @FXML
    private RadioButton ad;

    private UserService userService = new UserService();

    private User selectedUser;

    public void setSelectedUser(User user) {
        this.selectedUser = user;
        populateFields();
    }

    private void populateFields() {
        if (selectedUser != null) {
            nomNouv.setText(selectedUser.getNom());
            prenomNouv.setText(selectedUser.getPrenom());
            nationnaliteNouv.setText(selectedUser.getNationnalite());
            emailNouv.setText(selectedUser.getEmail());
            numtelNouv.setText(String.valueOf(selectedUser.getNumtel()));
            if (selectedUser.getRoles().equals("ADMIN")) {
                ad.setSelected(true);
            } else {
                fo.setSelected(true);
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        toggleGroup = new ToggleGroup();
        fo.setToggleGroup(toggleGroup);
        ad.setToggleGroup(toggleGroup);
        toggleGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            selectedRadioButton = (RadioButton) newValue;
        });
    }

    @FXML
    void modifierUser() {
        try {
            String nouveauNom = nomNouv.getText();
            String nouveauPrenom = prenomNouv.getText();
            String nouvelleNationnalite = nationnaliteNouv.getText();
            String nouveauEmail = emailNouv.getText();
            int nouveauNumtel = Integer.parseInt(numtelNouv.getText());

            if (selectedUser != null) {
                selectedUser.setNom(nouveauNom);
                selectedUser.setPrenom(nouveauPrenom);
                selectedUser.setNationnalite(nouvelleNationnalite);
                selectedUser.setEmail(nouveauEmail);
                selectedUser.setNumtel(nouveauNumtel);
                selectedUser.setRoles(selectedRadioButton.getText());

                userService.modifier(selectedUser);

                afficherMessage("Succès", "L'utilisateur a été modifié avec succès.");
            } else {
                System.err.println("Error: No user selected.");
            }
        } catch (SQLException e) {
            afficherErreur("Erreur", "Erreur lors de la modification de l'utilisateur : " + e.getMessage());
        }
    }

    @FXML
    void Modification(ActionEvent event) {
        modifierUser();
    }

    @FXML
    void VersAfficher(ActionEvent event) {
        retournerVers("/tn/esprit/crud/AfficherUsers.fxml");
    }

    private void retournerVers(String resourcePath) {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource(resourcePath));
        try {
            nomNouv.getScene().setRoot(fxmlLoader.load());
        } catch (IOException e) {
            System.err.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private void afficherMessage(String titre, String contenu) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(contenu);
        alert.showAndWait();
    }

    private void afficherErreur(String titre, String contenu) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(contenu);
        alert.showAndWait();
    }
}
