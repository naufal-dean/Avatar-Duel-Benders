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
    private Player gameActivePlayer;
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
        this.gameActivePlayer = Player.BOTTOM;
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
     * Getter for gameStatus, must call initGameStatus first
     * @return This class singleton
     */
    public static GameStatus getGameStatus() {
        return gameStatus;
    }

    /**
     * Getter for gameTurn
     * @return Active player now
     */
    public Player getGameActivePlayer() {
        return this.gameActivePlayer;
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
        return gameHealth.get(this.gameActivePlayer);
    }

    /**
     * Getter for enemy health
     * @return Enemy health
     */
    public Integer getEnemyHealth() {
        if (this.gameActivePlayer == Player.BOTTOM)
            return gameHealth.get(Player.TOP);
        else
            return gameHealth.get(Player.BOTTOM);
    }

    /**
     * Getter for gameDeck
     * @return this.gameDeck
     */
    public HashMap<Player, GameDeck> getGameDeck() {
        return this.gameDeck;
    }

    /**
     * Getter for our GameDeck
     * @return Our GameDeck
     */
    public GameDeck getOurDeck() {
        return this.gameDeck.get(this.gameActivePlayer);
    }

    /**
     * Getter for our GameHand
     * @return Our GameHand
     */
    public GameHand getOurHand() {
        return this.gameHand.get(this.gameActivePlayer);
    }

    /**
     * Getter for our GamePower
     * @return Our GamePower
     */
    public GamePower getOurPower() {
        return this.gamePower.get(this.gamePower);
    }

    /**
     * Next Turn
     */
    public void nextTurn() {
        if (this.gameActivePlayer == Player.BOTTOM)
            this.gameActivePlayer = Player.TOP;
        else
            this.gameActivePlayer = Player.BOTTOM;
    }
}
