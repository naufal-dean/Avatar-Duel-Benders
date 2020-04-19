package com.avatarduel.controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import com.avatarduel.AvatarDuel;
import com.avatarduel.model.*;
import com.avatarduel.model.Character;
import javafx.scene.text.TextAlignment;

public class CardController implements Initializable {
    /**
     * Card model
     */
    protected Card card;
    /**
     * Card fxml anchor pane
     */
    @FXML protected AnchorPane cardAncPane;
    /**
     * Display background
     */
    @FXML public ImageView background;
    /**
     * Display name
     */
    @FXML private Label name;
    /**
     * Display element
     */
    @FXML private ImageView element;
    /**
     * Display card decorator
     */
    @FXML private ImageView decorator;
    /**
     * Display image
     */
    @FXML private ImageView image;
    /**
     * Display card description and stats
     */
    @FXML private Label description, attack, defense, power;

    /**
     * Default constructor
     */
    public CardController() {}

    /**
     * Constructor
     * @param card The Card
     */
    public CardController(Card card) {
        this.card = card;
    }

    /**
     * Replace with new card then render
     * @param card The Card
     */
    public void setCard(Card card) {
        this.card = card;
        this.init();
    }

    /**
     * Getter for Card model
     * @return this.card
     */
    public Card getCard() {
        return this.card;
    }

    /**
     * Getter for card anchor pane
     * @return this.cardAncPane
     */
    public AnchorPane getCardAncPane() {
        return this.cardAncPane;
    }

    /**
     * Update FXML using current this.card
     */
    public void init() {
        // Set background
        String templatePath;
        if (this.card instanceof Character) {
            templatePath = "card/template/template_character.png";
        } else if (this.card instanceof Land) {
            templatePath = "card/template/template_land.png";
        } else { // this.card instanceof Skill
            if (this.card instanceof SkillAura)
                templatePath = "card/template/template_skill_aura.png";
            else
                templatePath = "card/template/template_skill.png";
        }
        this.background.setImage(new Image(AvatarDuel.class.getResource(templatePath).toString()));

        // Set element
        String elementPath;
        if (this.card.getElement() == Element.AIR)
            elementPath = "card/template/element_air.png";
        else if (this.card.getElement() == Element.EARTH)
            elementPath = "card/template/element_earth.png";
        else if (this.card.getElement() == Element.FIRE)
            elementPath = "card/template/element_fire.png";
        else if (this.card.getElement() == Element.WATER)
            elementPath = "card/template/element_water.png";
        else // this.card.getElement() == Element.ENERGY
            elementPath = "card/template/element_energy.png";
        this.element.setImage(new Image(AvatarDuel.class.getResource(elementPath).toString()));

        // Set decorator if any
        String decoratorPath;
        if (this.card instanceof Character) {
            int starCount = (((Character) this.card).getAttack() + ((Character) this.card).getDefense()) / 2;
            decoratorPath = "card/template/star_" + ((starCount <= 10) ? (starCount) : (10)) +".png";
            this.decorator.setImage(new Image(AvatarDuel.class.getResource(decoratorPath).toString()));
        } else if (this.card instanceof Skill) {
            if (this.card instanceof SkillAura)
                decoratorPath = "card/template/skill_aura.png";
            else if (this.card instanceof SkillDestroy)
                decoratorPath = "card/template/skill_destroy.png";
            else // this.card instanceof SkillPowerUp
                decoratorPath = "card/template/skill_power_up.png";
            this.decorator.setImage(new Image(AvatarDuel.class.getResource(decoratorPath).toString()));
            this.decorator.setTranslateY(-3);
        } else {
            this.decorator.setImage(null);
        }

        // Set name
        double nameTextSize = 13;
        if (this.card.getName().length() <= 20)
            nameTextSize -= Math.max((this.card.getName().length() - 17), 0);
        else
            nameTextSize = 8.5;
        if (this.card.getName().toUpperCase().equals("CABBAGE MERCHANT"))
            nameTextSize = 12;
        this.name.setFont(Font.loadFont(AvatarDuel.class.getResourceAsStream("font/palatino-linotype-bold.ttf"), nameTextSize));
        this.name.setText(this.card.getName().toUpperCase());
        if (this.card.getCardType() != CardType.CHARACTER)
            this.name.setTextFill(Color.WHITE);
        this.name.setPrefWidth(this.background.getFitWidth() * 0.7);

        // Set image
        this.image.setImage(new Image(this.card.getImagePath()));

        // Set description
        this.description.setWrapText(true);
        this.description.setContentDisplay(ContentDisplay.TOP);
        double descTextSize = 9;
        if (this.card.getDescription().length() > 120) descTextSize--;
        if (this.card.getDescription().length() > 135) descTextSize--;
        if (this.card.getDescription().length() > 150) descTextSize -= 0.5;
        this.description.setFont(Font.loadFont(AvatarDuel.class.getResourceAsStream("font/palatino-linotype.ttf"), descTextSize));
        this.description.setText(this.card.getDescription());
        this.description.getStylesheets().add(AvatarDuel.class.getResource("css/transparent-bg-text-area.css").toString());

        // Set card stats
        if (this.card instanceof Character) {
            Character character = (Character) this.card;
            this.attack.setFont(Font.loadFont(AvatarDuel.class.getResourceAsStream("font/palatino-linotype-bold.ttf"), 8));
            this.attack.setText(String.valueOf(character.getAttack()));
            this.defense.setFont(Font.loadFont(AvatarDuel.class.getResourceAsStream("font/palatino-linotype-bold.ttf"), 8));
            this.defense.setText(String.valueOf(character.getDefense()));
            this.power.setFont(Font.loadFont(AvatarDuel.class.getResourceAsStream("font/palatino-linotype-bold.ttf"), 8));
            this.power.setText(String.valueOf(character.getPower()));
        } else if (this.card instanceof Skill) {
            Skill skill = (Skill) this.card;
            if (skill instanceof SkillAura) {
                this.attack.setFont(Font.loadFont(AvatarDuel.class.getResourceAsStream("font/palatino-linotype-bold.ttf"), 8));
                this.attack.setText(String.valueOf((((SkillAura) skill).getAttack())));
                this.defense.setFont(Font.loadFont(AvatarDuel.class.getResourceAsStream("font/palatino-linotype-bold.ttf"), 8));
                this.defense.setText(String.valueOf((((SkillAura) skill).getDefense())));
            } else {
                this.attack.setText("");
                this.defense.setText("");
            }
            this.power.setFont(Font.loadFont(AvatarDuel.class.getResourceAsStream("font/palatino-linotype-bold.ttf"), 8));
            this.power.setText(String.valueOf(skill.getPower()));
        } else {
            this.attack.setText("");
            this.defense.setText("");
            this.power.setText("");
        }
        this.attack.setTextAlignment(TextAlignment.RIGHT);
        this.defense.setTextAlignment(TextAlignment.RIGHT);
        this.power.setTextAlignment(TextAlignment.RIGHT);
    }

    /**
     * {@inheritDoc}
     */
    @Override @FXML
    public void initialize(URL url, ResourceBundle resources) {
        if (this.card != null)
            this.init();
    }
}
