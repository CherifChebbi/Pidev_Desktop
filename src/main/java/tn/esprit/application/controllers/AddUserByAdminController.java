package tn.esprit.application.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import tn.esprit.application.models.Role;
import tn.esprit.application.models.User;
import tn.esprit.application.services.UserService;
import tn.esprit.application.utils.MailUtil;

import java.util.Random;

public class AddUserByAdminController {

    @FXML
    private TextField fname;
    @FXML
    private TextField lname;
    @FXML
    private TextField email;
    @FXML
    private Label invalidText;
    @FXML
    private TextField phone;
    @FXML
    private TextField nationnalite; // Assuming this is the nationalite text field
    @FXML
    private TextField role; // Assuming this is the role text field

    @FXML
    private Label titleid;

    private Role selectedRole; // Assuming Role is the enum type

    public void initData(Role role) {
        this.selectedRole = role;
        this.titleid.setText("Add " + this.selectedRole.toString());
    }

    @FXML
    public void signupButtonOnAction(ActionEvent event) {
        if (selectedRole == null) {
            System.out.println("Role is not initialized. Make sure to call initData method before signupButtonOnAction.");
            return;
        }

        if (fname.getText().isEmpty() || lname.getText().isEmpty() || email.getText().isEmpty() || phone.getText().isEmpty() || nationnalite.getText().isEmpty() || role.getText().isEmpty()) {
            invalidText.setText("Please fill in all fields");
            invalidText.setVisible(true);
            return;
        }

        UserService userService = new UserService();
        if (userService.getUserByEmail(email.getText()) != null) {
            invalidText.setText("Email already exists");
            invalidText.setVisible(true);
            return;
        }

        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 6;
        Random random = new Random();
        String generatedString = random.ints(leftLimit, rightLimit + 1)
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();

        String password = random.ints(leftLimit, rightLimit + 1)
                .limit(12)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();

        User user = new User(email.getText(), generatedString, "avatar.png", selectedRole, true, fname.getText(), lname.getText(), Integer.parseInt(phone.getText()));
        // Assuming you have a setter method for nationnalite and role in the User class
        user.setNationnalite(nationnalite.getText());
        user.setRole(role.getText());

        if (userService.ajouterUser(user)) {
            MailUtil.sendPassword(email.getText(), password);
            System.out.println("User added successfully");
        } else {
            invalidText.setText("User already exists");
            invalidText.setVisible(true);
        }
    }
}
