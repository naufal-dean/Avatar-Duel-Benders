package com.avatarduel.model;

public class SkillDestroy extends Skill {
    /**
     * Default Constructor
     */
    public SkillDestroy() {
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
    public SkillDestroy(int id, String name, Element element, String description, String imagePath, int power) {
        super(id, name, element, description, imagePath, power);
    }

    /**
     * {@inheritDoc}
     s     */
    @Override
    public Effect getEffect() {
        return Effect.DESTROY;
    }
}
