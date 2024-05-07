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


    exports controllers.Restaurant;
    opens controllers.Restaurant to javafx.fxml;
    exports controllers.Plat;
    opens controllers.Plat to javafx.fxml;
    exports controllers.Reservation;
    opens controllers.Reservation to javafx.fxml;
    opens models.PlatEntity to javafx.base;
    opens models.ReservationEntity to javafx.base;
    opens models.RestaurantEntity to javafx.base;
}