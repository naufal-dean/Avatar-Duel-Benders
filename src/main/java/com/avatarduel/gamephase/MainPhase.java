package com.avatarduel.gamephase;

import java.io.IOException;

import javafx.event.EventHandler;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

import com.avatarduel.controller.*;
import com.avatarduel.gameutils.GameStatus;
import com.avatarduel.model.*;
import com.avatarduel.model.Character;

public class MainPhase implements GamePhase {
    /**
     * Class singleton instance
     */
    private static MainPhase mainPhase;
    /**
     * Land card placed counter
     */
    private int landCardPlaced;
    /**
     * Shadow effect
     */
    private DropShadow handHoverShadow, cellAvailableShadow, cellHoverShadow;

    /**
     * Constructor
     */
    private MainPhase() {
        this.landCardPlaced = 0;
        // Hand hover shadow
        this.handHoverShadow = new DropShadow();
        this.handHoverShadow.setColor(Color.RED);
        this.handHoverShadow.setWidth(70);
        this.handHoverShadow.setHeight(70);
        // Cell available shadow
        this.cellAvailableShadow = new DropShadow();
        this.cellAvailableShadow.setColor(Color.YELLOW);
        this.cellAvailableShadow.setWidth(50);
        this.cellAvailableShadow.setHeight(50);
        this.cellAvailableShadow.setSpread(0.9);
        // Cell hover shadow
        this.cellHoverShadow = new DropShadow();
        this.cellHoverShadow.setColor(Color.RED);
        this.cellHoverShadow.setWidth(50);
        this.cellHoverShadow.setHeight(50);
        this.cellHoverShadow.setSpread(0.9);
    }

    /**
     * Getter for class singleton instance
     * @return Class singleton instance
     */
    public static MainPhase getMainPhase() {
        if (mainPhase == null)
            mainPhase = new MainPhase();
        return mainPhase;
    }

    /**
     * Start main phase
     * @param mainController The MainController for the UI
     */
    @Override
    public void startPhase(MainController mainController) {
        this.landCardPlaced = 0;
        // Update game status
        GameStatus.getGameStatus().setGamePhase(Phase.MAIN);
        // Add event handler to hand card and then to field
        FieldController fieldController = mainController.getFieldController();
        Player activePlayer = GameStatus.getGameStatus().getGameActivePlayer();
        HandController handController = mainController.getHandControllerMap().get(activePlayer);
        this.addHandEventHandler(fieldController, handController);
    }

    /**
     * End the main phase
     * @param mainController The MainController for the UI
     */
    @Override
    public void endPhase(MainController mainController) {
        // Clear any handler that cannot be reused
        Player activePlayer = GameStatus.getGameStatus().getGameActivePlayer();
        HandController handController = mainController.getHandControllerMap().get(activePlayer);
        this.clearHandEventHandler(handController);
        this.clearFieldEventHandler(mainController.getFieldController());
        // Proceed to battle phase
        BattlePhase.getBattlePhase().endPhase(mainController);
    }

    /**
     * Summon character card
     * @param handController The HandController
     * @param isAttack Is summon in attack position
     * @param col Target column
     * @param fieldController The FieldController
     */
    public void summonCharCard(HandController handController, boolean isAttack, int col, FieldController fieldController) {
        int row = (GameStatus.getGameStatus().getGameActivePlayer() == Player.BOTTOM) ? (FieldController.CHAR_ROW_BOT) : (FieldController.CHAR_ROW_TOP);
        try {
            fieldController.setCardOnField(handController.getActiveHandCard().getCard(),
                                           GameStatus.getGameStatus().getGameActivePlayer(), isAttack, col, row);
            handController.removeCardOnHand(handController.getActiveHandCard());
            handController.removeActiveHandCard();
            this.clearFieldEventHandler(fieldController);
        } catch (IOException e) {
            System.out.println("Summon char card failed: " + e);
        }
    }

    /**
     * Summon skill card
     * @param handController The HandController
     * @param col Target column
     * @param fieldController The FieldController
     */
    public void summonSkillCard(HandController handController, int col, FieldController fieldController) {
        int row = (GameStatus.getGameStatus().getGameActivePlayer() == Player.BOTTOM) ? (FieldController.SKILL_ROW_BOT) : (FieldController.SKILL_ROW_TOP);
        try {
            fieldController.setCardOnField(handController.getActiveHandCard().getCard(),
                                           GameStatus.getGameStatus().getGameActivePlayer(), true, col, row);
            handController.removeCardOnHand(handController.getActiveHandCard());
            handController.removeActiveHandCard();
            this.clearFieldEventHandler(fieldController);
        } catch (IOException e) {
            System.out.println("Summon char card failed: " + e);
        }
    }

    /**
     * Summon land card
     * @param card The land card
     * @param col Target column
     * @param powerController The PowerController
     */
    public void summonLandCard(Land card, int col, PowerController powerController) { // TODO: implement PowerController as param
        if (this.landCardPlaced > 0)
            return;
        this.landCardPlaced++;
        GameStatus.getGameStatus().getOurPower().incMaxPower(card.getElement());
        powerController.render();
    }

    /**
     * Add click event handler to hand card
     * @param fieldController The FieldController
     * @param handController The HandController
     */
    public void addHandEventHandler(FieldController fieldController, HandController handController) {
        for (HandCardController handCardController : handController.getCardControllerList()) {
            // Only add to the new hand card
            if (handCardController.getCardAncPane().onMouseClickedProperty().get() == null) {
                // On mouse clicked handler
                handCardController.getCardAncPane().onMouseClickedProperty().set((EventHandler<MouseEvent>) (MouseEvent e) -> {
                    if (e.getButton() == MouseButton.PRIMARY && GameStatus.getGameStatus().getGamePhase() == Phase.MAIN) {
                        // Set active card in hand
                        handController.setActiveCard(handCardController);
                        // Add event handler to field
                        if (handController.getActiveHandCard() != null)
                            this.addFieldEventHandler(fieldController, handController);
                        else
                            this.clearFieldEventHandler(fieldController);
                    }
                });
                // On mouse entered handler
                handCardController.getCardAncPane().onMouseEnteredProperty().set((EventHandler<MouseEvent>) (MouseEvent e) -> {
                    if (GameStatus.getGameStatus().getGamePhase() == Phase.MAIN) {
                        handCardController.getCardAncPane().setEffect(this.handHoverShadow);
                        // TODO: Add show card detail
                    }
                });
                // On mouse exited handler
                handCardController.getCardAncPane().onMouseExitedProperty().set((EventHandler<MouseEvent>) (MouseEvent e) -> {
                    if (GameStatus.getGameStatus().getGamePhase() == Phase.MAIN) {
                        if (handCardController != handController.getActiveHandCard())
                            handCardController.getCardAncPane().setEffect(null);
                    }
                });
            }
        }
    }

    /**
     * Clear event handler from hand
     * @param handController The HandController
     */
    public void clearHandEventHandler(HandController handController) {
        // Clear glow effect from hand card if any
        for (HandCardController handCardController : handController.getCardControllerList())
            handCardController.getCardAncPane().setEffect(null);
        // Remove active hand card
        handController.removeActiveHandCard();
    }

    /**
     * Add event handler to field
     * @param fieldController The FieldController
     * @param handController The HandController
     */
    public void addFieldEventHandler(FieldController fieldController, HandController handController) {
        // Clear previous event handler
        this.clearFieldEventHandler(fieldController);
        // If land card return
        CardType cardType = handController.getActiveHandCard().getCard().getCardType();
        if (cardType == CardType.LAND)
            return;

        // Init data needed
        Player activePlayer = GameStatus.getGameStatus().getGameActivePlayer();
        int row;
        if (cardType == CardType.CHARACTER && activePlayer == Player.BOTTOM) {
            row = FieldController.CHAR_ROW_BOT;
        } else if (cardType == CardType.CHARACTER && activePlayer == Player.TOP) {
            row = FieldController.CHAR_ROW_TOP;
        } else if (cardType == CardType.SKILL && activePlayer == Player.BOTTOM) {
            row = FieldController.SKILL_ROW_BOT;
        } else {
            row = FieldController.SKILL_ROW_TOP;
        }

        for (int x = 0; x < 6; x++) {
            if (fieldController.getCardController(x, row) == null) {
                int col = x;
                // On mouse clicked handler
                fieldController.getEmptyCell(col, row).onMouseClickedProperty().set((EventHandler<MouseEvent>) (MouseEvent e) -> {
                    Card card = handController.getActiveHandCard().getCard();
                    if (cardType == CardType.CHARACTER) {
                        Character character = (Character) handController.getActiveHandCard().getCard();
                        if (e.getButton() == MouseButton.PRIMARY)
                            this.summonCharCard(handController, true, col, fieldController);
                        else if (e.getButton() == MouseButton.SECONDARY)
                            this.summonCharCard(handController, false, col, fieldController);
                    } else if (cardType == CardType.SKILL && ((Skill) card).getEffect() == Effect.AURA) {
                        SkillAura skillAura = (SkillAura) handController.getActiveHandCard().getCard();
                        if (e.getButton() == MouseButton.PRIMARY)
                            this.summonSkillCard(handController, col, fieldController);
                    }
                });
                // On mouse entered handler
                fieldController.getEmptyCell(col, row).onMouseEnteredProperty().set((EventHandler<MouseEvent>) (MouseEvent e) -> {
                    fieldController.getEmptyCell(col, row).setEffect(this.cellHoverShadow);
                    // TODO: Add show card detail
                });
                // On mouse exited handler
                fieldController.getEmptyCell(col, row).onMouseExitedProperty().set((EventHandler<MouseEvent>) (MouseEvent e) -> {
                    fieldController.getEmptyCell(col, row).setEffect(this.cellAvailableShadow);
                });

                // Field effect
                fieldController.getEmptyCell(col, row).setEffect(this.cellAvailableShadow);
            }
        }
    }

    /**
     * Clear event handler from field
     * @param fieldController The FieldController
     */
    public void clearFieldEventHandler(FieldController fieldController) {
        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 6; x++) {
                fieldController.getEmptyCell(x, y).onMouseClickedProperty().set(null);
                fieldController.getEmptyCell(x, y).onMouseEnteredProperty().set(null);
                fieldController.getEmptyCell(x, y).onMouseExitedProperty().set(null);
                fieldController.getEmptyCell(x, y).setEffect(null);
            }
        }
    }
}
