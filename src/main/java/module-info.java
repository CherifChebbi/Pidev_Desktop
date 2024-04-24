module tn.esprit.application.pidev {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;
    requires java.sql;
    requires jbcrypt;
    requires mail;

    opens tn.esprit.application to javafx.fxml;
    exports tn.esprit.application;
    exports tn.esprit.application.controllers;
    exports tn.esprit.application.services;
    opens tn.esprit.application.controllers to javafx.fxml;
}