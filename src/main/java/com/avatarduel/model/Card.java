package com.avatarduel.model;

import java.nio.file.Paths;

public abstract class Card {
    private int id;
    private String name;
    private Element element;
    private String description;
    private String imagePath;

    public Card() {
        this.id = -1;
        this.name = "";
        this.element = Element.AIR;
        this.description = "";
        this.imagePath = "";
    }

    public Card(int id, String name, Element element, String description, String imagePath) {
        this.id = id;
        this.name = name;
        this.element = element;
        this.description = description;
        this.imagePath = Paths.get(imagePath).getFileName().toString();
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

    public String getImagePath() {
        return this.imagePath;
    }

    public abstract CardType getCardType();
}