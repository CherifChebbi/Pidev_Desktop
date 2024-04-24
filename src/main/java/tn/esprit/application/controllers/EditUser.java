package tn.esprit.application.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import tn.esprit.application.models.User;
import tn.esprit.application.services.UserService;

public class EditUser {

    @FXML
    private TextField userEmailLabel;

    @FXML
    private CheckBox userActiveCheckBox;

    private User user;
    private UserService userService;

    public EditUser() {
        userService = new UserService();
    }

    public void initData(User user) {
        this.user = user;
        userEmailLabel.setText(user.getEmail());
        userActiveCheckBox.setSelected(user.isBanned());
    }

    @FXML
    private void saveChanges() {
        if (user != null) {
            user.setBanned(userActiveCheckBox.isSelected());
            userService.modifierUser(user); // Assuming you have a method to update the user in your UserService
        }
        closeDialog();
    }

    @FXML
    private void closeDialog() {
        userActiveCheckBox.getScene().getWindow().hide();
    }

}
