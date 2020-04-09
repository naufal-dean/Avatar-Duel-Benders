package com.avatarduel.gamephase;

import com.avatarduel.controller.DeckController;
import com.avatarduel.gameutils.GameStatus;
import com.avatarduel.model.Player;

import java.util.HashMap;

public class DrawPhase {
    /**
     * Class singleton instance
     */
    private static DrawPhase drawPhase;

    /**
     * Constructor
     */
    private DrawPhase() {
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
     */
    public void startDrawPhase() {
        // TODO: implement
    }
}
