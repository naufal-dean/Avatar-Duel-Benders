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
        // Update game status
        GameStatus.getGameStatus().setGamePhase(Phase.DRAW);
        // Add card to hand
        GameDeck deck = GameStatus.getGameStatus().getOurDeck();
        Player activePlayer = GameStatus.getGameStatus().getGameActivePlayer();
        HandController handController = mainController.getHandControllerMap().get(activePlayer);
        try {
            handController.addCardOnHand(deck.draw(), activePlayer);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
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
