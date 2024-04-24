package tn.esprit.application.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import tn.esprit.application.models.Role;
import tn.esprit.application.models.User;
import tn.esprit.application.services.UserService;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class ListUsers  {
    @FXML
    private ListView<User> listUsers;

    private ObservableList<User> codeList;

    public void initData(Role rolee) {

        initializeListView();
        populateListView();
    }

  /*  @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeListView();
        populateListView();
    }
*/
    private void initializeListView() {
        codeList = FXCollections.observableArrayList();
        listUsers.setItems(codeList);

        // Set custom cell factory to display attributes and actions
        listUsers.setCellFactory(new Callback<ListView<User>, ListCell<User>>() {
            @Override
            public ListCell<User> call(ListView<User> listView) {
                return new ListCell<User>() {
                    @Override
                    protected void updateItem(User userr, boolean empty) {
                        super.updateItem(userr, empty);

                        if (empty || userr == null) {
                            setGraphic(null);
                        } else {
                            UserService us = new UserService();
                            int id = userr.getId();
                            userr = us.getUserById(userr.getId());
                            GridPane gridPane = new GridPane();
                            gridPane.setHgap(10);
                            gridPane.setVgap(5);

                            // Add labels for attribute names as titles at the top of each column
                            gridPane.add(new Label("ID"), 0, 0);
                            gridPane.add(new Label("email"), 1, 0);
                            gridPane.add(new Label("role"), 2, 0);
                            gridPane.add(new Label("nom"), 3, 0);
                            gridPane.add(new Label("prenom"), 4, 0);
                            gridPane.add(new Label("age"), 5, 0);

                            // Add values for each attribute below the titles
                            gridPane.add(new Label(String.valueOf(userr.getId())), 0, 1);
                            gridPane.add(new Label(String.valueOf(userr.getEmail())), 1, 1);
                            gridPane.add(new Label(String.valueOf(userr.getRole())), 2, 1);
                            gridPane.add(new Label(String.valueOf(userr.getNom())), 3, 1);
                            gridPane.add(new Label(String.valueOf(userr.getPrenom())), 4, 1);
                            gridPane.add(new Label(String.valueOf(userr.getAge())), 5, 1);


                            // Create edit and delete buttons
                            Button editButton = new Button("Edit");
                            Button deleteButton = new Button("Delete");

                            // Set actions for edit and delete buttons
                            editButton.setOnAction(event -> {
                                // Handle edit action here

                                try {
                                    editCodePromo(id);
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            });

                            deleteButton.setOnAction(event -> {
                                // Handle delete action here
                                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                                alert.setTitle("Delete Confirmation");
                                alert.setHeaderText("Confirm Delete");
                                alert.setContentText("Are you sure you want to delete this item?");

                                Optional<ButtonType> result = alert.showAndWait();
                                if (result.isPresent() && result.get() == ButtonType.OK) {
                                    // Call delete method if "OK" is clicked

                                        deleteCodePromo(id);

                                }
                            });

                            // Create an HBox to hold the action buttons
                            HBox actionButtons = new HBox(6, editButton, deleteButton);

                            // Add action buttons to the "Actions" column
                            gridPane.add(actionButtons, 6, 1);

                            setGraphic(gridPane);
                        }
                    }
                };
            }
        });
    }

    private void populateListView( ) {
        UserService gestionCodePromo = new UserService();
        codeList.addAll(gestionCodePromo.getAll());
    }

    private void deleteCodePromo(int id) {
        UserService gs = new UserService();
       gs.deleteUser(gs.getUserById(id));
        initializeListView();
        populateListView( );
    }
    private void editCodePromo(int id) throws IOException {
       UserService gs = new UserService();
        User user = new User();
        user = gs.getUserById(id);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/editUser.fxml"));
        Parent profileInterface = loader.load();
        EditUser profileController = loader.getController();
        profileController.initData(user);
        Scene profileScene = new Scene(profileInterface);
        Stage profileStage = new Stage();
        profileStage.setScene(profileScene);
        profileStage.show();
        Stage currentStage = (Stage) listUsers.getScene().getWindow();
        //currentStage.close();


    }
    public void editUser(int id) throws IOException {
        UserService us = new UserService();
        User u = new User();
        u = us.getUserById(id);
        goToProfile(u);
    }
    public void navigateToProfile() {

    }
    public void goToProfile(User user) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/forgetPassword.fxml"));
        Parent profileInterface = loader.load();
        Profile profileController = loader.getController();
        profileController.initData(user);
        Scene profileScene = new Scene(profileInterface);
        Stage profileStage = new Stage();
        profileStage.setScene(profileScene);
        profileStage.show();
        Stage currentStage = (Stage) listUsers.getScene().getWindow();
        currentStage.close();
    }


}
