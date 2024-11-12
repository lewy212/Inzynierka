package com.example.inzynierka.klasy.Xml;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "decisionTree")
public class XmlDrzewo {
    private Wezel node;

    public Wezel getNode() {
        return node;
    }

    @XmlElement
    public void setNode(Wezel node) {
        this.node = node;
    }
}
