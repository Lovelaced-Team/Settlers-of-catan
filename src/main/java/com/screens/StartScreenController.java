package com.screens;

import java.io.IOException;
import java.util.Objects;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class StartScreenController {

    @FXML
    private ImageView creditsButton, startButton, exitButton ,tutorialButton;

    @FXML
    private BorderPane startPane;

    private Stage stage;
    private Parent root;

    @FXML
    void mouseClicked(MouseEvent event) throws IOException {
        //START
        if (event.getSource() == startButton)
            sceneGenerator("PlayerCustomizationScreen-view.fxml", event, "Player Screen");

        //CREDITS
        else if (event.getSource() == creditsButton)
            sceneGenerator("CreditScreen-view.fxml", event, "Credit Screen");

        //TUTORIAL
        else if (event.getSource() == tutorialButton)
            sceneGenerator("TutorialScreen-view.fxml", event, "Player Screen");

        //EXIT
        else if (event.getSource() == exitButton)
            System.exit(0);

    }

    private void sceneGenerator(String name, MouseEvent event, String Player_Screen) throws IOException {
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(name)));

        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.getScene().setRoot(root);
        stage.setFullScreen(true);
        stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        stage.setTitle(Player_Screen);
        stage.show();
    }

    @FXML
    void animationPop(MouseEvent event) {
        ((ImageView)event.getSource()).setStyle("-fx-effect: dropShadow(gaussian, " + "#FFC31C" + ", 28, 0.7, 0, 0)");
    }
    @FXML
    void animationPopUp(MouseEvent event) {
        ((ImageView)event.getSource()).setStyle(null);
    }

    @FXML
    void initialize() {
        assert creditsButton != null : "fx:id=\"creditsButton\" was not injected: check your FXML file 'StartScreen-view.fxml'.";
        assert exitButton != null : "fx:id=\"exitButton\" was not injected: check your FXML file 'StartScreen-view.fxml'.";
        assert startButton != null : "fx:id=\"startButton\" was not injected: check your FXML file 'StartScreen-view.fxml'.";
        assert startPane != null : "fx:id=\"startPane\" was not injected: check your FXML file 'StartScreen-view.fxml'.";
        assert tutorialButton != null : "fx:id=\"tutorialButton\" was not injected: check your FXML file 'StartScreen-view.fxml'.";

    }

}
