package com.avatarduel.controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import com.avatarduel.gamephase.Phase;
import com.avatarduel.gameutils.GamePower;
import com.avatarduel.gameutils.GameStatus;
import com.avatarduel.model.Element;
import com.avatarduel.model.Player;
import javafx.scene.paint.Color;

public class PowerController implements Initializable {
    /**
     * Owner of the power display
     */
    private Player owner;
    /**
     * Shadow effect
     */
    private DropShadow availableShadow, hoverShadow;
    /**
     * Event handler status and signal
     */
    private BooleanProperty activeHandler, cardSummonedSignal;
    /**
     * Root pane
     */
    @FXML private AnchorPane rootPane;
    /**
     * Power status
     */
    @FXML private Label air, earth, energy, fire, water;

    /**
     * Constructor
     */
    public PowerController(Player owner) {
        this.owner = owner;
        this.activeHandler = new SimpleBooleanProperty(false);
        this.cardSummonedSignal = new SimpleBooleanProperty(false);
        // Cell available shadow
        this.availableShadow = new DropShadow();
        this.availableShadow.setColor(Color.YELLOW);
        this.availableShadow.setWidth(50);
        this.availableShadow.setHeight(50);
        this.availableShadow.setSpread(0.9);
        // Cell hover shadow
        this.hoverShadow = new DropShadow();
        this.hoverShadow.setColor(Color.RED);
        this.hoverShadow.setWidth(50);
        this.hoverShadow.setHeight(50);
        this.hoverShadow.setSpread(0.9);
    }

    /**
     * Getter for cardSummonedSignal
     * @return this.cardSummonedSignal
     */
    public BooleanProperty getCardSummonedSignalProperty() {
        return this.cardSummonedSignal;
    }

    /**
     * Turn on card summoned signal
     */
    public void turnOnCardSummonedSignal() {
        this.cardSummonedSignal.setValue(true);
    }

    /**
     * Turn off card summoned signal
     */
    public void turnOffCardSummonedSignal() {
        this.cardSummonedSignal.setValue(false);
    }

    /**
     * Activate event handler in this controller
     */
    public void activateEventHandler() {
        this.activeHandler.setValue(true);
    }

    /**
     * Deactivate event handler in this controller
     */
    public void deactivateEventHandler() {
        this.activeHandler.setValue(false);
    }

    /**
     * Init power panel based on GameStatus
     */
    public void init() {
        GamePower gamePower = GameStatus.getGameStatus().getGamePowerMap().get(this.owner);
        this.air.setText(String.valueOf(gamePower.getCurrPowerList().get(Element.AIR)) + "/"
                       + String.valueOf(gamePower.getCurrPowerList().get(Element.AIR)));
        this.earth.setText(String.valueOf(gamePower.getCurrPowerList().get(Element.EARTH)) + "/"
                         + String.valueOf(gamePower.getMaxPowerList().get(Element.EARTH)));
        this.energy.setText(String.valueOf(gamePower.getCurrPowerList().get(Element.ENERGY)) + "/"
                          + String.valueOf(gamePower.getMaxPowerList().get(Element.ENERGY)));
        this.fire.setText(String.valueOf(gamePower.getCurrPowerList().get(Element.FIRE)) + "/"
                        + String.valueOf(gamePower.getMaxPowerList().get(Element.FIRE)));
        this.water.setText(String.valueOf(gamePower.getCurrPowerList().get(Element.WATER)) + "/"
                         + String.valueOf(gamePower.getMaxPowerList().get(Element.WATER)));
    }

    /**
     * {@inheritDoc}
     */
    @Override @FXML
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.init();
        // Add event handler
        // On mouse clicked handler
        this.rootPane.onMouseClickedProperty().set((EventHandler<MouseEvent>) (MouseEvent e) -> {
            if (activeHandler.get() && GameStatus.getGameStatus().getGamePhase() == Phase.MAIN) {
                turnOnCardSummonedSignal();
            }
        });
        // On mouse entered handler
        this.rootPane.onMouseEnteredProperty().set((EventHandler<MouseEvent>) (MouseEvent e) -> {
            if (activeHandler.get() && GameStatus.getGameStatus().getGamePhase() == Phase.MAIN) {
                rootPane.setEffect(hoverShadow);
            }
        });
        // On mouse exited handler
        this.rootPane.onMouseExitedProperty().set((EventHandler<MouseEvent>) (MouseEvent e) -> {
            if (activeHandler.get() && GameStatus.getGameStatus().getGamePhase() == Phase.MAIN) {
                rootPane.setEffect(availableShadow);
            }
        });

        // Add event listener to change display effect
        this.activeHandler.addListener((observable, oldValue, newValue) -> {
            if (GameStatus.getGameStatus().getGamePhase() == Phase.MAIN) {
                // Field effect
                if (oldValue == false && newValue == true)
                    rootPane.setEffect(availableShadow);
                else
                    rootPane.setEffect(null);
            }
        });
    }
}
