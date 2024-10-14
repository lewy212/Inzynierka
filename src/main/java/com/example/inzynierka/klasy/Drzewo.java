package com.example.inzynierka.klasy;

import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Drzewo {
    private Graph graf;
    private List<Wierzcholek> listaWierzcholkow;
    public Drzewo() {
        this.graf = new SingleGraph("Drzewo Decyzyjne");
        this.listaWierzcholkow = new ArrayList<>();
    }

    public Graph getGraf() {
        return graf;
    }

    public void dodajWierzcholek(Wierzcholek wierzcholek) {
        graf.addNode(wierzcholek.getId()).setAttribute("ui.label", wierzcholek.getLabel());
        listaWierzcholkow.add(wierzcholek);
    }

    public void usunWierzcholek(Wierzcholek wierzcholek) {
        graf.removeNode(wierzcholek.getId());
    }

    // Metoda do dodawania krawędzi do drzewa
    public void dodajKrawedz(Krawedz krawedz) {
        // Dodajemy krawędź do grafu
        graf.addEdge(krawedz.getId(), krawedz.getPierwszyPunktId(), krawedz.getDrugiPunktId())
                .setAttribute("ui.label", krawedz.getLabel());

        // Szukamy rodzica
        Optional<Wierzcholek> opcjonalnyRodzic = listaWierzcholkow.stream()
                .filter(w -> w.getId().equals(krawedz.getPierwszyPunktId()))
                .findFirst();

        // Szukamy dziecka
        Optional<Wierzcholek> opcjonalneDziecko = listaWierzcholkow.stream()
                .filter(w -> w.getId().equals(krawedz.getDrugiPunktId()))
                .findFirst();
        // Ustalanie rodzica i dziecka
        if (opcjonalnyRodzic.isPresent() && opcjonalneDziecko.isPresent()) {
            Wierzcholek rodzic = opcjonalnyRodzic.get();
            Wierzcholek dziecko = opcjonalneDziecko.get();
            dziecko.setWartosc(krawedz.getLabel());
            dziecko.setRodzicId(rodzic.getId());
            rodzic.dodajDziecko(dziecko.getId());

            System.out.println("Ustawiam rodzica: " + rodzic.getId() + " jako rodzica dla " + dziecko.getId());
        } else {
            System.out.println("Nie znaleziono wierzchołków dla podanej krawędzi.");
        }

    }

    // Metoda do usuwania krawędzi z drzewa
    public void usunKrawedz(Krawedz krawedz) {
        graf.removeEdge(krawedz.getId());

        // Można dodać logikę do usunięcia rodzica z dziecka, jeśli to potrzebne
        // W tym przypadku, możesz znaleźć dziecko i ustawić jego rodzica na null
    }


    public List<Wierzcholek> getListaWierzcholkow() {
        return listaWierzcholkow;
    }

    public void setListaWierzcholkow(List<Wierzcholek> listaWierzcholkow) {
        this.listaWierzcholkow = listaWierzcholkow;
    }

}
