package com.avatarduel.gameassets;

import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;

public class GameDropShadow {
    /**
     * Class singleton
     */
    private static GameDropShadow gameDropShadow;
    /**
     * The drop shadow database
     */
    private DropShadow shadowRedField, shadowYellowField, shadowGreenField, shadowRedHand, shadowYellowHand, shadowYellowPower, shadowGreenPower;

    /**
     * Constructor
     */
    private GameDropShadow() {
        // Field
        // Shadow red
        this.shadowRedField = new DropShadow();
        this.shadowRedField.setColor(Color.RED);
        this.shadowRedField.setWidth(40);
        this.shadowRedField.setHeight(40);
        this.shadowRedField.setSpread(0.85);
        // Shadow yellow
        this.shadowYellowField = new DropShadow();
        this.shadowYellowField.setColor(Color.YELLOW);
        this.shadowYellowField.setWidth(40);
        this.shadowYellowField.setHeight(40);
        this.shadowYellowField.setSpread(0.85);
        // Shadow green
        this.shadowGreenField = new DropShadow();
        this.shadowGreenField.setColor(Color.GREEN);
        this.shadowGreenField.setWidth(40);
        this.shadowGreenField.setHeight(40);
        this.shadowGreenField.setSpread(0.85);

        // Hand
        // Shadow red
        this.shadowRedHand = new DropShadow();
        this.shadowRedHand.setColor(Color.RED);
        this.shadowRedHand.setWidth(70);
        this.shadowRedHand.setHeight(70);
        // Shadow yellow
        this.shadowYellowHand = new DropShadow();
        this.shadowYellowHand.setColor(Color.YELLOW);
        this.shadowYellowHand.setWidth(70);
        this.shadowYellowHand.setHeight(70);

        // Power
        // Shadow yellow
        this.shadowYellowPower = new DropShadow();
        this.shadowYellowPower.setColor(Color.YELLOW);
        this.shadowYellowPower.setWidth(30);
        this.shadowYellowPower.setHeight(30);
        this.shadowYellowPower.setSpread(0.7);
        // Shadow green yellow
        this.shadowGreenPower = new DropShadow();
        this.shadowGreenPower.setColor(Color.GREENYELLOW);
        this.shadowGreenPower.setWidth(30);
        this.shadowGreenPower.setHeight(30);
        this.shadowGreenPower.setSpread(0.7);
    }

    /**
     * Get class instance
     */
    public static GameDropShadow getGameDropShadow() {
        if (gameDropShadow == null)
            gameDropShadow = new GameDropShadow();
        return gameDropShadow;
    }

    public DropShadow getShadowRedField() {
        return this.shadowRedField;
    }

    public DropShadow getShadowYellowField() {
        return this.shadowYellowField;
    }

    public DropShadow getShadowGreenField() {
        return this.shadowGreenField;
    }

    public DropShadow getShadowRedHand() {
        return this.shadowRedHand;
    }

    public DropShadow getShadowYellowHand() {
        return this.shadowYellowHand;
    }

    public DropShadow getShadowYellowPower() {
        return this.shadowYellowPower;
    }

    public DropShadow getShadowGreenPower() {
        return this.shadowGreenPower;
    }
}
