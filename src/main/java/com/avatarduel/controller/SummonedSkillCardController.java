package com.avatarduel.controller;

import com.avatarduel.model.Player;
import com.avatarduel.model.Skill;

public class SummonedSkillCardController extends SummonedCardController {
    /**
     * Its target coordinate in field
     */
    private int targetX, targetY;

    /**
     * Constructor
     * @param skillCard The Skill Card (Only AURA and POWER UP)
     * @param owner The owner of the card
     * @param x Card x coordinate in field
     * @param y Card y coordinate in field
     */
    public SummonedSkillCardController(Skill skillCard, Player owner, int x, int y) {
        super(skillCard, owner, x, y);
        this.targetX = -1;
        this.targetY = -1;
    }

    /**
     * Getter for targetX
     * @return this.targetX
     */
    public int getTargetX() {
        return this.targetX;
    }

    /**
     * Setter for targetX
     * @param targetX The new targetX
     */
    public void setTargetX(int targetX) {
        this.targetX = targetX;
    }

    /**
     * Getter for targetY
     * @return this.targetY
     */
    public int getTargetY() {
        return this.targetY;
    }

    /**
     * Setter for targetY
     * @param targetY The new targetY
     */
    public void setTargetY(int targetY) {
        this.targetY = targetY;
    }
}