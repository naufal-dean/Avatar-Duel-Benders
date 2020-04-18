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
     * Health Bar Controller
     */
    private HashMap<Player, HealthController> healthControllerMap;
    /**
     * Card details controller
     */
    private CardDetailsController cardDetailsController;
    /**
     * Phase controller
     */
    private PhaseController phaseController;
    /**
     * Field controller
     */
    private FieldController fieldController;
    /**
     * Deck controller
     */
    private HashMap<Player, DeckController> deckControllerMap;
    /**
     * Power controller
     */
    private HashMap<Player, PowerController> powerControllerMap;
    /**
     * Hand controller
     */
    private HashMap<Player, HandController> handControllerMap;
    /**
     * Card detail display
     */
     @FXML private Pane mainAnchor;
    /**
     * Card detail display
     */
    @FXML private Pane mainHBox;
    /**
     * Main background
     */
    @FXML private ImageView mainBackground;
    /**
     * Health display
     */
    @FXML private Pane healthBottom, healthTop;
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
        this.healthControllerMap = new HashMap<>();
        this.deckControllerMap = new HashMap<>();
        this.powerControllerMap = new HashMap<>();
        this.handControllerMap = new HashMap<>();
    }

    /**
     * Getter for healthControllerMap
     * @return this.healthControllerMap
     */
    public HashMap<Player, HealthController> getHealthControllerMap() {
        return this.healthControllerMap;
    }

    /**
     * Getter for phaseController
     * @return this.phaseController
     */
    public PhaseController getPhaseController() {
        return this.phaseController;
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
     * Initialize health display
     */
    public void initHealth() {
        FXMLLoader healthBottomLoader = new FXMLLoader();
        HealthController healthBottomController = new HealthController(Player.BOTTOM);
        healthBottomLoader.setLocation(AvatarDuel.class.getResource("view/Health.fxml"));
        healthBottomLoader.setController(healthBottomController);

        FXMLLoader healthTopLoader = new FXMLLoader();
        HealthController healthTopController = new HealthController(Player.TOP);
        healthTopLoader.setLocation(AvatarDuel.class.getResource("view/Health.fxml"));
        healthTopLoader.setController(healthTopController);

        // Create and assign pane
        try {
            StackPane healthBottomPane = healthBottomLoader.load();
            this.healthBottom.getChildren().add(healthBottomPane);
            StackPane healthTopPane = healthTopLoader.load();
            this.healthTop.getChildren().add(healthTopPane);
        } catch (IOException e) {
            System.out.println("Error occured: " + e);
        }
        // Assign health controller
        this.healthControllerMap.put(Player.BOTTOM, healthBottomController);
        this.healthControllerMap.put(Player.TOP, healthTopController);
    }

    /**
     * Initialize card details display
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
     * Initialize phase display
     */
    public void initPhase() {
        // Create loader
        FXMLLoader phaseLoader = new FXMLLoader();
        PhaseController phaseController = new PhaseController();
        phaseLoader.setLocation(AvatarDuel.class.getResource("view/Phase.fxml"));
        phaseLoader.setController(phaseController);

        // Create and assign pane
        try {
            AnchorPane phasePane = phaseLoader.load();
            this.phase.getChildren().add(phasePane);
        } catch (IOException e) {
            System.out.println("Error occured: " + e);
        }
        // Assign field controller
        this.phaseController = phaseController;
    }

    /**
     * Initialize field display, need card details controller
     */
    public void initField() {
        // Set background image
        this.fieldBackground.setImage(new Image(AvatarDuel.class.getResource("img/background/field_background.jpg").toString()));
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
     * Initialize deck display
     */
    public void initDeck() {
        FXMLLoader deckBottomLoader = new FXMLLoader();
        DeckController deckBottomController = new DeckController(Player.BOTTOM);
        deckBottomLoader.setLocation(AvatarDuel.class.getResource("view/Deck.fxml"));
        deckBottomLoader.setController(deckBottomController);

        FXMLLoader deckTopLoader = new FXMLLoader();
        DeckController deckTopController = new DeckController(Player.TOP);
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
        // Assign deck controller
        this.deckControllerMap.put(Player.BOTTOM, deckBottomController);
        this.deckControllerMap.put(Player.TOP, deckTopController);
    }

    /**
     * Initialize power display
     */
    public void initPower() {
        // Create loader
        FXMLLoader powerBottomLoader = new FXMLLoader();
        PowerController powerBottomController = new PowerController(Player.BOTTOM);
        powerBottomLoader.setLocation(AvatarDuel.class.getResource("view/Power.fxml"));
        powerBottomLoader.setController(powerBottomController);

        FXMLLoader powerTopLoader = new FXMLLoader();
        PowerController powerTopController = new PowerController(Player.TOP);
        powerTopLoader.setLocation(AvatarDuel.class.getResource("view/Power.fxml"));
        powerTopLoader.setController(powerTopController);

        // Create and assign pane
        try {
            AnchorPane powerBottomPane = powerBottomLoader.load();
            this.powerBottom.getChildren().add(powerBottomPane);
            AnchorPane powerTopPane = powerTopLoader.load();
            this.powerTop.getChildren().add(powerTopPane);
        } catch (IOException e) {
            System.out.println("Error occured: " + e);
        }
        // Assign power controller
        this.powerControllerMap.put(Player.BOTTOM, powerBottomController);
        this.powerControllerMap.put(Player.TOP, powerTopController);
    }

    /**
     * Initialize hand display, need card details controller
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
    
    /**
     * {@inheritDoc}
     */
    @Override @FXML
    public void initialize(URL url, ResourceBundle resources) {
//        this.mainBackground.setImage(new Image(AvatarDuel.class.getResource("background/main2.png").toString()));
        this.initHealth();
        this.initCardDetails();
        this.phase.setStyle("-fx-border-color: black");
        this.initPhase();
        this.initField();
        this.deckBottom.setStyle("-fx-border-color: black");
        this.deckTop.setStyle("-fx-border-color: black");
        this.initDeck();
        this.powerBottom.setStyle("-fx-border-color: black");
        this.powerTop.setStyle("-fx-border-color: black");
        this.initPower();
        this.initHand();
    }
}