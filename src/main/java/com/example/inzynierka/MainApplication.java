package com.example.inzynierka;

import com.example.inzynierka.klasy.Drzewo;
import com.example.inzynierka.klasy.GrafWczytajTxt;
import com.example.inzynierka.klasy.Krawedz;
import com.example.inzynierka.klasy.Wierzcholek;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.graphstream.graph.*;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.fx_viewer.FxViewer;
import org.graphstream.ui.view.View;
import org.graphstream.ui.view.Viewer;

public class MainApplication extends Application {
    @Override
    public void start(Stage primaryStage) {
        Drzewo drzewo = new Drzewo();
        GrafWczytajTxt grafWczytajTxt = new GrafWczytajTxt();
        grafWczytajTxt.loadGraph("/Dane/drzewo_decyzyjne_1.txt",drzewo);

//        drzewo.getGraf().getNode("A").setAttribute("xyz", 0, 4,0);
//        drzewo.getGraf().getNode("B").setAttribute("xyz", -0.5, 3,0);
//        drzewo.getGraf().getNode("C").setAttribute("xyz", 0.5, 3,0);



        drzewo.getGraf().setAttribute("ui.stylesheet", "node { fill-color: red; text-alignment:center;  size-mode: fit; shape: box;}");

        System.out.println(drzewo.getGraf().getNodeCount());
        String tekst ="";

        for(int i=0;i<drzewo.getGraf().getNodeCount();i++){

            tekst=tekst+drzewo.getGraf().getNode(i).getId()+" ";
        }
        drzewo.getGraf().getNode(0).setAttribute("xy",0,0);
        System.out.println(tekst);

        Viewer viewer = new FxViewer(drzewo.getGraf(), Viewer.ThreadingModel.GRAPH_IN_GUI_THREAD);
        Scene scene = new Scene((Parent) viewer.addDefaultView(true), 1400, 820);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Graph Visualization");
        primaryStage.show();


    }

    public static void main(String[] args) {
        launch(args);
    }
}