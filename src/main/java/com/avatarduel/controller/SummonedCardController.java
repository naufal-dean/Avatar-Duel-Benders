package com.avatarduel.controller;

import com.avatarduel.model.Card;
import com.avatarduel.model.Player;

public class SummonedCardController extends CardController {
    /**
     * Card owner
     */
    protected Player owner;
    /**
     * Card coordinate in field
     */
    private int x, y;

    /**
     * Constructor
     * @param card The Card
     * @param owner The owner of the card
     * @param x Card x coordinate in field
     * @param y Card y coordinate in field
     */
    public SummonedCardController(Card card, Player owner, int x, int y) {
        super(card);
        this.owner = owner;
        this.x = x;
        this.y = y;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void init() {
        super.init();
        // Rotate card if owned by Player.TOP
        if (this.owner == Player.TOP)
            this.cardAncPane.setRotate(this.cardAncPane.getRotate() + 180);
    }
}
