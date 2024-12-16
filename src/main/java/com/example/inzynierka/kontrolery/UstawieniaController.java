package com.example.inzynierka.kontrolery;

import com.example.inzynierka.MainApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.prefs.Preferences;

public class UstawieniaController {
    @FXML
    private Label ustawieniaLabel;
    @FXML
    private ChoiceBox<String> shapeWezlow;
    @FXML
    private ChoiceBox<String> shapeLisci;
    @FXML
    private Spinner<Integer> liczbaPx;
    @FXML
    private ChoiceBox<String> kolorWezlow;
    @FXML
    private ChoiceBox<String> kolorLisci;
    @FXML
    private Spinner<Integer> obramowaniePx;
    @FXML
    private ChoiceBox<String> kolorObramowania;
    @FXML
    private ChoiceBox<String> sizeMode;
    @FXML
    private ChoiceBox<String> kolorTekstu;
    @FXML
    private ChoiceBox<String> kolorWyboranegoElementu;
    @FXML
    private ChoiceBox<String> kolorKrawedzi;

    @FXML
    private Spinner<Integer> minimalnaOdleglosc;
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
        shapeWezlow.setValue("circle");
        shapeLisci.setValue("box");
        liczbaPx.getValueFactory().setValue(1);
        obramowaniePx.getValueFactory().setValue(1);
        kolorWezlow.setValue("black");
        kolorLisci.setValue("black");
        kolorObramowania.setValue("black");
        sizeMode.setValue("fit");
        kolorTekstu.setValue("black");
        kolorWyboranegoElementu.setValue("red");
        kolorKrawedzi.setValue("black");
        minimalnaOdleglosc.getValueFactory().setValue(50);
        // Wczytanie zapisanej wartości
        loadSettings();
    }
    private void saveSettings() {
        Preferences prefs = Preferences.userNodeForPackage(UstawieniaController.class);
        prefs.put("shapeWezlow", shapeWezlow.getValue());
        prefs.put("shapeLisci", shapeLisci.getValue());
        prefs.put("liczbaPx",String.valueOf(liczbaPx.getValue()));
        prefs.put("kolorWezlow", kolorWezlow.getValue());
        prefs.put("kolorLisci", kolorLisci.getValue());
        prefs.put("obramowaniePx",String.valueOf(obramowaniePx.getValue()));
        prefs.put("kolorObramowania", kolorObramowania.getValue());
        prefs.put("sizeMode", sizeMode.getValue());
        prefs.put("kolorTekstu", kolorTekstu.getValue());
        prefs.put("kolorWyboranegoElementu", kolorWyboranegoElementu.getValue());
        prefs.putInt("minimalnaOdleglosc", minimalnaOdleglosc.getValue());
    }
    private void loadSettings() {
        Preferences prefs = Preferences.userNodeForPackage(UstawieniaController.class);
        String savedShape = prefs.get("shapeWezlow", null);
        if (savedShape != null) {
            shapeWezlow.setValue(savedShape);
        }

        String liscShapePrefs = prefs.get("shapeLisci", null);
        if (liscShapePrefs != null) {
            shapeLisci.setValue(liscShapePrefs);
        }

        String liczbaPxPrefsString = prefs.get("liczbaPx", null);
        if (liczbaPxPrefsString != null) {
            int liczbaPxPrefs = Integer.parseInt(liczbaPxPrefsString);
            liczbaPx.getValueFactory().setValue(liczbaPxPrefs);
        }

        String kolorWezlowPrefs = prefs.get("kolorWezlow", null);
        if (kolorWezlowPrefs != null) {
            kolorWezlow.setValue(kolorWezlowPrefs);
        }
        String kolorLisciPrefs = prefs.get("kolorLisci", null);
        if (kolorLisciPrefs != null) {
            kolorLisci.setValue(kolorLisciPrefs);
        }

        String obamowaniePxPrefsString = prefs.get("obramowaniePx", null);
        if (obamowaniePxPrefsString != null) {
            int obamowaniePxPrefs = Integer.parseInt(obamowaniePxPrefsString);
            obramowaniePx.getValueFactory().setValue(obamowaniePxPrefs);
        }

        String kolorObramowaniaPrefs = prefs.get("kolorObramowania", null);
        if (kolorObramowaniaPrefs != null) {
            kolorObramowania.setValue(kolorObramowaniaPrefs);
        }
        String sizeModePrefs = prefs.get("sizeMode", null);
        if (sizeModePrefs != null) {
            sizeMode.setValue(sizeModePrefs);
        }
        String kolorTekstuPrefs = prefs.get("kolorTekstu", null);
        if (kolorTekstuPrefs != null) {
            kolorTekstu.setValue(kolorTekstuPrefs);
        }
        String kolorWyboranegoElementuPrefs = prefs.get("kolorWyboranegoElementu", null);
        if (kolorWyboranegoElementuPrefs != null) {
            kolorWyboranegoElementu.setValue(kolorWyboranegoElementuPrefs);
        }

        minimalnaOdleglosc.getValueFactory().setValue(prefs.getInt("minimalnaOdleglosc", 50));
    }
}
