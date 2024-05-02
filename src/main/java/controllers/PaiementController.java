package controllers;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import entities.Event;
import entities.ReservationEvent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import services.ServiceReservationEvent;

import java.sql.SQLException;
import java.time.LocalDate;

public class PaiementController {

    // Créez une instance de ServiceReservationEvent
    private ServiceReservationEvent serviceReservationEvent;

    // Méthode pour définir le service de réservation
    public void setServiceReservationEvent(ServiceReservationEvent serviceReservationEvent) {
        this.serviceReservationEvent = serviceReservationEvent;
    }

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

    private static final String STRIPE_SECRET_KEY = "sk_test_51PBEmP01yLhDc9PJS8ZQspuhCr1it38n8rnCx7Yq8Tzux4D0ib9ntWAe15XD7TBdIvt0z8s90dIvuLWc73Jt0g3800LIFQ6v51";

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
            PaymentIntentCreateParams.Builder builder = new PaymentIntentCreateParams.Builder()
                    .setCurrency("eur")
                    .setAmount((long) (event.getPrix() * 100))
                    .setPaymentMethod("pm_card_visa")
                    .setConfirmationMethod(PaymentIntentCreateParams.ConfirmationMethod.MANUAL)
                    .setReturnUrl("https://example.com/checkout/success")
                    .setConfirm(true);
            PaymentIntentCreateParams params = builder.build();

            PaymentIntent paymentIntent = PaymentIntent.create(params);

            if (paymentIntent.getStatus().equals("succeeded")) {
                enregistrerReservation();
                afficherMessagePaiementSuccess();
                afficherMessageConfirmationEmail();
            } else {
                afficherMessagePaiementFailure();
            }
        } catch (StripeException e) {
            e.printStackTrace();
            afficherMessagePaiementFailure();
        } catch (Exception e) {
            e.printStackTrace();
            afficherMessagePaiementFailure();
        }
    }

    private void enregistrerReservation() {
        try {
            int eventId = event.getId();
            String nom = nomTitulaire.getText();
            String email = emailPaiement.getText();
            String telephone = "";
            java.util.Date dateReservation = java.sql.Date.valueOf(LocalDate.now());

            // Créer une instance de ReservationEvent avec les données récupérées
            ReservationEvent reservationEvent = new ReservationEvent(eventId, nom, email, telephone, dateReservation);

            // Appeler la méthode ajouter de votre service pour enregistrer la réservation dans la base de données
            serviceReservationEvent.ajouter(reservationEvent);

            // Afficher un message de succès
            afficherMessagePaiementSuccess();
            afficherMessageConfirmationEmail();
        } catch (Exception e) {
            e.printStackTrace();
            afficherMessagePaiementFailure();
        }
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

    private void afficherMessageConfirmationEmail() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Confirmation envoyée");
        alert.setHeaderText("Un e-mail de confirmation vous sera envoyé sous peu.");
        alert.setContentText("Veuillez vérifier votre boîte de réception.");
        alert.showAndWait();
    }
}
