package com.avatarduel.gamephase;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import com.avatarduel.controller.*;
import com.avatarduel.gameutils.GameStatus;
import com.avatarduel.model.*;
import com.avatarduel.model.Character;
import jdk.tools.jaotc.Main;

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
    ChangeListener<Boolean> handToFieldConnector, fieldToHandConnector, handToPowerConnector, powerToHandConnector;

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
        // Add connector hand-field, hand-power
        this.connectHandAndField(mainController);
        this.connectHandAndPower(mainController);
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
        PowerController powerController = mainController.getPowerControllerMap().get(activePlayer);

        // Clear glow effect from hand card if any
        for (HandCardController handCardController : handController.getCardControllerList())
            handCardController.getCardAncPane().setEffect(null);
        // Remove active hand card
        handController.removeActiveHandCard();
        // Deactivate event handler in entire field
        fieldController.clearFieldEventHandler();
        // Remove waiting hand card
        fieldController.removeWaitingHandCard();
        // Deactivate event handler in power
        powerController.deactivateEventHandler();

        // Disconnect hand-field, hand-power
        this.disconnectHandAndField(mainController);
        this.disconnectHandAndPower(mainController);

        // Proceed to battle phase
        BattlePhase.getBattlePhase().endPhase(mainController);
    }

    /**
     * Add connection between hand and field controller, using listener on property
     * @param mainController The MainController
     */
    public void connectHandAndField(MainController mainController) {
        Player activePlayer = GameStatus.getGameStatus().getGameActivePlayer();
        HandController handController = mainController.getHandControllerMap().get(activePlayer);
        FieldController fieldController = mainController.getFieldController();
        // Create ChangeListener object
        if (this.handToFieldConnector == null) {
            this.handToFieldConnector = new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue observable, Boolean oldValue, Boolean newValue) {
                    fieldController.clearFieldEventHandler();
                    if (handController.getActiveHandCard() == null)
                        return;
                    // Summon CHARACTER and SKILL AURA card
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
        }
        if (this.fieldToHandConnector == null) {
            this.fieldToHandConnector = new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue observable, Boolean oldValue, Boolean newValue) {
                    if (oldValue == false && newValue == true)
                        handController.removeCardOnHand(fieldController.getWaitingHandCard());
                }
            };
        }

        // Add connector as listener to property
        handController.getActiveHandCardSetProperty().addListener(this.handToFieldConnector);
        fieldController.getCardSummonedProperty().addListener(this.fieldToHandConnector);
    }

    /**
     * Remove connection between hand and field controller
     * @param mainController The MainController
     */
    public void disconnectHandAndField(MainController mainController) {
        Player activePlayer = GameStatus.getGameStatus().getGameActivePlayer();
        HandController handController = mainController.getHandControllerMap().get(activePlayer);
        FieldController fieldController = mainController.getFieldController();
        // Remove listener
        handController.getActiveHandCardSetProperty().removeListener(this.handToFieldConnector);
        fieldController.getCardSummonedProperty().removeListener(this.fieldToHandConnector);
    }

    /**
     * Add connection between hand and power controller, using listener on property
     * @param mainController The MainController
     */
    public void connectHandAndPower(MainController mainController) {
        Player activePlayer = GameStatus.getGameStatus().getGameActivePlayer();
        HandController handController = mainController.getHandControllerMap().get(activePlayer);
        PowerController powerController = mainController.getPowerControllerMap().get(activePlayer);
        // Create ChangeListener object
        if (this.handToPowerConnector == null) {
            this.handToPowerConnector = new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue observable, Boolean oldValue, Boolean newValue) {
                    powerController.deactivateEventHandler();
                    // Zeroth check
                    if (handController.getActiveHandCard() == null || landCardPlaced > 0)
                        return;
                    // Summon LAND card
                    if (handController.getActiveHandCard().getCard().getCardType() != CardType.LAND)
                        return;
                    // LAND card in hand clicked, activate event handler in power display
                    if (oldValue == false && newValue == true)
                        powerController.activateEventHandler();
                }
            };
        }
        if (this.powerToHandConnector == null) {
            this.powerToHandConnector = new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue observable, Boolean oldValue, Boolean newValue) {
                    // LAND card summon succeed, remove card, increment power, update display, increment land counter
                    if (oldValue == false && newValue == true) {
                        HandCardController handCardController = handController.getActiveHandCard();
                        handController.removeCardOnHand(handCardController);
                        GameStatus.getGameStatus().getGamePower().get(activePlayer).incMaxPower(handCardController.getCard().getElement());
                        powerController.init();
                        powerController.deactivateEventHandler();
                        landCardPlaced++;
                    }
                }
            };
        }

        // Add connector as listener to property
        handController.getActiveHandCardSetProperty().addListener(this.handToPowerConnector);
        powerController.getCardSummonedSignalProperty().addListener(this.powerToHandConnector);
    }

    /**
     * Remove connection between hand and field controller
     * @param mainController The MainController
     */
    public void disconnectHandAndPower(MainController mainController) {
        Player activePlayer = GameStatus.getGameStatus().getGameActivePlayer();
        HandController handController = mainController.getHandControllerMap().get(activePlayer);
        PowerController powerController = mainController.getPowerControllerMap().get(activePlayer);
        // Remove listener
        handController.getActiveHandCardSetProperty().removeListener(this.handToFieldConnector);
        powerController.getCardSummonedSignalProperty().removeListener(this.fieldToHandConnector);
    }

    /**
     * Check if current energy enough to summon card
     * @param card The Card (non LAND)
     */
    public boolean enoughEnergy(Card card) {
        int currPower = GameStatus.getGameStatus().getOurPower().getCurrPowerList().get(card.getElement());
        if (card.getCardType() == CardType.CHARACTER) {
            return ((Character) card).getPower() <= currPower;
        } else{ // card.getCardType() == CardType.SKILL
            return ((Skill) card).getPower() <= currPower;
        }
    }
}
