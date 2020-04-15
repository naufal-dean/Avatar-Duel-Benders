package com.avatarduel.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;

import com.avatarduel.AvatarDuel;
import com.avatarduel.model.*;
import com.avatarduel.gameutils.GameDeck;
import com.avatarduel.gameutils.GameStatus;

public class DeckController {
    private GameDeck gameDeck;

    @FXML private Label cardsCount;

    public DeckController(int capacity){
        this.gameDeck = new GameDeck(capacity]);
    }

    public void init() {
        // Set background
        int cardQuantity = this.gameDeck.getCardQuantity();
        String templatePath;
        if ((cardQuantity <= 40) && (cardQuantity > 20)){
            templatePath = "card/template/Deck.png";
        } else if((cardQuantity <= 20) && (cardQuantity > 10)){
            templatePath = "card/template/Deck_below_20.png";
        } else if ((cardQuantity <= 10) && (cardQuantity > 1)){ // this.card.getCardType() == CardType.SKILL
            templatePath = "card/template/Deck_below_10.png";
        } else if (cardQuantity== 1){
            templatePath = "card/template/Deck_equal_1.png";
        } else { //Card in deck quantity is zero
            //dunno 
        }
        this.deckVisual.setImage(new Image(AvatarDuel.class.getResource(templatePath).toString()));
        this.cardsCount.setText(this.gameDeck.getCardQuantity());
    }


    

}
