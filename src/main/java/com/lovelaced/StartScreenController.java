package com.lovelaced;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.Duration;

public class StartScreenController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ImageView creditsButton;

    @FXML
    private ImageView startButton;

    @FXML
    private BorderPane startPane;

    @FXML
    private ImageView exitButton;

    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    void mouseClicked(MouseEvent event) throws IOException {
        if (event.getSource() == startButton) {
            System.out.println("clickedStartButton");
        }
        else if (event.getSource() == creditsButton) {
            root = FXMLLoader.load(getClass().getResource("CreditScreen-view.fxml"));

            stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.setFullScreen(true);
            stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
            stage.setTitle("Credit Screen");
            stage.show();
        }
        else if (event.getSource() == exitButton) {
            System.out.println("clickedExitPane");
            System.exit(0);
        }
    }

    @FXML
    void initialize() {
        assert creditsButton != null : "fx:id=\"creditsButton\" was not injected: check your FXML file 'StartScreen-view.fxml'.";
        assert startButton != null : "fx:id=\"startButton\" was not injected: check your FXML file 'StartScreen-view.fxml'.";
        assert startPane != null : "fx:id=\"startPane\" was not injected: check your FXML file 'StartScreen-view.fxml'.";

    }

}
