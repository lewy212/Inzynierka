package com.example.inzynierka;

import com.example.inzynierka.klasy.Drzewo;
import com.example.inzynierka.klasy.Json.GrafWczytajJson;
import com.example.inzynierka.klasy.Json.JsonDrzewo;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class MainApplication extends Application {
    @Override
    public void start(Stage primaryStage) throws IOException {

        // Inicjalizowanie ObjectMapper
        ObjectMapper objectMapper = new ObjectMapper();

        // Wczytanie JSON z pliku do obiektu DecisionTree
        // InputStream inputStream = getClass().getResourceAsStream("/Dane/drzewo_decyzyjne_json.json");
        //  JsonDrzewo rootNode = objectMapper.readValue(inputStream, JsonDrzewo.class);
//            GrafWczytajJson grafWczytajJson = new GrafWczytajJson();
//            grafWczytajJson.loadGraph("/Dane/drzewo_decyzyjne_json.json",new Drzewo());
        // Wyświetlanie wyników
        // printDecisionTree(rootNode, 0);

        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("drzewo-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1600, 900);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    public static void printDecisionTree(JsonDrzewo node, int depth) {
        // Drukowanie pytania
        if (node != null) {
            for (int i = 0; i < depth; i++) {
                System.out.print("  "); // Wcięcia dla wizualnej reprezentacji
            }
            System.out.println(node.getQuestion());

            // Rekurencyjne wywołanie dla gałęzi "yes" i "no"
            if (node.getYes() != null) {
                printDecisionTree(node.getYes(), depth + 1);
            }

            if (node.getNo() != null) {
                printDecisionTree(node.getNo(), depth + 1);
            }
        }
    }
    public static void main(String[] args) {
        launch(args);
    }
}
