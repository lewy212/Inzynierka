package com.example.inzynierka;

import javafx.embed.swing.SwingFXUtils;
import com.example.inzynierka.klasy.Drzewo;
import com.example.inzynierka.klasy.GrafWczytajTxt;
import com.example.inzynierka.klasy.Krawedz;
import com.example.inzynierka.klasy.Wierzcholek;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.WritableImage;
import javafx.stage.Stage;
import org.graphstream.graph.*;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.fx_viewer.FxViewer;
import org.graphstream.ui.view.Viewer;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class MainApplication extends Application {
    @Override
    public void start(Stage primaryStage) {
        try {
            // Load the FXML file
            FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("hello-view.fxml"));
            Parent root = loader.load();

            // Create the scene
            Scene scene = new Scene(root);

            // Set the scene on the stage
            primaryStage.setScene(scene);
            primaryStage.setTitle("Graph Visualization with FXML");
            primaryStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
