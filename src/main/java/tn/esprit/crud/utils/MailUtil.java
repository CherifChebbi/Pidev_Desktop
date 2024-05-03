package tn.esprit.crud.utils;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class MailUtil {
    private static final String HOST = "smtp-relay.brevo.com";
    private static final String USERNAME = "karat6657@gmail.com";
    private static final String PASSWORD = "Jd0z7k2DxNbn6QZ8";

    public static boolean sendPasswordResetMail(String toEmail, String verificationCode) {
        // Set mail properties
        Properties props = new Properties();
        props.put("mail.smtp.host", HOST);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "587"); // You may need to change the port

        // Create session with authentication
        Session session = Session.getInstance(props,
                new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(USERNAME, PASSWORD);
                    }
                });

        try {
            // Create a default MimeMessage object
            Message message = new MimeMessage(session);

            // Set From: header field of the header.
            message.setFrom(new InternetAddress("noreplay@coolConnect.com"));

            // Set To: header field of the header.
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(toEmail));

            // Set Subject: header field
            message.setSubject("Password Reset Request");

            // Now set the actual message
            String emailBody = "Dear User,\n\n"
                    + "Please use the following verification code to reset your password: "
                    + verificationCode + "\n\n"
                    + "Thank you,\nYour Application Team";
            message.setText(emailBody);

            // Send message
            Transport.send(message);

            System.out.println("Password reset email sent successfully to " + toEmail);
            return true;

        } catch (MessagingException e) {
            System.err.println("Error sending password reset email: " + e.getMessage());
            return false;
        }
    }
    public static boolean sendPassword(String toEmail, String verificationCode) {
        // Set mail properties
        Properties props = new Properties();
        props.put("mail.smtp.host", HOST);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "587"); // You may need to change the port

        // Create session with authentication
        Session session = Session.getInstance(props,
                new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(USERNAME, PASSWORD);
                    }
                });

        try {
            // Create a default MimeMessage object
            Message message = new MimeMessage(session);

            // Set From: header field of the header.
            message.setFrom(new InternetAddress("noreplay@coolConnect.com"));

            // Set To: header field of the header.
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(toEmail));

            // Set Subject: header field
            message.setSubject("Your Password ");

            // Now set the actual message
            String emailBody = "Dear User,\n\n"
                    + "Please use the following password:  "
                    + verificationCode + "\n\n"
                    + "Thank you,\nYour Application Team";
            message.setText(emailBody);

            // Send message
            Transport.send(message);

            System.out.println("Password  email sent successfully to " + toEmail);
            return true;

        } catch (MessagingException e) {
            System.err.println("Error sending password  email: " + e.getMessage());
            return false;
        }
    }
}