package com.avatarduel.model;

public abstract class Card {
    private int id;
    private String name;
    private Element element;
    private String description;
    private String imagepath;

    public Card() {
        this.id = -1;
        this.name = "";
        this.element = Element.AIR;
        this.description = "";
        this.imagepath = "";
    }

    public Card(int id, String name, Element element, String description, String imagepath) {
        this.id = id;
        this.name = name;
        this.element = element;
        this.description = description;
        this.imagepath = imagepath;
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public Element getElement() {
        return this.element;
    }

    public String getDescription() {
        return this.description;
    }

    public String getImagepath() {
        return this.imagepath;
    }

    public abstract CardType getCardType();
}