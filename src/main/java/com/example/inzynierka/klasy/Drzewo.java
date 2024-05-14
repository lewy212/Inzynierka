package com.example.inzynierka.klasy;

import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;

public class Drzewo {
    private Graph graf;

    public Drzewo() {
        this.graf = new SingleGraph("Drzewo Decyzyjne");
    }

    public Graph getGraf() {
        return graf;
    }

    public void dodajWierzcholek(Wierzcholek wierzcholek) {
        graf.addNode(wierzcholek.getId()).setAttribute("ui.label", wierzcholek.getLabel());
        //graf.getNode(nodeId).setAttribute("data", data); // Przechowuje dane wierzchołka
    }

    public void usunWierzcholek(Wierzcholek wierzcholek) {
        graf.removeNode(wierzcholek.getId());
    }

    // Metoda do dodawania krawędzi do drzewa
    public void dodajKrawedz(Krawedz krawedz) {
        graf.addEdge(krawedz.getId(),krawedz.getPierwszyPunktId(), krawedz.getDrugiPunktId()).setAttribute("ui.label", krawedz.getLabel());
    }

    // Metoda do usuwania krawędzi z drzewa
    public void usunKrawedz(Krawedz krawedz) {
        graf.removeEdge(krawedz.getId());
    }
}
