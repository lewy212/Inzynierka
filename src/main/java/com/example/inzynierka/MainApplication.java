package com.example.inzynierka;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.xml.bind.JAXBException;
import java.io.IOException;

public class MainApplication extends Application {
    @Override
    public void start(Stage primaryStage) throws IOException, JAXBException {

        // Inicjalizowanie ObjectMapper
        ObjectMapper objectMapper = new ObjectMapper();
      //  GrafWczytajXml grafWczytajXml = new GrafWczytajXml();
    //    grafWczytajXml.loadGraph("/Dane/decisionTree.xml",new Drzewo());
        // Wczytanie JSON z pliku do obiektu DecisionTree
        // InputStream inputStream = getClass().getResourceAsStream("/Dane/drzewo_decyzyjne_json.json");
        //  JsonDrzewo rootNode = objectMapper.readValue(inputStream, JsonDrzewo.class);
//            GrafWczytajJson grafWczytajJson = new GrafWczytajJson();
//            grafWczytajJson.loadGraph("/Dane/drzewo_decyzyjne_json.json",new Drzewo());
        // Wyświetlanie wyników
        // printDecisionTree(rootNode, 0);

        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("menu-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1600, 900);
        Image icon = new Image("drzewoIkonka.png");
        primaryStage.getIcons().add(icon);
        primaryStage.setTitle("Wizualizator drzew decyzyjnych");
        primaryStage.setScene(scene);
        primaryStage.setOnCloseRequest(event -> {
            Platform.exit();
            System.exit(0);
        });
        primaryStage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }
}
