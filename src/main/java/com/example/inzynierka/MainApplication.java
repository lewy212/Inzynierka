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
//
//        // Dodawanie wierzchołków
//        Wierzcholek wierzcholekA = new Wierzcholek("A", "Wierzchołek A");
//        Wierzcholek wierzcholekB = new Wierzcholek("B", "Wierzchołek B");
//        Wierzcholek wierzcholekC = new Wierzcholek("C", "Wierzchołek C");
//        drzewo.dodajWierzcholek(wierzcholekA);
//        drzewo.dodajWierzcholek(wierzcholekB);
//        drzewo.dodajWierzcholek(wierzcholekC);
//
//        // Dodawanie krawędzi
//        Krawedz krawedzAB = new Krawedz("AB", "A", "B", "Zimno");
//        Krawedz krawedzAC = new Krawedz("AC", "A", "C", "Ciepło");
//        drzewo.dodajKrawedz(krawedzAB);
//        drzewo.dodajKrawedz(krawedzAC);
//        drzewo.getGraf().getNode("A").setAttribute("xyz", 0, 4,0);
//        drzewo.getGraf().getNode("B").setAttribute("xyz", -0.5, 3,0);
//        drzewo.getGraf().getNode("C").setAttribute("xyz", 0.5, 3,0);
        // Tworzenie sceny JavaFX


        drzewo.getGraf().setAttribute("ui.stylesheet", "node { fill-color: red; text-alignment:center;  size-mode: fit; shape: box;}");

        System.out.println(drzewo.getGraf().getNodeCount());
        String tekst ="";
        for(int i=0;i<drzewo.getGraf().getNodeCount();i++){
            drzewo.getGraf().getNode(i).setAttribute("xyz",1*i,1*i,0);

            tekst=tekst+drzewo.getGraf().getNode(i).getId()+" ";
        }
        System.out.println(tekst);
        //drzewo.dodajKrawedz(new Krawedz("x","F1","F5",""));
        // Konfiguracja i wyświetlenie sceny
        Viewer viewer = new FxViewer(drzewo.getGraf(), Viewer.ThreadingModel.GRAPH_IN_GUI_THREAD);
        Scene scene = new Scene((Parent) viewer.addDefaultView(true), 1400, 820);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Graph Visualization");
        primaryStage.show();

        // Tworzenie grafu
//        Graph graph = new SingleGraph("SimpleGraph");
//        graph.setStrict(false);
//        graph.setAutoCreate(true);
//        // Dodawanie wierzchołków
//        Node nodeA = graph.addNode("A");
//        Node nodeB = graph.addNode("B");
//        Node nodeC = graph.addNode("C");
//       // nodeA.setAttribute("xyz", 0, 100, 0);
//        nodeA.setAttribute("layout.xy", 0, 100);
//        // Ustawianie etykiet
//        nodeA.setAttribute("ui.label", "Wierzchołek A");
//        nodeB.setAttribute("ui.label", "Wierzchołek B");
//        nodeC.setAttribute("ui.label", "Wierzchołek C");
//        Edge edgeAB = graph.addEdge("AB", "A", "B");
//        Edge edgeAC = graph.addEdge("AC", "A", "C");
//        edgeAB.setAttribute("ui.label", "Zimno");
//        edgeAC.setAttribute("ui.label", "Ciepło");
//        // Dodawanie krawędzi
//       // graph.addEdge("AB", "A", "B");
//        nodeA.setAttribute("ui.disableInteraction", true);
//        nodeB.setAttribute("ui.disableInteraction", true);
//        nodeC.setAttribute("ui.disableInteraction", true);
//        graph.setAttribute("ui.disableInteraction", true);
//        // Konfiguracja widoku
//        Viewer viewer = new FxViewer(graph, Viewer.ThreadingModel.GRAPH_IN_GUI_THREAD);
//        //viewer.enableAutoLayout();
//        nodeA.setAttribute("xyz", 0, 4,0);
//        nodeB.setAttribute("xyz", -0.5, 3,0);
//        nodeC.setAttribute("xyz", 0.5, 3,0);

    }

    public static void main(String[] args) {
        launch(args);
    }
}