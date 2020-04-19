package com.avatarduel.controller;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import com.avatarduel.AvatarDuel;
import com.avatarduel.model.Card;
import com.avatarduel.model.Player;

public class SummonedCardController extends CardController {
    /**
     * Card owner
     */
    protected Player owner;
    /**
     * Card coordinate in field
     */
    private int x, y;
    /**
     * Additional image
     */
    private ImageView attach;

    /**
     * Constructor
     * @param card The Card
     * @param owner The owner of the card
     * @param x Card x coordinate in field
     * @param y Card y coordinate in field
     */
    public SummonedCardController(Card card, Player owner, int x, int y) {
        super(card);
        this.owner = owner;
        this.x = x;
        this.y = y;
    }

    /**
     * Getter for owner of the card
     * @return this.owner
     */
    public Player getOwner() {
        return this.owner;
    }

    /**
     * Getter for x coordinate
     * @return this.x
     */
    public int getX() {
        return this.x;
    }

    /**
     * Getter for y coordinate
     * @return this.y
     */
    public int getY() {
        return this.y;
    }

    /**
     * Show/hide attachment icon
     * @param showAttach The new show attach status
     */
    public void setShowAttach(boolean showAttach) {
        this.attach.setOpacity((showAttach) ? (30) : (0));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void init() {
        super.init();
        // Rotate card if owned by Player.TOP
        if (this.owner == Player.TOP)
            this.cardAncPane.setRotate(this.cardAncPane.getRotate() + 180);
        this.attach = new ImageView(new Image(AvatarDuel.class.getResource("img/etc/attach.png").toString()));
        this.attach.setMouseTransparent(true);
        this.attach.setOpacity(0);
        this.cardRootPane.getChildren().add(attach);
    }
}
