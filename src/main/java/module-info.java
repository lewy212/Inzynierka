module com.example.inzynierka {
    requires javafx.controls;
    requires javafx.fxml;
    requires gs.core;
    requires gs.ui.javafx;
    requires java.desktop;
    requires javafx.swing;


    opens com.example.inzynierka to javafx.fxml;
    exports com.example.inzynierka;
    exports com.example.inzynierka.kontrolery;
    opens com.example.inzynierka.kontrolery to javafx.fxml;
}