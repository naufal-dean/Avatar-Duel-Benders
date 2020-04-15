package com.avatarduel;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;

import com.avatarduel.gamephase.Phase;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import com.avatarduel.controller.*;
import com.avatarduel.gamephase.DrawPhase;
import com.avatarduel.gameutils.GameDeck;
import com.avatarduel.gameutils.GameStatus;
import com.avatarduel.model.*;
import com.avatarduel.model.Character;
import com.avatarduel.util.*;

public class AvatarDuel extends Application {
  private static final String LAND_CSV_FILE_PATH = "card/data/land.csv";
  private static final String CHARACTER_CSV_FILE_PATH = "card/data/character.csv";
  private static final String SKILL_AURA_CSV_FILE_PATH = "card/data/skill_aura.csv";
  private static final int INITIAL_CARD_IN_HAND = 7;
  private List<Card> cardList;

  // TODO: remove loadCards as it is not used
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
   # @param stage The Stage
   * @throws Exception When exception occurs render error page
   */
  void renderGame(Stage stage) throws Exception {
    try {
      // Load main UI
      FXMLLoader loader = new FXMLLoader();
      MainController mainController = new MainController();
      loader.setLocation(getClass().getResource("view/Main.fxml"));
      loader.setController(mainController);
      Parent root = loader.load();
      // Load game
      loadGame(mainController);
      // Present game
      Scene scene = new Scene(root, 1550, 800);
      stage.setTitle("Avatar Duel");
      stage.setScene(scene);
      stage.show();
      // TODO: stage.setFullScreen(true);
      // Start the game logic
      startGame(mainController);
    } catch (Exception e) {
      System.out.println(e);
    }
  }

  /**
   * Main game setup procedure
   * @param mainController The MainController for the UI
   * @throws Exception From fieldController.setCardOnField
   */
  void loadGame(MainController mainController) throws Exception {
    // Get game deck
    GameDeck deckBottom = GameStatus.getGameStatus().getGameDeck().get(Player.BOTTOM);
    GameDeck deckTop = GameStatus.getGameStatus().getGameDeck().get(Player.TOP);
    // Load hand
    HandController handBottomController = mainController.getHandControllerMap().get(Player.BOTTOM);
    HandController handTopController = mainController.getHandControllerMap().get(Player.TOP);
    for (int i = 0; i < INITIAL_CARD_IN_HAND; i++)
      handBottomController.addCardOnHand(deckBottom.draw(), Player.BOTTOM);
    for (int i = 0; i < INITIAL_CARD_IN_HAND; i++)
      handTopController.addCardOnHand(deckTop.draw(), Player.TOP);
    handTopController.flipCardInHand();
    // Update deck view
    mainController.getDeckControllerMap().get(Player.BOTTOM).init();
    mainController.getDeckControllerMap().get(Player.TOP).init();
  }

  /**
   * Main game logic procedure
   */
  void startGame(MainController mainController) {
    // Start draw phase as entry point in game loop
    DrawPhase.getDrawPhase().startPhase(mainController);
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
      renderGame(stage);
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