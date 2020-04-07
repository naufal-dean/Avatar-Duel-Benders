package com.avatarduel.controller;

import java.net.URL;
import java.util.ResourceBundle;

import com.avatarduel.AvatarDuel;
import com.avatarduel.model.CardType;
import com.avatarduel.model.Element;
import com.sun.javafx.scene.control.skin.Utils;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import com.avatarduel.model.Card;

public class CardController implements Initializable {
    /**
     * Card model
     */
    private Card card;

    /**
     * Display background
     */
    @FXML
    private ImageView background;

    /**
     * Display name
     */
    @FXML
    private Label name;

    /**
     * Display element
     */
    @FXML
    private ImageView element;

    /**
     * Display image
     */
    @FXML
    private ImageView image;

    /**
     * Display description
     */
    @FXML
    private TextArea description;

    /**
     * Constructor
     *
     * @param card The Card
     */
    public CardController(Card card) {
        this.card = card;
    }

    /**
     * Initialize controller
     */
    @Override
    public void initialize(URL url, ResourceBundle resources) {
        // Set background
        String templatePath;
        if (this.card.getCardType() == CardType.CHARACTER)
            templatePath = "card/template/template_character.png";
        else if (this.card.getCardType() == CardType.LAND)
            templatePath = "card/template/template_land.png";
        else // this.card.getCardType() == CardType.SKILL
            templatePath = "card/template/template_skill.png";
        this.background.setImage(new Image(AvatarDuel.class.getResource(templatePath).toString()));
        // Set element
        String elementPath;
        if (this.card.getElement() == Element.AIR)
            elementPath = "card/template/element_air.png";
        else if (this.card.getElement() == Element.EARTH)
            elementPath = "card/template/element_earth.png";
        else if (this.card.getElement() == Element.FIRE)
            elementPath = "card/template/element_fire.png";
        else // this.card.getElement() == Element.WATER
            elementPath = "card/template/element_water.png";
        this.element.setImage(new Image(AvatarDuel.class.getResource(elementPath).toString()));
        // Set name
        this.name.setFont(Font.loadFont(AvatarDuel.class.getResourceAsStream("font/palatino-linotype-bold.ttf"), 12));
        this.name.setText(this.card.getName().toUpperCase());
        if (this.card.getCardType() != CardType.CHARACTER)
            this.name.setTextFill(Color.WHITE);
        this.name.setPrefWidth(this.background.getFitWidth() * 0.7);
        // Set image
        this.image.setImage(new Image(this.card.getImagePath()));
        // Set description
        this.description.setDisable(true);
        this.description.setWrapText(true);
        this.description.setFont(Font.loadFont(AvatarDuel.class.getResourceAsStream("font/palatino-linotype.ttf"), 9));
        this.description.setText(this.card.getDescription());
    }
}
