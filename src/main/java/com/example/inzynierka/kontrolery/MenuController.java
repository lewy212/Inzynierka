package com.example.inzynierka.kontrolery;

import com.example.inzynierka.MainApplication;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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

        dialog.getDialogPane().getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
        dialog.getDialogPane().getStyleClass().add("custom-dialog");
        ImageView ikona = new ImageView(getClass().getResource("/drzewoIkonka.png").toExternalForm());
        Stage dialogStage = (Stage) dialog.getDialogPane().getScene().getWindow();
        dialogStage.getIcons().add(new Image(getClass().getResourceAsStream("/drzewoIkonka.png")));
        ikona.setFitWidth(50); // Rozmiar szerokości
        ikona.setFitHeight(50); // Rozmiar wysokości
        dialog.setGraphic(ikona);
        ButtonType wczytajPlikButton = new ButtonType("Wczytaj własny plik");
        ButtonType przykladowaButton = new ButtonType("Przykładowa wizualizacja");
        ButtonType generujButton = new ButtonType("Generuj drzewo");
        ButtonType cancelButton = new ButtonType("Cancel",ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getButtonTypes().setAll(wczytajPlikButton, przykladowaButton, generujButton, cancelButton);
        Button cancelButtonNode = (Button) dialog.getDialogPane().lookupButton(cancelButton);
        cancelButtonNode.getStyleClass().add("cancel-button");
        Optional<ButtonType> result = dialog.showAndWait();

        if (result.isPresent()) {
            if (result.get() == wczytajPlikButton) {
                wybierzPlik();
            }
            else if(result.get() == generujButton)
            {
                podajGlebokoscDrzewa();
            }
            else if (result.get() == przykladowaButton) {
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
            String fileName = selectedFile.getName();
            String format = "";
            int dotIndex = fileName.lastIndexOf('.');
            if (dotIndex > 0 && dotIndex < fileName.length() - 1) {
                format = fileName.substring(dotIndex + 1).toLowerCase();
            }

            if (format.equals("txt") || format.equals("json") || format.equals("xml")) {
                Preferences prefs = Preferences.userNodeForPackage(MenuController.class);
                String filePath = selectedFile.getAbsolutePath();
                prefs.put("format", format);
                prefs.put("sciezka", filePath);
                zmienOknoNaWizualizacje();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.getDialogPane().getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
                alert.getDialogPane().getStyleClass().add("custom-dialog");
                Stage dialogStage = (Stage) alert.getDialogPane().getScene().getWindow();
                dialogStage.getIcons().add(new Image(getClass().getResourceAsStream("/drzewoIkonka.png")));
                alert.setTitle("Błąd formatu pliku");
                alert.setHeaderText("Nieprawidłowy format pliku");
                alert.setContentText("Dozwolone formaty: .txt, .json, .xml.\nWybrano plik: " + fileName);
                alert.showAndWait();
            }
        }
    }

    private void podajGlebokoscDrzewa() {
        TextInputDialog dialog = new TextInputDialog("2");
        dialog.setTitle("Podaj głębokość drzewa");
        dialog.setHeaderText("Wprowadź głębokość drzewa");
        dialog.setContentText("Głębokość (od 2 do 5):");
        dialog.getDialogPane().getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
        dialog.getDialogPane().getStyleClass().add("custom-dialog");
        ImageView ikona = new ImageView(getClass().getResource("/drzewoIkonka.png").toExternalForm());
        Stage dialogStage = (Stage) dialog.getDialogPane().getScene().getWindow();
        dialogStage.getIcons().add(new Image(getClass().getResourceAsStream("/drzewoIkonka.png")));
        ikona.setFitWidth(50); // Rozmiar szerokości
        ikona.setFitHeight(50); // Rozmiar wysokości
        dialog.setGraphic(ikona);
        Button cancelButton = (Button) dialog.getDialogPane().lookupButton(ButtonType.CANCEL);
        cancelButton.getStyleClass().add("cancel-button");
        Optional<String> result = dialog.showAndWait();

        result.ifPresent(input -> {
            try {
                int glebokosc = Integer.parseInt(input);
                if (glebokosc >= 2 && glebokosc <= 5) {
                    Preferences prefs = Preferences.userNodeForPackage(MenuController.class);
                    prefs.putInt("glebokoscGenerowana", glebokosc);
                    prefs.put("format", "json");
                    prefs.put("sciezka","");
                    try {
                        zmienOknoNaWizualizacje();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    showError("Głębokość musi być liczbą od 2 do 5.");
                    podajGlebokoscDrzewa(); // Ponowne wywołanie, jeśli błąd
                }
            } catch (NumberFormatException e) {
                showError("Podano nieprawidłową wartość. Spróbuj ponownie.");
                podajGlebokoscDrzewa(); // Ponowne wywołanie, jeśli błąd
            }
        });
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
        choiceDialog.getDialogPane().getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
        choiceDialog.getDialogPane().getStyleClass().add("custom-dialog");
        ImageView ikona = new ImageView(getClass().getResource("/drzewoIkonka.png").toExternalForm());
        Stage dialogStage = (Stage) choiceDialog.getDialogPane().getScene().getWindow();
        dialogStage.getIcons().add(new Image(getClass().getResourceAsStream("/drzewoIkonka.png")));
        ikona.setFitWidth(50); // Rozmiar szerokości
        ikona.setFitHeight(50); // Rozmiar wysokości
        choiceDialog.setGraphic(ikona);
        Button cancelButton = (Button) choiceDialog.getDialogPane().lookupButton(ButtonType.CANCEL);
        cancelButton.getStyleClass().add("cancel-button");
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

        stage.setScene(new Scene(root, currentScene.getWidth(), currentScene.getHeight()));
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Błąd");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    @FXML
    protected void zamknijProgram ()
    {
        Platform.exit();
        System.exit(0);
    }
}
