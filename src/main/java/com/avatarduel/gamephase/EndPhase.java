package com.avatarduel.gamephase;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import com.avatarduel.controller.CardBackController;
import com.avatarduel.controller.HandCardController;
import com.avatarduel.controller.HandController;
import com.avatarduel.controller.MainController;
import com.avatarduel.gameutils.GameStatus;
import com.avatarduel.model.Player;

public class EndPhase implements GamePhase {
    /**
     * Class singleton instance
     */
    private static EndPhase endPhase;
    /**
     * Connector
     */
    ChangeListener<Boolean> cardDiscardedHandler;

    /**
     * Constructor
     */
    private EndPhase() {}

    /**
     * Getter for class singleton instance
     * @return Class singleton instance
     */
    public static EndPhase getEndPhase() {
        if (endPhase == null)
            endPhase = new EndPhase();
        return endPhase;
    }

    /**
     * Start end phase
     * @param mainController The MainController for the UI
     */
    @Override
    public void startPhase(MainController mainController) {
        // Update game status
        GameStatus.getGameStatus().setGamePhase(Phase.END);
        // Check if card > 9
        Player activePlayer = GameStatus.getGameStatus().getGameActivePlayer();
        HandController handController = mainController.getHandControllerMap().get(activePlayer);
        if (handController.getCardControllerList().size() > 9) {
            // Discard card in hand if card count > 9
            handController.turnOnDiscardCardPeriodSignal();
            this.cardDiscardedHandler = new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue observable, Boolean oldValue, Boolean newValue) {
                    if (oldValue == true && newValue == false) {
                        handController.getDiscardCardPeriodSignalProperty().removeListener(cardDiscardedHandler);
                        endPhase(mainController);
                    }
                }
            };
            handController.getDiscardCardPeriodSignalProperty().addListener(this.cardDiscardedHandler);
        } else {
            // End phase
            this.endPhase(mainController);
        }
    }

    /**
     * End end phase, next turn if not game over yet
     * @param mainController The MainController for the UI
     */
    @Override
    public void endPhase(MainController mainController) {
        // Execute flip animation in both player hands
        // Pre calculation
        Player activePlayer = GameStatus.getGameStatus().getGameActivePlayer();
        HandController actHandController = mainController.getHandControllerMap().get(activePlayer);
        CardBackController actLastCB = null;
        if (actHandController.getCardBackControllerList().size() > 0)
            actLastCB = actHandController.getCardBackControllerList().get(0);

        Player enemyPlayer = GameStatus.getGameStatus().getGameNonActivePlayer();
        HandController enHandController = mainController.getHandControllerMap().get(enemyPlayer);
        HandCardController enLastC = null;
        if (enHandController.getCardControllerList().size() > 0)
            enLastC = enHandController.getCardControllerList().get(0);

        // Flip active player hand if any
        if (actLastCB != null) {
            actHandController.flipCardInHand();
            HandCardController tempEnLastC = enLastC;
            actLastCB.getFlipUp().setOnFinished(e -> {
                // Flip enemy hand if any
                if (tempEnLastC != null) {
                    enHandController.flipCardInHand();
                    tempEnLastC.getFlipUp().setOnFinished(innerE -> {
                        // If not game over yet, next turn
                        GameStatus.getGameStatus().nextTurn();
                        DrawPhase.getDrawPhase().startPhase(mainController);
                    });
                } else {
                    GameStatus.getGameStatus().nextTurn();
                    DrawPhase.getDrawPhase().startPhase(mainController);
                }
            });
        } else {
            // Flip enemy hand if any
            if (enLastC != null) {
                enHandController.flipCardInHand();
                enLastC.getFlipUp().setOnFinished(e -> {
                    // If not game over yet, next turn
                    GameStatus.getGameStatus().nextTurn();
                    DrawPhase.getDrawPhase().startPhase(mainController);
                });
            } else {
                GameStatus.getGameStatus().nextTurn();
                DrawPhase.getDrawPhase().startPhase(mainController);
            }
        }
    }

    /**
     * Game over
     */
    public void gameEnd() {
        // TODO: render game winner
        GameStatus.getGameStatus().setGamePhase(Phase.END);
        System.out.println("game end, winner: " + GameStatus.getGameStatus().getGameWinner());
    }
}
