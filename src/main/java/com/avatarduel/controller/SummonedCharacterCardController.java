package com.avatarduel.controller;

import java.net.URL;
import java.util.ResourceBundle;

import com.avatarduel.model.Player;
import javafx.animation.Animation;
import javafx.animation.RotateTransition;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

import com.avatarduel.model.Card;
import com.avatarduel.model.Character;

public class SummonedCharacterCardController extends CardController {
    /**
     * is monster in attack position
     */
    private boolean isAttack;
    /**
     * is first time monster has summoned
     */
    private boolean isFirstTime;
    /**
     * Card owner
     */
    private Player owner;
    /**
     * Rotate transition object
     */
    private RotateTransition rotate;

    /**
     * Constructor
     * @param card The Card
     * @param owner The owner of the card
     * @param isAttack Is summoned in attack position
     */
    public SummonedCharacterCardController(Card card, Player owner, boolean isAttack) {
        super(card);
        this.owner = owner;
        this.isAttack = isAttack;
        rotate = new RotateTransition();
        isFirstTime = true;
    }

    /**
     * Replace with new card then render
     * @param card The Card
     * @param isAttack Is summoned in attack position
     */
    public void setCard(Card card, boolean isAttack) {
        super.setCard(card);
        this.isAttack = isAttack;
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
        // Rotate card if owned by Player.TOP
        if (this.owner == Player.TOP)
            this.cardAncPane.setRotate(180);
        if (!this.isAttack)
            this.cardAncPane.setRotate(90);
    }

    /**
     * {@inheritDoc}
     */
    @Override @FXML
    public void initialize(URL url, ResourceBundle resources) {
        // Set on click handler
        this.cardAncPane.onMouseClickedProperty().set((EventHandler<MouseEvent>) (MouseEvent e) -> {
            if (e.getButton() == MouseButton.SECONDARY) {
                this.rotate();
            }
        });
        this.init();
    }
}
