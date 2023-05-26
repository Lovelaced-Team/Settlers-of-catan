package com.lovelaced;

import com.board.Board;
import com.board.Hexagon;
import com.game.*;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;



public class GameScreenController {
    private Parent root;

    // FXML elements
    @FXML
    private AnchorPane boardPane, menuScreen;

    @FXML
    private ListView<String> cards1, cards2, cards3, cards4;
    @FXML
    private ListView<Integer> stats1, stats2, stats3, stats4;

    @FXML
    private Label nameLabel1, nameLabel2, nameLabel3, nameLabel4;

    @FXML
    private ImageView playerBoard1, playerBoard2, playerBoard3, playerBoard4;

    @FXML
    private ListView<Integer> materials1, materials2, materials3, materials4;

    // Data structures to store player and UI elements
    private ArrayList<Player> players = new ArrayList<>();
    private HashMap<Player, ArrayList<Node>> playersItems = new HashMap<>();

    // Start the game board
    public void startBoard() throws FileNotFoundException {
        // Adds hexagons to the scene
        new Game();
        players = Game.getPlayerList();
        initializePlayers(); // Adds player info to the scene
        initializeHexagons();
    }

    @FXML
    void settings(MouseEvent event) {
        menuScreen.setVisible(!menuScreen.isVisible());
    }

    // Handle escape key event to toggle the menu screen visibility
    @FXML
    void escape(KeyEvent event) {
        if (event.getCode() == KeyCode.P) {
            menuScreen.setVisible(!menuScreen.isVisible());
        }
    }

    @FXML
    void mute(MouseEvent event) throws FileNotFoundException {
        String isMuted;
        isMuted = (Music.songPlaying())? "ON" : "OFF";
        ((ImageView) event.getSource()).setImage(new Image(new FileInputStream("src/main/resources/assets/settings/sound_" + isMuted + ".png")));
    }

    // Exit the game and go back to the start screen
    @FXML
    void exitGame(MouseEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("StartScreen-view.fxml"));

        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        configureStage(stage, "Start");
    }

    private void configureStage(Stage stage, String stageTitle) throws IOException {
        stage.getScene().setRoot(root);
        stage.setFullScreen(true);
        stage.setTitle(stageTitle + " " + "Screen");
        stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        stage.show();
    }


    // Initialize the mapping between players and UI elements
    private void initializePlayerItemsMap() {
        ArrayList<Node> playerNodes;
        for (int i = 0; i < players.size(); i++) {
            playerNodes = new ArrayList<>();
            Player player = players.get(i);
            switch (i) {
                case 0 -> {
                    playerNodes.add(nameLabel1);
                    playerNodes.add(playerBoard1);
                    playerNodes.add(materials1);
                    playerNodes.add(cards1);
                    playerNodes.add(stats1);
                }
                case 1 -> {
                    playerNodes.add(nameLabel2);
                    playerNodes.add(playerBoard2);
                    playerNodes.add(materials2);
                    playerNodes.add(cards2);
                    playerNodes.add(stats2);
                }
                case 2 -> {
                    playerNodes.add(nameLabel3);
                    playerNodes.add(playerBoard3);
                    playerNodes.add(materials3);
                    playerNodes.add(cards3);
                    playerNodes.add(stats3);
                }
                case 3 -> {
                    playerNodes.add(nameLabel4);
                    playerNodes.add(playerBoard4);
                    playerNodes.add(materials4);
                    playerNodes.add(cards4);
                    playerNodes.add(stats4);
                }
                default -> throw new IllegalStateException("Unexpected value: " + i);
            };
            playersItems.put(player, playerNodes);
        }
    }


    // Initialize the hexagons on the game board
    private void initializeHexagons() throws FileNotFoundException {
        for(Hexagon hexagon : Board.getHexagonList()) {
            ImageView boardItems = new ImageView(hexagon.getImage());
            initializeBoardImages(hexagon, boardItems, 0, 0, 178.0, 195.0);

            if (!hexagon.getBiome().equals("Desert")) {
                String path = "src/main/resources/assets/hexagons/hexagonNumbers/no." + hexagon.getNumber() + ".png";
                boardItems = new ImageView(new Image(new FileInputStream(path)));
                initializeBoardImages(hexagon, boardItems, 48, 52, 80.0, 80.0);
            } else {
                boardItems = new ImageView(hexagon.getHasPirate().getImage());
                initializeBoardImages(hexagon, boardItems, 55, 23, 93.0, 119.0);
            }

        }
    }


    // Initialize the layout and properties of the board items
    private void initializeBoardImages(Hexagon hexagon, ImageView boardItems, int x, int y, double width, double height) {
        double layoutX = hexagon.getCoords().getX() + x;
        double layoutY = hexagon.getCoords().getY() + y;
        boardItems.setLayoutX(layoutX);
        boardItems.setLayoutY(layoutY);
        boardItems.setFitWidth(width);
        boardItems.setFitHeight(height);
        boardItems.toFront();
        boardPane.getChildren().add(boardItems);
    }


    // Initialize player information on the UI
    private void initializePlayers() {
        Music.changeSong(Music.gameScreenSong);
        initializePlayerItemsMap();
        initializePlayerInfo();
        initializeMaterialList();
        initializeCardList();
        initializeStatsList();
    }

    // Initialize player names and images on the UI
    private void initializePlayerInfo() {
        for (Player player : players) {
            ArrayList<Node> playerNodes = playersItems.get(player);
            Label tempLabel = (Label) playerNodes.get(0);
            ImageView tempImageView = (ImageView) playerNodes.get(1);
            tempLabel.getParent().setVisible(true);
            tempLabel.setText(player.getName());
            tempImageView.setImage(player.getImage());
        }
    }


    // Initialize material lists for each player
    private void initializeMaterialList() {
        for (Player player : players) {
            HashMap<String, Integer> materials = player.getMaterials();
            for (String material : materials.keySet()) {
                if (!material.equals("Sum")) {
                    ListView<Integer> tempList = (ListView<Integer>) playersItems.get(player).get(2);
                    tempList.getItems().add(materials.get(material));
                    tempList.setMouseTransparent(true);
                    tempList.setFocusTraversable(false);
                }
            }
        }
    }

    private void initializeCardList() {
        for (Player player : players) {
            ArrayList<Card> cards = player.getCardsList();
            for (Card card : cards) {
                ListView<String> tempList = (ListView<String>) playersItems.get(player).get(3);
                tempList.getItems().add(card.getName());
            }
        }
    }

    private void initializeStatsList() {
        for (Player player : players) {
            ListView<Integer> tempList = (ListView<Integer>) playersItems.get(player).get(4);
            tempList.getItems().add(player.getPoints());
            tempList.getItems().add(player.getCardListSize());
            tempList.getItems().add(Quest.getPlayerArmyAmount(player));
        }
    }

    // FXML initialization method
    @FXML
    void initialize() throws FileNotFoundException {
        startBoard();
        assert boardPane != null : "fx:id=\"boardPane\" was not injected: check your FXML file 'GameScreen-view.fxml'.";
        assert cards1 != null : "fx:id=\"cards1\" was not injected: check your FXML file 'GameScreen-view.fxml'.";
        assert cards2 != null : "fx:id=\"cards2\" was not injected: check your FXML file 'GameScreen-view.fxml'.";
        assert cards3 != null : "fx:id=\"cards3\" was not injected: check your FXML file 'GameScreen-view.fxml'.";
        assert cards4 != null : "fx:id=\"cards4\" was not injected: check your FXML file 'GameScreen-view.fxml'.";
        assert stats1 != null : "fx:id=\"stats1\" was not injected: check your FXML file 'GameScreen-view.fxml'.";
        assert stats2 != null : "fx:id=\"stats2\" was not injected: check your FXML file 'GameScreen-view.fxml'.";
        assert stats3 != null : "fx:id=\"stats3\" was not injected: check your FXML file 'GameScreen-view.fxml'.";
        assert stats4 != null : "fx:id=\"stats4\" was not injected: check your FXML file 'GameScreen-view.fxml'.";
        assert materials1 != null : "fx:id=\"materials1\" was not injected: check your FXML file 'GameScreen-view.fxml'.";
        assert materials2 != null : "fx:id=\"materials2\" was not injected: check your FXML file 'GameScreen-view.fxml'.";
        assert materials3 != null : "fx:id=\"materials3\" was not injected: check your FXML file 'GameScreen-view.fxml'.";
        assert materials4 != null : "fx:id=\"materials4\" was not injected: check your FXML file 'GameScreen-view.fxml'.";
        assert menuScreen != null : "fx:id=\"menuScreen\" was not injected: check your FXML file 'GameScreen-view.fxml'.";
        assert nameLabel1 != null : "fx:id=\"nameLabel1\" was not injected: check your FXML file 'GameScreen-view.fxml'.";
        assert nameLabel2 != null : "fx:id=\"nameLabel2\" was not injected: check your FXML file 'GameScreen-view.fxml'.";
        assert nameLabel3 != null : "fx:id=\"nameLabel3\" was not injected: check your FXML file 'GameScreen-view.fxml'.";
        assert nameLabel4 != null : "fx:id=\"nameLabel4\" was not injected: check your FXML file 'GameScreen-view.fxml'.";
        assert playerBoard1 != null : "fx:id=\"playerBoard1\" was not injected: check your FXML file 'GameScreen-view.fxml'.";
        assert playerBoard2 != null : "fx:id=\"playerBoard2\" was not injected: check your FXML file 'GameScreen-view.fxml'.";
        assert playerBoard3 != null : "fx:id=\"playerBoard3\" was not injected: check your FXML file 'GameScreen-view.fxml'.";
        assert playerBoard4 != null : "fx:id=\"playerBoard4\" was not injected: check your FXML file 'GameScreen-view.fxml'.";
    }

}
