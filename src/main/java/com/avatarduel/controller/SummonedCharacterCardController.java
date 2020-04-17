package com.avatarduel.controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.animation.Animation;
import javafx.animation.RotateTransition;
import javafx.fxml.FXML;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

import com.avatarduel.model.*;
import com.avatarduel.model.Character;

public class SummonedCharacterCardController extends SummonedCardController {
    /**
     * Is monster in attack position
     */
    private boolean isAttack;
    /**
     * Is char card had attacked
     */
    private boolean hadAttacked;
    /**
     * Attached skill aura controller
     */
    private List<SummonedSkillCardController> attachedAuraControllerList;
    /**
     * Is char card powered up
     */
    private List<SummonedSkillCardController> attachedPowerUpControllerList;
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
        this.hadAttacked = true;
        this.attachedAuraControllerList = new ArrayList<>();
        this.attachedPowerUpControllerList = new ArrayList<>();
        this.rotate = new RotateTransition();
    }

    /**
     * Return this card attack / defense in field based on card position
     * @return This card attack / defense in field
     */
    public int getCardValue() {
        Character character = (Character) this.card;
        int attack = character.getAttack();
        int defense = character.getAttack();
        for (SummonedSkillCardController ssCardController : this.attachedAuraControllerList) {
            SkillAura skillAura = (SkillAura) ssCardController.getCard();
            attack += skillAura.getAttack();
            defense += skillAura.getDefense();
        }
        return (this.isAttack) ? (attack) : (defense);
    }

    /**
     * Getter for isAttack status
     * @return this.isAttack
     */
    public boolean getIsAttack() {
        return this.isAttack;
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
     * Getter for attachedAuraList
     * @return this.attachedAuraList
     */
    public List<SummonedSkillCardController> getAttachedAuraControllerList() {
        return this.attachedAuraControllerList;
    }

    /**
     * Getter for attachedPowerUpList
     * @return this.attachedPowerUpList
     */
    public List<SummonedSkillCardController> getAttachedPowerUpControllerList() {
        return this.attachedPowerUpControllerList;
    }

    /**
     * Add skill attached
     * @param ssCardController The new skill controller to be attached
     */
    public void addSkillCard(SummonedSkillCardController ssCardController) {
        if (ssCardController.getCard() instanceof SkillAura)
            this.attachedAuraControllerList.add(ssCardController);
        else if (ssCardController.getCard() instanceof SkillPowerUp)
            this.attachedPowerUpControllerList.add(ssCardController);
    }

    /**
     * Remove skill attached
     * @param ssCardController The skill controller to be removed
     */
    public void removeSkillCard(SummonedSkillCardController ssCardController) {
        if (ssCardController.getCard() instanceof SkillAura)
            this.attachedAuraControllerList.remove(ssCardController);
        else if (ssCardController.getCard() instanceof SkillPowerUp)
            this.attachedPowerUpControllerList.remove(ssCardController);
    }

    /**
     * Is summoned char card has power up
     * @return True if has power up, false otherwise
     */
    public boolean isPoweredUp() {
        return this.attachedPowerUpControllerList.size() > 0;
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
