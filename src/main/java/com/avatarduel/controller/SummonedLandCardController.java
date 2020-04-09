package com.avatarduel.controller;

import com.avatarduel.model.Land;
import com.avatarduel.model.Player;

public class SummonedLandCardController extends CardController {
    /**
     * Land card model
     */
    private Land landCard;
    /**
     * The owner of the card
     */
    private Player owner;

    /**
     * Constructor
     * @param landCard The Card
     * @param owner The owner of the card
     */
    public SummonedLandCardController(Land landCard, Player owner) {
        super(landCard);
        this.landCard = landCard;
        this.owner = owner;
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
