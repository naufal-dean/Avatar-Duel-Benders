package com.avatarduel.gameutils;

import com.avatarduel.controller.DeckController;
import com.avatarduel.exception.GameStatusInitializationFailed;
import com.avatarduel.model.Player;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;

public class GameStatus {
    /**
     * Class singleton
     */
    private static GameStatus gameStatus;
    /**
     * Player turn
     */
    private Player gameTurn;
    /**
     * Player deck
     */
    private HashMap<Player, GameDeck> gameDeck;
    /**
     * Player hand
     */
    private HashMap<Player, GameHand> gameHand;

    /**
     * Constructor
     */
    private GameStatus() throws IOException, URISyntaxException {
        // Initialize player turn
        this.gameTurn = Player.BOTTOM;
        // Initialize game deck
        this.gameDeck.put(Player.BOTTOM, new GameDeck(60));
        this.gameDeck.put(Player.TOP, new GameDeck((60)));
    }

    /**
     * Getter for gameStatus
     */
    public static GameStatus getGameStatus() throws GameStatusInitializationFailed {
        if (gameStatus == null) {
            try {
                gameStatus = new GameStatus();
            } catch (Exception err) {
                throw new GameStatusInitializationFailed(err.getMessage());
            }
        }
        return gameStatus;
    }

    /**
     * Getter for gameTurn
     */
    public Player getGameTurn() {
        return this.gameTurn;
    }

    /**
     * Getter for player game deck
     */
    public GameDeck getPlayerDeck() {
        return this.gameDeck.get(this.gameTurn);
    }

    /**
     * Getter for player game deck
     */
    public GameHand getPlayerHand() {
        return this.gameHand.get(this.gameTurn);
    }

    /**
     * Next Turn
     */
    public void nextTurn() {
        if (this.gameTurn == Player.BOTTOM)
            this.gameTurn = Player.TOP;
        else
            this.gameTurn = Player.BOTTOM;
    }
}
