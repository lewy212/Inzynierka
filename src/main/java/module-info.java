module com.example.inzynierka {
    requires javafx.controls;
    requires javafx.fxml;
    requires gs.core;
    requires gs.ui.javafx;
    requires java.desktop;
    requires javafx.swing;
    requires com.fasterxml.jackson.databind;
    requires java.xml.bind;
    requires java.prefs;

    // Otwarcie pakietów dla JavaFX
    opens com.example.inzynierka to javafx.fxml; // Umożliwia dostęp dla JavaFX do kontrolerów
    opens com.example.inzynierka.kontrolery to javafx.fxml; // Umożliwia dostęp dla JavaFX do kontrolerów
    opens com.example.inzynierka.klasy.Xml to java.xml.bind;
    // Dodaj otwarcie dla klasy Wierzcholek, aby umożliwić dostęp do jego właściwości
    opens com.example.inzynierka.klasy to javafx.base; // Umożliwia dostęp do klas z javafx.base
    opens com.example.inzynierka.klasy.Json to com.fasterxml.jackson.databind;
    exports com.example.inzynierka; // Eksportuj główny pakiet
    exports com.example.inzynierka.kontrolery;
    opens com.example.inzynierka.klasy.ElementyDrzewa to javafx.base;
    opens com.example.inzynierka.klasy.Txt to javafx.base; // Eksportuj pakiet kontrolerów
}
