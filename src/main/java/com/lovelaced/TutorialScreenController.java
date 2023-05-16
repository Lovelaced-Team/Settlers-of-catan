package com.lovelaced;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class TutorialScreenController {

    @FXML
    private ImageView leftArrow, rightArrow, tutorialInfo;

    private Stage stage;
    private Parent root;

    String page = "src/main/resources/assets/tutorialScreen/TutorialScreen";
    int numberOfPage = 1;

    @FXML
    void changePage(MouseEvent event) throws FileNotFoundException {

        if (event.getSource() == rightArrow && numberOfPage < 11)
            numberOfPage++;
            
        else if (event.getSource() == leftArrow && numberOfPage > 1)
            numberOfPage--;
        
        tutorialInfo.setImage(new Image(new FileInputStream(page + numberOfPage + ".png")));
        switch(numberOfPage) {
            case 1 -> leftArrow.setVisible(false);
            case 2 -> leftArrow.setVisible(true);
            case 10 -> rightArrow.setVisible(true);
            case 11 -> rightArrow.setVisible(false);

        }

    }
    @FXML
    public void exit(MouseEvent event) throws IOException {
        sceneGenerator("StartScreen-view.fxml", event, "Start Screen");
    }

    private void sceneGenerator(String name, MouseEvent event, String Player_Screen) throws IOException {
        root = FXMLLoader.load(getClass().getResource(name));

        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.getScene().setRoot(root);
        stage.setFullScreen(true);
        stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        stage.setTitle(Player_Screen);
        stage.show();
    }

    @FXML
    void initialize() {
        assert leftArrow != null : "fx:id=\"leftArrow\" was not injected: check your FXML file 'TutorialScreen-view.fxml'.";
        assert rightArrow != null : "fx:id=\"rightArrow\" was not injected: check your FXML file 'TutorialScreen-view.fxml'.";
        assert tutorialInfo != null : "fx:id=\"tutorialInfo\" was not injected: check your FXML file 'TutorialScreen-view.fxml'.";

    }

}
