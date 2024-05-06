module test {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;
    requires java.sql;
    requires java.desktop;
    requires kernel;
    requires layout;
    requires java.mail;
    requires javafx.media;
    requires org.controlsfx.controls;

    opens test to javafx.fxml;
    opens models to javafx.base;
    exports test;
    exports controllers;
    opens controllers to javafx.fxml;
}