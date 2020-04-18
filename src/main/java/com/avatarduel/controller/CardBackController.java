package com.avatarduel.controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point3D;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

public class CardBackController implements Initializable, Flippable {
    /**
     * Card back fxml anchor pane
     */
    @FXML private AnchorPane cardBackAncPane;
    /**
     * Is card back down
     */
    private boolean isDown;
    /**
     * Flip animation
     */
    private Timeline flipUp, flipDown;

    /**
     * Constructor
     */
    public CardBackController() {
        this.isDown = true;
    }

    /**
     * Getter for flipUp timeline
     * @return this.flipUp
     */
    public Timeline getFlipUp() {
        return this.flipUp;
    }

    /**
     * Getter for flipDown timeline
     * @return this.flipDown
     */
    public Timeline getFlipDown() {
        return this.flipDown;
    }

    /**
     * Flip the card back display
     */
    public void flipCardBack() {
        if (this.isDown) {
            this.flipUp();
        } else {
            this.flipDown();
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
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Flip animation
        // Set rotation axis
        this.cardBackAncPane.setRotationAxis(new Point3D(0, 1, 0));
        this.cardBackAncPane.setRotate(90);

        // Initialize flipDown
        this.flipDown = new Timeline(
                // Rotate card back from 0 to 90 degree
                new KeyFrame(
                        Duration.ZERO,
                        new KeyValue(this.cardBackAncPane.rotateProperty(), 0)
                ),
                new KeyFrame(
                        Duration.millis(500),
                        new KeyValue(this.cardBackAncPane.rotateProperty(), 90)
                )
        );
        // Initialize flipUp
        this.flipUp = new Timeline(
                // Rotate card back from 0 to 90 degree
                new KeyFrame(
                        Duration.ZERO,
                        new KeyValue(this.cardBackAncPane.rotateProperty(), 90)
                ),
                new KeyFrame(
                        Duration.millis(500),
                        new KeyValue(this.cardBackAncPane.rotateProperty(), 0)
                )
        );
    }
}
