package com.avatarduel.controller;

import java.net.URL;
import java.util.ResourceBundle;

import com.avatarduel.model.Player;
import com.avatarduel.model.Skill;
import javafx.animation.Animation;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;

import com.avatarduel.model.Card;

public class SummonedSkillCardController extends CardController{
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
     * @param card The Card
     * @param owner The owner of the card
     * @param targetCard The targetted character card
     */
    public SummonedSkillCardController(Card card, Player owner, Card targetCard) {
        super(card);
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