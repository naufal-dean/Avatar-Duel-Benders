package com.avatarduel.gamephase;

import javafx.event.EventHandler;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

import com.avatarduel.controller.HandCardController;
import com.avatarduel.controller.HandController;
import com.avatarduel.controller.MainController;
import com.avatarduel.gameutils.GameDeck;
import com.avatarduel.gameutils.GameStatus;
import com.avatarduel.model.Player;

public class DrawPhase implements GamePhase {
    /**
     * Class singleton instance
     */
    private static DrawPhase drawPhase;
    /**
     * Shadow effect
     */
    DropShadow hoverShadow;
    /**
     * Constructor
     */
    private DrawPhase() {
        // Setup hover glow effect
        this.hoverShadow = new DropShadow();
        this.hoverShadow.setColor(Color.RED);
        this.hoverShadow.setWidth(70);
        this.hoverShadow.setHeight(70);
    }

    /**
     * Getter for class singleton instance
     * @return Class singleton instance
     */
    public static DrawPhase getDrawPhase() {
        if (drawPhase == null)
            drawPhase = new DrawPhase();
        return drawPhase;
    }

    /**
     * Start draw phase
     * @param mainController The MainController for the UI
     */
    @Override
    public void startPhase(MainController mainController) {
        // Add card to hand
        GameDeck deck = GameStatus.getGameStatus().getOurDeck();
        Player activePlayer = GameStatus.getGameStatus().getGameActivePlayer();
        HandController handController = mainController.getHandControllerMap().get(activePlayer);
        try {
            handController.addCardOnHand(deck.draw(), activePlayer);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        // Add hover handler to the last added card
        HandCardController handCardController = handController.getCardControllerList().get(
                                                                    handController.getCardControllerList().size() - 1);
        handCardController.getCardAncPane().onMouseEnteredProperty().set((EventHandler<MouseEvent>) (MouseEvent e) -> {
            if (GameStatus.getGameStatus().getGamePhase() == Phase.MAIN) {
                handCardController.getCardAncPane().setEffect(hoverShadow);
                // TODO: Add show card detail
            }
        });
        handCardController.getCardAncPane().onMouseExitedProperty().set((EventHandler<MouseEvent>) (MouseEvent e) -> {
            if (GameStatus.getGameStatus().getGamePhase() == Phase.MAIN) {
                if (handCardController != handController.getActiveHandCard())
                    handCardController.getCardAncPane().setEffect(null);
            }
        });
    }

    /**
     * End draw phase
     * @param mainController The MainController for the UI
     */
    @Override
    public void endPhase(MainController mainController) {
        // Clear glow effect from hand card if any
        Player activePlayer = GameStatus.getGameStatus().getGameActivePlayer();
        HandController handController = mainController.getHandControllerMap().get(activePlayer);
        for (HandCardController handCardController : handController.getCardControllerList())
            handCardController.getCardAncPane().setEffect(null);
        // Proceed to main phase
        MainPhase.getMainPhase().startPhase(mainController);
    }
}
