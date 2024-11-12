package com.example.inzynierka.klasy.Xml;

import javax.xml.bind.annotation.XmlElement;

public class Lisc {
    private String decision;
    private Wezel parent;

    public String getDecision() {
        return decision;
    }
    @XmlElement
    public void setDecision(String decision) {
        this.decision = decision;
    }

    public Wezel getParent() {
        return parent;
    }

    public void setParent(Wezel parent) {
        this.parent = parent;
    }
}
