package com.avatarduel.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import com.avatarduel.model.CardType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.GridPane;
import javafx.scene.transform.Scale;

import com.avatarduel.AvatarDuel;
import com.avatarduel.model.Card;

public class FieldController implements Initializable {
    /**
     * Field grid
     */
    @FXML private GridPane field;
    /**
     * CardController list
     */
    private List<List<CardController>> cardControllerList;

    /**
     * Constructor
     */
    public FieldController() {
        cardControllerList = new ArrayList<>();
        for (int i = 0; i < 8; i++)
            cardControllerList.add(Arrays.asList(null, null, null, null));
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
     * Set card on the cell x, y in field
     * @param card The card to be displayed
     * @param x The grid column index
     * @param y The grid row index
     */
    public void setCardOnField(Card card, boolean isAttack, int x, int y) throws IOException {
        if (this.cardControllerList.get(x).get(y) != null) {
            // Set new card controller
            if (card.getCardType().equals(CardType.CHARACTER)) {
                SummonedCharacterCardController scCardController = new SummonedCharacterCardController(card, isAttack);
                this.cardControllerList.get(x).set(y, scCardController);
            } else if (card.getCardType().equals(CardType.LAND)) {
                SummonedLandCardController scCardController = new SummonedLandCardController(card);
                this.cardControllerList.get(x).set(y, scCardController);
            } else {
                SummonedSkillCardController scCardController = new SummonedSkillCardController(card, null);
                this.cardControllerList.get(x).set(y, scCardController);
            }
        } else { // Card not exist
            // Create loader
            FXMLLoader loader = new FXMLLoader();
            if (card.getCardType().equals(CardType.CHARACTER)) {
                SummonedCharacterCardController scCardController = new SummonedCharacterCardController(card, isAttack);
                loader.setController(scCardController);
                this.cardControllerList.get(x).set(y, scCardController);
            } else if (card.getCardType().equals(CardType.LAND)) {
                SummonedLandCardController scCardController = new SummonedLandCardController(card);
                loader.setController(scCardController);
                this.cardControllerList.get(x).set(y, scCardController);
            } else {
                SummonedSkillCardController scCardController = new SummonedSkillCardController(card, null);
                loader.setController(scCardController);
                this.cardControllerList.get(x).set(y, scCardController);
            }
            loader.setLocation(AvatarDuel.class.getResource("view/Card.fxml"));

            // Create ancPane
            StackPane ancPane = loader.load();
            // Create scale for ancPane
            Scale scale = new Scale();
            scale.setX(0.4);
            scale.setY(0.4);
            scale.setPivotX(((this.field.getColumnConstraints().get(0).getPrefWidth() - 200 * 0.4) / 2d) + 7.5);
            double padShift = this.field.getPadding().getTop();
            scale.setPivotY(((this.field.getPrefHeight()) / 4d  - padShift) * 1.2);
            // Set scale for ancPane
            ancPane.getTransforms().add(scale);
            // Set ancPane as field children node
            this.field.add(ancPane, x, y);
            // Set cardController
        }
    }

    /**
     * Remove card on the cell x, y in field
     * @param x The grid column index
     * @param y The grid row index
     */
    public void removeCardOnField(int x, int y) {
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
     * {@inheritDoc}
     */
    @Override @FXML
    public void initialize(URL url, ResourceBundle resources) {}
}
