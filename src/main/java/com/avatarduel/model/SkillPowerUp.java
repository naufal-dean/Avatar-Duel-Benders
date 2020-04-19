package com.avatarduel.model;

public class SkillPowerUp extends Skill {
    /**
     * Default Constructor
     */
    public SkillPowerUp() {
        super();
    }

    /**
     * Constructor
     * @param id The card id
     * @param name The card name
     * @param element The card element
     * @param description The card description
     * @param imagePath The card imagePath
     * @param power The card power
     */
    public SkillPowerUp(int id, String name, Element element, String description, String imagePath, int power) {
        super(id, name, element, description, imagePath, power);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getImagePath() {
        return "com/avatarduel/card/image/skill/power up/" + super.getImagePath();
    }

    /**
     * {@inheritDoc}
s     */
    @Override
    public Effect getEffect() {
        return Effect.POWER_UP;
    }
}
