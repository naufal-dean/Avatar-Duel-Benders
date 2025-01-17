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
     * Card description and stats
     */
    @FXML private Label description, stats, addStats;
    /**
     * Card details background
     */
    @FXML private ImageView background;

    /**
     * Constructor
     */
     public CardDetailsController() {}

    /**
     * Set the card to be shown and add additional stats if any
     * @param cardController The Summoned Card Controller
     */
     public void setSummonedCardController(SummonedCardController cardController) {
        if (cardController instanceof SummonedCharacterCardController) {
            SummonedCharacterCardController scCardController = (SummonedCharacterCardController) cardController;
            int attack = 0, defense = 0;
            for (SummonedSkillCardController ssCardController : scCardController.getAttachedAuraControllerList()) {
                SkillAura skillAura = (SkillAura) ssCardController.getCard();
                attack += skillAura.getAttack();
                defense += skillAura.getDefense();
            }
            this.addStats.setText("ATK+/" + attack + "  DEF+/" + defense);
        }
        this.setCard(cardController.getCard());
     }

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
         if (card instanceof Character) {
             Character character = (Character) card;
             this.stats.setText("ATK/" + character.getAttack() + "  DEF/" + character.getDefense()
                                + "  POW/" + character.getPower());
         } else if (card instanceof Skill) {
             String stats = "";
             if (card instanceof SkillAura) {
                 SkillAura skillAura = (SkillAura) card;
                 stats += "ATK/" + skillAura.getAttack() + "  DEF/" + skillAura.getDefense();
                 stats += "  POW/" + skillAura.getPower();
             } else {
                 stats += "POW/" + card.getPower();
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
        this.addStats.setText("");
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
        this.description.setWrapText(true);
        this.description.setFont(Font.loadFont(AvatarDuel.class.getResourceAsStream("font/palatino-linotype.ttf"), 14));
        this.description.getStylesheets().add(AvatarDuel.class.getResource("css/transparent-bg-text-area.css").toString());
        this.stats.setFont(Font.loadFont(AvatarDuel.class.getResourceAsStream("font/palatino-linotype.ttf"), 14.5));
        this.addStats.setFont(Font.loadFont(AvatarDuel.class.getResourceAsStream("font/palatino-linotype.ttf"), 14.5));
        this.background.setImage(new Image(AvatarDuel.class.getResource("img/background/card_details_background.png").toString()));
    }
}