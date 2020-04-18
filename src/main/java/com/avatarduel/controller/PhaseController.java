package com.avatarduel.controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import com.avatarduel.AvatarDuel;
import com.avatarduel.gamephase.Phase;
import com.avatarduel.gameutils.GameStatus;

public class PhaseController implements Initializable {
    /**
     * Change phase signal
     */
    private BooleanProperty battlePhaseSignal, endPhaseSignal;
    /**
     * Background display
     */
    @FXML private ImageView background;
    /**
     * Phase button display
     */
    @FXML private ImageView drawPhase, mainPhase, battlePhase, endPhase;

    /**
     * Constructor
     */
    public PhaseController() {
        this.battlePhaseSignal = new SimpleBooleanProperty(false);
        this.endPhaseSignal = new SimpleBooleanProperty(false);
    }

    /**
     * Getter for battlePhaseSignal property
     * @return this.battlePhaseSignal
     */
    public BooleanProperty getBattlePhaseSignalProperty() {
        return this.battlePhaseSignal;
    }

    /**
     * Turn on battlePhaseSignal
     */
    public void turnOnBattlePhaseSignal() {
        this.battlePhaseSignal.setValue(true);
    }

    /**
     * Turn off battlePhaseSignal
     */
    public void turnOffBattlePhaseSignal() {
        this.battlePhaseSignal.setValue(false);
    }

    /**
     * Getter for endPhaseSignal property
     * @return this.endPhaseSignal
     */
    public BooleanProperty getEndPhaseSignalProperty() {
        return this.endPhaseSignal;
    }

    /**
     * Turn on endPhaseSignal
     */
    public void turnOnEndPhaseSignal() {
        this.endPhaseSignal.setValue(true);
    }

    /**
     * Turn off endPhaseSignal
     */
    public void turnOffEndPhaseSignal() {
        this.endPhaseSignal.setValue(false);
    }

    /**
     * {@inheritDoc}
     */
    @Override @FXML
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // TODO: add background
//        this.background.setImage(new Image(AvatarDuel.class.getResource("...").toString()));
        this.drawPhase.setImage(new Image(AvatarDuel.class.getResource("img/phase/dp_button.png").toString()));
        this.mainPhase.setImage(new Image(AvatarDuel.class.getResource("img/phase/mp_button.png").toString()));
        this.battlePhase.setImage(new Image(AvatarDuel.class.getResource("img/phase/bp_button.png").toString()));
        this.endPhase.setImage(new Image(AvatarDuel.class.getResource("img/phase/ep_button.png").toString()));

        // Add event handler
        this.battlePhase.onMouseClickedProperty().set((EventHandler<MouseEvent>) (MouseEvent e) -> {
            if (GameStatus.getGameStatus().getGamePhase() == Phase.MAIN) {
                turnOnBattlePhaseSignal();
            }
        });
        this.endPhase.onMouseClickedProperty().set((EventHandler<MouseEvent>) (MouseEvent e) -> {
            if (GameStatus.getGameStatus().getGamePhase() == Phase.MAIN || GameStatus.getGameStatus().getGamePhase() == Phase.BATTLE) {
                turnOnEndPhaseSignal();
            }
        });
    }
}
