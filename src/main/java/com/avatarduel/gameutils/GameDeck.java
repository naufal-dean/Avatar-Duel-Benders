package com.avatarduel.gameutils;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.avatarduel.model.*;
import com.avatarduel.model.Character;
import com.avatarduel.util.CSVReader;

public class GameDeck {
    private static final String LAND_CSV_FILE_PATH = "../card/data/land.csv";
    private static final String CHARACTER_CSV_FILE_PATH = "../card/data/character.csv";
    private static final String SKILL_AURA_CSV_FILE_PATH = "../card/data/skill_aura.csv";
    /**
     * List of cards on the deck
     */
    private List<Card> cardList;
    /**
     *  Capacity of the deck
     */
    private int capacity;

    /**
     * Constructor
     */
    public GameDeck(int capacity) throws IOException, URISyntaxException {
        this.cardList = new ArrayList<>();
        this.capacity = capacity;
        this.loadCards();
    }

    /**
     * Getter for deck capacity
     * @return this.capacity
     */
    public int getCapacity() {
        return this.capacity;
    }

    /**
     * Card quantity in deck
     * @return The size of cardList
     */
    public int getCardQuantity() {
        return this.cardList.size();
    }

    /**
     * Load cards to deck
     */
    public void loadCards() throws IOException, URISyntaxException {
        Random rand = new Random();
        int rand_int;

        // Read land cards
        File landCSVFile = new File(getClass().getResource(LAND_CSV_FILE_PATH).toURI());
        CSVReader landReader = new CSVReader(landCSVFile, "\t");
        landReader.setSkipHeader(true);
        List<String[]> landRows = landReader.read();
        for (int i = 0; i < Math.round((this.capacity/5f)*2); i++){
            rand_int = rand.nextInt(landRows.size());
            String[] row = landRows.get(rand_int);
            cardList.add(new Land(Integer.valueOf(row[0]), row[1], Element.valueOf(row[2]), row[3], row[4]));
        }

        // Read character cards
        File characterCSVFile = new File(getClass().getResource(CHARACTER_CSV_FILE_PATH).toURI());
        CSVReader characterReader = new CSVReader(characterCSVFile, "\t");
        characterReader.setSkipHeader(true);
        List<String[]> characterRows = characterReader.read();
        for (int i = 0; i < Math.round((this.capacity/5f)*2); i++) {
            rand_int = rand.nextInt(characterRows.size());
            String[] row = characterRows.get(rand_int);
            cardList.add(new Character(Integer.valueOf(row[0]), row[1], Element.valueOf(row[2]), row[3], row[4], Integer.valueOf(row[5]), Integer.valueOf(row[6]), Integer.valueOf(row[7])));
        }

        // Read skillAura cards
        File skillAuraCSVFile = new File(getClass().getResource(SKILL_AURA_CSV_FILE_PATH).toURI());
        CSVReader skillAuraReader = new CSVReader(skillAuraCSVFile, "\t");
        skillAuraReader.setSkipHeader(true);
        List<String[]> skillAuraRows = skillAuraReader.read();
        for (int i = 0; i <= Math.round(this.capacity/5f); i++) {
            rand_int = rand.nextInt(skillAuraRows.size());
            String[] row = skillAuraRows.get(rand_int);
            cardList.add(new SkillAura(Integer.valueOf(row[0]), row[1], Element.valueOf(row[2]), row[3], row[4], Integer.valueOf(row[5]), Integer.valueOf(row[6]), Integer.valueOf(row[7])));
        }
    }

    /**
     * Draw a card from deck
     */
    public Card draw(){
        Random rand = new Random();
        int rand_int = rand.nextInt(this.cardList.size());
        Card drawCard = this.cardList.get(rand_int);
        this.cardList.remove(rand_int);
        return drawCard;
    }
}
