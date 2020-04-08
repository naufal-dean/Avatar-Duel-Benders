package com.avatarduel.controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.animation.Animation;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;

import com.avatarduel.model.Card;

public class SummonedSkillCardController extends CardController{
    /**
     *  Target of Skill Card
     */
    private Card targetCard;

    /**
     * Constructor
     */
    public SummonedSkillCardController(Card card, Card targetCard){
        super(card);
        this.targetCard=targetCard;
    }

}