package com.avatarduel.controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;

import com.avatarduel.AvatarDuel;
import com.avatarduel.model.*;
import com.avatarduel.model.Character;

public class CardDetailsController implements Initializable {
    /**
     * The Card Controller
     */
    private CardController cardController;
    /**
     * Card display
     */
    @FXML private StackPane card;
    /**
     * Card description
     */
    @FXML private TextArea description;
    /**
     * Card stats
     */
    @FXML private Label stats;
    /**
     * Card details background
     */
    @FXML private ImageView background;

    /**
     * Constructor
     */
     public CardDetailsController() {}

    /**
     * Set the card to be shown
     * @param card The Card
     */
     public void setCard(Card card) {
         // Set card display
         this.cardController.setCard(card);
         this.card.setOpacity(100);

         // Set description display
         this.description.setText(card.getDescription());

         // Set stats display
         this.stats.setText("");
         if (card.getCardType() == CardType.CHARACTER) {
             Character character = (Character) card;
             this.stats.setText("ATK/" + character.getAttack() + "  DEF/" + character.getDefense()
                                + "  POW/" + character.getPower());
         } else if (card.getCardType() == CardType.SKILL) {
             String stats = "";
             if (((Skill) card).getEffect() == Effect.AURA) {
                 SkillAura skillAura = (SkillAura) card;
                 stats += "ATK/" + skillAura.getAttack() + "  DEF/" + skillAura.getDefense();
                 stats += "  POW/" + skillAura.getPower();
             }
             else {
                 SkillDestroy skillDestroy = (SkillDestroy) card;
                 stats += "POW/" + skillDestroy.getPower();
             }
             this.stats.setText(stats);
         }
     }

    /**
     * Remove card from display
     */
    public void removeCard() {
        this.card.setOpacity(0);
        this.description.setText("");
        this.stats.setText("");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Setup card display
        this.card.setAlignment(Pos.CENTER);
        try {
            // Create loader
            FXMLLoader loader = new FXMLLoader();
            CardController cardController = new CardController();
            loader.setController(cardController);
            loader.setLocation(AvatarDuel.class.getResource("view/Card.fxml"));
            // Create root
            StackPane root = loader.load();
            // Create scale for root
            DoubleProperty scale = new SimpleDoubleProperty(1.1);
            root.scaleXProperty().bind(scale);
            root.scaleYProperty().bind(scale);
            // Set root as field children node
            this.card.getChildren().add(root);
            // Save card controller
            this.cardController = cardController;
        } catch (Exception err) {
            System.out.println(err.toString());
        }
        this.card.setOpacity(0);
        // Setup desc, stats, and background
        this.description.setDisable(true);
        this.description.setWrapText(true);
        this.description.setFont(Font.loadFont(AvatarDuel.class.getResourceAsStream("font/palatino-linotype.ttf"), 14));
        this.description.getStylesheets().add(AvatarDuel.class.getResource("css/transparent-bg-text-area.css").toString());
        this.stats.setFont(Font.loadFont(AvatarDuel.class.getResourceAsStream("font/palatino-linotype.ttf"), 15));
        this.background.setImage(new Image(AvatarDuel.class.getResource("img/background/card_details_background.png").toString()));
    }
}