package com.avatarduel.gamephase;

import com.avatarduel.controller.FieldController;
import com.avatarduel.controller.HandController;
import com.avatarduel.controller.MainController;
import com.avatarduel.gameutils.GameStatus;
import com.avatarduel.model.Player;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

public class BattlePhase implements GamePhase {
    /**
     * Class singleton instance
     */
    private static BattlePhase battlePhase;

    /**
     * Connector
     */
    ChangeListener<Boolean> endPhaseChange, directAttackReadyHandler, directAttackLaunchedHandler;
    ChangeListener<Number> fieldDamageSignalListener;

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
     * @param mainController The MainController for the UI
     */
    @Override
    public void startPhase(MainController mainController) {
        // Update game status
        GameStatus.getGameStatus().setGamePhase(Phase.BATTLE);

        // Add connection between elements and GameStatus
        this.connectFieldSignal(mainController);
        this.connectFieldAndHand(mainController);

        // Add event listener to phase change
        this.addPhaseChangeListener(mainController);
        // Check if battle phase skipped
        if (mainController.getPhaseController().getEndPhaseSignalProperty().get()) {
            mainController.getPhaseController().turnOffEndPhaseSignal();
            this.endPhase(mainController);
        }
    }

    /**
     * End battle phase
     * @param mainController The MainController for the UI
     */
    @Override
    public void endPhase(MainController mainController) {
        // Clear field controller handler and active card
        mainController.getFieldController().clearSummCardHandler();
        mainController.getFieldController().resetActiveFieldCardController();

        // Deactivate enemy deck handler
        Player enemyPlayer = GameStatus.getGameStatus().getGameNonActivePlayer();
        mainController.getHandControllerMap().get(enemyPlayer).deactivateRootEventHandler();

        // Remove connection between elements and GameStatus
        this.disconnectFieldSignal(mainController);
        this.disconnectFieldAndHand(mainController);

        // Disconnect phase change listener
        this.removePhaseChangeListener(mainController);

        // Proceed to end phase
        EndPhase.getEndPhase().startPhase(mainController);
    }

    /**
     * Add connection that listen to field controller signal
     * @param mainController The MainController
     */
    public void connectFieldSignal(MainController mainController) {
        // Create ChangeListener object
        this.fieldDamageSignalListener = new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue observable, Number oldValue, Number newValue) {
                if (newValue.intValue() > 0) {
                    // Battle ended, damage dealt to enemy player
                    Player enemyPlayer = GameStatus.getGameStatus().getGameNonActivePlayer();
                    int enemyHealth = GameStatus.getGameStatus().getGameHealthMap().get(enemyPlayer);
                    GameStatus.getGameStatus().getGameHealthMap().put(enemyPlayer, enemyHealth - newValue.intValue());
                    // Update display
                    mainController.getHealthControllerMap().get(enemyPlayer).init();
                    // Turn off signal
                    mainController.getFieldController().setDamageDealtSignal(-1);
                }
            }
        };

        // Add connector as listener to property
        mainController.getFieldController().getDamageDealtSignalProperty().addListener(this.fieldDamageSignalListener);
    }

    /**
     * Remove connection that listen to field controller signal
     * @param mainController The MainController
     */
    public void disconnectFieldSignal(MainController mainController) {
        // Remove listener
        mainController.getFieldController().getDamageDealtSignalProperty().removeListener(this.fieldDamageSignalListener);
    }

    /**
     * Add connection between field and hand
     * @param mainController The MainController
     */
    public void connectFieldAndHand(MainController mainController) {
        Player enemyPlayer = GameStatus.getGameStatus().getGameNonActivePlayer();
        FieldController fieldController = mainController.getFieldController();
        HandController enemyHandController = mainController.getHandControllerMap().get(enemyPlayer);
        // Create ChangeListener object
        this.directAttackReadyHandler = new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue observable, Boolean oldValue, Boolean newValue) {
                enemyHandController.deactivateRootEventHandler();
                if (oldValue == false && newValue == true) {
                    // Activate enemy hand root handler, turn off signal
                    enemyHandController.activateRootEventHandler();
                }
            }
        };
        this.directAttackLaunchedHandler = new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue observable, Boolean oldValue, Boolean newValue) {
                if (oldValue == false && newValue == true) {
                    // Direct attack launched, damage dealt to enemy player
                    int enemyHealth = GameStatus.getGameStatus().getGameHealthMap().get(enemyPlayer);
                    int damageDealt = fieldController.getActiveFieldCardController().getCardValue();
                    GameStatus.getGameStatus().getGameHealthMap().put(enemyPlayer, Math.max(enemyHealth - damageDealt, 0));
                    // Update display
                    mainController.getHealthControllerMap().get(enemyPlayer).init();
                    // Reset active field card controller
                    fieldController.getActiveFieldCardController().setHadAttacked(true);
                    fieldController.resetActiveFieldCardController();
                    // Deactivate handler and turn off signal
                    enemyHandController.deactivateRootEventHandler();
                    enemyHandController.turnOffDirectAttackLaunchedSignal();
                }
            }
        };

        // Add connector as listener to property
        fieldController.getDirectAttackSignalReadyProperty().addListener(this.directAttackReadyHandler);
        enemyHandController.getDirectAttackLaunchedSignalProperty().addListener(this.directAttackLaunchedHandler);
    }

    /**
     * Remove connection between field and hand
     * @param mainController The MainController
     */
    public void disconnectFieldAndHand(MainController mainController) {
        Player enemyPlayer = GameStatus.getGameStatus().getGameNonActivePlayer();
        FieldController fieldController = mainController.getFieldController();
        HandController enemyHandController = mainController.getHandControllerMap().get(enemyPlayer);
        // Remove listener
        fieldController.getDirectAttackSignalReadyProperty().removeListener(this.directAttackReadyHandler);
        enemyHandController.getDirectAttackLaunchedSignalProperty().removeListener(this.directAttackLaunchedHandler);
    }

    /**
     * Connect and listen to phase change event
     * @param mainController The MainController
     */
    public void addPhaseChangeListener(MainController mainController) {
        // Create ChangeListener object
        this.endPhaseChange = new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue observable, Boolean oldValue, Boolean newValue) {
                if (oldValue == false && newValue == true) {
                    endPhase(mainController);
                    mainController.getPhaseController().turnOffEndPhaseSignal();
                }
            }
        };
        // Add listener
        mainController.getPhaseController().getEndPhaseSignalProperty().addListener(this.endPhaseChange);
    }

    /**
     * Disconnect phase change event listener
     * @param mainController The MainController
     */
    public void removePhaseChangeListener(MainController mainController) {
        mainController.getPhaseController().getBattlePhaseSignalProperty().removeListener(this.endPhaseChange);
        mainController.getPhaseController().getEndPhaseSignalProperty().removeListener(this.endPhaseChange);
    }
}
