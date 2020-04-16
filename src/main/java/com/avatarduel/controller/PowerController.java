package com.avatarduel.controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import com.avatarduel.gameutils.GamePower;
import com.avatarduel.gameutils.GameStatus;
import com.avatarduel.model.Element;
import com.avatarduel.model.Player;

public class PowerController implements Initializable {
    /**
     * Owner of the power display
     */
    private Player owner;
    /**
     * Power status
     */
    @FXML private Label air, earth, energy, fire, water;

    /**
     * Constructor
     */
    public PowerController(Player owner) {
        this.owner = owner;
    }

    /**
     * Init power panel based on GameStatus
     */
    public void init() {
        GamePower gamePower = GameStatus.getGameStatus().getGamePower().get(this.owner);
        this.air.setText(String.valueOf(gamePower.getCurrPowerList().get(Element.AIR)));
        this.earth.setText(String.valueOf(gamePower.getCurrPowerList().get(Element.EARTH)));
        this.energy.setText(String.valueOf(gamePower.getCurrPowerList().get(Element.ENERGY)));
        this.fire.setText(String.valueOf(gamePower.getCurrPowerList().get(Element.FIRE)));
        this.water.setText(String.valueOf(gamePower.getCurrPowerList().get(Element.WATER)));
    }

    /**
     * {@inheritDoc}
     */
    @Override @FXML
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.init();
    }
}
