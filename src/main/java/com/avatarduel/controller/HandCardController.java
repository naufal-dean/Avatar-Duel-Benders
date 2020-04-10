package com.avatarduel.controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.EventHandler;
import javafx.geometry.Point3D;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;

import com.avatarduel.model.Card;
import com.avatarduel.model.Player;


public class HandCardController extends CardController implements Flippable {
    /**
     * Card owner
     */
    protected Player owner;
    /**
     * Card column in hand
     */
    private int col;
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
     * @param col Card column in hand
     */
    public HandCardController(Card card, Player owner, int col, CardBackController cardBackController) {
        super(card);
        this.owner = owner;
        this.col = col;
        this.isDown = false;
        this.cardBackController = cardBackController;
    }

    /**
     * Remove card from hand
     * @param handController The HandController
     */
    public void removeCardFromHand(HandController handController) {
        handController.removeCardOnHand(this.col);
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
        // TODO: this handler is for debugging, remove after use
        this.cardAncPane.onMouseClickedProperty().set((EventHandler<MouseEvent>) (MouseEvent e) -> {
            if (e.getButton() == MouseButton.PRIMARY) {
                this.flipCard();
            }
        });
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
                        Duration.millis(250),
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
                        Duration.millis(250),
                        new KeyValue(this.cardAncPane.rotateProperty(), 0)
                )
        );
    }
}
