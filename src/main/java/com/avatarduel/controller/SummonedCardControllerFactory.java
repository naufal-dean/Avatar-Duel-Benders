package com.avatarduel.controller;

import javafx.event.EventHandler;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import com.avatarduel.gameassets.GameDropShadow;
import com.avatarduel.gamephase.Phase;
import com.avatarduel.gameutils.GameStatus;
import com.avatarduel.model.*;
import com.avatarduel.model.Character;

public class SummonedCardControllerFactory {
    public static SummonedCardController setupSummonedCardController(FieldController fieldController, SummonedCardController cardController) {
        Card card = cardController.getCard();
        // Add on click handler
        if (card instanceof Character) {
            setupSummCharOnClickHandler(fieldController, (SummonedCharacterCardController) cardController);
        } else if (card instanceof SkillAura || card instanceof SkillPowerUp) {
            setupSummSkillOnClickHandler(fieldController, (SummonedSkillCardController) cardController);
        }
        // Add on hover handler
        setupSummCardHoverHandler(fieldController, cardController);
        // Return
        return cardController;
    }

    private static void setupSummCharOnClickHandler(FieldController fieldController, SummonedCharacterCardController scCardController) {
        int x = scCardController.getX();
        int y = scCardController.getY();
        DropShadow shadowYellow = GameDropShadow.getGameDropShadow().getShadowYellowField();
        // Mouse clicked
        scCardController.getCardAncPane().onMouseClickedProperty().set((EventHandler<MouseEvent>) (MouseEvent e) -> {
            if (GameStatus.getGameStatus().getGamePhase() == Phase.MAIN) {
                // Main phase
                if (e.getButton() == MouseButton.PRIMARY && fieldController.getActiveSummCardHandler().get(x).get(y).get()) {
                    if (fieldController.getAttachSkillPeriodSignalProperty().get()) {
                        // Attach skill
                        if (fieldController.getSkillCardControllerToBeAttached().getCard() instanceof SkillDestroy) {
                            fieldController.removeCardFromField(scCardController.getX(), scCardController.getY());
                            fieldController.removeCardFromField(fieldController.getSkillCardControllerToBeAttached().getX(), fieldController.getSkillCardControllerToBeAttached().getY());
                        } else {
                            scCardController.addSkillCard(fieldController.getSkillCardControllerToBeAttached());
                            fieldController.getSkillCardControllerToBeAttached().setTargetX(x);
                            fieldController.getSkillCardControllerToBeAttached().setTargetY(y);
                        }
                        fieldController.setSkillCardControllerToBeAttached(null);
                        fieldController.turnOffAttachSkillPeriodSignal();
                    }
                } else if (e.getButton() == MouseButton.SECONDARY &&
                        GameStatus.getGameStatus().getGameActivePlayer() == scCardController.getOwner() &&
                        !fieldController.getDisableCardClick()) {
                    scCardController.rotate();  // Rotate card
                }
            } else if (GameStatus.getGameStatus().getGamePhase() == Phase.BATTLE) {
                // Battle phase
                if (e.getButton() == MouseButton.PRIMARY &&
                        GameStatus.getGameStatus().getGameActivePlayer() == scCardController.getOwner() &&
                        scCardController.getIsAttack() &&
                        !(scCardController.getHadAttacked())) {
                    fieldController.onSummonedCharCardClickHandler(scCardController);
                } else if (e.getButton() == MouseButton.PRIMARY &&
                        GameStatus.getGameStatus().getGameActivePlayer() != scCardController.getOwner() &&
                        fieldController.getActiveSummCardHandler().get(x).get(y).get()) {
                    fieldController.initBattle(scCardController);
                }
            }
        });
        // Add event listener
        fieldController.getAttachSkillPeriodSignalProperty().addListener((observable, oldValue, newValue) -> {
            if (GameStatus.getGameStatus().getGamePhase() == Phase.MAIN) {
                if (oldValue == false && newValue == true) {
                    scCardController.getCardAncPane().setEffect(shadowYellow);
                    fieldController.getActiveSummCardHandler().get(x).get(y).setValue(true);
                } else {
                    scCardController.getCardAncPane().setEffect(null);
                    fieldController.getActiveSummCardHandler().get(x).get(y).setValue(false);
                }
            }
        });
        fieldController.getActiveFieldCardSetSignalProperty().addListener((observable, oldValue, newValue) -> {
            if (GameStatus.getGameStatus().getGamePhase() == Phase.BATTLE) {
                if (GameStatus.getGameStatus().getGameActivePlayer() != scCardController.getOwner()) {
                    if (oldValue == false && newValue == true &&
                            fieldController.getActiveFieldCardController().getCardValue() > scCardController.getCardValue()) {
                        scCardController.getCardAncPane().setEffect(shadowYellow);
                        fieldController.getActiveSummCardHandler().get(x).get(y).setValue(true);
                    } else {
                        scCardController.getCardAncPane().setEffect(null);
                        fieldController.getActiveSummCardHandler().get(x).get(y).setValue(false);
                    }
                }
            }
        });
    }

    private static void setupSummSkillOnClickHandler(FieldController fieldController, SummonedSkillCardController ssCardController) {
        ssCardController.getCardAncPane().onMouseClickedProperty().set((EventHandler<MouseEvent>) (MouseEvent e) -> {
            if (GameStatus.getGameStatus().getGamePhase() == Phase.MAIN) {
                // Set on right click remove card from field
                if (e.getButton() == MouseButton.SECONDARY &&
                        GameStatus.getGameStatus().getGameActivePlayer() == ssCardController.getOwner() &&
                        !fieldController.getDisableCardClick()) {
                    fieldController.removeCardFromField(ssCardController.getX(), ssCardController.getY()); // Remove card
                }
            }
        });
    }

    private static void setupSummCardHoverHandler(FieldController fieldController, SummonedCardController cardController) {
        Card card = cardController.getCard();
        int x = cardController.getX();
        int y = cardController.getY();
        DropShadow shadowRed = GameDropShadow.getGameDropShadow().getShadowRedField();
        DropShadow shadowYellow = GameDropShadow.getGameDropShadow().getShadowYellowField();
        DropShadow shadowGreen = GameDropShadow.getGameDropShadow().getShadowGreenField();
        // Mouse entered
        cardController.getCardAncPane().onMouseEnteredProperty().set((EventHandler<MouseEvent>) (MouseEvent e) -> {
            if (GameStatus.getGameStatus().getGamePhase() == Phase.MAIN) {
                if (fieldController.getAttachSkillPeriodSignalProperty().get() && fieldController.getSkillCardControllerToBeAttached() != null) {
                    if (card instanceof Character) {
                        Card c = fieldController.getSkillCardControllerToBeAttached().getCard();
                        if (c instanceof SkillAura)
                            if (((SkillAura) c).getAttack() + ((SkillAura) c).getDefense() >= 0)
                                cardController.getCardAncPane().setEffect(shadowGreen);
                            else
                                cardController.getCardAncPane().setEffect(shadowRed);
                        else if (c instanceof SkillPowerUp)
                            cardController.getCardAncPane().setEffect(shadowGreen);
                        else
                            cardController.getCardAncPane().setEffect(shadowRed);
                    }
                } else {
                    cardController.getCardAncPane().setEffect(shadowYellow);
                }
            } else if (GameStatus.getGameStatus().getGamePhase() == Phase.BATTLE) {
                if (card instanceof Character) {
                    if (!((SummonedCharacterCardController) cardController).getHadAttacked() &&
                            ((SummonedCharacterCardController) cardController).getIsAttack())
                        cardController.getCardAncPane().setEffect(shadowRed);
                    else if (fieldController.getActiveSummCardHandler().get(x).get(y).get())
                        cardController.getCardAncPane().setEffect(shadowRed);
                }
            }
            fieldController.getCardDetailsController().setCard(cardController.getCard());
            if (card instanceof Character)
                ((SummonedCharacterCardController) cardController).setShowAttachedSkill(true);
        });
        // Mouse exited
        cardController.getCardAncPane().onMouseExitedProperty().set((EventHandler<MouseEvent>) (MouseEvent e) -> {
            if (GameStatus.getGameStatus().getGamePhase() == Phase.MAIN) {
                if (fieldController.getAttachSkillPeriodSignalProperty().get() && card instanceof Character)
                    cardController.getCardAncPane().setEffect(shadowYellow);
                else
                    cardController.getCardAncPane().setEffect(null);
            } else if (GameStatus.getGameStatus().getGamePhase() == Phase.BATTLE) {
                if (card instanceof Character) {
                    if (cardController != fieldController.getActiveFieldCardController() && !fieldController.getActiveSummCardHandler().get(x).get(y).get())
                        cardController.getCardAncPane().setEffect(null);
                    else if (fieldController.getActiveSummCardHandler().get(x).get(y).get())
                        cardController.getCardAncPane().setEffect(shadowYellow);
                }
            }
            fieldController.getCardDetailsController().removeCard();
            if (card instanceof Character)
                ((SummonedCharacterCardController) cardController).setShowAttachedSkill(false);
        });
    }
}
