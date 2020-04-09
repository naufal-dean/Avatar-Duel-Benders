package com.avatarduel.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import com.avatarduel.AvatarDuel;
import com.avatarduel.model.*;

public class HandController implements Initializable {
    /**
     * Display card in hand
     */
    @FXML private HBox hand;
    /**
     * Field to place hand
     */
    @FXML private StackPane handField;
    /**
     * Wrapper for all of hand display
     */
    @FXML private VBox handWrapper;
    /**
     * Card controller list
     */
    private List<CardController> cardControllerList;


    /**
     * Constructor
     */
    public HandController() {
        this.cardControllerList = new ArrayList<>();
    }

    /**
     * Add card to player hands
     * @param card The card to be displayed
     * @param owner The owner of the card
     * @throws IOException From FXML loader
     */
    public void addCardOnHand(Card card, Player owner) throws IOException {
        // Create loader
        FXMLLoader loader = new FXMLLoader();
        HandCardController cardController = new HandCardController(card, owner, this.cardControllerList.size());
        loader.setController(cardController);
        loader.setLocation(AvatarDuel.class.getResource("view/Card.fxml"));

        // Create root
        StackPane root = loader.load();
        // Scale card
        DoubleProperty scale = new SimpleDoubleProperty(0.5);
        root.scaleXProperty().bind(scale);
        root.scaleYProperty().bind(scale);
        root.setPrefWidth(root.getPrefWidth() * scale.doubleValue());
        root.setPrefHeight(root.getPrefHeight() * scale.doubleValue());
        // Set root as field children node
        this.hand.getChildren().add(root);
        // Add controller to cardControllerList
        this.cardControllerList.add(cardController);
    }

    /**
     * Remove card on the cell x, y in field
     * @param col The HBox column index
     */
    public void removeCardOnHand(int col) {
        Node node = this.hand.getChildren().get(col);
        this.hand.getChildren().remove((StackPane) node);
        this.cardControllerList.remove(col);
    }

    /**
     * Flip the hand display
     */
    public void flipHandDisplay() {
        this.handWrapper.setRotate(180);
    }

    /**
     * {@inheritDoc}
     */
    @Override @FXML
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.handField.setAlignment(Pos.CENTER);
        this.hand.setAlignment(Pos.CENTER);
    }
}
