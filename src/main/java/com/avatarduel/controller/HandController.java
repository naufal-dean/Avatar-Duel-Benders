package com.avatarduel.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import com.avatarduel.AvatarDuel;
import com.avatarduel.gamephase.Phase;
import com.avatarduel.gameutils.GameStatus;
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
     * Active card in hand
     */
    private HandCardController activeHandCard;
    /**
     * Active card in hand set property
     */
    private BooleanProperty activeHandCardSet;
    /**
     * Shadow effect
     */
    private DropShadow activeShadow;
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
        this.activeHandCardSet = new SimpleBooleanProperty(false);
        // Setup selected shadow effect
        this.activeShadow = new DropShadow();
        this.activeShadow.setColor(Color.RED);
        this.activeShadow.setWidth(70);
        this.activeShadow.setHeight(70);
    }

    /**
     * Get card controller list
     * @return this.cardControllerList
     */
    public List<HandCardController> getCardControllerList() {
        return this.cardControllerList;
    }

    /**
     * Getter for active hand card
     * @return this.activeHandCard
     */
    public HandCardController getActiveHandCard() {
        return this.activeHandCard;
    }

    /**
     * Getter for activeHandCardSet property
     * @return this.activeHandCardSet
     */
    public BooleanProperty getActiveHandCardSetProperty() {
        return this.activeHandCardSet;
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
        HandCardController cardController = new HandCardController(card, owner, cardBackController);
        loader.setController(cardController);
        loader.setLocation(AvatarDuel.class.getResource("view/Card.fxml"));
        // Create root
        StackPane root = loader.load();
        // Scale card
        root.scaleXProperty().bind(scale);
        root.scaleYProperty().bind(scale);
        root.setPrefWidth(root.getPrefWidth() * scale.doubleValue());
        root.setPrefHeight(root.getPrefHeight() * scale.doubleValue());
        // Add event handler
        // On mouse clicked handler
        cardController.getCardAncPane().onMouseClickedProperty().set((EventHandler<MouseEvent>) (MouseEvent e) -> {
            if (e.getButton() == MouseButton.PRIMARY && GameStatus.getGameStatus().getGamePhase() == Phase.MAIN)
                onHandCardClickedHandler(cardController); // Set active hand card
        });
        // On mouse entered handler
        cardController.getCardAncPane().onMouseEnteredProperty().set((EventHandler<MouseEvent>) (MouseEvent e) -> {
            if (GameStatus.getGameStatus().getGamePhase() == Phase.MAIN) {
                cardController.getCardAncPane().setEffect(this.activeShadow);
                // TODO: set card detail
            }
        });
        // On mouse exited handler
        cardController.getCardAncPane().onMouseExitedProperty().set((EventHandler<MouseEvent>) (MouseEvent e) -> {
            if (GameStatus.getGameStatus().getGamePhase() == Phase.MAIN) {
                if (cardController != activeHandCard)
                    cardController.getCardAncPane().setEffect(null);
            }
        });
        // Set root as hand children node
        this.hand.getChildren().add(root);
        // Add controller to cardControllerList
        this.cardControllerList.add(cardController);
    }

    /**
     * Remove card on the cell x, y in field
     * @param cardController The card controller to be deleted
     */
    public void removeCardOnHand(CardController cardController) {
        int idx = this.cardControllerList.indexOf(cardController);
        // TODO: remove assertion
        assert this.cardControllerList.indexOf(cardController) == this.cardControllerList.lastIndexOf(cardController);
        // Remove card
        Node node = this.hand.getChildren().get(idx);
        this.hand.getChildren().remove((StackPane) node);
        this.cardControllerList.remove(idx);
        // Remove card back
        Node nodeBack = this.handBack.getChildren().get(idx);
        this.handBack.getChildren().remove((StackPane) nodeBack);
        this.cardBackControllerList.remove(idx);
    }

    /**
     * Rotate the hand display
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
     * On click handler
     * @param handCardController The HandCard selected
     */
    public void onHandCardClickedHandler(HandCardController handCardController) {
        // Remove old effect if any
        if (this.activeHandCard != null) {
            this.activeHandCard.getCardAncPane().setEffect(null);
            this.activeHandCardSet.setValue(false);
        }
        // Check input
        if (this.activeHandCard == handCardController) { // Remove active card
            this.activeHandCard = null;
        } else { // Add active card
            this.activeHandCard = handCardController;
            this.activeHandCard.getCardAncPane().setEffect(this.activeShadow);
            this.activeHandCardSet.setValue(true);
        }
    }

    /**
     * Remove active hand card
     */
    public void removeActiveHandCard() {
        this.activeHandCard = null;
        this.activeHandCardSet.setValue(false);
    }

    /**
     * {@inheritDoc}
     */
    @Override @FXML
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.handField.setAlignment(Pos.CENTER);
        this.hand.setAlignment(Pos.CENTER);
        this.handBack.setAlignment(Pos.CENTER);
    }
}
