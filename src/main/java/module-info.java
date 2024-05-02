module test {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;
    requires java.sql;
    requires java.desktop;
    requires kernel;
    requires layout;
    requires java.mail;

    opens test to javafx.fxml;
    opens Entity to javafx.base;
    exports test;
    exports Controller;
    opens Controller to javafx.fxml;
}