package com.example.inzynierka;

import javafx.embed.swing.SwingFXUtils;
import com.example.inzynierka.klasy.Drzewo;
import com.example.inzynierka.klasy.GrafWczytajTxt;
import com.example.inzynierka.klasy.Krawedz;
import com.example.inzynierka.klasy.Wierzcholek;
import javafx.application.Application;
import javafx.application.Platform;
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
        Drzewo drzewo = new Drzewo();
        GrafWczytajTxt grafWczytajTxt = new GrafWczytajTxt();
        grafWczytajTxt.loadGraph("/Dane/drzewo_decyzyjne_1.txt", drzewo);

        drzewo.getGraf().setAttribute("ui.stylesheet", "node { fill-color: red; size:25px; text-size: 12px; text-alignment:center; }" +
                "edge { text-alignment:under; text-background-mode: plain; text-size: 12px; }");

        System.out.println(drzewo.getGraf().getNodeCount());
        String tekst = "";

        for (int i = 0; i < drzewo.getGraf().getNodeCount(); i++) {
            tekst = tekst + drzewo.getGraf().getNode(i).getId() + " ";
        }
        drzewo.getGraf().getNode(0).setAttribute("xy", 0, 0);
        System.out.println(tekst);

        Viewer viewer = new FxViewer(drzewo.getGraf(), Viewer.ThreadingModel.GRAPH_IN_GUI_THREAD);
        Scene scene = new Scene((Parent) viewer.addDefaultView(true), 1400, 820);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Graph Visualization");
        primaryStage.show();

        System.out.println("Stage shown");

    }

    public static void main(String[] args) {
        launch(args);
    }
}
