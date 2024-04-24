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
import java.net.MalformedURLException;
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
    private TextField phone;
    @FXML
    private TextField adresse;
    @FXML
    private TextField sexe;
    @FXML
    private TextField taille;
    @FXML
    private TextField poids;
    @FXML
    private TextArea maladies;
    @FXML
    private Button editButton;
    @FXML
    private Button updateButton;
    @FXML
    private ImageView profileImageView;
    @FXML
    private Button logoutButton;

    @FXML
    private Button uploadButton;

    public void initData(User usere) {
        if(usere.getRole().equals(Role.ADMIN)){
            logoutButton.setVisible(false);
        }
      //  textFiedTest.setText("testtt");
        UserService us = new UserService();

        User user =us.getUserById(usere.getId());
        this.currentUser = user;
        nom.setText(user.getNom());
        prenom.setText(user.getPrenom());
        email.setText(user.getEmail());
        System.out.println(user.getImage());

        String imageUrl = "/static/images/" + user.getImage(); // Assuming user.getImage() returns the image filename
        Image image = new Image(getClass().getResource(imageUrl).toExternalForm());
        profileImageView.setImage(image);
        System.out.println(user);
        populateProfileInformation(user);

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
            us.updateImage(currentUser,selectedFile.getName());
            currentUser.setImage(selectedFile.getName());
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Image updated successfully");

        }
    }
    private void populateProfileInformation(User user) {

    }

    private void handleEditButtonAction() {
        // Implement edit profile action
    }

    public void enabelEditing(){
        nom.setDisable(false);
        prenom.setDisable(false);
        email.setDisable(false);
        phone.setDisable(false);
        adresse.setDisable(false);
        sexe.setDisable(false);
        taille.setDisable(false);
        poids.setDisable(false);
        maladies.setDisable(false);
        updateButton.setVisible(true);
    }
    public void UpdateData(){
        UserService us = new UserService();
        User u = new User();
        u.setImage(currentUser.getImage());
        u.setEmail(email.getText());
        u.setRole(currentUser.getRole());

        u.setNom(nom.getText());
        u.setPrenom(prenom.getText());
        us.updateProfile(u);
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Informations Updated");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {

        }
    }
    private String saveImageAndGetUrl(InputStream inputStream, String filename) throws IOException {
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

        // Return the URL of the saved image
        return  filename; // Adjust the URL format as needed
    }
public void logout(){
    Stage stage = (Stage) updateButton.getScene().getWindow();
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