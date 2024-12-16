package com.example.inzynierka.klasy;

import com.example.inzynierka.kontrolery.UstawieniaController;
import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;

import java.util.*;
import java.util.prefs.Preferences;
import java.util.stream.Collectors;

public class Drzewo {
    private Graph graf;
    private List<Wierzcholek> listaWierzcholkow;

    private int maksymalnaGlebokosc;
    private double maxLewo,maxPrawo,minOdlegosc;
    public Drzewo() {
        this.graf = new SingleGraph("Drzewo Decyzyjne");
        this.listaWierzcholkow = new ArrayList<>();
        this.maksymalnaGlebokosc=0;
        this.maxLewo = -357.50;
        this.maxPrawo = 294.00;
        Preferences prefs = Preferences.userNodeForPackage(UstawieniaController.class);
        this.minOdlegosc = prefs.getInt("minimalnaOdleglosc", 50);
    }

    public Graph getGraf() {
        return graf;
    }

    public void dodajWierzcholek(Wierzcholek wierzcholek) {
        graf.addNode(wierzcholek.getId()).setAttribute("label", wierzcholek.getFullLabel());
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

          //  System.out.println("Ustawiam rodzica: " + rodzic.getId() + " jako rodzica dla " + dziecko.getId());
        } else {
            //System.out.println("Nie znaleziono wierzchołków dla podanej krawędzi.");
        }
        System.out.println(krawedz.getId()+ "   "+krawedz.getLabel());
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
            staryWierzcholek.setFullLabel(nowyWierzcholek.getFullLabel());
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



    public Wierzcholek getWierzcholekByid(String id)
    {
        return listaWierzcholkow.stream()
                .filter(w -> w.getId().equals(id)) // Filtruje elementy po pasującym id
                .findFirst()                       // Znajduje pierwszy pasujący element
                .orElse(null);
    }

    public List<Wierzcholek> getListaWierzcholkow() {
        return listaWierzcholkow;
    }

    public void setListaWierzcholkow(List<Wierzcholek> listaWierzcholkow) {
        this.listaWierzcholkow = listaWierzcholkow;
    }
    public int getMaksymalnaGlebokosc() {
        return maksymalnaGlebokosc;
    }

    public void setMaksymalnaGlebokosc(int maksymalnaGlebokosc) {
        if(maksymalnaGlebokosc > this.maksymalnaGlebokosc)
        {
            this.maksymalnaGlebokosc = maksymalnaGlebokosc;
        }
    }
    private  void ustawPozycjeXYzUstawien()
    {
        for(Wierzcholek wierzcholek : listaWierzcholkow)
        {
            graf.getNode(wierzcholek.getId()).setAttribute("xy",wierzcholek.getPozX(),wierzcholek.getPozY());
        }
    }
    public void poprawUstawienieWierzcholkow() {
       for(int a = 0; a<2;a++)
       {
           for (int i = this.maksymalnaGlebokosc; i >= 0; i--)
           {
               int finalI = i;

               List<Wierzcholek> listaGlebokosci = listaWierzcholkow.stream()
                       .filter(w -> w.getGlebokosc() == finalI)
                       .sorted(Comparator.comparingDouble(Wierzcholek::getPozX))
                       .collect(Collectors.toList());

               boolean czyZmiana;
               do {
                   czyZmiana = false;
                   for (int j = 0; j < listaGlebokosci.size() - 1; j++)
                   {
                       Wierzcholek aktualny = listaGlebokosci.get(j);
                       Wierzcholek kolejny = listaGlebokosci.get(j + 1);

                       double odleglosc = kolejny.getPozX() - aktualny.getPozX();

                       if (odleglosc < minOdlegosc)
                       {
                           double przestrzenLewo = j == 0 ? aktualny.getPozX() - maxLewo : aktualny.getPozX() - listaGlebokosci.get(j - 1).getPozX();
                           double przestrzenPrawo = j + 1 == listaGlebokosci.size() - 1 ? maxPrawo - kolejny.getPozX() : listaGlebokosci.get(j + 2).getPozX() - kolejny.getPozX();

                           if (przestrzenPrawo >= przestrzenLewo)
                           {
                               kolejny.setPozX(kolejny.getPozX() + (minOdlegosc - odleglosc));
                           }
                           else
                           {
                               aktualny.setPozX(aktualny.getPozX() - (minOdlegosc - odleglosc));
                           }

                           czyZmiana = true;
                       }
                   }
               } while (czyZmiana);

               if (finalI > 0)
               {
                   List<Wierzcholek> rodzice = listaWierzcholkow.stream()
                           .filter(w -> w.getGlebokosc() == finalI - 1)
                           .sorted(Comparator.comparingDouble(Wierzcholek::getPozX))
                           .collect(Collectors.toList());

                   for (Wierzcholek rodzic : rodzice)
                   {
                       List<Wierzcholek> dzieci = listaWierzcholkow.stream()
                               .filter(w -> rodzic.getId() != null && rodzic.getId().equals(w.getRodzicId()))
                               .sorted(Comparator.comparingDouble(Wierzcholek::getPozX))
                               .collect(Collectors.toList());

                       if (!dzieci.isEmpty())
                       {
                           // Obliczamy nową pozycję rodzica jako środek jego dzieci
                           double nowaPozycja = (dzieci.get(0).getPozX() + dzieci.get(dzieci.size() - 1).getPozX()) / 2;

                           // Sprawdzamy, czy nowa pozycja rodzica koliduje z innymi na głębokości i-1
                           boolean kolizja = rodzice.stream()
                                   .anyMatch(r -> !r.equals(rodzic) && Math.abs(r.getPozX() - nowaPozycja) < minOdlegosc);

                           if (!kolizja)
                           {
                               rodzic.setPozX(nowaPozycja);
                           }
                           else
                           {
                               // Jeśli jest kolizja, przesuwamy rodzica tak, aby zachować minimalny odstęp
                               for (int j = 0; j < rodzice.size() - 1; j++) {
                                   Wierzcholek aktualnyRodzic = rodzice.get(j);
                                   Wierzcholek kolejnyRodzic = rodzice.get(j + 1);

                                   double odlegloscRodzicow = kolejnyRodzic.getPozX() - aktualnyRodzic.getPozX();
                                   if (odlegloscRodzicow < minOdlegosc) {
                                       double przesuniecie = minOdlegosc - odlegloscRodzicow;
                                       kolejnyRodzic.setPozX(kolejnyRodzic.getPozX() + przesuniecie);
                                   }
                               }
                           }
                       }
                   }
               }
           }
       }
       poprawSymetrieRodzicow();
    }
    private void poprawSymetrieRodzicow() {
        // Iterujemy od najmniejszej głębokości do największej
        for (int i = 0; i < this.maksymalnaGlebokosc; i++) {
            int finalI = i;

            // Pobieramy wierzchołki na bieżącej głębokości (rodzice)
            List<Wierzcholek> listaRodzicow = listaWierzcholkow.stream()
                    .filter(w -> w.getGlebokosc() == finalI)
                    .sorted(Comparator.comparingDouble(Wierzcholek::getPozX))
                    .collect(Collectors.toList());

            // Pobieramy wierzchołki na głębokości niższej (dzieci)
            List<Wierzcholek> listaDzieci = listaWierzcholkow.stream()
                    .filter(w -> w.getGlebokosc() == finalI + 1)
                    .sorted(Comparator.comparingDouble(Wierzcholek::getPozX))
                    .collect(Collectors.toList());

            for (Wierzcholek rodzic : listaRodzicow) {
                // Znajdujemy dzieci danego rodzica
                List<Wierzcholek> dzieci = listaDzieci.stream()
                        .filter(w -> rodzic.getId() != null && rodzic.getId().equals(w.getRodzicId()))
                        .sorted(Comparator.comparingDouble(Wierzcholek::getPozX))
                        .collect(Collectors.toList());

                if (dzieci.size() >= 2) {
                    Wierzcholek leweDziecko = dzieci.get(0);
                    Wierzcholek praweDziecko = dzieci.get(dzieci.size() - 1);

                    // Obliczamy idealną pozycję rodzica (środek dzieci)
                    double idealnaPozycjaRodzica = (leweDziecko.getPozX() + praweDziecko.getPozX()) / 2;

                    if (Math.abs(rodzic.getPozX() - idealnaPozycjaRodzica) > 1e-6) {
                        // Rodzic nie jest na środku - musimy przesunąć bliższe dziecko
                        double odlegloscDoLewego = Math.abs(rodzic.getPozX() - leweDziecko.getPozX());
                        double odlegloscDoPrawego = Math.abs(rodzic.getPozX() - praweDziecko.getPozX());

                        // Wybieramy dziecko bliższe rodzicowi
                        Wierzcholek blizszeDziecko = odlegloscDoLewego < odlegloscDoPrawego ? leweDziecko : praweDziecko;
                        Wierzcholek dalszeDziecko = blizszeDziecko == leweDziecko ? praweDziecko : leweDziecko;

                        // Obliczamy nową pozycję bliższego dziecka
                        double noweXBlizszego = rodzic.getPozX() + (rodzic.getPozX() - dalszeDziecko.getPozX());

                        // Sprawdzamy, czy przesunięcie bliższego dziecka nie powoduje konfliktów
                        boolean czyMoznaPrzesunac = listaDzieci.stream()
                                .filter(w -> !w.equals(blizszeDziecko))
                                .noneMatch(w -> Math.abs(w.getPozX() - noweXBlizszego) < minOdlegosc);

                        if (czyMoznaPrzesunac) {
                            blizszeDziecko.setPozX(noweXBlizszego);
                        }
                    }
                }
            }
        }
        ustawPozycjeXYzUstawien();
    }






}
