package com.example.inzynierka.klasy.ElementyDrzewa;

import java.util.ArrayList;
import java.util.List;

public class Wierzcholek {
    private String id;
    private String label;
    private String fullLabel;
    private String rodzicId; // Id rodzica
    private List<String> dzieciId; // Lista identyfikatorów dzieci
    private Double pozX;
    private Double pozY;
    private Double roznica;
    private String wartosc;
    private String strona;
    private int glebokosc;

    public Wierzcholek(String id, String label) {
        this.id = id;
        this.label = label;
        this.rodzicId = null; // Domyślnie brak rodzica
        this.dzieciId = new ArrayList<>(); // Inicjalizacja listy dzieci
    }
    public Wierzcholek(String id, String label,String fullLabel) {
        this.id = id;
        this.label = label;
        this.fullLabel = fullLabel;
        this.rodzicId = null; // Domyślnie brak rodzica
        this.dzieciId = new ArrayList<>(); // Inicjalizacja listy dzieci
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
        String[] tekst  = this.fullLabel.split(" (?=\\S)");
        this.fullLabel = label+ " " + tekst[1]+ " " + tekst[2];
    }

    public String getRodzicId() {
        return rodzicId;
    }

    public void setRodzicId(String rodzicId) {
        this.rodzicId = rodzicId;
    }

    public List<String> getDzieciId() {
        return dzieciId;
    }

    public void setDzieciId(List<String> dzieciId) {
        this.dzieciId = dzieciId;
    }

    public void dodajDziecko(String dzieckoId) {
        this.dzieciId.add(dzieckoId);
    }

    public String getWartosc() {
        return wartosc;
    }

    public void setWartosc(String wartosc) {
        this.wartosc = wartosc;
    }

    public Double getPozX() {
        return pozX;
    }

    public void setPozX(Double pozX) {
        this.pozX = pozX;
    }

    public Double getPozY() {
        return pozY;
    }

    public void setPozY(Double pozY) {
        this.pozY = pozY;
    }

    public Double getRoznica() {
        return roznica;
    }

    public void setRoznica(Double roznica) {
        this.roznica = roznica;
    }

    public String getFullLabel() {
        return fullLabel;
    }

    public void setFullLabel(String fullLabel) {
        this.fullLabel = fullLabel;
    }

    public int getGlebokosc() {
        return glebokosc;
    }

    public String getStrona() {
        return strona;
    }

    public void setStrona(String strona) {
        this.strona = strona;
    }

    public void setGlebokosc(int glebokosc) {
        this.glebokosc = glebokosc;
    }
}
