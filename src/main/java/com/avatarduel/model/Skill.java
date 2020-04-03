package com.avatarduel.model;

public abstract class Skill extends Card {
    private int power;

    public Skill() {
        super();
        this.power = 0;
    }

    public Skill(int id, String name, Element element, String description, String imagepath, int power) {
        super(id, name, element, description, imagepath);
        this.power = power;
    }

    public int getPower() {
        return this.power;
    }

    @Override
    public CardType getCardType() {
        return CardType.SKILL;
    }

    public abstract Effect getEffect();
}