package Controller;


import Entity.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import Services.userservice;

import java.sql.SQLException;
import java.util.List;

public class RegisterController {
    userservice SR = new userservice();

    @FXML
    private TableView<User> afficher;

    @FXML
    private TextField prenom;

    @FXML
    private TableColumn<User, String> descriptioncol;
    @FXML
    private TableColumn<User, String> imagecol;
    @FXML
    private TableColumn<User, String> emailcol;
    @FXML
    private TableColumn<User, String> passcol;


    @FXML
    private TextField Phone;

    @FXML
    private TextField nationnalite;

    @FXML
    private TableColumn<User, Integer> idcol;

    @FXML
    private TableColumn<User, String> locationcol;

    @FXML
    private TextField email;

    @FXML
    private TableColumn<User, String> nomcol;

    @FXML
    private TextField password;
    @FXML
    private TextField nom;



    @FXML
    void ajouter(ActionEvent event) throws SQLException {
        String y = String.valueOf(email.getText());
        String z = String.valueOf(password.getText());
        String i = String.valueOf(nom.getText());
        String j = String.valueOf(prenom.getText());
        String n = String.valueOf(Phone.getText());
        String k = String.valueOf(nationnalite.getText());
        SR.ajouter(new User(y, z, i, j, n, k));
        afficher();
    }
    /* User user = new User();
                user.setId(resultSet.getInt("Id"));
                user.setEmail(resultSet.getString("email"));
                user.setPassword(resultSet.getString("password"));
                user.setImage(resultSet.getString("profile_picture"));
                user.setRole(Role.valueOf(resultSet.getString("roles")));
                users.add(user);*/







    @FXML
    public void afficher() throws SQLException {
        List<User> userList = SR.afficher();
        ObservableList<User> observableList = FXCollections.observableArrayList(userList);
        afficher.setItems(observableList);
    }



    @FXML
    void modifier(ActionEvent event) throws SQLException {
        // Check if a row is selected
        User selectedUser = afficher.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            // Retrieve updated data from input fields
            String newNom = nom.getText();
            String newPrenom = prenom.getText();
            String newEmail = email.getText();
            String newPassword = password.getText();
            String newNationnalite = nationnalite.getText();
            String newNumTel = Phone.getText();

            // Update the selected Restaurant object
            selectedUser.setNom(newNom);
            selectedUser.setPrenom(newPrenom);
            selectedUser.setEmail(newEmail);
            selectedUser.setPassword(newPassword);
            selectedUser.setNationnalite(newNationnalite);
            selectedUser.setNumtel(newNumTel);


            // Call the modifier method in ServiceRestaurant
            SR.modifier(selectedUser);

            // Refresh TableView
            afficher();
        } else {
            // Display an error message or prompt the user to select a row
            System.out.println("Please select a user to delete.");
        }
    }


    @FXML
    void supprimer(ActionEvent event) throws SQLException {
        User selectedUser = afficher.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            SR.supprimer(selectedUser);
            refreshTableView();
            System.out.println("User deleted successfully!");
        } else {
            System.out.println("Please select a user to delete.");
        }
    }

    private void refreshTableView() {try {
        afficher();
    } catch (SQLException e) {
        e.printStackTrace();
    }
    }

    @FXML
    private void initialize() {
        idcol.setCellValueFactory(new PropertyValueFactory<>("id"));
        nomcol.setCellValueFactory(new PropertyValueFactory<>("nom"));
        locationcol.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        imagecol.setCellValueFactory(new PropertyValueFactory<>("nationnalite"));
        descriptioncol.setCellValueFactory(new PropertyValueFactory<>("numtel"));
        emailcol.setCellValueFactory(new PropertyValueFactory<>("email"));
        passcol.setCellValueFactory(new PropertyValueFactory<>("password"));

    }




}