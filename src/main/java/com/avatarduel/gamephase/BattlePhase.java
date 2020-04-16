package com.avatarduel.gamephase;

import com.avatarduel.controller.MainController;
import com.avatarduel.gameutils.GameStatus;
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

        // Disconnect phase change listener
        this.removePhaseChangeListener(mainController);

        // Proceed to end phase
        EndPhase.getEndPhase().startPhase(mainController);
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
