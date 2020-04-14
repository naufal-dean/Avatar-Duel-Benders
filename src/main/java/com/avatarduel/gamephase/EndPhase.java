package com.avatarduel.gamephase;

import com.avatarduel.controller.MainController;
import com.avatarduel.gameutils.GameStatus;

public class EndPhase implements GamePhase {
    /**
     * Class singleton instance
     */
    private static EndPhase endPhase;

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
        GameStatus.getGameStatus().nextTurn();
        // Reset land placed counter
        MainPhase.getMainPhase().resetLandPlacedCounter();
        // Flip card in both hand
        mainController.getHandBottomController().flipCardInHand();
        mainController.getHandTopController().flipCardInHand();
    }

    /**
     * End end phase
     * @param mainController The MainController for the UI
     */
    @Override
    public void endPhase(MainController mainController) {
        DrawPhase.getDrawPhase().startPhase(mainController);
    }
}
