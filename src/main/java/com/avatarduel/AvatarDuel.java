package com.avatarduel;

import java.awt.*;

import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import com.avatarduel.controller.*;
import com.avatarduel.gamephase.DrawPhase;
import com.avatarduel.gameutils.GameDeck;
import com.avatarduel.gameutils.GameStatus;
import com.avatarduel.model.*;

public class AvatarDuel extends Application {
  private final Dimension screenDimension = Toolkit.getDefaultToolkit().getScreenSize();
  private final int INITIAL_CARD_IN_HAND = 7;

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
      Node root = loader.load();
      // Load game
      loadGame(mainController);
      // Set main UI in wrapper
      GridPane wrapper = new GridPane();
      wrapper.setAlignment(Pos.CENTER);
      GridPane.setHalignment(root, HPos.CENTER);
      GridPane.setValignment(root, VPos.CENTER);
      wrapper.add(root, 0, 0);
      // Scale main UI
      // Create scale for root
      DoubleProperty scale = new SimpleDoubleProperty(screenDimension.getWidth() / 1550d);
      root.scaleXProperty().bind(scale);
      root.scaleYProperty().bind(scale);
      // Present game
      Scene scene = new Scene(wrapper, screenDimension.getWidth(), screenDimension.getHeight());
      stage.setTitle("Avatar Duel");
      stage.setScene(scene);
      stage.show();
      stage.setFullScreen(true);
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
    GameDeck deckBottom = GameStatus.getGameStatus().getGameDeckMap().get(Player.BOTTOM);
    GameDeck deckTop = GameStatus.getGameStatus().getGameDeckMap().get(Player.TOP);
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