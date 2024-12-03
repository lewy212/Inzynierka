package com.example.inzynierka.klasy.Json;

public class DaneNazwy {
    private String nazwa;
    private String label;

    public DaneNazwy(JsonDrzewo jsonDrzewo)
    {
        String[] parts = jsonDrzewo.getQuestion().split(" (?=\\S)");
        if(jsonDrzewo.getType().contains("node"))
        {
            this.nazwa = parts[0];
            this.label = parts[1]+" "+parts[2];
        }
        if(jsonDrzewo.getType().contains("leaf"))
        {

            String text = parts[1];
            text.substring(0,text.length()-1);
            this.nazwa = text;
            this.label=parts[0]+" "+parts[2];
        }
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
