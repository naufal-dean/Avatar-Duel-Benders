package com.avatarduel.controller;

import com.avatarduel.model.Card;
import com.avatarduel.model.Player;

public class HandCardController extends CardController {
    /**
     * Card owner
     */
    protected Player owner;
    /**
     * Card column in hand
     */
    private int col;

    /**
     * Constructor
     * @param card The Card
     * @param owner The owner of the card
     * @param col Card column in hand
     */
    public HandCardController(Card card, Player owner, int col) {
        super(card);
        this.owner = owner;
        this.col = col;
    }

    /**
     * Remove card from hand
     * @param handController The HandController
     */
    public void removeCardFromHand(HandController handController) {
        handController.removeCardOnHand(this.col);
    }
}
