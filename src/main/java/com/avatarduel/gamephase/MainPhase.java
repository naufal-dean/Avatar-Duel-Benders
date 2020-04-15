package com.avatarduel.gamephase;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

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
     * Connector
     */
    ChangeListener<Boolean> handToFieldConnector, fieldToHandConnector;

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
        // Add connector between hand and field
        connectHandAndField(mainController);
    }

    /**
     * End the main phase
     * @param mainController The MainController for the UI
     */
    @Override
    public void endPhase(MainController mainController) {
        Player activePlayer = GameStatus.getGameStatus().getGameActivePlayer();
        HandController handController = mainController.getHandControllerMap().get(activePlayer);
        FieldController fieldController = mainController.getFieldController();

        // Clear glow effect from hand card if any
        for (HandCardController handCardController : handController.getCardControllerList())
            handCardController.getCardAncPane().setEffect(null);
        // Remove active hand card
        handController.removeActiveHandCard();

        // Deactivate event handler in entire field
        fieldController.clearFieldEventHandler();
        // Remove waiting hand card
        fieldController.removeWaitingHandCard();

        // Remove hand and field connector
        handController.getActiveHandCardSetProperty().removeListener(this.handToFieldConnector);
        fieldController.getCardSummonedProperty().removeListener(this.fieldToHandConnector);

        // Proceed to battle phase
        BattlePhase.getBattlePhase().endPhase(mainController);
    }

    public void connectHandAndField(MainController mainController) {
        // Add connector from hand to field
        Player activePlayer = GameStatus.getGameStatus().getGameActivePlayer();
        HandController handController = mainController.getHandControllerMap().get(activePlayer);
        FieldController fieldController = mainController.getFieldController();
        // Create ChangeListener object
        this.handToFieldConnector = new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue observable, Boolean oldValue, Boolean newValue) {
                fieldController.clearFieldEventHandler();
                // Only CHARACTER and SKILL_AURA card can be summoned
                if (handController.getActiveHandCard().getCard().getCardType() == CardType.LAND) {
                    return;
                } else if (handController.getActiveHandCard().getCard().getCardType() == CardType.SKILL) {
                    if (((Skill) handController.getActiveHandCard().getCard()).getEffect() != Effect.AURA)
                        return;
                }
                // Pass signal to fieldController
                if (oldValue == false && newValue == true)
                    fieldController.setWaitingHandCard(handController.getActiveHandCard());
            }
        };
        this.fieldToHandConnector = new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue observable, Boolean oldValue, Boolean newValue) {
                if (oldValue == false && newValue == true)
                    handController.removeCardOnHand(fieldController.getWaitingHandCard());
            }
        };

        // Add connector as listener to property
        handController.getActiveHandCardSetProperty().addListener(this.handToFieldConnector);
        fieldController.getCardSummonedProperty().addListener(this.fieldToHandConnector);
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
