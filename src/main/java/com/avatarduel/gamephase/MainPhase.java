package com.avatarduel.gamephase;

import com.avatarduel.controller.*;
import com.avatarduel.gameutils.GameStatus;
import com.avatarduel.model.*;
import com.avatarduel.model.Character;

public class MainPhase implements GamePhase {
    /**
     * Class singleton instance
     */
    private static MainPhase mainPhase;
    /**
     * Land card placed counter
     */
    private int landCardPlaced;

    /**
     * Constructor
     */
    private MainPhase() {
        this.landCardPlaced = 0;
    }

    /**
     * Getter for class singleton instance
     * @return Class singleton instance
     */
    public static MainPhase getMainPhase() {
        if (mainPhase == null)
            mainPhase = new MainPhase();
        return mainPhase;
    }

    /**
     * Start main phase
     * @param mainController The MainController for the UI
     */
    @Override
    public void startPhase(MainController mainController) {
        this.landCardPlaced = 0;
        // Update game status
        GameStatus.getGameStatus().setGamePhase(Phase.MAIN);
        // Add connector from hand to field
        Player activePlayer = GameStatus.getGameStatus().getGameActivePlayer();
        HandController handController = mainController.getHandControllerMap().get(activePlayer);
        FieldController fieldController = mainController.getFieldController();
        handController.getActiveHandCardSetProperty().addListener((observable, oldValue, newValue) -> {
            // Only CHARACTER and SKILL_AURA card can be summoned
            if (handController.getActiveHandCard().getCard().getCardType() == CardType.LAND) {
                return;
            } else if (handController.getActiveHandCard().getCard().getCardType() == CardType.SKILL) {
                if (((Skill) handController.getActiveHandCard().getCard()).getEffect() != Effect.AURA)
                    return;
            }
            if (oldValue == false && newValue == true)
                if (enoughEnergy(handController.getActiveHandCard().getCard()))
                    fieldController.setWaitingHandCard(handController.getActiveHandCard());
                else
                    fieldController.clearFieldEventHandler();
            else
                fieldController.clearFieldEventHandler();
        });
        // Add connector from field to hand
        fieldController.getCardSummonedProperty().addListener((observable, oldValue, newValue) -> {
            if (oldValue == false && newValue == true)
                handController.removeCardOnHand(fieldController.getWaitingHandCard());
        });
    }

    /**
     * End the main phase
     * @param mainController The MainController for the UI
     */
    @Override
    public void endPhase(MainController mainController) {
        // Clear glow effect from hand card if any
        Player activePlayer = GameStatus.getGameStatus().getGameActivePlayer();
        HandController handController = mainController.getHandControllerMap().get(activePlayer);
        for (HandCardController handCardController : handController.getCardControllerList())
            handCardController.getCardAncPane().setEffect(null);
        // Remove active hand card
        handController.removeActiveHandCard();
        // Deactivate event handler in entire field
        mainController.getFieldController().clearFieldEventHandler();
        // Proceed to battle phase
        BattlePhase.getBattlePhase().endPhase(mainController);
    }

    /**
     * Summon land card
     * @param card The land card
     * @param powerController The PowerController
     */
    public void summonLandCard(Land card, PowerController powerController) { // TODO: implement PowerController as param
        if (this.landCardPlaced > 0)
            return;
        this.landCardPlaced++;
        GameStatus.getGameStatus().getOurPower().incMaxPower(card.getElement());
        powerController.render();
    }

    /**
     * Check if current energy enough to summon card
     * @param card The Card (non LAND)
     */
    public boolean enoughEnergy(Card card) {
        int currPower = GameStatus.getGameStatus().getOurPower().getCurrPowerList().get(card.getElement());
        if (card.getCardType() == CardType.CHARACTER)
            return ((Character) card).getPower() <= currPower;
        else // card.getCardType() == CardType.SKILL
            return ((Skill) card).getPower() <= currPower;
    }
}
