package tn.esprit.application.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import tn.esprit.application.models.Role;
import tn.esprit.application.models.User;
import tn.esprit.application.services.UserService;

import java.io.IOException;
import java.util.Map;

public class DashboardAdmin {

    @FXML
    private Button add;

    @FXML
    private PieChart pieChart;

    private User connectedUser; // Add a field to hold the connected user

    // Method to initialize the data
    public void initData(User user) {
        this.connectedUser = user;
    }

    public void initialize() {
        UserService userService = new UserService();
        // Call getUserDataByStatus() from UserService
        pieChart.getData().clear();

        // Add data points to the pie chart
        Map<String, Integer> userDataByStatus = userService.getUserDataByStatus();
        for (Map.Entry<String, Integer> entry : userDataByStatus.entrySet()) {
            PieChart.Data slice = new PieChart.Data(entry.getKey(), entry.getValue());
            pieChart.getData().add(slice);
        }
    }

    public void logout() {
        Stage stage = (Stage) pieChart.getScene().getWindow();
        stage.close();
        Stage newStage = new Stage();
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/gui/login.fxml"));
            Scene scene = new Scene(root);
            newStage.setScene(scene);
            newStage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void addUserByAdmin() {
        Stage newStage = new Stage();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/addUserByAdmin.fxml"));
            Parent root = loader.load();
            AddUserByAdminController addController = loader.getController();
            addController.initData(Role.ROLE_ADMIN); // Assuming you want to add an admin
            newStage.setScene(new Scene(root));
            newStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void openUsersList(ActionEvent actionEvent) {
        Stage newStage = new Stage();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/ListUsers.fxml"));
            Parent root = loader.load();
            ListUsers listUsersController = loader.getController();
            listUsersController.initData(); // Remove the argument
            newStage.setScene(new Scene(root));
            newStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
