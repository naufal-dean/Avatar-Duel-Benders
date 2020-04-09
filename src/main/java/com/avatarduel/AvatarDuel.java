package com.avatarduel;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import com.avatarduel.controller.*;
import com.avatarduel.gameutils.GameDeck;
import com.avatarduel.gameutils.GameStatus;
import com.avatarduel.model.*;
import com.avatarduel.model.Character;
import com.avatarduel.util.*;

public class AvatarDuel extends Application {
  private static final String LAND_CSV_FILE_PATH = "card/data/land.csv";
  private static final String CHARACTER_CSV_FILE_PATH = "card/data/character.csv";
  private static final String SKILL_AURA_CSV_FILE_PATH = "card/data/skill_aura.csv";
  private List<Card> cardList;

  /**
   * Function to load card from csv
   * @throws IOException
   * @throws URISyntaxException
   */
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

  /**
   * Render game
   * @param gameStatus The Game Status
   # @param stage The Stage
   * @throws Exception When exception occurs render error page
   */
  void renderGame(Stage stage, GameStatus gameStatus) throws Exception {
    GameDeck deck = gameStatus.getOurDeck();
    // Load main UI
    Parent root = new Parent() {};
    try {
      // Load
      FXMLLoader loader = new FXMLLoader();
      MainController mainController = new MainController();
      loader.setLocation(getClass().getResource("view/Main.fxml"));
      loader.setController(mainController);
      root = loader.load();
      // Get field controller
      FieldController fieldController = mainController.getFieldController();
      Card card;
      for (int i = 0; i < 6; i++)
        for (int j = 0; j < 4; j++)
          if (!(card = deck.draw()).getCardType().equals(CardType.LAND))
            fieldController.setCardOnField(card, (j > 1) ? (Player.BOTTOM) : (Player.TOP), ((i + j) % 2 == 0) ? (true) : (false), i, j);
      // Get hand controller
      HandController handBottomController = mainController.getHandBottomController();
      HandController handTopController = mainController.getHandTopController();
      for (int i = 0; i < 6; i++)
        handBottomController.addCardOnHand(deck.draw(), Player.BOTTOM);
      for (int i = 0; i < 1; i++)
        handTopController.addCardOnHand(deck.draw(), Player.TOP);
    } catch (Exception e) {
      System.out.println(e);
    }
    // Present
    Scene scene = new Scene(root, 1550, 800);
    stage.setTitle("Avatar Duel");
    stage.setScene(scene);
    stage.show();
//    stage.setFullScreen(true);
  }

  /**
   * Render screen error
   * @param stage The Stage
   * @param err Error message
   */
  void renderError(Stage stage, String err) {
    // Add error message
    Text title = new Text();
    title.setText("Welp error happened...");
    title.setFont(Font.font ("Consolas", 40));
    title.setX(50);
    title.setY(50);

    Text error = new Text();
    error.setText(err);
    error.setFont(Font.font ("Consolas", 20));
    error.setX(50);
    error.setY(90);

    Group root = new Group();
    root.getChildren().add(title);
    root.getChildren().add(error);

    // Present
    Scene scene = new Scene(root, 1280, 720);
    stage.setTitle("Avatar Duel");
    stage.setScene(scene);
    stage.show();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void start(Stage stage) {
    // Initialize Game Status
    try {
      GameStatus.initGameStatus();
      GameStatus gameStatus = GameStatus.getGameStatus();
      renderGame(stage, gameStatus);
    } catch (Exception err) {
      renderError(stage, err.toString());
    }
  }

  /**
   * Main function
   * @param args The arguments
   */
  public static void main(String[] args) {
    launch();
  }
}