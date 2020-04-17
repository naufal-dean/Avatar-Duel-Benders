package com.avatarduel.gamephase;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import com.avatarduel.controller.*;
import com.avatarduel.gameutils.GameStatus;
import com.avatarduel.model.*;

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
    ChangeListener<Boolean> handToFieldConnector, fieldToHandConnector, handToPowerConnector, powerToHandConnector,
            battlePhaseChange, endPhaseChange;

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
        // Add event listener to phase change
        this.addPhaseChangeListener(mainController);
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
        handController.resetActiveHandCard();
        // Deactivate event handler in entire field
        fieldController.clearCellEventHandler();
        // Deactivate event handler in power
        powerController.deactivateEventHandler();

        // Disconnect hand-field, hand-power
        this.disconnectHandAndField(mainController);
        this.disconnectHandAndPower(mainController);

        // Disconnect phase change listener
        this.removePhaseChangeListener(mainController);

        // Proceed to battle phase
        BattlePhase.getBattlePhase().startPhase(mainController);
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
        this.handToFieldConnector = new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue observable, Boolean oldValue, Boolean newValue) {
                fieldController.clearCellEventHandler();
                // Zeroth check
                if (handController.getActiveHandCard() == null) {
                    fieldController.setDisableCardClick(false);
                    return;
                }
                // Summon only for CHARACTER, SKILL AURA, SKILL POWER UP
                Card card = handController.getActiveHandCard().getCard();
                if (card instanceof Land || card instanceof SkillDestroy)
                    return;
                // LAND or SKILL AURA card in hand clicked, set waiting card then activate cell event handler in field
                if (oldValue == false && newValue == true) {// TODO:  && enoughEnergy(handController.getActiveHandCard().getCard())
                    fieldController.setDisableCardClick(true);
                    fieldController.setWaitingHandCardController(handController.getActiveHandCard());
                }
            }
        };
        this.fieldToHandConnector = new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue observable, Boolean oldValue, Boolean newValue) {
                // LAND or SKILL AURA card summon succeed, remove field waiting card, subtract current power,
                // update display, deactivate field event handler, remove active hand card
                if (oldValue == false && newValue == true) {
                    GameStatus.getGameStatus().getGamePowerMap().get(activePlayer)
                              .subCurrPower(handController.getActiveHandCard().getCard().getElement(),
                                            handController.getActiveHandCard().getCard().getPower());
                    mainController.getPowerControllerMap().get(activePlayer).init();
                    fieldController.resetWaitingHandCardController();
                    fieldController.clearCellEventHandler();
                    handController.removeActiveCardFromHand();
                    fieldController.turnOffCardSummonedSignal();
                }
            }
        };

        // Add connector as listener to property
        handController.getActiveHandCardSetSignalProperty().addListener(this.handToFieldConnector);
        fieldController.getCardSummonedSignalProperty().addListener(this.fieldToHandConnector);
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
        handController.getActiveHandCardSetSignalProperty().removeListener(this.handToFieldConnector);
        fieldController.getCardSummonedSignalProperty().removeListener(this.fieldToHandConnector);
    }

    /**
     * Add connection between hand and power controller, using listener on property
     * @param mainController The MainController
     */
    public void connectHandAndPower(MainController mainController) {
        Player activePlayer = GameStatus.getGameStatus().getGameActivePlayer();
        HandController handController = mainController.getHandControllerMap().get(activePlayer);
        PowerController powerController = mainController.getPowerControllerMap().get(activePlayer);
        FieldController fieldController = mainController.getFieldController();
        // Create ChangeListener object
        this.handToPowerConnector = new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue observable, Boolean oldValue, Boolean newValue) {
                powerController.deactivateEventHandler();
                // Zeroth check
                if (handController.getActiveHandCard() == null) {
                    fieldController.setDisableCardClick(false);
                    return;
                }
                // Summon LAND card, max 1 per turn
                if (!(handController.getActiveHandCard().getCard() instanceof Land) || landCardPlaced > 0) {
                    return;
                }
                // LAND card in hand clicked, activate event handler in power display
                if (oldValue == false && newValue == true) {
                    fieldController.setDisableCardClick(true);
                    powerController.activateEventHandler();
                }
            }
        };
        this.powerToHandConnector = new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue observable, Boolean oldValue, Boolean newValue) {
                // LAND card summon succeed, increment power, update display, remove active hand card,
                // deactivate power event handler, increment land counter
                if (oldValue == false && newValue == true) {
                    GameStatus.getGameStatus().getGamePowerMap().get(activePlayer)
                              .incMaxPower(handController.getActiveHandCard().getCard().getElement());
                    handController.removeActiveCardFromHand();
                    powerController.deactivateEventHandler();
                    powerController.init();
                    landCardPlaced++;
                    powerController.turnOffCardSummonedSignal();
                }
            }
        };

        // Add connector as listener to property
        handController.getActiveHandCardSetSignalProperty().addListener(this.handToPowerConnector);
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
        handController.getActiveHandCardSetSignalProperty().removeListener(this.handToFieldConnector);
        powerController.getCardSummonedSignalProperty().removeListener(this.fieldToHandConnector);
    }

    /**
     * Connect and listen to phase change event
     * @param mainController The MainController
     */
    public void addPhaseChangeListener(MainController mainController) {
        // Create ChangeListener object
        this.battlePhaseChange = new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue observable, Boolean oldValue, Boolean newValue) {
                if (oldValue == false && newValue == true) {
                    mainController.getPhaseController().turnOffBattlePhaseSignal();
                    endPhase(mainController);
                }
            }
        };
        this.endPhaseChange = new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue observable, Boolean oldValue, Boolean newValue) {
                // Keep endPhaseSignal, so it can be passed to battlePhase
                if (oldValue == false && newValue == true) {
                    endPhase(mainController);
                }
            }
        };
        // Add listener
        mainController.getPhaseController().getBattlePhaseSignalProperty().addListener(this.battlePhaseChange);
        mainController.getPhaseController().getEndPhaseSignalProperty().addListener(this.endPhaseChange);
    }

    /**
     * Disconnect phase change event listener
     * @param mainController The MainController
     */
    public void removePhaseChangeListener(MainController mainController) {
        mainController.getPhaseController().getBattlePhaseSignalProperty().removeListener(this.battlePhaseChange);
        mainController.getPhaseController().getEndPhaseSignalProperty().removeListener(this.endPhaseChange);
    }

    /**
     * Check if current energy enough to summon card
     * @param card The Card
     */
    public boolean enoughEnergy(Card card) {
        return card.getPower() <= GameStatus.getGameStatus().getOurPower().getCurrPowerList().get(card.getElement());
    }
}
