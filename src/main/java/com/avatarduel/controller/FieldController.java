package com.avatarduel.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import javafx.beans.property.*;
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

import com.avatarduel.AvatarDuel;
import com.avatarduel.gameassets.GameDropShadow;
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
    private List<List<BooleanProperty>> activeCellHandler, activeSummCardHandler;
    /**
     * Shadow effect
     */
    private DropShadow shadowYellow, shadowRed, shadowGreen;
    /**
     * Card that waiting to be summoned
     */
    private HandCardController waitingHandCardController;
    /**
     * Current selected character card controller in field
     */
    private SummonedCharacterCardController activeFieldCardController;
    /**
     * Current summoned skill card that need to be attached
     */
    private SummonedSkillCardController skillCardControllerToBeAttached;
    /**
     * Boolean property as signal emitter
     */
    private BooleanProperty cardSummonedSignal, activeFieldCardSetSignal, directAttackReadySignal, attachSkillPeriodSignal;
    /**
     * Integer property as damage emitter
     */
    private IntegerProperty damageDealtSignal;
    /**
     * Card click lock
     */
    private boolean disableCardClick;
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
        this.activeCellHandler = new ArrayList<>();
        for (int i = 0; i < 6; i++)
            this.activeCellHandler.add(Arrays.asList(new SimpleBooleanProperty(false), new SimpleBooleanProperty(false),
                                                 new SimpleBooleanProperty(false), new SimpleBooleanProperty(false)));
        this.activeSummCardHandler = new ArrayList<>();
        for (int i = 0; i < 6; i++)
            this.activeSummCardHandler.add(Arrays.asList(new SimpleBooleanProperty(false), new SimpleBooleanProperty(false),
                    new SimpleBooleanProperty(false), new SimpleBooleanProperty(false)));
        this.cardSummonedSignal = new SimpleBooleanProperty(false);
        this.activeFieldCardSetSignal = new SimpleBooleanProperty(false);
        this.directAttackReadySignal = new SimpleBooleanProperty(false);
        this.attachSkillPeriodSignal = new SimpleBooleanProperty(false);
        this.damageDealtSignal = new SimpleIntegerProperty(-1);
        this.cardDetailsController = cardDetailsController;
        this.disableCardClick = false;
        // Drop Shadow
        this.shadowRed = GameDropShadow.getGameDropShadow().getShadowRedField();
        this.shadowYellow = GameDropShadow.getGameDropShadow().getShadowYellowField();
        this.shadowGreen = GameDropShadow.getGameDropShadow().getShadowGreenField();
    }

    /**
     * Getter for activeFieldCardController
     * @return  this.activeFieldCardController
     */
    public SummonedCharacterCardController getActiveFieldCardController() {
        return this.activeFieldCardController;
    }

    /**
     * Getter for activeSummCardHandler
     * @return this.activeSummCardHandler
     */
    public List<List<BooleanProperty>> getActiveSummCardHandler() {
        return this.activeSummCardHandler;
    }

    /**
     * Getter for skillCardControllerToBeAttached
     * @return this.skillCardControllerToBeAttached
     */
    public SummonedSkillCardController getSkillCardControllerToBeAttached() {
        return this.skillCardControllerToBeAttached;
    }

    /**
     * Getter for cardDetailsController
     * @return this.cardDetailsController
     */
    public CardDetailsController getCardDetailsController() {
        return this.cardDetailsController;
    }

    /**
     * Setter for skillCardControllerToBeAttached
     * @param skillCardControllerToBeAttached The new skillCardControllerToBeAttached
     */
    public void setSkillCardControllerToBeAttached(SummonedSkillCardController skillCardControllerToBeAttached) {
        this.skillCardControllerToBeAttached = skillCardControllerToBeAttached;
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
     * Getter for directAttackSignal property
     * @return this.directAttackSignal
     */
    public IntegerProperty getDamageDealtSignalProperty() {
        return this.damageDealtSignal;
    }

    /**
     * Turn on direct attack signal
     * @param damage The damage dealt, -1 means off
     */
    public void setDamageDealtSignal(int damage) {
        this.damageDealtSignal.setValue(damage);
    }

    /**
     * Getter for directAttackSignal property
     * @return this.directAttackSignal
     */
    public BooleanProperty getDirectAttackReadySignalProperty() {
        return this.directAttackReadySignal;
    }

    /**
     * Turn on direct attack signal
     */
    public void turnOnDirectAttackReadySignal() {
        this.directAttackReadySignal.setValue(true);
    }

    /**
     * Turn off direct attack signal
     */
    public void turnOffDirectAttackReadySignal() {
        this.directAttackReadySignal.setValue(false);
    }

    /**
     * Getter for attachSkillPeriodSignal property
     * @return this.attachSkillPeriodSignal
     */
    public BooleanProperty getAttachSkillPeriodSignalProperty() {
        return this.attachSkillPeriodSignal;
    }

    /**
     * Turn on attachSkillPeriodSignal signal
     */
    public void turnOnAttachSkillPeriodSignal() {
        this.attachSkillPeriodSignal.setValue(true);
    }

    /**
     * Turn off attachSkillPeriodSignal signal
     */
    public void turnOffAttachSkillPeriodSignal() {
        this.attachSkillPeriodSignal.setValue(false);
    }

    /**
     * Getter for activeFieldCardSetSignal property
     * @return this.activeFieldCardSetSignal
     */
    public BooleanProperty getActiveFieldCardSetSignalProperty() {
        return this.activeFieldCardSetSignal;
    }

    /**
     * Getter for disableCardClick
     * @return this.disableCardClick
     */
    public boolean getDisableCardClick() {
        return this.disableCardClick;
    }

    /**
     * Setter for disableCardClick
     * @param disableCardClick The new disableCardClick value
     */
    public void setDisableCardClick(boolean disableCardClick) {
        this.disableCardClick = disableCardClick;
    }

    /**
     * Setter for waiting hand card
     * @param waitingHandCardController The new waiting hand card (card type CHARACTER or SKILL AURA)
     */
    public void setWaitingHandCardController(HandCardController waitingHandCardController) {
        this.waitingHandCardController = waitingHandCardController;
        // Activate event handler on the right target cells
        int row;
        if (waitingHandCardController.getOwner() == Player.BOTTOM) {
            row = (waitingHandCardController.getCard().getCardType() == CardType.CHARACTER) ? (CHAR_ROW_BOT) : (SKILL_ROW_BOT);
        } else {
            row = (waitingHandCardController.getCard().getCardType() == CardType.CHARACTER) ? (CHAR_ROW_TOP) : (SKILL_ROW_TOP);
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
    public void resetWaitingHandCardController() {
        this.waitingHandCardController = null;
    }

    /**
     * Reset active field card
     */
    public void resetActiveFieldCardController() {
        if (this.activeFieldCardController == null)
            return;
        this.activeFieldCardController.getCardAncPane().setEffect(null);
        this.activeFieldCardController = null;
        this.activeFieldCardSetSignal.setValue(false);
        this.turnOffDirectAttackReadySignal();
    }

    /**
     * Destroy skill card that failed to be attached, e.g. not attached before main phase end
     */
    public void destroySkillCardControllerToBeAttached() {
        if (this.skillCardControllerToBeAttached == null)
            return;
        this.removeCardFromField(this.skillCardControllerToBeAttached.getX(), this.skillCardControllerToBeAttached.getY());
        turnOffAttachSkillPeriodSignal();
    }

    /**
     * Reset summoned character card hadAttacked status
     */
    public void resetSummCardHadAttackedStatus() {
        int y = (GameStatus.getGameStatus().getGameActivePlayer() == Player.BOTTOM) ? (CHAR_ROW_BOT) : (CHAR_ROW_TOP);
        for (int x = 0; x < 6; x++) {
            if (this.cardControllerList.get(x).get(y) != null)
                ((SummonedCharacterCardController) this.cardControllerList.get(x).get(y)).setHadAttacked(false);
        }
    }

    /**
     * Set sword show status in summoned character card
     */
    public void setSummCardShowSword(boolean showSword) {
        int y = (GameStatus.getGameStatus().getGameActivePlayer() == Player.BOTTOM) ? (CHAR_ROW_BOT) : (CHAR_ROW_TOP);
        for (int x = 0; x < 6; x++) {
            if (this.cardControllerList.get(x).get(y) != null) {
                SummonedCharacterCardController sc = (SummonedCharacterCardController) this.cardControllerList.get(x).get(y);
                sc.setShowSword(showSword && !sc.getHadAttacked() && sc.getIsAttack());
            }
        }
    }

    /**
     * Set card on the cell x, y in field
     * @param card The card to be displayed (Card type only CHARACTER and SKILL AURA)
     * @param owner The owner of the card
     * @param x The grid column index
     * @param y The grid row index
     * @throws IOException From FXML loader
     */
    public void setCardOnField(Card card, Player owner, boolean isAttack, int x, int y) throws IOException {
        if (this.cardControllerList.get(x).get(y) == null) {
            // Create loader
            FXMLLoader loader = new FXMLLoader();
            SummonedCardController cardController;
            if (card instanceof Character) {
                cardController = new SummonedCharacterCardController((Character) card, owner, x, y, isAttack);
            } else if (card instanceof Skill) {
                cardController = new SummonedSkillCardController((Skill) card, owner, x, y);
            } else {
                return;
            }
            loader.setController(cardController);
            loader.setLocation(AvatarDuel.class.getResource("view/Card.fxml"));
            StackPane root = loader.load();
            // Setup event handler for the controller
            if (cardController != null) {
                cardController = SummonedCardControllerFactory.setupSummonedCardController(this, cardController);
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
            // Save controller
            this.cardControllerList.get(x).set(y, cardController);
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
                if (this.cardControllerList.get(x).get(y) instanceof SummonedCharacterCardController) {
                    // If char card, delete the attached skill aura and power up if any
                    SummonedCharacterCardController scCardController = (SummonedCharacterCardController) this.cardControllerList.get(x).get(y);
                    while (scCardController.getAttachedAuraControllerList().size() > 0) {
                        SummonedSkillCardController aura = scCardController.getAttachedAuraControllerList().remove(0);
                        this.removeCardFromField(aura.getX(), aura.getY());
                    }
                    while (scCardController.getAttachedPowerUpControllerList().size() > 0) {
                        SummonedSkillCardController powerUp = scCardController.getAttachedPowerUpControllerList().remove(0);
                        this.removeCardFromField(powerUp.getX(), powerUp.getY());
                    }
                } else if (this.cardControllerList.get(x).get(y) instanceof SummonedSkillCardController) {
                    // If skill card, detach from its parent char card
                    SummonedSkillCardController ssCardController = (SummonedSkillCardController) this.cardControllerList.get(x).get(y);
                    int targetX, targetY;
                    if ((targetX = ssCardController.getTargetX()) != -1 && (targetY = ssCardController.getTargetY()) != -1) {
                        if (this.cardControllerList.get(targetX).get(targetY) != null &&
                                this.cardControllerList.get(targetX).get(targetY) instanceof SummonedCharacterCardController) {
                            ((SummonedCharacterCardController) this.cardControllerList.get(targetX).get(targetY)).removeSkillCard(ssCardController);
                        }
                    }
                }
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
        this.activeCellHandler.get(x).get(y).setValue(true);
    }

    /**
     * Deactivate cell event handler on entire field
     */
    public void clearCellEventHandler() {
        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 6; x++) {
                this.activeCellHandler.get(x).get(y).setValue(false);
            }
        }
    }

    /**
     * Deactivate summoned card event handler on entire field
     */
    public void clearSummCardHandler() {
        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 6; x++) {
                this.activeSummCardHandler.get(x).get(y).setValue(false);
            }
        }
    }

    /**
     * On summoned click handler
     * @param scCardController The SummonedCharacterCardController selected
     */
    public void onSummonedCharCardClickHandler(SummonedCharacterCardController scCardController) {
        // Remove old effect if any
        if (this.activeFieldCardController != null) {
            this.activeFieldCardController.getCardAncPane().setEffect(null);
        }
        // Check input
        if (this.activeFieldCardController == scCardController) { // Remove active card
            this.resetActiveFieldCardController();
        } else { // Add active card
            this.activeFieldCardSetSignal.setValue(false);
            this.turnOffDirectAttackReadySignal();
            this.activeFieldCardController = scCardController;
            this.activeFieldCardController.getCardAncPane().setEffect(this.shadowRed);
            this.activeFieldCardSetSignal.setValue(true);
        }
        // Turn on direct attack signal if enemy summoned char card is 0, and any active char card in field
        if (this.activeFieldCardController == null)
            return;
        int row = (GameStatus.getGameStatus().getGameActivePlayer() == Player.BOTTOM) ? (CHAR_ROW_TOP) : (CHAR_ROW_BOT);
        for (int col = 0; col < 6; col++)
            if (this.cardControllerList.get(col).get(row) != null)
                return;
        this.turnOnDirectAttackReadySignal();
    }

    /**
     * Initialize battle, this.activeFieldCardController attack scCardController
     * @param scCardController Attack target char card
     */
    public void initBattle(SummonedCharacterCardController scCardController) {
        if (scCardController.getIsAttack() || this.activeFieldCardController.isPoweredUp())
            this.setDamageDealtSignal(this.activeFieldCardController.getCardValue() - scCardController.getCardValue());
        if (GameStatus.getGameStatus().getGameWinner() != null)
            return;
        this.removeCardFromField(scCardController.getX(), scCardController.getY());
        this.activeFieldCardController.setHadAttacked(true);
        this.activeFieldCardController.setShowSword(false);
        this.resetActiveFieldCardController();
    }

    /**
     * {@inheritDoc}
     */
    @Override @FXML
    public void initialize(URL url, ResourceBundle resources) {
        this.fieldGrid.setImage(new Image(AvatarDuel.class.getResource("img/background/field_grid.png").toString()));
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
                    if (activeCellHandler.get(col).get(row).get() && GameStatus.getGameStatus().getGamePhase() == Phase.MAIN) {
                        Card card = waitingHandCardController.getCard();
                        try {
                            if (card instanceof Character) {
                                if (e.getButton() == MouseButton.PRIMARY) {
                                    setCardOnField(card, waitingHandCardController.getOwner(), true, col, row);
                                    turnOnCardSummonedSignal();
                                } else if (e.getButton() == MouseButton.SECONDARY) {
                                    setCardOnField(card, waitingHandCardController.getOwner(), false, col, row);
                                    turnOnCardSummonedSignal();
                                }
                            } else if (card instanceof Skill) {
                                if (e.getButton() == MouseButton.PRIMARY) {
                                    setCardOnField(card, waitingHandCardController.getOwner(), true, col, row);
                                    turnOnCardSummonedSignal();
                                    for (int innerY = 1; innerY <= 2; innerY++) {
                                        for (int innerX = 0; innerX < 6; innerX++) {
                                            if (this.cardControllerList.get(innerX).get(innerY) != null) {
                                                skillCardControllerToBeAttached = (SummonedSkillCardController) cardControllerList.get(col).get(row);
                                                turnOnAttachSkillPeriodSignal();
                                                return;
                                            }
                                        }
                                    }
                                    removeCardFromField(col, row);  // No char card in field, destroy skill card
                                }
                            }
                        } catch (IOException err) {
                            System.out.println(err.toString());
                        }
                    }
                });
                // On mouse entered handler
                emptyCell.onMouseEnteredProperty().set((EventHandler<MouseEvent>) (MouseEvent e) -> {
                    if (activeCellHandler.get(col).get(row).get() && GameStatus.getGameStatus().getGamePhase() == Phase.MAIN) {
                        emptyCell.setEffect(shadowGreen);
                    }
                });
                // On mouse exited handler
                emptyCell.onMouseExitedProperty().set((EventHandler<MouseEvent>) (MouseEvent e) -> {
                    if (activeCellHandler.get(col).get(row).get() && GameStatus.getGameStatus().getGamePhase() == Phase.MAIN) {
                        emptyCell.setEffect(shadowYellow);
                    }
                });

                // Add event listener
                this.activeCellHandler.get(col).get(row).addListener((observable, oldValue, newValue) -> {
                    if (GameStatus.getGameStatus().getGamePhase() == Phase.MAIN) {
                        // Field effect
                        if (oldValue == false && newValue == true) {
                            emptyCell.setEffect(shadowYellow);
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
