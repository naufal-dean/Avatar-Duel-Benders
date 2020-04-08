package com.avatarduel.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

import com.avatarduel.AvatarDuel;
import com.avatarduel.model.Card;
import javafx.scene.transform.Scale;

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
     * Set card on the cell x, y in field
     * @param card The card to be displayed
     * @param x The col grid index
     * @param y The row grid index
     */
    public void setCardOnField(Card card, int x, int y) throws IOException {
        if (this.cardControllerList.get(x).get(y) != null) {
            // setCard via cardController
            this.cardControllerList.get(x).get(y).setCard(card);
        } else { // Controller not exist
            // Create loader
            FXMLLoader loader = new FXMLLoader();
            CardController cardController = new CardController(card);
            loader.setLocation(AvatarDuel.class.getResource("view/Card.fxml"));
            loader.setController(cardController);
            // Create ancPane
            AnchorPane ancPane = loader.load();
            // Create scale for ancPane
            Scale scale = new Scale();
            scale.setX(0.5);
            scale.setY(0.5);
            scale.setPivotX(0);
            double padShift = this.field.getPadding().getTop() + this.field.getPadding().getBottom();
            double gapShift = this.field.getVgap() * 3;
            scale.setPivotY((this.field.getPrefHeight() - padShift - gapShift) / 4d);
            // Set scale for ancPane
            ancPane.getTransforms().add(scale);
            // Set ancPane as field children node
            this.field.add(ancPane, x, y);
            // Set cardController
            this.cardControllerList.get(x).set(y, cardController);
        }
    }

    /**
     * Remove card on the cell x, y in field
     * @param x The col grid index
     * @param y The row grid index
     */
    public void removeCardOnField(int x, int y) {
        for (Node node : this.field.getChildren()) {
            if(node instanceof AnchorPane && this.field.getColumnIndex(node) == x && this.field.getRowIndex(node) == y) {
                // Remove field children node
                this.field.getChildren().remove((AnchorPane) node);
                // Remove cardController
                this.cardControllerList.get(x).set(y, null);
                break;
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initialize(URL url, ResourceBundle resources) {}
}
