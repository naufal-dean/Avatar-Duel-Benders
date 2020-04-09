package com.avatarduel.gamephase;

import com.avatarduel.gameutils.GameStatus;

public class EndPhase {
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
     */
    public void startEndPhase() {
        // Update game status
        GameStatus.getGameStatus().nextTurn();
        // Reset land placed counter
        MainPhase.getMainPhase().resetLandPlacedCounter();
    }
}
