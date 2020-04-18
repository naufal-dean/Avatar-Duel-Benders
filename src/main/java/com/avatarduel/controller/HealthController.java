package com.avatarduel.controller;

import java.net.URL;
import java.util.ResourceBundle;

import com.avatarduel.model.Player;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

import com.avatarduel.AvatarDuel;
import com.avatarduel.gameutils.GameStatus;
import javafx.scene.text.Font;


public class HealthController implements Initializable {
    /**
     * The owner's current health
     */
    private Player owner;
    /**
     * Health Bar root pane
     */
    @FXML private StackPane healthRoot;
    /**
     * Health Bar image
     */
    @FXML private ImageView healthVisual;
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
        String templatePath = "img/health/health_bar_" + healthPoint + ".png";
        this.healthVisual.setImage(new Image(AvatarDuel.class.getResource(templatePath).toString()));
        this.healthRemain.setText(String.valueOf(healthPoint) + "/80");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.healthLabel.setFont(Font.loadFont(AvatarDuel.class.getResourceAsStream("font/palatino-linotype-bold.ttf"), 48));
        this.healthRemain.setFont(Font.loadFont(AvatarDuel.class.getResourceAsStream("font/palatino-linotype-bold.ttf"), 48));
        this.init();
    }
}
