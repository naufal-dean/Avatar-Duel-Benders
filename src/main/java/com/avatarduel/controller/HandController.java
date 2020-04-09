package com.avatarduel.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import com.avatarduel.AvatarDuel;
import com.avatarduel.model.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.transform.Scale;

public class HandController implements Initializable {
    /**
     * Display card in hand
     */
    @FXML private HBox hand;
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
    public void addCardOnHand(Card card, Player owner, boolean isAttack) throws IOException {
        // Create loader
        FXMLLoader loader = new FXMLLoader();
        if (card.getCardType().equals(CardType.CHARACTER)) {
//            HandCardController cardController = new HandCardController((Character) card, owner, this.cardControllerList.size(), isAttack);
//            loader.setController(cardController);
//            this.cardControllerList.add(cardController);
        } else if (card.getCardType().equals(CardType.SKILL)) {

        } else { // card.getCardType().equals(CardType.SKILL)

        }
        loader.setLocation(AvatarDuel.class.getResource("view/Card.fxml"));

        // Create root
        StackPane root = loader.load();
        // Create scale for root
        Scale scale = new Scale();
        scale.setX(0.4);
        scale.setY(0.4);
        scale.setPivotX(0); // TODO: set pivot
        scale.setPivotY(0);
        // Set scale for root
        root.getTransforms().add(scale);
        // Set root as field children node
        this.hand.getChildren().add(root);
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
     * {@inheritDoc}
     */
    @Override @FXML
    public void initialize(URL url, ResourceBundle resourceBundle) {}
}
