package tn.esprit.crud.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import tn.esprit.crud.services.UserService;
import tn.esprit.crud.test.HelloApplication;

import java.io.IOException;
import java.sql.SQLException;

public class SupprimerUser {

    @FXML
    private TextField supprimerTF; // TextField pour saisir l'ID de l'utilisateur à supprimer

    private UserService userService = new UserService();



    @FXML
    void supprimerUser(ActionEvent event) {
        // Récupérer l'ID de l'utilisateur à partir du TextField
        String idUtilisateurStr = supprimerTF.getText();

        // Vérifier si l'ID est un entier valide
        try {
            int idUtilisateur = Integer.parseInt(idUtilisateurStr);

            // Appeler la méthode de service pour supprimer l'utilisateur par son ID
            userService.supprimer(idUtilisateur);
            System.out.println("L'utilisateur avec l'ID " + idUtilisateur + " a été supprimé avec succès.");
            // Vous pouvez ajouter ici des actions supplémentaires après la suppression de l'utilisateur
        } catch (NumberFormatException e) {
            System.err.println("L'ID de l'utilisateur doit être un entier valide.");
            // Gérer l'erreur si l'utilisateur entre un ID non valide (par exemple, afficher un message d'erreur à l'utilisateur)
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression de l'utilisateur : " + e.getMessage());
            // Gérer l'erreur (afficher un message à l'utilisateur, journaliser l'erreur, etc.)
        }

        // Afficher un message de confirmation à l'utilisateur
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Suppression");
        alert.setContentText("L'utilisateur a été supprimé avec succès.");
        alert.showAndWait();

    }
    @FXML
    void ReturnToAfficher(ActionEvent event) {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/tn/esprit/crud/AfficherUsers.fxml"));
        try {
            supprimerTF.getScene().setRoot(fxmlLoader.load());
        } catch (IOException e) {
            System.err.println(e.getMessage());
            throw new RuntimeException(e);
        }

    }

    @FXML
    void VersAjouter(ActionEvent event) {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/tn/esprit/crud/AjouterUser.fxml"));
            try {
                supprimerTF.getScene().setRoot(fxmlLoader.load());
            } catch (IOException e) {
                System.err.println(e.getMessage());
                throw new RuntimeException(e);
            }
    }

    @FXML
    void VersModifier(ActionEvent event) {

                FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/tn/esprit/crud/ModifierUser.fxml"));
                try {
                    supprimerTF.getScene().setRoot(fxmlLoader.load());
                } catch (IOException e) {
                    System.err.println(e.getMessage());
                    throw new RuntimeException(e);
                }
    }

}
