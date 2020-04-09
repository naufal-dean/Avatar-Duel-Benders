package com.avatarduel.controller;

import com.avatarduel.model.Card;
import com.avatarduel.model.Player;
import com.avatarduel.model.SkillAura;

public class SummonedSkillCardController extends SummonedCardController {
    /**
     *  Target of Skill Card
     */
    private Card targetCard;

    /**
     * Constructor
     * @param skillAuraCard The Skill Aura Card
     * @param owner The owner of the card
     * @param x Card x coordinate in field
     * @param y Card y coordinate in field
     */
    public SummonedSkillCardController(SkillAura skillAuraCard, Player owner, int x, int y, Card targetCard) {
        super(skillAuraCard, owner, x, y);
        this.targetCard = targetCard;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void init() {
        super.init();
        // Rotate card if owned by Player.TOP
        if (this.owner == Player.TOP)
            this.cardAncPane.setRotate(180);
    }
}