package com.avatarduel.gamephase;

import com.avatarduel.controller.MainController;
import com.avatarduel.gameutils.GameDeck;
import com.avatarduel.gameutils.GameStatus;
import com.avatarduel.model.Player;

import java.util.concurrent.TimeUnit;

public class DrawPhase implements GamePhase {
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
     * @param mainController The MainController for the UI
     */
    @Override
    public void startPhase(MainController mainController) {
        // TODO: implement
    }

    /**
     * End draw phase
     * @param mainController The MainController for the UI
     */
    @Override
    public void endPhase(MainController mainController) {
        MainPhase.getMainPhase().startPhase(mainController);
    }
}
