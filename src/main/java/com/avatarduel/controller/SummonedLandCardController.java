package com.avatarduel.controller;

import java.net.URL;
import java.util.ResourceBundle;

import com.avatarduel.model.Land;
import com.avatarduel.model.Player;
import javafx.animation.Animation;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;

import com.avatarduel.model.Card;
public class SummonedLandCardController extends CardController{
    /**
     * The owner of the card
     */
    private Player owner;

    /**
     * Constructor
     * @param card The Card
     * @param owner The owner of the card
     */
    public SummonedLandCardController(Card card, Player owner) {
        super(card);
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
