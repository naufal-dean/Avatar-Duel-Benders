package com.avatarduel.controller;

import com.avatarduel.model.Card;
import com.avatarduel.model.Player;
import com.avatarduel.model.SkillAura;

public class SummonedSkillCardController extends CardController{
    /**
     * Skill aura card model
     */
    private SkillAura skillAuraCard;
    /**
     * The owner of the card
     */
    private Player owner;
    /**
     *  Target of Skill Card
     */
    private Card targetCard;

    /**
     * Constructor
     * @param skillAuraCard The Card
     * @param owner The owner of the card
     * @param targetCard The targetted character card
     */
    public SummonedSkillCardController(SkillAura skillAuraCard, Player owner, Card targetCard) {
        super(skillAuraCard);
        this.skillAuraCard = skillAuraCard;
        this.owner = owner;
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