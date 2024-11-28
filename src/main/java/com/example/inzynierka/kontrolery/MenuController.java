package com.example.inzynierka.kontrolery;

import com.example.inzynierka.MainApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class MenuController {
    @FXML
    private Label menuLabel;
    @FXML
    private Button menuButton;
    @FXML
    protected void goToUstawienia() throws IOException {

        //String savedDifficulty = readDifficultySetting();
        FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("ustawienia-view.fxml"));
        Parent root = loader.load();
        Scene currentScene = menuLabel.getScene();
        Stage stage = (Stage) currentScene.getWindow();
       // root.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());

        //WynikiModel.getInstance().getWyniki().add(new Wynik("GraczXD", savedDifficulty, 100));


        stage.setScene(new Scene(root, currentScene.getWidth(), currentScene.getHeight()));
    }

    @FXML
    protected void goToWizualizacja()throws IOException{

        FXMLLoader loader =new FXMLLoader(MainApplication.class.getResource("drzewo-view.fxml"));
        Parent root = loader.load();
        Scene currentScene = menuLabel.getScene();
        Stage stage = (Stage) currentScene.getWindow();
       // root.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());

        //WynikiModel.getInstance().getWyniki().add(new Wynik("GraczXD", savedDifficulty, 100));


        stage.setScene(new Scene(root, currentScene.getWidth(), currentScene.getHeight()));
    }
}
