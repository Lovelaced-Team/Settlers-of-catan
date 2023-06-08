package com.screens;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;

import com.game.Game;
import com.game.Music;
import com.game.Player;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

public class PlayerCustomizationController {

    private HashMap<String,String> selectedColors = new HashMap<>();
    private HashMap<String,ImageView> selectedFlasks = new HashMap<>();
    private HashMap<String, String> selectedNameFields = new HashMap<>();
    private HashMap<String, Image> selectedPlayers = new HashMap<>();


    @FXML
    private ImageView arrow1, arrow2, arrow3, arrow4;

    @FXML
    private TextField thirdField, fourthField;

    @FXML
    private ImageView exitButton, leftAddButton, rightAddButton, leftDelete, rightDelete;

    @FXML
    private AnchorPane pane1, pane2, pane3, pane4;

    @FXML
    private ImageView firstPlayer, secondPlayer, thirdPlayer, fourthPlayer;

    @FXML
    private TextField errorMessage;

    private Stage stage;
    private Parent root;

    Player createPlayer(String name, Image avatar, String colored) {
        return new Player(name, avatar, colored, 0);
    }

    @FXML
    public void deletePlayer(MouseEvent event) {
        Music.playButtonSound();
        AnchorPane pane;
        Image imgBlank = new Image(Objects.requireNonNull(StartScreen.class.getResourceAsStream("/assets/playerCustomizationScreen/playerAvatars/Character_Window_blank.png")));

        if(event.getSource() == leftDelete) {
            pane = pane3;
            thirdPlayer.setImage(imgBlank);
            thirdField.clear();
        }
        else {
            pane = pane4;
            fourthPlayer.setImage(imgBlank);
            fourthField.clear();
        }

        if (selectedFlasks.containsKey(pane.getId()))
            selectedFlasks.get(pane.getId()).setStyle(null);

        selectedFlasks.remove(pane.getId());
        selectedColors.remove(pane.getId());
        selectedNameFields.remove(pane.getId());
        selectedPlayers.remove(pane.getId());

        pane.setVisible(false);
    }

    public boolean checkForDuplicates() {
        HashSet<String> playerNames = new HashSet<>(selectedNameFields.values());

        if(playerNames.size() != selectedNameFields.values().size()) {
            errorMessage.setVisible(true);
            return true;
        }

        errorMessage.setVisible(false);
        return false;
    }

    @FXML
    public void startGame(MouseEvent event) throws IOException {
        Music.playButtonSound();

        Game.getPlayerList().clear();
        String name;
        String colored;
        Image avatar;
        boolean playerCreationFilter = true;

        if( !checkForDuplicates() ) {
            for (String pane : selectedNameFields.keySet()) {
                if (selectedColors.containsKey(pane)) {
                    name = selectedNameFields.get(pane);
                    colored = selectedColors.get(pane);
                    avatar = selectedPlayers.get(pane);
                    if (!name.equals(""))
                        Game.addPlayer(createPlayer(name, avatar, colored));
                    else
                        playerCreationFilter = false;
                } else
                    playerCreationFilter = false;

            }

            if (Game.getPlayerList().size() >= 2 && playerCreationFilter) {
                sceneGenerator("GameScreen-view.fxml", event, "Game Screen");
            }
        }
    }

    @FXML
    public void nameTyped(KeyEvent event) {
        TextField nameFiled = (TextField) event.getSource();
        String pane = nameFiled.getParent().getId();
        selectedNameFields.put(pane, nameFiled.getText());
    }

    @FXML
    public void exitClicked(MouseEvent event) throws IOException {
        Music.playButtonSound();
        sceneGenerator("StartScreen-view.fxml", event, "Start Screen");
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
    public void leftClicked(MouseEvent event) {
        Music.playButtonSound();
        pane3.setVisible(true);
    }
    @FXML
    public void rightClicked(MouseEvent event) {
        Music.playButtonSound();
        pane4.setVisible(true);
    }
    private ImageView arrow = new ImageView();

    @FXML
    public void flaskClicked(MouseEvent event) throws FileNotFoundException {
        ImageView flask = (ImageView) event.getSource();
        String pane = flask.getParent().getId();
        String color = colorPicker(flask);
        if ( !selectedColors.containsValue(color) ) {
            Music.playButtonSound();

            selectedColors.put( pane, color );

            if( selectedFlasks.containsKey(pane) ){
                selectedFlasks.get(pane).setStyle(null);
            }

            selectedFlasks.put( pane, flask );
            selectedFlasks.get(pane).setStyle("-fx-effect: dropShadow(three-pass-box, " + color + ", 30, 0, 0, 0)");
            animateArrow(pane,selectedFlasks.get(pane),1000);

            setPlayerImage(pane);
        }

    }

    public String colorPicker( ImageView flask) {
        String colorUrl = flask.getImage().getUrl();
        int pathIndex = colorUrl.lastIndexOf("flask" );
        String color = colorUrl.substring( pathIndex );

        switch (color) {
            case "flaskYellow.png" -> color = "yellow";
            case "flaskRed.png" -> color = "red";
            case "flaskBlue.png" -> color = "blue";
            case "flaskGreen.png" -> color = "green";
        }
        return color;
    }


    @FXML
    void animationPop(MouseEvent event) {
        String color;
        if(event.getSource() == leftAddButton || event.getSource() == rightAddButton)
            color = "#1DCC04";
        else
            color = "#FFC31C";
        ((ImageView)event.getSource()).setStyle("-fx-effect: dropShadow(gaussian, " + color + ", 28, 0.7, 0, 0)");
    }
    @FXML
    void animationPopUp(MouseEvent event) {
        ((ImageView)event.getSource()).setStyle(null);
    }


    public void animateArrow(String pane, ImageView flask, int duration) {
        arrow.setVisible(false);

        switch (pane) {
            case "pane1" -> arrow = arrow1;
            case "pane2" -> arrow = arrow2;
            case "pane3" -> arrow = arrow3;
            case "pane4" -> arrow = arrow4;
        }

        double numberY;
        double numberX;
        numberX = arrow.getLayoutX();
        numberY = arrow.getLayoutY() - 55;
        arrow.setVisible( true );

        TranslateTransition translate = new TranslateTransition();
        if ( arrow.getX() == 0 )
            translate.setToX(flask.getLayoutX() - numberX);
        else
            translate.setToX(flask.getLayoutX());
        if ( arrow.getY() == 0 )
            translate.setToY(flask.getLayoutY() - numberY);
        else
            translate.setToY(flask.getLayoutY());

        translate.setDuration(Duration.millis(duration));
        translate.setAutoReverse(true);
        translate.setNode(arrow);
        translate.play();

    }

    public void setPlayerImage(String pane) {
        String path = "/assets/gameScreen/Character_Window_";
        switch (pane) {
            case "pane1" -> {
                firstPlayer.setImage(new Image(Objects.requireNonNull(StartScreen.class.getResourceAsStream(path + selectedColors.get(pane) + ".png"))));
                selectedPlayers.put(pane, firstPlayer.getImage());
            }
            case "pane2" -> {
                secondPlayer.setImage(new Image(Objects.requireNonNull(StartScreen.class.getResourceAsStream(path + selectedColors.get(pane) + ".png"))));
                selectedPlayers.put(pane, secondPlayer.getImage());
            }
            case "pane3" -> {
                thirdPlayer.setImage(new Image(Objects.requireNonNull(StartScreen.class.getResourceAsStream(path + selectedColors.get(pane) + ".png"))));
                selectedPlayers.put(pane, thirdPlayer.getImage());
            }
            case "pane4" -> {
                fourthPlayer.setImage(new Image(Objects.requireNonNull(StartScreen.class.getResourceAsStream(path + selectedColors.get(pane) + ".png"))));
                selectedPlayers.put(pane, fourthPlayer.getImage());
            }
        }
    }

    @FXML
    public void initialize() {
        assert arrow1 != null : "fx:id=\"arrow1\" was not injected: check your FXML file 'PlayerCustomizationScreen-view.fxml'.";
        assert arrow2 != null : "fx:id=\"arrow2\" was not injected: check your FXML file 'PlayerCustomizationScreen-view.fxml'.";
        assert arrow3 != null : "fx:id=\"arrow3\" was not injected: check your FXML file 'PlayerCustomizationScreen-view.fxml'.";
        assert arrow4 != null : "fx:id=\"arrow4\" was not injected: check your FXML file 'PlayerCustomizationScreen-view.fxml'.";
        assert errorMessage != null : "fx:id=\"errorMessage\" was not injected: check your FXML file 'PlayerCustomizationScreen-view.fxml'.";
        assert exitButton != null : "fx:id=\"exitButton\" was not injected: check your FXML file 'PlayerCustomizationScreen-view.fxml'.";
        assert firstPlayer != null : "fx:id=\"firstPlayer\" was not injected: check your FXML file 'PlayerCustomizationScreen-view.fxml'.";
        assert fourthField != null : "fx:id=\"fourthField\" was not injected: check your FXML file 'PlayerCustomizationScreen-view.fxml'.";
        assert fourthPlayer != null : "fx:id=\"fourthPlayer\" was not injected: check your FXML file 'PlayerCustomizationScreen-view.fxml'.";
        assert leftAddButton != null : "fx:id=\"leftAddButton\" was not injected: check your FXML file 'PlayerCustomizationScreen-view.fxml'.";
        assert leftDelete != null : "fx:id=\"leftDelete\" was not injected: check your FXML file 'PlayerCustomizationScreen-view.fxml'.";
        assert pane1 != null : "fx:id=\"pane1\" was not injected: check your FXML file 'PlayerCustomizationScreen-view.fxml'.";
        assert pane2 != null : "fx:id=\"pane2\" was not injected: check your FXML file 'PlayerCustomizationScreen-view.fxml'.";
        assert pane3 != null : "fx:id=\"pane3\" was not injected: check your FXML file 'PlayerCustomizationScreen-view.fxml'.";
        assert pane4 != null : "fx:id=\"pane4\" was not injected: check your FXML file 'PlayerCustomizationScreen-view.fxml'.";
        assert rightAddButton != null : "fx:id=\"rightAddButton\" was not injected: check your FXML file 'PlayerCustomizationScreen-view.fxml'.";
        assert rightDelete != null : "fx:id=\"rightDelete\" was not injected: check your FXML file 'PlayerCustomizationScreen-view.fxml'.";
        assert secondPlayer != null : "fx:id=\"secondPlayer\" was not injected: check your FXML file 'PlayerCustomizationScreen-view.fxml'.";
        assert thirdField != null : "fx:id=\"thirdField\" was not injected: check your FXML file 'PlayerCustomizationScreen-view.fxml'.";
        assert thirdPlayer != null : "fx:id=\"thirdPlayer\" was not injected: check your FXML file 'PlayerCustomizationScreen-view.fxml'.";
    }

}
