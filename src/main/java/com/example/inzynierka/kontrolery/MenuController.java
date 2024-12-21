package com.example.inzynierka.kontrolery;

import com.example.inzynierka.MainApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.prefs.Preferences;

public class MenuController {

    @FXML
    private Label menuLabel;
    @FXML
    private Button menuButton;
    @FXML
    private Button wczytajDrzewoButton;

    @FXML
    protected void goToUstawienia() throws IOException {
        FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("ustawienia-view.fxml"));
        Parent root = loader.load();
        Scene currentScene = menuLabel.getScene();
        Stage stage = (Stage) currentScene.getWindow();
        stage.setScene(new Scene(root, currentScene.getWidth(), currentScene.getHeight()));
    }
    public void initialize()
    {

    }
    @FXML
    protected void goToWizualizacja() throws IOException {
        // Wyświetl okienko dialogowe z przyciskami
        Alert dialog = new Alert(Alert.AlertType.CONFIRMATION);
        dialog.setTitle("Wybór opcji");
        dialog.setHeaderText("Wybierz jedną z opcji");
        dialog.setContentText("Co chcesz zrobić?");

        ButtonType wczytajPlikButton = new ButtonType("Wczytaj własny plik");
        ButtonType przykladowaButton = new ButtonType("Przykładowa wizualizacja");
        ButtonType cancelButton = new ButtonType("Anuluj");

        dialog.getButtonTypes().setAll(wczytajPlikButton, przykladowaButton, cancelButton);

        Optional<ButtonType> result = dialog.showAndWait();

        if (result.isPresent()) {
            if (result.get() == wczytajPlikButton) {
                // Wczytaj plik z komputera
                wybierzPlik();
            } else if (result.get() == przykladowaButton) {
                // Wyświetl opcje wizualizacji
                wybierzPrzykladowaWizualizacja();
            }
        }
    }

    private void wybierzPlik() throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Wybierz plik");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Wszystkie pliki", "*.*"),
                new FileChooser.ExtensionFilter("Pliki tekstowe", "*.txt"),
                new FileChooser.ExtensionFilter("Pliki JSON", "*.json"),
                new FileChooser.ExtensionFilter("Pliki XML", "*.xml")
        );

        Stage stage = (Stage) menuLabel.getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(stage);

        if (selectedFile != null) {
            Preferences prefs = Preferences.userNodeForPackage(MenuController.class);
            String filePath = selectedFile.getAbsolutePath();
            String fileName = selectedFile.getName();
            System.out.println("Nazwa pliku: " + fileName);

            // Wyodrębnij format (rozszerzenie pliku)
            String format = "";
            int dotIndex = fileName.lastIndexOf('.');
            if (dotIndex > 0 && dotIndex < fileName.length() - 1)
            {
                format = fileName.substring(dotIndex + 1).toLowerCase();
            }
            prefs.put("format",format);
            prefs.put("sciezka",filePath);
            zmienOknoNaWizualizacje();
            // Możesz dodać logikę wczytywania pliku tutaj
        }
    }

    private void wybierzPrzykladowaWizualizacja() {
        List<String> opcje = new ArrayList<>();
        opcje.add("TXT");
        opcje.add("JSON");
        opcje.add("XML");

        ChoiceDialog<String> choiceDialog = new ChoiceDialog<>("TXT", opcje);
        choiceDialog.setTitle("Przykładowa wizualizacja");
        choiceDialog.setHeaderText("Wybierz format wizualizacji");
        choiceDialog.setContentText("Dostępne formaty:");

        Optional<String> result = choiceDialog.showAndWait();

        result.ifPresent(format -> {
            Preferences prefs = Preferences.userNodeForPackage(MenuController.class);
            if (format.equals("TXT")) {
                prefs.put("format", "txt");
                prefs.put("sciezka","");
            } else if (format.equals("JSON")) {
                prefs.put("format", "json");
                prefs.put("sciezka","");
            } else if (format.equals("XML")) {
                prefs.put("format", "xml");
                prefs.put("sciezka","");
            }
            try {
                zmienOknoNaWizualizacje();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
    private void zmienOknoNaWizualizacje() throws IOException {
        FXMLLoader loader =new FXMLLoader(MainApplication.class.getResource("drzewo-view.fxml"));
        Parent root = loader.load();
        Scene currentScene = menuLabel.getScene();
        Stage stage = (Stage) currentScene.getWindow();
        // root.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());

        //WynikiModel.getInstance().getWyniki().add(new Wynik("GraczXD", savedDifficulty, 100));


        stage.setScene(new Scene(root, currentScene.getWidth(), currentScene.getHeight()));
    }


}
