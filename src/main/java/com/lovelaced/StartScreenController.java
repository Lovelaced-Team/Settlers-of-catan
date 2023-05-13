package com.lovelaced;

import java.io.IOException;
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
    private ImageView creditsButton;

    @FXML
    private ImageView startButton;

    @FXML
    private BorderPane startPane;

    @FXML
    private ImageView exitButton;

    @FXML
    private ImageView tutorialButton;

    private Stage stage;
    private Parent root;

    @FXML
    void mouseClicked(MouseEvent event) throws IOException {
        if (event.getSource() == startButton) {
            root = FXMLLoader.load(getClass().getResource("PlayerCustomizationScreen-view.fxml"));

            stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
            stage.getScene().setRoot(root);
            stage.setFullScreen(true);
            stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
            stage.setTitle("Player Screen");
            stage.show();
        }
        else if (event.getSource() == creditsButton) {
            root = FXMLLoader.load(getClass().getResource("CreditScreen-view.fxml"));

            stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
            stage.getScene().setRoot(root);
            stage.setFullScreen(true);
            stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
            stage.setTitle("Credit Screen");
            stage.show();
        }
        else if (event.getSource() == exitButton) {
            System.exit(0);
        }
        else if (event.getSource() == tutorialButton) {
            root = FXMLLoader.load(getClass().getResource("TutorialScreen-view.fxml"));

            stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
            stage.getScene().setRoot(root);
            stage.setFullScreen(true);
            stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
            stage.setTitle("Player Screen");
            stage.show();
        }
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
