package com.example.inzynierka.kontrolery;

import com.example.inzynierka.MainApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.prefs.Preferences;

public class UstawieniaController {
    @FXML
    private Label ustawieniaLabel;
    @FXML
    private ChoiceBox<String> shapeWezlow;
    @FXML
    protected void Powrot() throws IOException {
        FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("menu-view.fxml"));
        Parent root = loader.load();
        Scene currentScene = ustawieniaLabel.getScene();
        Stage stage = (Stage) currentScene.getWindow();
        // root.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
        saveSettings();
        //WynikiModel.getInstance().getWyniki().add(new Wynik("GraczXD", savedDifficulty, 100));
        stage.setScene(new Scene(root, currentScene.getWidth(), currentScene.getHeight()));
    }
    public void initialize() {
        // Ustawienie domyślnej wartości
        shapeWezlow.setValue("box");
        // Wczytanie zapisanej wartości
        loadSettings();
    }
    private void saveSettings() {
        Preferences prefs = Preferences.userNodeForPackage(UstawieniaController.class);
        prefs.put("shapeWezlow", shapeWezlow.getValue());
    }
    private void loadSettings() {
        Preferences prefs = Preferences.userNodeForPackage(UstawieniaController.class);
        String savedShape = prefs.get("shapeWezlow", null);
        if (savedShape != null) {
            shapeWezlow.setValue(savedShape);
        }
    }
}
