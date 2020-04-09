package com.avatarduel.gamephase;

import com.avatarduel.controller.DeckController;
import com.avatarduel.model.Player;

import java.util.HashMap;

public class DrawPhase {
    /**
     * Class singleton
     */
    private DrawPhase drawPhase;

    private DrawPhase() {
    }

    /**
     * Getter for class instance
     * @return Class instance
     */
    public DrawPhase getDrawPhase() {
        return this.drawPhase;
    }


}
