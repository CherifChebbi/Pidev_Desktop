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

    exports test;

    exports controllers.Calendar;
    exports controllers.Restaurant;
    opens controllers.Restaurant to javafx.fxml;
    opens controllers.Calendar to javafx.fxml;

    exports controllers.Plat;
    opens controllers.Plat to javafx.fxml;
    exports controllers.Reservation;
    opens controllers.Reservation to javafx.fxml;


}