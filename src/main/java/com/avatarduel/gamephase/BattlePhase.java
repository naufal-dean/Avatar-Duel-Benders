package com.avatarduel.gamephase;

import com.avatarduel.controller.FieldController;
import com.avatarduel.controller.HandController;
import com.avatarduel.controller.MainController;
import com.avatarduel.gameutils.GameStatus;
import com.avatarduel.model.CardType;
import com.avatarduel.model.Effect;
import com.avatarduel.model.Player;
import com.avatarduel.model.Skill;
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
    ChangeListener<Boolean> endPhaseChange;
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

        // TODO: implement
        // Add connection between elements and GameStatus
        this.connectFieldSignal(mainController);

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
        // TODO: implement

        // Clear field controller handler and active card
        mainController.getFieldController().clearSummCardHandler();
        mainController.getFieldController().resetActiveFieldCardController();

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
