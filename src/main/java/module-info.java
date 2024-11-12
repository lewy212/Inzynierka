module com.example.inzynierka {
    requires javafx.controls;
    requires javafx.fxml;
    requires gs.core;
    requires gs.ui.javafx;
    requires java.desktop;
    requires javafx.swing;
    requires com.fasterxml.jackson.databind;

    // Otwarcie pakietów dla JavaFX
    opens com.example.inzynierka to javafx.fxml; // Umożliwia dostęp dla JavaFX do kontrolerów
    opens com.example.inzynierka.kontrolery to javafx.fxml; // Umożliwia dostęp dla JavaFX do kontrolerów

    // Dodaj otwarcie dla klasy Wierzcholek, aby umożliwić dostęp do jego właściwości
    opens com.example.inzynierka.klasy to javafx.base; // Umożliwia dostęp do klas z javafx.base
    opens com.example.inzynierka.klasy.Json to com.fasterxml.jackson.databind;
    exports com.example.inzynierka; // Eksportuj główny pakiet
    exports com.example.inzynierka.kontrolery; // Eksportuj pakiet kontrolerów
}
