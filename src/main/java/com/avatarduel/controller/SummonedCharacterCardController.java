package com.avatarduel.controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.animation.Animation;
import javafx.animation.RotateTransition;
import javafx.fxml.FXML;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

import com.avatarduel.model.Character;
import com.avatarduel.model.Player;

public class SummonedCharacterCardController extends SummonedCardController {
    /**
     * Is monster in attack position
     */
    private boolean isAttack;
    /**
     * Is first time monster has summoned
     */
    private boolean hadAttacked;
    /**
     * Rotate transition object
     */
    private RotateTransition rotate;

    /**
     * Constructor
     * @param characterCard The Character Card
     * @param owner The owner of the card
     * @param x Card x coordinate in field
     * @param y Card y coordinate in field
     * @param isAttack Is summoned in attack position
     */
    public SummonedCharacterCardController(Character characterCard, Player owner, int x, int y, boolean isAttack) {
        super(characterCard, owner, x, y);
        this.isAttack = isAttack;
        this.rotate = new RotateTransition();
        this.hadAttacked = true;
    }

    /**
     * Getter for isAttack status
     * @return this.isAttack
     */
    public boolean getIsAttack() {
        return this.isAttack;
    }

    /**
     * Return this card attack / defense based on card position
     * @return This card attack / defense
     */
    public int getCardValue() {
        return (this.isAttack) ? (((Character) this.card).getAttack()) : (((Character) this.card).getDefense());
    }

    /**
     * Getter for hadAttacked status
     * @return this.hadAttacked
     */
    public boolean getHadAttacked() {
        return this.hadAttacked;
    }

    /**
     * Setter for hadAttacked status
     * @param hadAttacked The new hadAttacked
     */
    public void setHadAttacked(boolean hadAttacked) {
        this.hadAttacked = hadAttacked;
    }

    /**
     * Switch the card position
     */
    public void rotate() {
        // Create rotate transition
        if (rotate.getStatus() == Animation.Status.RUNNING)
            return;
        rotate.setAxis(Rotate.Z_AXIS);
        rotate.setByAngle((this.isAttack) ? (90) : (-90));
        rotate.setCycleCount(1);
        rotate.setDuration(Duration.millis(500));
        rotate.setNode(this.cardAncPane);
        rotate.play();
        // Update card position
        this.isAttack = !this.isAttack;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void init() {
        super.init();
        // If defense rotate 90
        if (!this.isAttack)
            this.cardAncPane.setRotate(this.cardAncPane.getRotate() + 90);
    }

    /**
     * {@inheritDoc}
     */
    @Override @FXML
    public void initialize(URL url, ResourceBundle resources) {
        this.init();
    }
}
