package com.example.inzynierka.klasy;

public class Para
{
    private String nazwa;
    private int liczbaKresek;
    private double srodek;

    public Para(String nazwa, int liczbaKresek) {
        this.nazwa = nazwa;
        this.liczbaKresek = liczbaKresek;
    }

    public String getNazwa() {
        return nazwa;
    }

    public void setNazwa(String nazwa) {
        this.nazwa = nazwa;
    }

    public int getLiczbaKresek() {
        return liczbaKresek;
    }

    public void setLiczbaKresek(int liczbaKresek) {
        this.liczbaKresek = liczbaKresek;
    }

    public double getSrodek() {
        return srodek;
    }

    public void setSrodek(double srodek) {
        this.srodek = srodek;
    }
}
