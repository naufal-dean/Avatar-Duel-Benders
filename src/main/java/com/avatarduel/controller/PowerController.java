package com.avatarduel.controller;

import java.net.URL;
import java.util.Collections;
import java.util.ResourceBundle;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

import com.avatarduel.AvatarDuel;
import com.avatarduel.gameassets.GameDropShadow;
import com.avatarduel.gamephase.Phase;
import com.avatarduel.gameutils.GamePower;
import com.avatarduel.gameutils.GameStatus;
import com.avatarduel.model.Element;
import com.avatarduel.model.Player;
import javafx.scene.text.Font;

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
     * Main HBox
     */
    @FXML private HBox mainHBox;
    /**
     * Power status icon
     */
    @FXML private ImageView airIcon, earthIcon, energyIcon, fireIcon, waterIcon;
    /**
     * Power status value
     */
    @FXML private Label air, earth, energy, fire, water;

    /**
     * Constructor
     */
    public PowerController(Player owner) {
        this.owner = owner;
        this.activeHandler = new SimpleBooleanProperty(false);
        this.cardSummonedSignal = new SimpleBooleanProperty(false);
        // Drop Shadow
        this.availableShadow = GameDropShadow.getGameDropShadow().getShadowYellowPower();
        this.hoverShadow = GameDropShadow.getGameDropShadow().getShadowGreenPower();
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
                       + String.valueOf(gamePower.getMaxPowerList().get(Element.AIR)));
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
        // Set icon
        this.airIcon.setImage(new Image(AvatarDuel.class.getResource("img/power/power_air.png").toString()));
        this.earthIcon.setImage(new Image(AvatarDuel.class.getResource("img/power/power_earth.png").toString()));
        this.energyIcon.setImage(new Image(AvatarDuel.class.getResource("img/power/power_energy.png").toString()));
        this.fireIcon.setImage(new Image(AvatarDuel.class.getResource("img/power/power_fire.png").toString()));
        this.waterIcon.setImage(new Image(AvatarDuel.class.getResource("img/power/power_water.png").toString()));
        // Set font
        this.air.setFont(Font.loadFont(AvatarDuel.class.getResourceAsStream("font/palatino-linotype-bold.ttf"), 14));
        this.air.setAlignment(Pos.CENTER);
        this.earth.setFont(Font.loadFont(AvatarDuel.class.getResourceAsStream("font/palatino-linotype-bold.ttf"), 14));
        this.earth.setAlignment(Pos.CENTER);
        this.energy.setFont(Font.loadFont(AvatarDuel.class.getResourceAsStream("font/palatino-linotype-bold.ttf"), 14));
        this.energy.setAlignment(Pos.CENTER);
        this.fire.setFont(Font.loadFont(AvatarDuel.class.getResourceAsStream("font/palatino-linotype-bold.ttf"), 14));
        this.fire.setAlignment(Pos.CENTER);
        this.water.setFont(Font.loadFont(AvatarDuel.class.getResourceAsStream("font/palatino-linotype-bold.ttf"), 14));
        this.water.setAlignment(Pos.CENTER);
        // Swap HBox order for player TOP
        if (this.owner == Player.TOP) {
            ObservableList<Node> newOrder = FXCollections.observableArrayList(this.mainHBox.getChildren());
            Collections.swap(newOrder, 1, 2);
            Collections.swap(newOrder, 0, 3);
            this.mainHBox.getChildren().setAll(newOrder);
        }
        // Init value
        this.init();
        // Add event handler
        // On mouse clicked handler
        this.rootPane.onMouseClickedProperty().set((EventHandler<MouseEvent>) (MouseEvent e) -> {
            if (activeHandler.get() && GameStatus.getGameStatus().getGamePhase() == Phase.MAIN) {
                if (e.getButton() == MouseButton.PRIMARY)
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
