package tn.esprit.application.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import tn.esprit.application.models.Role;
import tn.esprit.application.models.User;
import tn.esprit.application.services.UserService;

import java.io.*;
import java.util.Optional;

public class Profile {

    private User currentUser;

    @FXML
    private TextField nom;
    @FXML
    private TextField prenom;
    @FXML
    private TextField email;
    @FXML
    private TextField numtel;
    @FXML
    private TextField nationnalite;
    @FXML
    private CheckBox isBanned;
    @FXML
    private CheckBox isVerified;
    @FXML
    private ImageView profileImageView;
    @FXML
    private Button logoutButton;
    @FXML
    private Button uploadButton;

    public void initData(User user) {
        if(user.getRole().equals(Role.ADMIN)){
            logoutButton.setVisible(false);
        }

        UserService userService = new UserService();
        this.currentUser = userService.getUserById(user.getId());

        nom.setText(currentUser.getNom());
        prenom.setText(currentUser.getPrenom());
        email.setText(currentUser.getEmail());
        numtel.setText(String.valueOf(currentUser.getNumtel()));
        nationnalite.setText(currentUser.getNationnalite());
        isBanned.setSelected(currentUser.isBanned());
        isVerified.setSelected(currentUser.isVerified());

        // Assuming you have a method to load the image from a file path or URL
        loadImage(currentUser.getProfilePicture());
    }

    private void loadImage(String imagePath) {
        if (imagePath != null && !imagePath.isEmpty()) {
            File file = new File(imagePath);
            if (file.exists()) {
                Image image = new Image(file.toURI().toString());
                profileImageView.setImage(image);
            } else {
                // Handle case where image file doesn't exist
            }
        } else {
            // Handle case where image path is empty or null
        }
    }

    @FXML
    private void uploadImage(ActionEvent event) throws IOException {
        UserService us = new UserService();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Image File");
        // Set the initial directory to the user's home directory
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        // Restrict file types to images
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );

        // Show the file chooser dialog
        File selectedFile = fileChooser.showOpenDialog(new Stage());
        if (selectedFile != null) {
            // Display the selected image in the ImageView
            Image image = new Image(selectedFile.toURI().toString());
            profileImageView.setImage(image);
            saveImageAndGetUrl(selectedFile.toPath().toUri().toURL().openStream(), selectedFile.getName());
            us.updateImage(currentUser, selectedFile.getName());
            currentUser.setProfilePicture(selectedFile.getName());
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Image updated successfully");
        }
    }

    private void saveImageAndGetUrl(InputStream inputStream, String filename) throws IOException {
        // Define the directory where images will be stored
        String uploadDirectory = "src/main/resources/static/images"; // Change this to your desired directory path
        File directory = new File(uploadDirectory);
        if (!directory.exists()) {
            directory.mkdirs(); // Create the directory if it doesn't exist
        }

        // Save the image file to the upload directory
        String filePath = uploadDirectory + File.separator + filename;
        try (OutputStream outputStream = new FileOutputStream(new File(filePath))) {
            int read;
            byte[] bytes = new byte[1024];
            while ((read = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
            }
        }
    }

    @FXML
    private void enabelEditing() {
        nom.setDisable(false);
        prenom.setDisable(false);
        email.setDisable(false);
        numtel.setDisable(false);
        nationnalite.setDisable(false);
        isBanned.setDisable(false);
        isVerified.setDisable(false);
    }

    @FXML
    private void updateData() {
        UserService us = new UserService();
        User updatedUser = new User();
        updatedUser.setId(currentUser.getId());
        updatedUser.setNom(nom.getText());
        updatedUser.setPrenom(prenom.getText());
        updatedUser.setEmail(email.getText());
        updatedUser.setNumtel(Integer.parseInt(numtel.getText()));
        updatedUser.setNationnalite(nationnalite.getText());
        updatedUser.setBanned(isBanned.isSelected());
        updatedUser.setVerified(isVerified.isSelected());
        updatedUser.setProfilePicture(currentUser.getProfilePicture());
        us.updateProfile(updatedUser);
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Information Updated");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // Handle OK button click
        }
    }

    @FXML
    private void logout() {
        Stage stage = (Stage) logoutButton.getScene().getWindow();
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
}
