package com.screens;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import com.game.*;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

public class EndScreenController {

    @FXML
    private ImageView exitButton;
    @FXML
    private ImageView returnToMainButton;

    @FXML
    private Label first, second, third, fourth;

    private Stage stage;

    private Parent root;

    @FXML
    void mouseClicked(MouseEvent event) throws IOException {
        Music.playButtonSound();
        if (event.getSource() == exitButton) {
            System.exit(0);
        }
        else if(event.getSource() == returnToMainButton) {
            Music.changeSong(Music.getStartScreenSong());
            root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("StartScreen-view.fxml")));

            stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
            stage.getScene().setRoot(root);
            stage.setFullScreen(true);
            stage.setTitle("Start Screen");
            stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
            stage.show();
        }
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

    @FXML
    void initialize() {
        ArrayList<Label> winners = new ArrayList<>();
        ArrayList<Player> playersWon = Game.getPlayerList();

        Collections.sort(playersWon);

        winners.add(first);
        winners.add(second);
        winners.add(third);
        winners.add(fourth);

        for(int i = 0; i < playersWon.size(); i++) {
            winners.get(i).setText(playersWon.get(i).getName());
            winners.get(i).setVisible(true);
        }

        assert exitButton != null : "fx:id=\"exitButton\" was not injected: check your FXML file 'EndScreen-view.fxml'.";
        assert first != null : "fx:id=\"first\" was not injected: check your FXML file 'EndScreen-view.fxml'.";
        assert fourth != null : "fx:id=\"fourth\" was not injected: check your FXML file 'EndScreen-view.fxml'.";
        assert returnToMainButton != null : "fx:id=\"returnToMainButton\" was not injected: check your FXML file 'EndScreen-view.fxml'.";
        assert second != null : "fx:id=\"second\" was not injected: check your FXML file 'EndScreen-view.fxml'.";
        assert third != null : "fx:id=\"third\" was not injected: check your FXML file 'EndScreen-view.fxml'.";

    }

}