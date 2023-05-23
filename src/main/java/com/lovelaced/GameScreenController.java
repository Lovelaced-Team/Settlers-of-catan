package com.lovelaced;

import com.board.Board;
import com.board.Hexagon;
import com.game.Game;
import com.game.Player;

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
import java.util.List;


public class GameScreenController {

    private Stage stage;
    private Parent root;

    // FXML elements
    @FXML
    private AnchorPane boardPane, menuScreen;

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
        new Game();
        players = Game.getPlayerList();

        initializePlayers(); // Adds player info to the scene
        initializeHexagons(); // Adds hexagons to the scene
    }

    // Handle escape key event to toggle the menu screen visibility
    @FXML
    void escape(KeyEvent event) {
        if (event.getCode() == KeyCode.P) {
            menuScreen.setVisible(!menuScreen.isVisible());
        }
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
            Player player = players.get(i);
            playerNodes = switch (i) {
                case 0 -> new ArrayList<>(List.of(nameLabel1, playerBoard1, materials1));
                case 1 -> new ArrayList<>(List.of(nameLabel2, playerBoard2, materials2));
                case 2 -> new ArrayList<>(List.of(nameLabel3, playerBoard3, materials3));
                case 3 -> new ArrayList<>(List.of(nameLabel4, playerBoard4, materials4));
                default -> throw new IllegalStateException("Unexpected value: " + i);
            };
            playersItems.put(player, playerNodes);
        }
    }


    // Initialize the hexagons on the game board
    private void initializeHexagons() throws FileNotFoundException {
        Board.getHexagonList().forEach(hexagon -> {
            ImageView boardItems;
            if (!hexagon.getBiome().equals("Desert")) {
                String path = "src/main/resources/assets/hexagons/hexagonNumbers/no." + hexagon.getNumber() + ".png";
                try {
                    boardItems = new ImageView(new Image(new FileInputStream(path)));
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
                initializeBoardImages(hexagon, boardItems, 48, 52, 80.0, 80.0);
            } else {
                boardItems = new ImageView(hexagon.getHasPirate().getImage());
                initializeBoardImages(hexagon, boardItems, 55, 23, 119.0, 93.0);
            }
            initializeBoardImages(hexagon, boardItems, 0, 0, 195.0, 178.0);
        });
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
        initializePlayerItemsMap();
        initializePlayerInfo();
        initializeMaterialList();
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
                }
            }
        }
    }

    // FXML initialization method
    @FXML
    void initialize() {
        assert boardPane != null : "fx:id=\"boardPane\" was not injected: check your FXML file 'GameScreen-view.fxml'.";
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
