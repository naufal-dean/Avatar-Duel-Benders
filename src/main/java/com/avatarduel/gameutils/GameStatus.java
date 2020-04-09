package com.avatarduel.gameutils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;

import com.avatarduel.exception.GameStatusInitializationFailed;
import com.avatarduel.model.Player;

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
     * Player health
     */
    private HashMap<Player, Integer> gameHealth;
    /**
     * Player deck
     */
    private HashMap<Player, GameDeck> gameDeck;
    /**
     * Player hand
     */
    private HashMap<Player, GameHand> gameHand;
    /**
     * Player power
     */
    private HashMap<Player, GamePower> gamePower;

    /**
     * Constructor
     */
    private GameStatus() throws IOException, URISyntaxException {
        // Initialize player turn
        this.gameTurn = Player.BOTTOM;
        // Initialize game health
        this.gameHealth = new HashMap<>();
        this.gameHealth.put(Player.BOTTOM, 80);
        this.gameHealth.put(Player.TOP, 80);
        // Initialize game deck
        this.gameDeck = new HashMap<>();
        this.gameDeck.put(Player.BOTTOM, new GameDeck(60));
        this.gameDeck.put(Player.TOP, new GameDeck((60)));
        // Initialize game hand
        this.gameHand = new HashMap<>();
        this.gameHand.put(Player.BOTTOM, new GameHand());
        this.gameHand.put(Player.TOP, new GameHand());
        // Initialize game power
        this.gamePower = new HashMap<>();
        this.gamePower.put(Player.BOTTOM, new GamePower());
        this.gamePower.put(Player.TOP, new GamePower());
    }

    /**
     * Initialize gameStatus singleton
     */
    public static void initGameStatus() throws GameStatusInitializationFailed {
        try {
            gameStatus = new GameStatus();
        } catch (Exception err) {
            throw new GameStatusInitializationFailed(err.toString());
        }
    }

    /**
     * Getter for gameStatus
     */
    public static GameStatus getGameStatus() {
        return gameStatus;
    }

    /**
     * Getter for gameTurn
     */
    public Player getGameTurn() {
        return this.gameTurn;
    }

    /**
     * Getter for gameHealth
     * @return this.gameHealth
     */
    public HashMap<Player, Integer> getGameHealth() {
        return this.gameHealth;
    }

    /**
     * Getter for our health
     * @return Our health
     */
    public Integer getOurHealth() {
        return gameHealth.get(this.gameTurn);
    }

    /**
     * Getter for enemy health
     * @return Enemy health
     */
    public Integer getEnemyHealth() {
        if (this.gameTurn == Player.BOTTOM)
            return gameHealth.get(Player.TOP);
        else
            return gameHealth.get(Player.BOTTOM);
    }

    /**
     * Getter for our GameDeck
     */
    public GameDeck getOurDeck() {
        return this.gameDeck.get(this.gameTurn);
    }

    /**
     * Getter for our GameHand
     */
    public GameHand getOurHand() {
        return this.gameHand.get(this.gameTurn);
    }

    /**
     * Getter for our GameHand
     */
    public GamePower getOurPower() {
        return this.gamePower.get(this.gamePower);
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
