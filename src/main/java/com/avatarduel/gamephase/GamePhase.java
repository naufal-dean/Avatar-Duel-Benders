package com.avatarduel.gamephase;

import com.avatarduel.controller.MainController;

public interface GamePhase {
    /**
     * Start the phase
     * @param mainController The MainController for the UI
     */
    void startPhase(MainController mainController);

    /**
     * End the phase
     * @param mainController The MainController for the UI
     */
    void endPhase(MainController mainController);
}
