package com.example.inzynierka.klasy.Xml;

import com.example.inzynierka.klasy.Json.JsonDrzewo;

public class DaneNazwyXml {

    private String nazwa;
    private String label;

    public DaneNazwyXml(Wezel wezel)
    {
        String[] parts = wezel.getQuestion().split(" ");

        this.nazwa = parts[0];
        this.label = parts[1]+" "+parts[2];
    }
    public DaneNazwyXml(Lisc lisc)
    {
        String[] parts = lisc.getDecision().split(" ");

        String text = parts[1];
        text.substring(0,text.length()-1);
        this.nazwa = text;
        this.label="";
    }

    public String getNazwa() {
        return nazwa;
    }

    public void setNazwa(String nazwa) {
        this.nazwa = nazwa;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
