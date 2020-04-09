package com.avatarduel.gamephase;

import com.avatarduel.controller.FieldController;
import com.avatarduel.controller.PowerController;
import com.avatarduel.gameutils.GameStatus;
import com.avatarduel.model.*;
import com.avatarduel.model.Character;

import java.io.IOException;

public class MainPhase {
    /**
     * Class instance
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
     * Summon character card
     * @param card The character card
     * @param col Target column
     */
    public void summonCharCard(Character card, boolean isAttack, int col, Player player, FieldController fieldController) {
        int row = (player == Player.BOTTOM) ? (FieldController.CHAR_ROW_BOT) : (FieldController.CHAR_ROW_TOP);
        try {
            fieldController.setCardOnField(card, isAttack, col, row);
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
    public void summonSkillCard(SkillAura card, int col, Player player, FieldController fieldController) {
        int row = (player == Player.BOTTOM) ? (FieldController.SKILL_ROW_BOT) : (FieldController.SKILL_ROW_TOP);
        try {
            fieldController.setCardOnField(card, true, col, row);
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
