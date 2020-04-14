package com.avatarduel.controller;

import java.io.IOException;
import java.net.URL;
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

public class MainController implements Initializable {
    /**
     * Field controller
     */
    private FieldController fieldController;
    /**
     * Hand controller
     */
    private HandController handBottomController, handTopController;
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
    public MainController() {}

    /**
     * Getter for fieldController
     * @return this.fieldController
     */
    public FieldController getFieldController() {
        return this.fieldController;
    }

    /**
     * Getter for handBottomController
     * @return this.handBottomController
     */
    public HandController getHandBottomController() {
        return this.handBottomController;
    }

    /**
     * Getter for handTopController
     * @return this.handTopController
     */
    public HandController getHandTopController() {
        return this.handTopController;
    }

    /**
     * Initialize field
     */
    public void initField() {
        // Set background image
        this.fieldBackground.setImage(new Image(AvatarDuel.class.getResource("background/field_background.jpg").toString()));
        // Create loader
        FXMLLoader fieldLoader = new FXMLLoader();
        FieldController fieldController = new FieldController();
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
     * Initialize hand
     */
    public void initHand() {
        // Create loader
        FXMLLoader handBottomLoader = new FXMLLoader();
        HandController handBottomController = new HandController();
        handBottomLoader.setLocation(AvatarDuel.class.getResource("view/Hand.fxml"));
        handBottomLoader.setController(handBottomController);

        FXMLLoader handTopLoader = new FXMLLoader();
        HandController handTopController = new HandController();
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
        this.handBottomController = handBottomController;
        this.handTopController = handTopController;
    }

    /**
     * {@inheritDoc}
     */
    @Override @FXML
    public void initialize(URL url, ResourceBundle resources) {
        this.cardDetail.setStyle("-fx-border-color: black");
        this.phase.setStyle("-fx-border-color: black");
        this.initField();
        this.deckBottom.setStyle("-fx-border-color: black");
        this.deckTop.setStyle("-fx-border-color: black");
        this.powerBottom.setStyle("-fx-border-color: black");
        this.powerTop.setStyle("-fx-border-color: black");
        this.initHand();
    }
}