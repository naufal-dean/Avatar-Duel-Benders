package com.avatarduel.gameutils;

import java.util.ArrayList;
import java.util.List;

import com.avatarduel.model.Card;

public class GameHand {
    public static final int MAX_CARD_IN_HAND = 10; // TODO: can be changed later
    /**
     * List card in hand
     */
    private List<Card> cardInHand;

    /**
     * Constructor
     */
    public GameHand() {
        this.cardInHand = new ArrayList<>(MAX_CARD_IN_HAND);
    }

    /**
     * Add card to hand
     * @param card The Card to be added
     */
    public void addCard(Card card) {
        this.cardInHand.add(card);
    }

    /**
     * Remove card from hand
     * @param card The Card to be removed
     */
    public void removeCard(Card card) {
        this.cardInHand.remove(card);
    }
}
