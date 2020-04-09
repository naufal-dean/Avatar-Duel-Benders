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
     * Main field display
     */
    @FXML private StackPane field;
    /**
     * Field background
     */
    @FXML private ImageView fieldBackground;
    /**
     * Left side of the field
     */
    @FXML private Pane sideFieldLeft;
    /**
     * Right side of the field
     */
    @FXML private Pane sideFieldRight;

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
     * Initialize field
     */
    public void initField() {
        // Initialize field
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
     * {@inheritDoc}
     */
    @Override @FXML
    public void initialize(URL url, ResourceBundle resources) {
        this.initField();
    }
}