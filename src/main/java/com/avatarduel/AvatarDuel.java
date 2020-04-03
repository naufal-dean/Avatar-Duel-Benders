package com.avatarduel;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import com.avatarduel.model.Card;
import com.avatarduel.model.CardType;
import com.avatarduel.model.Character;
import com.avatarduel.model.Effect;
import com.avatarduel.model.Element;
import com.avatarduel.model.Land;
import com.avatarduel.model.Skill;
import com.avatarduel.model.SkillAura;
import com.avatarduel.util.CSVReader;

public class AvatarDuel extends Application {
  private static final String LAND_CSV_FILE_PATH = "card/data/land.csv";
  private static final String CHARACTER_CSV_FILE_PATH = "card/data/character.csv";
  private static final String SKILL_AURA_CSV_FILE_PATH = "card/data/skill_aura.csv";
  private List<Card> cardList;

  public void loadCards() throws IOException, URISyntaxException {
    this.cardList = new ArrayList<>();

    // Read land cards
    File landCSVFile = new File(getClass().getResource(LAND_CSV_FILE_PATH).toURI());
    CSVReader landReader = new CSVReader(landCSVFile, "\t");
    landReader.setSkipHeader(true);
    List<String[]> landRows = landReader.read();
    for (String[] row : landRows) {
      cardList.add(new Land(Integer.valueOf(row[0]), row[1], Element.valueOf(row[2]), row[3], row[4]));
    }

    // Read character cards
    File characterCSVFile = new File(getClass().getResource(CHARACTER_CSV_FILE_PATH).toURI());
    CSVReader characterReader = new CSVReader(characterCSVFile, "\t");
    characterReader.setSkipHeader(true);
    List<String[]> characterRows = characterReader.read();
    for (String[] row : characterRows) {
      cardList.add(new Character(Integer.valueOf(row[0]), row[1], Element.valueOf(row[2]), row[3], row[4], Integer.valueOf(row[5]), Integer.valueOf(row[6]), Integer.valueOf(row[7])));
    }

    // Read skillAura cards
    File skillAuraCSVFile = new File(getClass().getResource(SKILL_AURA_CSV_FILE_PATH).toURI());
    CSVReader skillAuraReader = new CSVReader(skillAuraCSVFile, "\t");
    skillAuraReader.setSkipHeader(true);
    List<String[]> skillAuraRows = skillAuraReader.read();
    for (String[] row : skillAuraRows) {
      cardList.add(new SkillAura(Integer.valueOf(row[0]), row[1], Element.valueOf(row[2]), row[3], row[4], Integer.valueOf(row[5]), Integer.valueOf(row[6]), Integer.valueOf(row[7])));
    }
  }

  @Override
  public void start(Stage stage) {
    Text text = new Text();
    text.setText("Loading...");
    text.setX(50);
    text.setY(50);

    Group root = new Group();
    root.getChildren().add(text);

    Scene scene = new Scene(root, 1280, 720);

    stage.setTitle("Avatar Duel");
    stage.setScene(scene);
    stage.show();

    try {
      this.loadCards();
      text.setText("Avatar Duel!");
    } catch (Exception e) {
      text.setText("Failed to load cards: " + e);
    }
  }

  public static void main(String[] args) {
    launch();
  }
}