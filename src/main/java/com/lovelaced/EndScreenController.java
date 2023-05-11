package com.lovelaced;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class EndScreenController {

    @FXML
    private ImageView exitButton;
    @FXML
    private ImageView returnToMainButton;

    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    void mouseClicked(MouseEvent event) throws IOException {
        if (event.getSource() == exitButton) {
            System.exit(0);
        }
        else if(event.getSource() == returnToMainButton) {
            root = FXMLLoader.load(getClass().getResource("StartScreen-view.fxml"));

            stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
            stage.getScene().setRoot(root);
            stage.setFullScreen(true);
            stage.setTitle("Start Screen");
            stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
            stage.show();
        }
    }

    @FXML
    void initialize() {
        assert exitButton != null : "fx:id=\"exitButton\" was not injected: check your FXML file 'CreditScreen-view.fxml'.";

    }

}
