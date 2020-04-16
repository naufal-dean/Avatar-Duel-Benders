package com.avatarduel.model;

public abstract class Skill extends Card {
    private int power;

    public Skill() {
        super();
        this.power = 0;
    }

    public Skill(int id, String name, Element element, String description, String imagePath, int power) {
        super(id, name, element, description, imagePath);
        this.power = power;
    }

    @Override
    public int getPower() {
        return this.power;
    }

    @Override
    public String getImagePath() {
        return "com/avatarduel/card/image/skill/" + super.getImagePath();
    }

    @Override
    public CardType getCardType() {
        return CardType.SKILL;
    }

    public abstract Effect getEffect();
}