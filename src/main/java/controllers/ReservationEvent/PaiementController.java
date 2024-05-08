package controllers.ReservationEvent;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import models.Event;
import models.ReservationEvent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import services.ServiceReservationEvent;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

import java.time.LocalDate;

public class PaiementController {

    private ServiceReservationEvent serviceReservationEvent;

    public void setServiceReservationEvent(ServiceReservationEvent serviceReservationEvent) {
        this.serviceReservationEvent = serviceReservationEvent;
    }


    private static final String ACCOUNT_SID = "AC7d0461f0c5c67f8ec8df466b5085708b";
    private static final String AUTH_TOKEN = "da49924fe6c1f9be3da2b866de3be14a";
    private static final String TWILIO_NUMBER = "+16504375251";

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
    private String nom;
    private String email;
    private String telephone;
    private LocalDate dateReservation;

    public void initData(Event event, String nom, String email, String telephone, LocalDate dateReservation) {
        this.event = event;
        this.nom = nom;
        this.email = email;
        this.telephone = telephone;
        this.dateReservation = dateReservation;
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
                enregistrerReservation(); // Enregistrer la réservation uniquement si le paiement est réussi
                afficherMessagePaiementSuccess();
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
            java.util.Date dateReservation = java.sql.Date.valueOf(this.dateReservation);

            // Enregistrer le numéro de téléphone sans l'indicatif du pays dans la base de données
            String numeroSansIndicatifPays = telephone;

            // Ajouter l'indicatif du pays au numéro de téléphone pour l'envoyer avec Twilio
            String numeroAvecIndicatifPays = "+216" + telephone;

            ReservationEvent reservationEvent = new ReservationEvent(eventId, nom, email, numeroSansIndicatifPays, dateReservation);

            serviceReservationEvent.ajouter(reservationEvent);

            // Envoyer un SMS avec Twilio en utilisant le numéro avec l'indicatif du pays
            envoyerSMS("Votre réservation pour l'événement a été confirmée.", numeroAvecIndicatifPays);

            afficherMessagePaiementSuccess();
        } catch (Exception e) {
            e.printStackTrace();
            afficherMessagePaiementFailure();
        }
    }




    private void envoyerSMS(String message, String numeroDestinataire) {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        Message.creator(
                        new PhoneNumber(numeroDestinataire),
                        new PhoneNumber(TWILIO_NUMBER),
                        message)
                .create();
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
