package controllers;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.model.Token;
import com.stripe.param.PaymentIntentCreateParams;
import entities.Event;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class PaiementController {

    @FXML
    private TextField carteCVC;

    @FXML
    private TextField emailPaiement;

    @FXML
    private DatePicker expirationDate;

    @FXML
    private TextField nomTitulaire;

    @FXML
    private TextField numCarte;

    @FXML
    private Label prixEventLabel;

    private static final String STRIPE_SECRET_KEY = "sk_test_51PBEmP01yLhDc9PJS8ZQspuhCr1it38n8rnCx7Yq8Tzux4D0ib9ntWAe15XD7TBdIvt0z8s90dIvuLWc73Jt0g3800LIFQ6v51"; // Remplacez par votre clé secrète Stripe

    private Event event;

    public void initData(Event event) {
        this.event = event;
        prixEventLabel.setText(String.valueOf(event.getPrix()));
    }

    @FXML
    void confirmerPaiement(ActionEvent actionEvent) {
        String nomTitulaireCarte = nomTitulaire.getText();
        String email = emailPaiement.getText();

        Stripe.apiKey = STRIPE_SECRET_KEY;

        try {
            // Utiliser le token fourni par Stripe pour créer une intention de paiement
            PaymentIntentCreateParams.Builder builder = new PaymentIntentCreateParams.Builder()
                    .setCurrency("eur")
                    .setAmount((long) (event.getPrix() * 100))
                    .setPaymentMethod("tok_visa") // Utiliser le token Stripe fourni
                    .setConfirmationMethod(PaymentIntentCreateParams.ConfirmationMethod.MANUAL)
                    .setConfirm(true);
            PaymentIntentCreateParams params = builder.build();

            PaymentIntent paymentIntent = PaymentIntent.create(params);

            if (paymentIntent.getStatus().equals("succeeded")) {
                enregistrerReservation();
                afficherMessagePaiementSuccess();
            } else {
                afficherMessagePaiementFailure();
            }
        } catch (StripeException e) {
            e.printStackTrace();
            afficherMessagePaiementFailure();
        }
    }


    private void enregistrerReservation() {
        // Implémentez la logique pour enregistrer la réservation dans la base de données
    }

    private void afficherMessagePaiementSuccess() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Paiement réussi");
        alert.setHeaderText("Le paiement a été effectué avec succès.");
        alert.setContentText("Votre réservation a été enregistrée.");
        alert.showAndWait();
    }

    private void afficherMessagePaiementFailure() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Échec du paiement");
        alert.setHeaderText("Le paiement a échoué.");
        alert.setContentText("Veuillez réessayer ou contacter le support.");
        alert.showAndWait();
    }
}
