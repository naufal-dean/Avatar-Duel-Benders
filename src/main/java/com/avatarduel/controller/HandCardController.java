package com.avatarduel.controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.Point3D;
import javafx.util.Duration;

import com.avatarduel.model.Card;
import com.avatarduel.model.Player;


public class HandCardController extends CardController implements Flippable {
    /**
     * Card owner
     */
    protected Player owner;
    /**
     * Is card down
     */
    private boolean isDown;
    /**
     * The controller of card back of this card
     */
    private CardBackController cardBackController;
    /**
     * Flip animation
     */
    private Timeline flipUp, flipDown;

    /**
     * Constructor
     * @param card The Card
     * @param owner The owner of the card
     * @param cardBackController The card back controller pair
     */
    public HandCardController(Card card, Player owner, CardBackController cardBackController) {
        super(card);
        this.owner = owner;
        this.isDown = false;
        this.cardBackController = cardBackController;
    }

    /**
     * Getter for the owner of the hand card
     * @return this.owner
     */
    public Player getOwner() {
        return this.owner;
    }

    /**
     * Getter for flipUp timeline
     * @return this.flipUp
     */
    public Timeline getFlipUp() {
        return this.flipUp;
    }

    /**
     * Flip the card display
     */
    public void flipCard() {
        if (this.isDown) {
            this.cardBackController.flipCardBack();
            this.cardBackController.getFlipDown().setOnFinished(e -> this.flipUp());
        } else {
            this.flipDown();
            this.flipDown.setOnFinished(e -> this.cardBackController.flipCardBack());
        }
        this.isDown = !this.isDown;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void flipUp() {
        this.flipUp.play();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void flipDown() {
        this.flipDown.play();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initialize(URL url, ResourceBundle resources) {
        super.initialize(url, resources);
        // Flip animation
        // Set rotation axis
        this.cardAncPane.setRotationAxis(new Point3D(0, 1, 0));
        // Initialize flipDown
        this.flipDown = new Timeline(
                // Rotate card from 0 to 90 degree
                new KeyFrame(
                        Duration.ZERO,
                        new KeyValue(this.cardAncPane.rotateProperty(), 0)
                ),
                new KeyFrame(
                        Duration.millis(500),
                        new KeyValue(this.cardAncPane.rotateProperty(), 90)
                )
        );
        // Initialize flipUp
        this.flipUp = new Timeline(
                // Rotate card from 90 to 0 degree
                new KeyFrame(
                        Duration.ZERO,
                        new KeyValue(this.cardAncPane.rotateProperty(), 90)
                ),
                new KeyFrame(
                        Duration.millis(500),
                        new KeyValue(this.cardAncPane.rotateProperty(), 0)
                )
        );
    }
}
