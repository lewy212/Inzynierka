package com.example.inzynierka.klasy.Xml;

import javax.xml.bind.annotation.XmlElement;

public class Wezel {
    private String question;
    private Wezel yes;
    private Wezel no;
    private Lisc leaf;
    private Wezel parent;

    public String getQuestion() {
        return question;
    }
    @XmlElement
    public void setQuestion(String question) {
        this.question = question;
    }

    public Wezel getYes() {
        return yes;
    }
    @XmlElement
    public void setYes(Wezel yes) {
        this.yes = yes;
        if(yes !=null)
        {
            yes.setParent(this);
        }
    }

    public Wezel getNo() {
        return no;
    }
    @XmlElement
    public void setNo(Wezel no) {
        this.no = no;
        if (no != null) {
            no.setParent(this);
        }
    }

    public Lisc getLeaf() {
        return leaf;
    }

    @XmlElement
    public void setLeaf(Lisc leaf) {
        this.leaf = leaf;
        if (leaf != null) {
            leaf.setParent(this);
        }
    }

    public Wezel getParent() {
        return parent;
    }

    public void setParent(Wezel parent) {
        this.parent = parent;
    }
}
