package com.avatarduel.gamephase;

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
     * Constructor
     */
    private DrawPhase() {}

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
        // Update game status and phase button display
        GameStatus.getGameStatus().setGamePhase(Phase.DRAW);
        mainController.getPhaseController().init();

        // Draw card
        Player activePlayer = GameStatus.getGameStatus().getGameActivePlayer();
        GameDeck deck = GameStatus.getGameStatus().getGameDeckMap().get(activePlayer);
        // If no cards left, end game
        if (deck.getCardQuantity() == 0) {
            GameStatus.getGameStatus().setGameWinner(GameStatus.getGameStatus().getGameNonActivePlayer());
            this.endPhase(mainController);
            return;
        }
        // Else draw card
        HandController handController = mainController.getHandControllerMap().get(activePlayer);
        try {
            if (handController.getCardControllerList().size() < 10)
                handController.addCardOnHand(deck.draw(), activePlayer);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        // Update game deck display
        mainController.getDeckControllerMap().get(activePlayer).init();

        // Reset power
        GameStatus.getGameStatus().getGamePowerMap().get(activePlayer).resetCurrPower();
        // Update power display
        mainController.getPowerControllerMap().get(activePlayer).init();

        // Reset hadAttacked status on summoned char
        mainController.getFieldController().resetSummCardHadAttackedStatus();

        // End phase
        this.endPhase(mainController);
    }

    /**
     * End draw phase
     * @param mainController The MainController for the UI
     */
    @Override
    public void endPhase(MainController mainController) {
        // End draw phase
        if (GameStatus.getGameStatus().getGameWinner() == null) {
            // Proceed to main phase
            MainPhase.getMainPhase().startPhase(mainController);
        } else {
            // Render game winner
            EndPhase.getEndPhase().gameEnd(mainController);
        }
    }
}
