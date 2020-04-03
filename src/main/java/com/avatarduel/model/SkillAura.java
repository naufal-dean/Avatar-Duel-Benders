package com.avatarduel.model;

public class SkillAura extends Skill {
    private int attack;
    private int defense;

    public SkillAura() {
        super();
        this.attack = 0;
        this.defense = 0;
    }

    public SkillAura(int id, String name, Element element, String description, String imagepath, int power, int attack, int defense) {
        super(id, name, element, description, imagepath, power);
        this.attack = attack;
        this.defense = defense;
    }

    public int getAttack() {
        return this.attack;
    }

    public int getDefense() {
        return this.defense;
    }

    @Override
    public Effect getEffect() {
        return Effect.AURA;
    }
}