module test {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;
    requires java.sql;
    requires java.desktop;
    requires java.mail;
    requires org.apache.poi.poi;
    requires org.apache.poi.ooxml;
    requires org.apache.pdfbox;

    opens test to javafx.fxml;
    opens models to javafx.base;
    exports test;


    exports controllers.CategoryH;
    opens controllers.CategoryH to javafx.fxml;
    exports controllers.Hebergement;
    opens controllers.Hebergement to javafx.fxml;
    exports controllers.ReservationH;
    opens controllers.ReservationH to javafx.fxml;
}