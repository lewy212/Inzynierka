package com.example.inzynierka;

import com.example.inzynierka.klasy.Drzewo;
import com.example.inzynierka.klasy.Json.GrafWczytajJson;
import com.example.inzynierka.klasy.Json.JsonDrzewo;
import com.example.inzynierka.klasy.Xml.GrafWczytajXml;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

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
        primaryStage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }
}
