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
        Player activePlayer = GameStatus.getGameStatus().getGameActivePlayer();
        // Update game status
        GameStatus.getGameStatus().setGamePhase(Phase.DRAW);

        // Draw card
        GameDeck deck = GameStatus.getGameStatus().getGameDeckMap().get(activePlayer);
        HandController handController = mainController.getHandControllerMap().get(activePlayer);
        try {
            if (handController.getCardControllerList().size() < 10) // TODO: discard 1 card if card size = 10 at the end of end phase
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
        // Proceed to main phase
        MainPhase.getMainPhase().startPhase(mainController);
    }
}
