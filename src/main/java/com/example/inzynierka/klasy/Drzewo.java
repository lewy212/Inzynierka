package com.example.inzynierka.klasy;

import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.fx_viewer.FxViewer;
import org.graphstream.ui.view.ViewerPipe;

import java.util.*;

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

    public void edytujWierzcholek(Wierzcholek nowyWierzcholek, String idStaregoWierzcholka) {

        Map<String, Wierzcholek> wierzcholekMap = new HashMap<>();
        for (Wierzcholek w : listaWierzcholkow) {
            wierzcholekMap.put(w.getId(), w);
        }
        System.out.println("Wywoluje "+idStaregoWierzcholka);
        Wierzcholek staryWierzcholek = wierzcholekMap.get(idStaregoWierzcholka);

        if (staryWierzcholek != null) {
            System.out.println("edytuje");
            // Zaktualizuj istniejący wierzchołek bez jego usuwania z listy
            staryWierzcholek.setLabel(nowyWierzcholek.getLabel());
            // Aktualizujemy dzieci
            List<String> stareDzieci = staryWierzcholek.getDzieciId();

            // Aktualizacja ID dzieci (jeśli się zmieniają)
            for (String dzieckoId : stareDzieci) {
                Wierzcholek dziecko = wierzcholekMap.get(dzieckoId);
                if (dziecko != null && dziecko.getRodzicId().equals(idStaregoWierzcholka)) {
                    dziecko.setRodzicId(nowyWierzcholek.getId());
                }
            }
            staryWierzcholek.setId(nowyWierzcholek.getId());
            System.out.println("Wierzchołek o ID " + idStaregoWierzcholka + " został zaktualizowany.");
        } else {
            System.out.println("Nie znaleziono wierzchołka o ID: " + idStaregoWierzcholka);
        }
    }

    public void usunKrawedz(Krawedz krawedz) {
        graf.removeEdge(krawedz.getId());

    }


    public List<Wierzcholek> getListaWierzcholkow() {
        return listaWierzcholkow;
    }

    public void setListaWierzcholkow(List<Wierzcholek> listaWierzcholkow) {
        this.listaWierzcholkow = listaWierzcholkow;
    }

}
