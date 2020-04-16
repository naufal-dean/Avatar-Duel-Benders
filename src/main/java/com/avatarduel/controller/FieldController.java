package com.avatarduel.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
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
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

import com.avatarduel.AvatarDuel;
import com.avatarduel.gamephase.Phase;
import com.avatarduel.gameutils.GameStatus;
import com.avatarduel.model.*;
import com.avatarduel.model.Character;

public class FieldController implements Initializable {
    public static final int SKILL_ROW_TOP = 0;
    public static final int CHAR_ROW_TOP = 1;
    public static final int CHAR_ROW_BOT = 2;
    public static final int SKILL_ROW_BOT = 3;
    /**
     * CardController list
     */
    private List<List<CardController>> cardControllerList;
    /**
     * Card details controller
     */
    private CardDetailsController cardDetailsController;
    /**
     * Cell active handler property list
     */
    private List<List<BooleanProperty>> activeHandler;
    /**
     * Shadow effect
     */
    private DropShadow cellAvailableShadow, cellHoverShadow;
    /**
     * Card that waiting to be summoned
     */
    private HandCardController waitingHandCard;
    /**
     * Card summoned property
     */
    private BooleanProperty cardSummonedSignal;
    /**
     * Field grid
     */
    @FXML private GridPane field;
    /**
     * Field background
     */
    @FXML private ImageView fieldGrid;

    /**
     * Constructor
     * @param cardDetailsController The CardDetailsController
     */
    public FieldController(CardDetailsController cardDetailsController) {
        this.cardControllerList = new ArrayList<>();
        for (int i = 0; i < 6; i++)
            this.cardControllerList.add(Arrays.asList(null, null, null, null));
        this.activeHandler = new ArrayList<>();;
        for (int i = 0; i < 6; i++)
            this.activeHandler.add(Arrays.asList(new SimpleBooleanProperty(false), new SimpleBooleanProperty(false),
                                                 new SimpleBooleanProperty(false), new SimpleBooleanProperty(false)));

        this.cardSummonedSignal = new SimpleBooleanProperty(false);
        this.cardDetailsController = cardDetailsController;
        // Cell available shadow
        this.cellAvailableShadow = new DropShadow();
        this.cellAvailableShadow.setColor(Color.YELLOW);
        this.cellAvailableShadow.setWidth(50);
        this.cellAvailableShadow.setHeight(50);
        this.cellAvailableShadow.setSpread(0.9);
        // Cell hover shadow
        this.cellHoverShadow = new DropShadow();
        this.cellHoverShadow.setColor(Color.RED);
        this.cellHoverShadow.setWidth(50);
        this.cellHoverShadow.setHeight(50);
        this.cellHoverShadow.setSpread(0.9);
    }

    /**
     * Getter for card controller
     * @param x The grid column index
     * @param y The grid row index
     * @return Controller for the (x, y) Card
     */
    public CardController getCardController(int x, int y) {
        return this.cardControllerList.get(x).get(y);
    }

    /**
     * Getter for cardSummoned property
     * @return this.cardSummoned
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
     * Setter for waiting hand card
     * @param waitingHandCard The new waiting hand card (card type CHARACTER or SKILL AURA)
     */
    public void setWaitingHandCard(HandCardController waitingHandCard) {
        this.waitingHandCard = waitingHandCard;
        // Activate event handler on the right target cells
        int row;
        if (waitingHandCard.getOwner() == Player.BOTTOM) {
            row = (waitingHandCard.getCard().getCardType() == CardType.CHARACTER) ? (CHAR_ROW_BOT) : (SKILL_ROW_BOT);
        } else {
            row = (waitingHandCard.getCard().getCardType() == CardType.CHARACTER) ? (CHAR_ROW_TOP) : (SKILL_ROW_TOP);
        }
        for (int col = 0; col < 6; col++) {
            if (this.cardControllerList.get(col).get(row) == null) {
                this.activateCellEventHandler(col, row);
            }
        }
    }

    /**
     * Reset waiting hand card
     */
    public void resetWaitingHandCard() {
        this.waitingHandCard = null;
    }

    /**
     * Set card on the cell x, y in field
     * @param card The card to be displayed
     * @param owner The owner of the card
     * @param x The grid column index
     * @param y The grid row index
     * @throws IOException From FXML loader
     */
    public void setCardOnField(Card card, Player owner, boolean isAttack, int x, int y) throws IOException {
        if (this.cardControllerList.get(x).get(y) == null) {
            // Create loader
            FXMLLoader loader = new FXMLLoader();
            if (card.getCardType().equals(CardType.CHARACTER)) {
                SummonedCharacterCardController cardController = new SummonedCharacterCardController((Character) card, owner, x, y, isAttack);
                loader.setController(cardController);
                this.cardControllerList.get(x).set(y, cardController);
            } else if (card.getCardType().equals(CardType.SKILL)) {
                SummonedSkillCardController cardController = new SummonedSkillCardController((SkillAura) card, owner, x, y, null);
                loader.setController(cardController);
                this.cardControllerList.get(x).set(y, cardController);
            }
            loader.setLocation(AvatarDuel.class.getResource("view/Card.fxml"));

            // Create root
            StackPane root = loader.load();
            // Add event handler
            CardController c = this.cardControllerList.get(x).get(y);
            // Add event handler
            if (c != null) {
                c.getCardAncPane().onMouseEnteredProperty().set((EventHandler<MouseEvent>) (MouseEvent e) -> {
                    cardDetailsController.setCard(c.getCard());
                });
                c.getCardAncPane().onMouseExitedProperty().set((EventHandler<MouseEvent>) (MouseEvent e) -> {
                    cardDetailsController.removeCard();
                });
            }
            // Create scale for root
            DoubleProperty scale = new SimpleDoubleProperty(0.4);
            root.scaleXProperty().bind(scale);
            root.scaleYProperty().bind(scale);
            root.setPrefWidth(root.getPrefWidth() * scale.doubleValue());
            root.setPrefHeight(root.getPrefHeight() * scale.doubleValue());
            // Set child alignment
            GridPane.setHalignment(root, HPos.CENTER);
            GridPane.setValignment(root, VPos.CENTER);
            // Set root as field children node
            this.field.add(root, x, y);
        }
    }

    /**
     * Remove card on the cell x, y in field
     * @param x The grid column index
     * @param y The grid row index
     */
    public void removeCardFromField(int x, int y) {
        for (Node node : this.field.getChildren()) {
            if(node instanceof StackPane && this.field.getColumnIndex(node) == x && this.field.getRowIndex(node) == y) {
                // Remove field children node
                this.field.getChildren().remove((StackPane) node);
                // Remove cardController
                this.cardControllerList.get(x).set(y, null);
                break;
            }
        }
    }

    /**
     * Activate event handler on cell (x, y)
     * @param x The row coordinate
     * @param y The column coordinate
     */
    public void activateCellEventHandler(int x, int y) {
        this.activeHandler.get(x).get(y).setValue(true);
    }

    /**
     * Deactivate event handler on entire field
     */
    public void clearCellEventHandler() {
        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 6; x++) {
                this.activeHandler.get(x).get(y).setValue(false);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override @FXML
    public void initialize(URL url, ResourceBundle resources) {
        this.fieldGrid.setImage(new Image(AvatarDuel.class.getResource("background/field_grid.png").toString()));
        this.field.setAlignment(Pos.CENTER);
        // Fill empty cell with pane
        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 6; x++) {
                Pane emptyCell = new Pane();
                emptyCell.setStyle("-fx-border-color: black");

                // Add event handler
                int row = y, col = x;
                // On mouse clicked handler
                emptyCell.onMouseClickedProperty().set((EventHandler<MouseEvent>) (MouseEvent e) -> {
                    if (activeHandler.get(col).get(row).get() && GameStatus.getGameStatus().getGamePhase() == Phase.MAIN) {
                        CardType cardType = waitingHandCard.getCard().getCardType();
                        try {
                            if (cardType == CardType.CHARACTER) {
                                if (e.getButton() == MouseButton.PRIMARY) {
                                    setCardOnField(waitingHandCard.getCard(), waitingHandCard.getOwner(), true, col, row);
                                } else if (e.getButton() == MouseButton.SECONDARY) {
                                    setCardOnField(waitingHandCard.getCard(), waitingHandCard.getOwner(), false, col, row);
                                }
                            } else if (cardType == CardType.SKILL) {
                                if (e.getButton() == MouseButton.PRIMARY)
                                    setCardOnField(waitingHandCard.getCard(), waitingHandCard.getOwner(), true, col, row);
                            }
                            // Send card summoned signal
                            turnOnCardSummonedSignal();
                        } catch (IOException err) {
                            System.out.println(err.toString());
                        }
                    }
                });
                // On mouse entered handler
                emptyCell.onMouseEnteredProperty().set((EventHandler<MouseEvent>) (MouseEvent e) -> {
                    if (activeHandler.get(col).get(row).get() && GameStatus.getGameStatus().getGamePhase() == Phase.MAIN) {
                        emptyCell.setEffect(cellHoverShadow);
                    }
                });
                // On mouse exited handler
                emptyCell.onMouseExitedProperty().set((EventHandler<MouseEvent>) (MouseEvent e) -> {
                    if (activeHandler.get(col).get(row).get() && GameStatus.getGameStatus().getGamePhase() == Phase.MAIN) {
                        emptyCell.setEffect(cellAvailableShadow);
                    }
                });

                // Add event listener
                this.activeHandler.get(col).get(row).addListener((observable, oldValue, newValue) -> {
                    if (GameStatus.getGameStatus().getGamePhase() == Phase.MAIN) {
                        // Field effect
                        if (oldValue == false && newValue == true) {
                            emptyCell.setEffect(cellAvailableShadow);
                        } else {
                            emptyCell.setEffect(null);
                        }
                    }
                });

                // Add emptyCell object to field
                this.field.add(emptyCell, x, y);
            }
        }
    }
}
