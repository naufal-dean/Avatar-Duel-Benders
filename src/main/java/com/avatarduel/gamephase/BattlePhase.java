package com.avatarduel.gamephase;

public class BattlePhase {
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
     */
    public void startBattlePhase() {
        // TODO: implement
    }
}
