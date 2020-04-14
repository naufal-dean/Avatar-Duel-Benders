package com.avatarduel.gamephase;

import java.io.IOException;

import com.avatarduel.controller.FieldController;
import com.avatarduel.controller.MainController;
import com.avatarduel.controller.PowerController;
import com.avatarduel.gameutils.GameStatus;
import com.avatarduel.model.*;
import com.avatarduel.model.Character;

public class MainPhase implements GamePhase {
    /**
     * Class singleton instance
     */
    private static MainPhase mainPhase;
    /**
     * Land card placed counter
     */
    private int landCardPlaced;

    /**
     * Constructor
     */
    private MainPhase() {
        this.landCardPlaced = 0;
    }

    /**
     * Getter for class singleton instance
     * @return Class singleton instance
     */
    public static MainPhase getMainPhase() {
        if (mainPhase == null)
            mainPhase = new MainPhase();
        return mainPhase;
    }

    /**
     * Start main phase
     * @param mainController The MainController for the UI
     */
    @Override
    public void startPhase(MainController mainController) {
        // TODO: implement
        //
    }

    /**
     * End the main phase
     * @param mainController The MainController for the UI
     */
    @Override
    public void endPhase(MainController mainController) {
        BattlePhase.getBattlePhase().endPhase(mainController);
    }

    /**
     * Reset land placed counter
     */
    public void resetLandPlacedCounter() {
        this.landCardPlaced = 0;
    }

    /**
     * Summon character card
     * @param card The character card
     * @param col Target column
     */
    public void summonCharCard(Character card, boolean isAttack, int col, FieldController fieldController) {
        int row = (GameStatus.getGameStatus().getGameActivePlayer() == Player.BOTTOM) ? (FieldController.CHAR_ROW_BOT) : (FieldController.CHAR_ROW_TOP);
        try {
            fieldController.setCardOnField(card, GameStatus.getGameStatus().getGameActivePlayer(), isAttack, col, row);
        } catch (IOException e) {
            System.out.println("Summon char card failed: " + e);
            // pass
        }
    }

    /**
     * Summon skill card
     * @param card The skill card
     * @param col Target column
     */
    public void summonSkillCard(SkillAura card, int col, FieldController fieldController) {
        int row = (GameStatus.getGameStatus().getGameActivePlayer() == Player.BOTTOM) ? (FieldController.SKILL_ROW_BOT) : (FieldController.SKILL_ROW_TOP);
        try {
            fieldController.setCardOnField(card, GameStatus.getGameStatus().getGameActivePlayer(), true, col, row);
        } catch (IOException e) {
            System.out.println("Summon char card failed: " + e);
            // pass
        }
    }

    /**
     * Summon land card
     * @param card The land card
     * @param col Target column
     */
    public void summonLandCard(Land card, int col, PowerController powerController) { // TODO: implement PowerController as param
        if (this.landCardPlaced > 0)
            return;
        this.landCardPlaced++;
        GameStatus.getGameStatus().getOurPower().incMaxPower(card.getElement());
        powerController.render();
    }
}
