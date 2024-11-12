package com.example.inzynierka.klasy.Json;

import com.fasterxml.jackson.annotation.JsonProperty;

public class JsonDrzewo {
    @JsonProperty("question")
    private String question;
    @JsonProperty("type")
    private String type;
    @JsonProperty("yes")
    private JsonDrzewo yes;
    @JsonProperty("no")
    private JsonDrzewo no;

    public JsonDrzewo() {
    }

    public JsonDrzewo(String question, String type, JsonDrzewo yes, JsonDrzewo no) {
        this.question = question;
        this.type = type;
        this.yes = yes;
        this.no = no;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public JsonDrzewo getYes() {
        return yes;
    }

    public void setYes(JsonDrzewo yes) {
        this.yes = yes;
    }

    public JsonDrzewo getNo() {
        return no;
    }

    public void setNo(JsonDrzewo no) {
        this.no = no;
    }
}
