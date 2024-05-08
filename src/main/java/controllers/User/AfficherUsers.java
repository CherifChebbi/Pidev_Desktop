package controllers.User;

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
import models.User;
import services.User.UserService;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class AfficherUsers {

    @FXML
    private TableView<User> tableView;

    @FXML
    private AnchorPane anchorPane; // Assuming the TableView is wrapped inside an AnchorPane

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

    @FXML
    private Pagination pagination;
    private ObservableList<User> usersList;
    private final int rowsPerPage = 5;
    private UserService userService;

    @FXML
    void auth(ActionEvent event) {
        System.out.println("btn");
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/User/login.fxml"));
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
            usersList = FXCollections.observableArrayList(users);
            pagination.setPageCount((int) Math.ceil((double) usersList.size() / rowsPerPage));

            pagination.setPageFactory(this::createPage);

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

    private Node createPage(int pageIndex) {
        int fromIndex = pageIndex * rowsPerPage;
        int toIndex = Math.min(fromIndex + rowsPerPage, usersList.size());

        tableView.setItems(FXCollections.observableArrayList(usersList.subList(fromIndex, toIndex)));

        // Wrap the TableView inside a ScrollPane
        ScrollPane scrollPane = new ScrollPane(tableView);

       // scrollPane.setMinWidth(850); // Set minimum width
      //  scrollPane.setPrefWidth(850); // Set preferred width

      //  scrollPane.setMinHeight(120); // Set minimum height
       // scrollPane.setPrefHeight(120); // Set preferred height

        return scrollPane;
    }


    @FXML
    void PageModifier(ActionEvent event) {
        // Get the selected user
        User selectedUser = tableView.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            // Load the ModifierUser.fxml file
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/User/ModifierUser.fxml"));
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
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/User/AjouterUser.fxml"));
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
       // tableView.setPrefSize(850, 400);



    }


    @FXML
    public void Back_Gestion_User(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/User/AfficherUsers.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    public void Back_Gestion_Pays(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Pays/AfficherPays.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    public void Back_Gestion_Restaurant(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Restaurant/Back.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    public void Back_Gestion_Hebergement(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/CategoryH/Category.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    public void Back_Gestion_Event(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Event/HomeBack.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
