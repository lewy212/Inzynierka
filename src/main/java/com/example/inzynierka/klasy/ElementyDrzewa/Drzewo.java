package com.example.inzynierka.klasy.ElementyDrzewa;

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

    private Preferences prefs;
    public Drzewo() {
        this.graf = new SingleGraph("Drzewo Decyzyjne");
        this.listaWierzcholkow = new ArrayList<>();
        this.maksymalnaGlebokosc=0;
        this.maxLewo = -357.50;
        this.maxPrawo = 294.00;
        this.prefs = Preferences.userNodeForPackage(UstawieniaController.class);
        this.minOdlegosc = prefs.getInt("minimalnaOdleglosc", 60);
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

        graf.addEdge(krawedz.getId(), krawedz.getPierwszyPunktId(), krawedz.getDrugiPunktId())
                .setAttribute("ui.label", krawedz.getLabel());

        Optional<Wierzcholek> opcjonalnyRodzic = listaWierzcholkow.stream()
                .filter(w -> w.getId().equals(krawedz.getPierwszyPunktId()))
                .findFirst();


        Optional<Wierzcholek> opcjonalneDziecko = listaWierzcholkow.stream()
                .filter(w -> w.getId().equals(krawedz.getDrugiPunktId()))
                .findFirst();

        if (opcjonalnyRodzic.isPresent() && opcjonalneDziecko.isPresent()) {
            Wierzcholek rodzic = opcjonalnyRodzic.get();
            Wierzcholek dziecko = opcjonalneDziecko.get();
            dziecko.setWartosc(krawedz.getLabel());
            dziecko.setRodzicId(rodzic.getId());
            rodzic.dodajDziecko(dziecko.getId());
        }

    }

    public void edytujWierzcholek(Wierzcholek nowyWierzcholek, String idStaregoWierzcholka) {

        Map<String, Wierzcholek> wierzcholekMap = new HashMap<>();
        for (Wierzcholek w : listaWierzcholkow) {
            wierzcholekMap.put(w.getId(), w);
        }
        Wierzcholek staryWierzcholek = wierzcholekMap.get(idStaregoWierzcholka);

        if (staryWierzcholek != null) {
            staryWierzcholek.setLabel(nowyWierzcholek.getLabel());
            staryWierzcholek.setFullLabel(nowyWierzcholek.getFullLabel());
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
                .filter(w -> w.getId().equals(id))
                .findFirst()
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

               double maxOdleglosc = 0;
               int maxOdlegloscIndex1 = -1;
               int maxOdlegloscIndex2 = -1;
               int iteracja = 1;
               minOdlegosc = prefs.getInt("minimalnaOdleglosc", 60);
               do {
                   czyZmiana = false;

                   double maxOdlegloscTemp = 0;
                   int maxIndex1Temp = -1;
                   int maxIndex2Temp = -1;


                   for (int j = 0; j < listaGlebokosci.size() - 1; j++) {
                       Wierzcholek aktualny = listaGlebokosci.get(j);
                       Wierzcholek kolejny = listaGlebokosci.get(j + 1);

                       double odleglosc = Math.abs(kolejny.getPozX() - aktualny.getPozX());

                       if (odleglosc > maxOdlegloscTemp) {
                           maxOdlegloscTemp = odleglosc;
                           maxIndex1Temp = j;
                           maxIndex2Temp = j + 1;
                       }


                       while (minOdlegosc * (listaGlebokosci.size() - 1) > Math.abs(maxLewo) + Math.abs(maxPrawo)) {
                           minOdlegosc -= 10;
                       }

                       if (odleglosc < this.minOdlegosc) {
                           double przestrzenLewo = j == 0 ? aktualny.getPozX() - maxLewo : aktualny.getPozX() - listaGlebokosci.get(j - 1).getPozX();
                           double przestrzenPrawo = j + 1 == listaGlebokosci.size() - 1 ? maxPrawo - kolejny.getPozX() : listaGlebokosci.get(j + 2).getPozX() - kolejny.getPozX();

                           if (przestrzenPrawo >= przestrzenLewo) {
                               kolejny.setPozX(kolejny.getPozX() + (minOdlegosc - odleglosc));
                           } else {
                               aktualny.setPozX(aktualny.getPozX() - (minOdlegosc - odleglosc));
                           }
                           czyZmiana = true;
                       }
                   }

                   if (iteracja %10==0) {
                       if (maxOdlegloscTemp > minOdlegosc) {
                           Wierzcholek wierzcholek1 = listaGlebokosci.get(maxIndex1Temp);
                           Wierzcholek wierzcholek2 = listaGlebokosci.get(maxIndex2Temp);

                           double przesuniecie = (maxOdlegloscTemp - minOdlegosc) / 2;

                           if (przesuniecie > 0) {
                               wierzcholek1.setPozX(wierzcholek1.getPozX() + przesuniecie);
                               wierzcholek2.setPozX(wierzcholek2.getPozX() - przesuniecie);
                           }
                           czyZmiana = true;
                       }
                       if(iteracja % 20 ==0)
                       {
                           minOdlegosc-=10;
                       }
                   }


                   iteracja++;
                    if(minOdlegosc<=0)
                    {
                        break;
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
                           double nowaPozycja = (dzieci.get(0).getPozX() + dzieci.get(dzieci.size() - 1).getPozX()) / 2;

                           boolean kolizja = rodzice.stream()
                                   .anyMatch(r -> !r.equals(rodzic) && Math.abs(r.getPozX() - nowaPozycja) < minOdlegosc);

                           if (!kolizja)
                           {
                               rodzic.setPozX(nowaPozycja);
                           }
                           else
                           {
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
       ustawKolejnosc();
    }
    private void poprawSymetrieRodzicow() {
        for (int i = 0; i < this.maksymalnaGlebokosc; i++) {
            int finalI = i;

            List<Wierzcholek> listaRodzicow = listaWierzcholkow.stream()
                    .filter(w -> w.getGlebokosc() == finalI)
                    .sorted(Comparator.comparingDouble(Wierzcholek::getPozX))
                    .collect(Collectors.toList());

            List<Wierzcholek> listaDzieci = listaWierzcholkow.stream()
                    .filter(w -> w.getGlebokosc() == finalI + 1)
                    .sorted(Comparator.comparingDouble(Wierzcholek::getPozX))
                    .collect(Collectors.toList());

            for (Wierzcholek rodzic : listaRodzicow) {
                List<Wierzcholek> dzieci = listaDzieci.stream()
                        .filter(w -> rodzic.getId() != null && rodzic.getId().equals(w.getRodzicId()))
                        .sorted(Comparator.comparingDouble(Wierzcholek::getPozX))
                        .collect(Collectors.toList());

                if (dzieci.size() >= 2) {
                    Wierzcholek leweDziecko = dzieci.get(0);
                    Wierzcholek praweDziecko = dzieci.get(dzieci.size() - 1);


                    double idealnaPozycjaRodzica = (leweDziecko.getPozX() + praweDziecko.getPozX()) / 2;

                    if (Math.abs(rodzic.getPozX() - idealnaPozycjaRodzica) > 1e-6) {

                        double odlegloscDoLewego = Math.abs(rodzic.getPozX() - leweDziecko.getPozX());
                        double odlegloscDoPrawego = Math.abs(rodzic.getPozX() - praweDziecko.getPozX());

                        Wierzcholek blizszeDziecko = odlegloscDoLewego < odlegloscDoPrawego ? leweDziecko : praweDziecko;
                        Wierzcholek dalszeDziecko = blizszeDziecko == leweDziecko ? praweDziecko : leweDziecko;

                        double noweXBlizszego = rodzic.getPozX() + (rodzic.getPozX() - dalszeDziecko.getPozX());
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

    }
    private void ustawKolejnosc()
    {
        for (int i = maksymalnaGlebokosc; i >= 0; i--) {
            int finalI = i;

            List<Wierzcholek> listaGlebokosci = listaWierzcholkow.stream()
                    .filter(w -> w.getGlebokosc() == finalI)
                    .collect(Collectors.toList());


            List<Double> odleglosci = listaGlebokosci.stream()
                    .map(Wierzcholek::getPozX)
                    .sorted(Comparator.reverseOrder())  // Sortowanie malejąco
                    .collect(Collectors.toList());

            for (int j = 0; j < listaGlebokosci.size(); j++) {
                Wierzcholek wierzcholek = listaGlebokosci.get(j);
                wierzcholek.setPozX(odleglosci.get(j));
            }

        }
        ustawPozycjeXYzUstawien();
    }
    public int obliczIleJuzJestTakichWierzcholkow(String newId)
    {
        return (int) listaWierzcholkow.stream()
                .filter(wierzcholek -> wierzcholek.getId().replaceAll("\\.*$", "").equals(newId))
                .count();
    }

    public void setGraf(Graph graf) {
        this.graf = graf;
    }

    public double getMaxLewo() {
        return maxLewo;
    }

    public void setMaxLewo(double maxLewo) {
        this.maxLewo = maxLewo;
    }

    public double getMaxPrawo() {
        return maxPrawo;
    }

    public void setMaxPrawo(double maxPrawo) {
        this.maxPrawo = maxPrawo;
    }

    public double getMinOdlegosc() {
        return minOdlegosc;
    }

    public void setMinOdlegosc(double minOdlegosc) {
        this.minOdlegosc = minOdlegosc;
    }
}
