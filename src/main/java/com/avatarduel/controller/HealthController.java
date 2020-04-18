package com.avatarduel.controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

import com.avatarduel.AvatarDuel;
import com.avatarduel.model.Player;
import com.avatarduel.gameutils.GameStatus;
import javafx.util.Duration;


public class HealthController implements Initializable {
    /**
     * The owner's current health
     */
    private Player owner;
    /**
     * Health change animation
     */
    private Timeline healthChange;
    /**
     * Health Bar root pane
     */
    @FXML private StackPane healthRoot;
    /**
     * Health bar pane
     */
    @FXML private Pane healthBarPane;
    /**
     * Health Bar image
     */
    @FXML private Rectangle healthBar;
    /**
     * Health text label
     */
    @FXML private Label healthLabel, healthRemain;

    /**
     * Constructor
     */
    public HealthController(Player owner) {
        this.owner = owner;
    }

    /**
     * Update FXML using current health point
     */
    public void init() {
        // Set background
        int healthPoint = GameStatus.getGameStatus().getGameHealthMap().get(this.owner);
        if (healthPoint < 0) healthPoint = 0;
        if (healthPoint > 80) healthPoint = 80;
        this.healthRemain.setText(String.valueOf(healthPoint) + "/80");
        // Health bar
        // Animation
        double oldWidth = this.healthBar.getWidth();
        double newWidth = this.healthBarPane.getPrefWidth() * healthPoint / 80d;
        this.healthChange = new Timeline(
                new KeyFrame(
                        Duration.ZERO,
                        new KeyValue(this.healthBar.widthProperty(), oldWidth)
                ),
                new KeyFrame(
                        Duration.millis(500),
                        new KeyValue(this.healthBar.widthProperty(), newWidth)
                )
        );
        this.healthChange.play();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.healthLabel.setFont(Font.loadFont(AvatarDuel.class.getResourceAsStream("font/palatino-linotype-bold.ttf"), 48));
        this.healthRemain.setFont(Font.loadFont(AvatarDuel.class.getResourceAsStream("font/palatino-linotype-bold.ttf"), 48));
        this.healthBar.setWidth(0);
        this.init();
    }
}
