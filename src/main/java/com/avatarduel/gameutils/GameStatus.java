package com.avatarduel.gameutils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;

import com.avatarduel.exception.GameStatusInitializationFailed;
import com.avatarduel.gamephase.Phase;
import com.avatarduel.model.Player;

public class GameStatus {
    /**
     * Class singleton
     */
    private static GameStatus gameStatus;
    /**
     * Active player
     */
    private Player gameActivePlayer;
    /**
     * Game phase now
     */
    private Phase gamePhase;
    /**
     * Player health
     */
    private HashMap<Player, Integer> gameHealthMap;
    /**
     * Player deck
     */
    private HashMap<Player, GameDeck> gameDeckMap;
    /**
     * Player power
     */
    private HashMap<Player, GamePower> gamePowerMap;

    /**
     * Constructor
     */
    private GameStatus() throws IOException, URISyntaxException {
        // Initialize active player
        this.gameActivePlayer = Player.BOTTOM;
        // Initialize game phase
        this.gamePhase = Phase.DRAW;
        // Initialize game health
        this.gameHealthMap = new HashMap<>();
        this.gameHealthMap.put(Player.BOTTOM, 80);
        this.gameHealthMap.put(Player.TOP, 80);
        // Initialize game deck
        this.gameDeckMap = new HashMap<>();
        this.gameDeckMap.put(Player.BOTTOM, new GameDeck(60));
        this.gameDeckMap.put(Player.TOP, new GameDeck((60)));
        // Initialize game power
        this.gamePowerMap = new HashMap<>();
        this.gamePowerMap.put(Player.BOTTOM, new GamePower());
        this.gamePowerMap.put(Player.TOP, new GamePower());
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
     * Getter for current active player
     * @return Current active player
     */
    public Player getGameActivePlayer() {
        return this.gameActivePlayer;
    }

    /**
     * Getter for current non active player
     * @return Current non active player
     */
    public Player getGameNonActivePlayer() {
        return (this.gameActivePlayer == Player.BOTTOM) ? (Player.TOP) : (Player.BOTTOM);
    }

    /**
     * Getter for gamePhase
     * @return this.gamePhase
     */
    public Phase getGamePhase() {
        return this.gamePhase;
    }

    /**
     * Setter for gamePhase
     * @param gamePhase The new game phase
     */
    public void setGamePhase(Phase gamePhase) {
        this.gamePhase = gamePhase;
    }

    /**
     * Getter for gameHealth
     * @return this.gameHealth
     */
    public HashMap<Player, Integer> getGameHealthMap() {
        return this.gameHealthMap;
    }

    /**
     * Getter for gameDeck
     * @return this.gameDeck
     */
    public HashMap<Player, GameDeck> getGameDeckMap() {
        return this.gameDeckMap;
    }

    /**
     * Getter for gamePower
     * @return this.gameDeck
     */
    public HashMap<Player, GamePower> getGamePowerMap() {
        return this.gamePowerMap;
    }

    /**
     * Getter for our GamePower
     * @return Our GamePower
     */
    public GamePower getOurPower() {
        return this.gamePowerMap.get(this.gameActivePlayer);
    }

    /**
     * Next Turn
     */
    public void nextTurn() {
        this.gameActivePlayer = this.getGameNonActivePlayer();
    }
}
