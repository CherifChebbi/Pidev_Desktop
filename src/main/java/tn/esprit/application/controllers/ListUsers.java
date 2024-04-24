package tn.esprit.application.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import tn.esprit.application.models.User;
import tn.esprit.application.services.UserService;

import java.io.IOException;
import java.util.Optional;

public class ListUsers {
    @FXML
    private ListView<User> listUsers;

    private ObservableList<User> userList;

    private UserService userService;

    public ListUsers() {
        userService = new UserService();
    }

    public void initData() {
        initializeListView();
        populateListView();
    }

    private void initializeListView() {
        userList = FXCollections.observableArrayList();
        listUsers.setItems(userList);

        listUsers.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(User user, boolean empty) {
                super.updateItem(user, empty);

                if (empty || user == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    GridPane gridPane = new GridPane();
                    gridPane.setHgap(10);
                    gridPane.setVgap(5);

                    // Add labels for attribute names as titles at the top of each column
                    gridPane.add(new Label("ID"), 0, 0);
                    gridPane.add(new Label("Email"), 1, 0);
                    gridPane.add(new Label("Role"), 2, 0);
                    gridPane.add(new Label("Nom"), 3, 0);
                    gridPane.add(new Label("Prenom"), 4, 0);

                    // Add values for each attribute below the titles
                    gridPane.add(new Label(String.valueOf(user.getId())), 0, 1);
                    gridPane.add(new Label(user.getEmail()), 1, 1);
                    gridPane.add(new Label(user.getRole()), 2, 1);
                    gridPane.add(new Label(user.getNom()), 3, 1);
                    gridPane.add(new Label(user.getPrenom()), 4, 1);

                    // Create edit and delete buttons
                    Button editButton = new Button("Edit");
                    Button deleteButton = new Button("Delete");

                    editButton.setOnAction(event -> {
                        // Handle edit action here
                        try {
                            editUser(user.getId());
                        } catch (IOException e) {
                            e.printStackTrace();
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
                            userService.deleteUser(user);
                            populateListView();
                        }
                    });

                    // Create an HBox to hold the action buttons
                    HBox actionButtons = new HBox(6, editButton, deleteButton);

                    // Add action buttons to the "Actions" column
                    gridPane.add(actionButtons, 5, 1);

                    setGraphic(gridPane);
                }
            }
        });
    }

    private void populateListView() {
        userList.clear();
        userList.addAll(userService.getAll());
    }

    private void editUser(int id) throws IOException {
        User user = userService.getUserById(id);
        // Handle edit action here
    }
}
