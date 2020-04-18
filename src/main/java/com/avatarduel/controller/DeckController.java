package com.avatarduel.controller;

import java.net.URL;
import java.util.ResourceBundle;

import com.avatarduel.model.Player;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

import com.avatarduel.AvatarDuel;
import com.avatarduel.gameutils.GameStatus;


public class DeckController implements Initializable {
    /**
     * The owner of the deck
     */
    private Player owner;
    /**
     * Deck root pane
     */
    @FXML private StackPane deckRoot;
    /**
     * Deck image
     */
    @FXML private ImageView deckVisual;
    /**
     * Count of cards remain in deck
     */
    @FXML private Label cardsCount;

    /**
     * Constructor
     */
    public DeckController(Player owner) {
        this.owner = owner;
    }

    /**
     * Update FXML using current deck
     */
    public void init() {
        // Set background
        int cardQuantity = GameStatus.getGameStatus().getGameDeckMap().get(this.owner).getCardQuantity();
        String templatePath;
        String midPath = (this.owner == Player.BOTTOM) ? ("bot") : ("top");
        if (40 <= cardQuantity) {
            templatePath = "img/deck/" + midPath + "_deck_default.png";
        } else if((20 <= cardQuantity) && (cardQuantity < 40)) {
            templatePath = "img/deck/" + midPath + "_deck_below_40.png";
        } else if ((1 < cardQuantity) && (cardQuantity < 20)) {
            templatePath = "img/deck/" + midPath + "_deck_below_20.png";
        } else {
            templatePath = "img/deck/" + midPath + "_deck_equal_1.png";
        }
        this.deckVisual.setImage(new Image(AvatarDuel.class.getResource(templatePath).toString()));
        this.cardsCount.setText(String.valueOf(cardQuantity));
        // Rotate if the owner is Player TOP
        if (this.owner == Player.TOP) {
            this.deckRoot.setRotate(180);
            this.deckVisual.setRotate(180);
            this.cardsCount.setRotate(180);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.init();
    }
}
