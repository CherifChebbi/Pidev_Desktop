package tn.esprit.crud.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import tn.esprit.crud.models.User;
import tn.esprit.crud.services.UserService;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InscriptionUser implements Initializable {
    private ToggleGroup toggleGroup;
    private RadioButton selectedRadioButton;

    private UserService userService = new UserService();

    @FXML
    private TextField adresseTF;

    @FXML
    private TextField emailTF;

    @FXML
    private TextField mdpTF; // Retained the same FX ID

    @FXML
    private TextField nomTF;

    @FXML
    private TextField prenomTF;

    @FXML
    private TextField numtelTF; // Retained the same FX ID

    @FXML
    private RadioButton et;

    @FXML
    private RadioButton fo;

    @FXML
    private RadioButton ad;

    @FXML
    void inscription(ActionEvent event) throws SQLException, IOException {
        String nom = nomTF.getText().trim();
        String prenom = prenomTF.getText().trim();
        String adresse = adresseTF.getText().trim();
        String email = emailTF.getText().trim();
        String password = mdpTF.getText().trim(); // Retained the same TextField name
        int numtel = Integer.parseInt(numtelTF.getText().trim());

        // Validate input fields
        if (nom.isEmpty() || prenom.isEmpty() || adresse.isEmpty() || email.isEmpty() || password.isEmpty()) {
            showAlert("Please fill in all fields");
            return;
        }

        // Validate email format
        if (!isValidEmail(email)) {
            showAlert("Please enter a valid email address");
            return;
        }

        // Validate password length
        if (password.length() < 8) {
            showAlert("Password must be at least 8 characters long");
            return;
        }

        // Proceed with user registration
        User user = new User();
        user.setNom(nom);
        user.setPrenom(prenom);
        user.setNationnalite(adresse);
        user.setEmail(email);
        user.setPassword(password); // Retained the same attribute name
        user.setRoles(selectedRadioButton.getText());
        user.setNumtel(numtel);

        try {
            userService.inscription(user);
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

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText(message);
        alert.showAndWait();
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
