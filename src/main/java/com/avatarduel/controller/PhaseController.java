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
     * Button image
     */
    private Image dpButtonImg, mpButtonImg, bpButtonImg, epButtonImg, dpButtonActiveImg, mpButtonActiveImg,
            bpButtonActiveImg, epButtonActiveImg;
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
     * Update FXML using current phase
     */
    public void init() {
        Phase gamePhase = GameStatus.getGameStatus().getGamePhase();
        this.drawPhase.setImage((gamePhase == Phase.DRAW) ? (dpButtonActiveImg) : (dpButtonImg));
        this.mainPhase.setImage((gamePhase == Phase.MAIN) ? (mpButtonActiveImg) : (mpButtonImg));
        this.battlePhase.setImage((gamePhase == Phase.BATTLE) ? (bpButtonActiveImg) : (bpButtonImg));
        this.endPhase.setImage((gamePhase == Phase.END) ? (epButtonActiveImg) : (epButtonImg));
    }

    /**
     * {@inheritDoc}
     */
    @Override @FXML
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.background.setImage(new Image(AvatarDuel.class.getResource("img/background/phase_background.png").toString()));
        // Init button image
        this.dpButtonImg = new Image(AvatarDuel.class.getResource("img/phase/dp_button.png").toString());
        this.mpButtonImg = new Image(AvatarDuel.class.getResource("img/phase/mp_button.png").toString());
        this.bpButtonImg = new Image(AvatarDuel.class.getResource("img/phase/bp_button.png").toString());
        this.epButtonImg = new Image(AvatarDuel.class.getResource("img/phase/ep_button.png").toString());
        this.dpButtonActiveImg = new Image(AvatarDuel.class.getResource("img/phase/dp_button_active.png").toString());
        this.mpButtonActiveImg = new Image(AvatarDuel.class.getResource("img/phase/mp_button_active.png").toString());
        this.bpButtonActiveImg = new Image(AvatarDuel.class.getResource("img/phase/bp_button_active.png").toString());
        this.epButtonActiveImg = new Image(AvatarDuel.class.getResource("img/phase/ep_button_active.png").toString());
        // Set button image
        this.drawPhase.setImage(dpButtonImg);
        this.mainPhase.setImage(mpButtonImg);
        this.battlePhase.setImage(bpButtonImg);
        this.endPhase.setImage(epButtonImg);

        // Add click event handler
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
        // Add hover event handler
        this.battlePhase.onMouseEnteredProperty().set((EventHandler<MouseEvent>) (MouseEvent e) -> {
            if (GameStatus.getGameStatus().getGamePhase() == Phase.MAIN) {
                battlePhase.setImage(bpButtonActiveImg);
            }
        });
        this.battlePhase.onMouseExitedProperty().set((EventHandler<MouseEvent>) (MouseEvent e) -> {
            if (GameStatus.getGameStatus().getGamePhase() == Phase.MAIN) {
                battlePhase.setImage(bpButtonImg);
            }
        });
        this.endPhase.onMouseEnteredProperty().set((EventHandler<MouseEvent>) (MouseEvent e) -> {
            if (GameStatus.getGameStatus().getGamePhase() == Phase.MAIN || GameStatus.getGameStatus().getGamePhase() == Phase.BATTLE) {
                endPhase.setImage(epButtonActiveImg);
            }
        });
        this.endPhase.onMouseExitedProperty().set((EventHandler<MouseEvent>) (MouseEvent e) -> {
            if (GameStatus.getGameStatus().getGamePhase() == Phase.MAIN || GameStatus.getGameStatus().getGamePhase() == Phase.BATTLE) {
                endPhase.setImage(epButtonImg);
            }
        });
    }
}
