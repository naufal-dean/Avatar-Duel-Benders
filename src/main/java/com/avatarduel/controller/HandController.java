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
     * Card details controller
     */
    private CardDetailsController cardDetailsController;
    /**
     * Active card in hand
     */
    private HandCardController activeHandCard;
    /**
     * Active card in hand set property
     */
    private BooleanProperty activeHandCardSetSignal, directAttackLaunchedSignal, activeRootHandler, discardCardPeriodSignal;
    /**
     * Card click lock
     */
    private boolean disableCardClick;
    /**
     * Shadow effect
     */
    private DropShadow shadowRed, shadowYellow;
    /**
     * The root pane
     */
    @FXML private StackPane rootPane;
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
     * @param cardDetailsController The CardDetailsController
     */
    public HandController(CardDetailsController cardDetailsController) {
        this.cardControllerList = new ArrayList<>();
        this.cardBackControllerList = new ArrayList<>();
        this.activeHandCardSetSignal = new SimpleBooleanProperty(false);
        this.activeRootHandler = new SimpleBooleanProperty(false);
        this.directAttackLaunchedSignal = new SimpleBooleanProperty(false);
        this.discardCardPeriodSignal = new SimpleBooleanProperty(false);
        this.cardDetailsController = cardDetailsController;
        this.disableCardClick = false;
        // Setup red shadow effect
        this.shadowRed = new DropShadow();
        this.shadowRed.setColor(Color.RED);
        this.shadowRed.setWidth(70);
        this.shadowRed.setHeight(70);
        // Setup yellow shadow effect
        this.shadowYellow = new DropShadow();
        this.shadowYellow.setColor(Color.YELLOW);
        this.shadowYellow.setWidth(70);
        this.shadowYellow.setHeight(70);
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
    public BooleanProperty getActiveHandCardSetSignalProperty() {
        return this.activeHandCardSetSignal;
    }

    /**
     * Getter for directAttackLaunched property
     * @return this.directAttackLaunched
     */
    public BooleanProperty getDirectAttackLaunchedSignalProperty() {
        return this.directAttackLaunchedSignal;
    }

    /**
     * Turn on direct attack signal
     */
    public void turnOnDirectAttackLaunchedSignal() {
        this.directAttackLaunchedSignal.setValue(true);
    }

    /**
     * Turn off direct attack signal
     */
    public void turnOffDirectAttackLaunchedSignal() {
        this.directAttackLaunchedSignal.setValue(false);
    }

    /**
     * Getter for directAttackLaunched property
     * @return this.directAttackLaunched
     */
    public BooleanProperty getDiscardCardPeriodSignalProperty() {
        return this.discardCardPeriodSignal;
    }

    /**
     * Turn on direct attack signal
     */
    public void turnOnDiscardCardPeriodSignal() {
        this.discardCardPeriodSignal.setValue(true);
    }

    /**
     * Turn off direct attack signal
     */
    public void turnOffDiscardCardPeriodSignal() {
        this.discardCardPeriodSignal.setValue(false);
    }

    /**
     * Setter for disableCardClick
     * @param disableCardClick The new disableCardClick value
     */
    public void setDisableCardClick(boolean disableCardClick) {
        this.disableCardClick = disableCardClick;
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
            if (GameStatus.getGameStatus().getGamePhase() == Phase.MAIN) {
                if (e.getButton() == MouseButton.PRIMARY && !disableCardClick)
                    onHandCardClickedHandler(cardController); // Set active hand card
            } else if (GameStatus.getGameStatus().getGamePhase() == Phase.END) {
                if (e.getButton() == MouseButton.PRIMARY && discardCardPeriodSignal.get()) {
                    removeCardFromHand(cardController); // Remove card from hand
                    turnOffDiscardCardPeriodSignal();
                }
            }
        });
        // On mouse entered handler
        cardController.getCardAncPane().onMouseEnteredProperty().set((EventHandler<MouseEvent>) (MouseEvent e) -> {
            if (GameStatus.getGameStatus().getGamePhase() == Phase.MAIN) {
                cardController.getCardAncPane().setEffect(this.shadowYellow);
            }
            cardDetailsController.setCard(cardController.getCard());
        });
        // On mouse exited handler
        cardController.getCardAncPane().onMouseExitedProperty().set((EventHandler<MouseEvent>) (MouseEvent e) -> {
            if (GameStatus.getGameStatus().getGamePhase() == Phase.MAIN) {
                if (cardController != activeHandCard)
                    cardController.getCardAncPane().setEffect(null);
            }
            cardDetailsController.removeCard();
        });
        // Add event listener
        this.discardCardPeriodSignal.addListener((observable, oldValue, newValue) -> {
            if (GameStatus.getGameStatus().getGamePhase() == Phase.END) {
                if (oldValue == false && newValue == true)
                    cardController.getCardAncPane().setEffect(shadowYellow);
                else
                    cardController.getCardAncPane().setEffect(null);
            }
        });
        // Set root as hand children node
        this.hand.getChildren().add(root);
        // Add controller to cardControllerList
        this.cardControllerList.add(cardController);
    }

    /**
     * Remove card from hand
     * @param cardController The card controller to be deleted
     */
    public void removeCardFromHand(CardController cardController) {
        int idx = this.cardControllerList.indexOf(cardController);
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
     * Remove active card in hand from hand
     */
    public void removeActiveCardFromHand() {
        if (this.activeHandCard != null) {
            this.removeCardFromHand(this.activeHandCard);
            this.resetActiveHandCard();
        }
    }

    /**
     * Reset active hand card back to null, and the set signal back to false
     */
    public void resetActiveHandCard() {
        this.activeHandCard = null;
        this.activeHandCardSetSignal.setValue(false);
    }

    /**
     * Activate root pane event handler
     */
    public void activateRootEventHandler() {
        this.activeRootHandler.setValue(true);
    }

    /**
     * Deactivate root pane event handler
     */
    public void deactivateRootEventHandler() {
        this.activeRootHandler.setValue(false);
    }

    /**
     * On click handler
     * @param handCardController The HandCard selected
     */
    public void onHandCardClickedHandler(HandCardController handCardController) {
        // Remove old effect if any
        if (this.activeHandCard != null) {
            this.activeHandCard.getCardAncPane().setEffect(null);
        }
        // Check input
        if (this.activeHandCard == handCardController) { // Remove active card
            this.resetActiveHandCard();
        } else { // Add active card
            this.activeHandCardSetSignal.setValue(false);
            this.activeHandCard = handCardController;
            this.activeHandCard.getCardAncPane().setEffect(this.shadowYellow);
            this.activeHandCardSetSignal.setValue(true);
        }
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
     * {@inheritDoc}
     */
    @Override @FXML
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.handField.setAlignment(Pos.CENTER);
        this.hand.setAlignment(Pos.CENTER);
        this.handBack.setAlignment(Pos.CENTER);
        // Add event handler to rootPane
        rootPane.onMouseClickedProperty().set((EventHandler<MouseEvent>) (MouseEvent e) -> {
            if (GameStatus.getGameStatus().getGamePhase() == Phase.BATTLE) {
                if (e.getButton() == MouseButton.PRIMARY && activeRootHandler.get()) {
                    turnOnDirectAttackLaunchedSignal();
                }
            }
        });
        // Add event listener to rootPane
        this.activeRootHandler.addListener((observable, oldValue, newValue) -> {
            if (GameStatus.getGameStatus().getGamePhase() == Phase.BATTLE) {
                if (oldValue == false && newValue == true) {
                    rootPane.setEffect(shadowYellow);
                } else {
                    rootPane.setEffect(null);
                }
            }
        });
    }
}
