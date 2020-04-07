package com.avatarduel.controller;

import java.net.URL;
import java.util.ResourceBundle;

import com.avatarduel.AvatarDuel;
import com.sun.javafx.scene.control.skin.Utils;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
        this.background.setImage(new Image(AvatarDuel.class.getResource("card/template/character_template.png").toString()));
        this.name.setFont(Font.loadFont(AvatarDuel.class.getResourceAsStream("font/palatino-linotype-bold.ttf"), 13));
        this.name.setText(this.card.getName().toUpperCase());
        this.name.setPrefWidth(this.background.getFitWidth() * 0.7);
        this.image.setImage(new Image(this.card.getImagePath()));
        this.description.setDisable(true);
        this.description.setWrapText(true);
        this.description.setText(this.card.getDescription());
    }
}
