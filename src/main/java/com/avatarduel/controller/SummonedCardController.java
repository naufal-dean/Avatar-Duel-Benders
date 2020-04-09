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
    }

    /**
     * Remove card from field
     * @param fieldController The FieldController
     */
    public void removeCardFromField(FieldController fieldController) {
        fieldController.removeCardOnField(this.x, this.y);
    }
}
