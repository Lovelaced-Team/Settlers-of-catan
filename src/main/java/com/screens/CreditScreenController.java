package com.screens;

import java.io.IOException;
import java.util.Objects;

import com.game.Music;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class CreditScreenController {

    @FXML
    private ImageView exitButton;

    private Stage stage;
    private Parent root;

    @FXML
    void returnToMain(MouseEvent event) throws IOException {
        Music.playButtonSound();
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("StartScreen-view.fxml")));
        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        configureStage(stage, "Start");
    }

    @FXML
    void animationPop(MouseEvent event) {
        String color = "#1DCC04";
        ((ImageView)event.getSource()).setStyle("-fx-effect: dropShadow(gaussian, " + color + ", 28, 0.7, 0, 0)");
    }
    @FXML
    void animationPopUp(MouseEvent event) {
        ((ImageView)event.getSource()).setStyle(null);
    }



    private void configureStage(Stage stage, String stageTitle) {
        stage.getScene().setRoot(root);
        stage.setFullScreen(true);
        stage.setTitle(stageTitle + " " + "Screen");
        stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        stage.show();
    }

    @FXML
    void initialize() {
        assert exitButton != null : "fx:id=\"exitButton\" was not injected: check your FXML file 'CreditScreen-view.fxml'.";

    }

}
