package com.avatarduel.controller;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

import com.avatarduel.AvatarDuel;
import com.avatarduel.model.Player;

public class MainController implements Initializable {
    /**
     * Card details controller
     */
    private CardDetailsController cardDetailsController;
    /**
     * Field controller
     */
    private FieldController fieldController;
    /**
     * Hand controller
     */
    private HashMap<Player, HandController> handControllerMap;
    /**
     * Power controller
     */
    private HashMap<Player, PowerController> powerControllerMap;
    /**
     * Power controller
     */
    private HashMap<Player, DeckController> deckControllerMap;
    /**
     * Card detail display
     */
     @FXML private Pane mainAnchor;
    /**
     * Card detail display
     */
    @FXML private Pane mainHBox;
    /**
     * Card detail display
     */
    @FXML private Pane cardDetail;
    /**
     * Game phase display
     */
    @FXML private StackPane phase;
    /**
     * Main field display
     */
    @FXML private StackPane field;
    /**
     * Field background
     */
    @FXML private ImageView fieldBackground;
    /**
     * Player deck display
     */
    @FXML private Pane deckBottom, deckTop;
    /**
     * Player power display
     */
    @FXML private Pane powerBottom, powerTop;
    /**
     * Player hands display
     */
    @FXML private Pane handBottom, handTop;

    /**
     * Constructor
     */
    public MainController() {
        this.handControllerMap = new HashMap<>();
        this.powerControllerMap = new HashMap<>();
        this.deckControllerMap = new HashMap<>();
    }

    /**
     * Getter for fieldController
     * @return this.fieldController
     */
    public FieldController getFieldController() {
        return this.fieldController;
    }

    /**
     * Getter for handControllerMap
     * @return this.handControllerMap
     */
    public HashMap<Player, HandController> getHandControllerMap() {
        return this.handControllerMap;
    }
      
    /**
     * Getter for powerControllerMap
     * @return this.powerControllerMap
     */
    public HashMap<Player, PowerController> getPowerControllerMap() {
        return this.powerControllerMap;
    }
    
    /**
     * Getter for deckControllerMap
     * @return this.deckControllerMap
     */
    public HashMap<Player, DeckController> getDeckControllerMap() {
        return this.deckControllerMap;
    }
  
    /**
     * Initialize card details
     */
    public void initCardDetails() {
        // Create loader
        FXMLLoader cardDetailsLoader = new FXMLLoader();
        CardDetailsController cardDetailsController = new CardDetailsController();
        cardDetailsLoader.setLocation(AvatarDuel.class.getResource("view/CardDetails.fxml"));
        cardDetailsLoader.setController(cardDetailsController);

        // Create and assign pane
        try {
            AnchorPane cardDetailsPane = cardDetailsLoader.load();
            this.cardDetail.getChildren().add(cardDetailsPane);
        } catch (IOException e) {
            System.out.println("Error occured: " + e);
        }
        // Assign card details controller
        this.cardDetailsController = cardDetailsController;
    }

    /**
     * Initialize field, need card details controller
     */
    public void initField() {
        // Set background image
        this.fieldBackground.setImage(new Image(AvatarDuel.class.getResource("background/field_background.jpg").toString()));
        // Create loader
        FXMLLoader fieldLoader = new FXMLLoader();
        FieldController fieldController = new FieldController(this.cardDetailsController);
        fieldLoader.setLocation(AvatarDuel.class.getResource("view/Field.fxml"));
        fieldLoader.setController(fieldController);

        // Create and assign pane
        try {
            AnchorPane fieldPane = fieldLoader.load();
            this.field.getChildren().add(fieldPane);
        } catch (IOException e) {
            System.out.println("Error occured: " + e);
        }
        // Assign field controller
        this.fieldController = fieldController;
    }

    /**
     * Initialize hand, need card details controller
     */
    public void initHand() {
        // Create loader
        FXMLLoader handBottomLoader = new FXMLLoader();
        HandController handBottomController = new HandController(this.cardDetailsController);
        handBottomLoader.setLocation(AvatarDuel.class.getResource("view/Hand.fxml"));
        handBottomLoader.setController(handBottomController);

        FXMLLoader handTopLoader = new FXMLLoader();
        HandController handTopController = new HandController(this.cardDetailsController);
        handTopLoader.setLocation(AvatarDuel.class.getResource("view/Hand.fxml"));
        handTopLoader.setController(handTopController);

        // Create and assign pane
        try {
            StackPane handBottomPane = handBottomLoader.load();
            this.handBottom.getChildren().add(handBottomPane);
            StackPane handTopPane = handTopLoader.load();
            this.handTop.getChildren().add(handTopPane);
        } catch (IOException e) {
            System.out.println("Error occured: " + e);
        }
        // Rotate hand top display
        handTopController.rotateHandDisplay();
        // Assign hand controller
        this.handControllerMap.put(Player.BOTTOM, handBottomController);
        this.handControllerMap.put(Player.TOP, handTopController);
    }

    public void initDeck() {
        FXMLLoader deckBottomLoader = new FXMLLoader();
        DeckController deckBottomController = new DeckController();
        deckBottomLoader.setLocation(AvatarDuel.class.getResource("view/Deck.fxml"));
        deckBottomLoader.setController(deckBottomController);

        FXMLLoader deckTopLoader = new FXMLLoader();
        DeckController deckTopController = new DeckController();
        deckTopLoader.setLocation(AvatarDuel.class.getResource("view/Deck.fxml"));
        deckTopLoader.setController(deckTopController);

        // Create and assign pane
        try {
            StackPane deckBottomPane = deckBottomLoader.load();
            this.deckBottom.getChildren().add(deckBottomPane);
            StackPane deckTopPane = deckTopLoader.load();
            this.deckTop.getChildren().add(deckTopPane);
        } catch (IOException e) {
            System.out.println("Error occured: " + e);
        }
        // Rotate hand top display
        deckTopController.rotateDeckDisplay();
        // Assign deck controller
        this.deckControllerMap.put(Player.BOTTOM, deckBottomController);
        this.deckControllerMap.put(Player.TOP, deckTopController);
    }

    
    /**
     * {@inheritDoc}
     */
    @Override @FXML
    public void initialize(URL url, ResourceBundle resources) {
        this.initCardDetails();
        this.phase.setStyle("-fx-border-color: black");
        this.initField();
        this.deckBottom.setStyle("-fx-border-color: black");
        this.deckTop.setStyle("-fx-border-color: black");
        this.initDeck();
        this.powerBottom.setStyle("-fx-border-color: black");
        this.powerTop.setStyle("-fx-border-color: black");
        this.initHand();
    }
}