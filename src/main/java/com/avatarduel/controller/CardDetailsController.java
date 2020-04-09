package com.avatarduel.controller;

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