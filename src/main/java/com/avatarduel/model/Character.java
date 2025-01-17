package com.avatarduel.model;

public class Character extends Card {
    private int attack;
    private int defense;
    private int power;

    public Character() {
        super();
        this.attack = 0;
        this.defense = 0;
        this.power = 0;
    }

    public Character(int id, String name, Element element, String description, String imagePath, int attack, int defense, int power) {
        super(id, name, element, description, imagePath);
        this.attack = attack;
        this.defense = defense;
        this.power = power;
    }

    public int getAttack() {
        return this.attack;
    }

    public int getDefense() {
        return this.defense;
    }

    @Override
    public int getPower() {
        return this.power;
    }

    @Override
    public String getImagePath() {
        return "com/avatarduel/card/image/character/" + super.getImagePath();
    }

    @Override
    public CardType getCardType() {
        return CardType.CHARACTER;
    }
}