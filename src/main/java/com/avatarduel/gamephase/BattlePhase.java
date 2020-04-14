package com.avatarduel.gamephase;

import com.avatarduel.controller.MainController;

public class BattlePhase implements GamePhase {
    /**
     * Class singleton instance
     */
    private static BattlePhase battlePhase;

    /**
     * Constructor
     */
    private BattlePhase() {
    }

    /**
     * Getter for class singleton instance
     * @return Class singleton instance
     */
    public static BattlePhase getBattlePhase() {
        if (battlePhase == null)
            battlePhase = new BattlePhase();
        return battlePhase;
    }

    /**
     * Start battle phase
     * @param mainController The MainController for the UI
     */
    @Override
    public void startPhase(MainController mainController) {
        // TODO: implement
    }

    /**
     * End battle phase
     * @param mainController The MainController for the UI
     */
    @Override
    public void endPhase(MainController mainController) {
        EndPhase.getEndPhase().startPhase(mainController);
    }
}
