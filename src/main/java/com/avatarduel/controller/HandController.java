package com.avatarduel.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import com.avatarduel.AvatarDuel;
import com.avatarduel.model.*;

public class HandController implements Initializable {
    /**
     * Card controller list
     */
    private List<HandCardController> cardControllerList;
    /**
     * Card back controller list
     */
    private List<CardBackController> cardBackControllerList;
    /**
     * Display card in hand
     */
    @FXML private HBox hand;
    /**
     * Display card back in hand
     */
    @FXML private HBox handBack;
    /**
     * Field to place hand
     */
    @FXML private StackPane handField;
    /**
     * Wrapper for all of hand display
     */
    @FXML private VBox handWrapper;

    /**
     * Constructor
     */
    public HandController() {
        this.cardControllerList = new ArrayList<>();
        this.cardBackControllerList = new ArrayList<>();
    }

    /**
     * Add card to player hands
     * @param card The card to be displayed
     * @param owner The owner of the card
     * @throws IOException From FXML loader
     */
    public void addCardOnHand(Card card, Player owner) throws IOException {
        // Add card back
        // Create loader
        FXMLLoader loaderBack = new FXMLLoader();
        CardBackController cardBackController = new CardBackController();
        loaderBack.setController(cardBackController);
        loaderBack.setLocation(AvatarDuel.class.getResource("view/CardBack.fxml"));
        // Create root
        StackPane rootBack = loaderBack.load();
        // Scale card
        DoubleProperty scale = new SimpleDoubleProperty(0.5);
        rootBack.scaleXProperty().bind(scale);
        rootBack.scaleYProperty().bind(scale);
        rootBack.setPrefWidth(rootBack.getPrefWidth() * scale.doubleValue());
        rootBack.setPrefHeight(rootBack.getPrefHeight() * scale.doubleValue());
        // Set rootBack as handBack children node
        this.handBack.getChildren().add(rootBack);
        // Add controller to cardControllerList
        this.cardBackControllerList.add(cardBackController);

        // Add card
        // Create loader
        FXMLLoader loader = new FXMLLoader();
        HandCardController cardController = new HandCardController(card, owner, this.cardControllerList.size(), cardBackController);
        loader.setController(cardController);
        loader.setLocation(AvatarDuel.class.getResource("view/Card.fxml"));
        // Create root
        StackPane root = loader.load();
        // Scale card
        root.scaleXProperty().bind(scale);
        root.scaleYProperty().bind(scale);
        root.setPrefWidth(root.getPrefWidth() * scale.doubleValue());
        root.setPrefHeight(root.getPrefHeight() * scale.doubleValue());
        // Set root as hand children node
        this.hand.getChildren().add(root);
        // Add controller to cardControllerList
        this.cardControllerList.add(cardController);
    }

    /**
     * Remove card on the cell x, y in field
     * @param col The HBox column index
     */
    public void removeCardOnHand(int col) {
        // Remove card
        Node node = this.hand.getChildren().get(col);
        this.hand.getChildren().remove((StackPane) node);
        this.cardControllerList.remove(col);
        // Remove card back
        Node nodeBack = this.handBack.getChildren().get(col);
        this.handBack.getChildren().remove((StackPane) nodeBack);
        this.cardBackControllerList.remove(col);
    }

    /**
     * Flip the hand display
     */
    public void rotateHandDisplay() {
        this.handWrapper.setRotate(180);
    }

    /**
     * Flip card in hand
     */
    public void flipCardInHand() {
        for (HandCardController handCardController : this.cardControllerList)
            handCardController.flipCard();
    }

    /**
     * {@inheritDoc}
     */
    @Override @FXML
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.handField.setAlignment(Pos.CENTER);
        this.hand.setAlignment(Pos.CENTER);
        this.handBack.setAlignment(Pos.CENTER);
        // TODO: this handler is for debugging, remove after use
        this.handField.onMouseClickedProperty().set((EventHandler<MouseEvent>) (MouseEvent e) -> {
            if (e.getButton() == MouseButton.PRIMARY) {
                this.flipCardInHand();
            }
        });
    }
}
