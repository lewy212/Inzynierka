package com.example.inzynierka.klasy;

public class Krawedz {
    private String id;
    private String pierwszyPunktId;
    private String drugiPunktId;
    private String label;

    public Krawedz(String id, String pierwszyPunktId, String drugiPunktId, String label) {
        this.id = id;
        this.pierwszyPunktId = pierwszyPunktId;
        this.drugiPunktId = drugiPunktId;
        this.label = label;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPierwszyPunktId() {
        return pierwszyPunktId;
    }

    public void setPierwszyPunktId(String pierwszyPunktId) {
        this.pierwszyPunktId = pierwszyPunktId;
    }

    public String getDrugiPunktId() {
        return drugiPunktId;
    }

    public void setDrugiPunktId(String drugiPunktId) {
        this.drugiPunktId = drugiPunktId;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
