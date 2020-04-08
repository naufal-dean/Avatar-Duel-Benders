package com.avatarduel.controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.animation.Animation;
import javafx.animation.RotateTransition;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

import com.avatarduel.model.Card;
import com.avatarduel.model.Character;

public class SummonedCharacterCardController extends CardController {
    /**
     * is monster in attack position
     */
    boolean isAttack;
    /**
     * Rotate transition object
     */
    RotateTransition rotate;
    /**
     * is first time monster has summoned
     */
    boolean isFirstTime;
    /**
     * Constructor
     * @param card The Card
     * @param isAttack Is summoned in attack position
     */
    public SummonedCharacterCardController(Character card, boolean isAttack) {
        super(card);
        this.isAttack = isAttack;
        rotate = new RotateTransition();
        isFirstTime=true;
    }

    /**
     * Replace with new card then render
     * @param card The Card
     * @param isAttack Is summoned in attack position
     */
    public void setCard(Character card, boolean isAttack) {
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
        rotate = new RotateTransition();
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
    @Override @FXML
    public void initialize(URL url, ResourceBundle resources) {
        // Set on click handler
        this.cardAncPane.addEventFilter(MouseEvent.MOUSE_RELEASED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                rotate();
            }
        });
        this.init();
    }
}
