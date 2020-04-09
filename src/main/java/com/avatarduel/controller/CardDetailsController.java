package com.avatarduel.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import com.avatarduel.model.Card;

public class CardDetailsController extends CardController{
    /**
        Card Details Anchor pane
     */
    @FXML protected AnchorPane mainCardDetails;
    /**

     */
    @FXML private VBox layoutCardDetails;
    /**
        Pane for Card
     */
    @FXML private Pane Card;
    /**
        Description for card
     */
    @FXML private Pane Description;
    /**
        Description text of card
     */
    @FXML private TextArea DescriptionText;
    /**
        Constructor
     */
     public CardDetailsController(Card card){
         super(card);
     }
}