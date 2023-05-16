package com.lovelaced;

import com.board.Board;
import com.board.Hexagon;
import com.game.Game;
import com.game.Player;
import javafx.collections.ObservableList;
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

    private Stage stage;
    private Parent root;

    @FXML
    private AnchorPane boardPane, menuScreen;

    @FXML
    private Label nameLabel1, nameLabel2, nameLabel3, nameLabel4;

    @FXML
    private ImageView playerBoard1, playerBoard2, playerBoard3, playerBoard4;

    @FXML
    private ListView <Integer> materials1, materials2 ,materials3, materials4;
    private ArrayList<Player> players = new ArrayList<>();

    private HashMap<Player, ArrayList<Node> > playersItems = new HashMap<>();


    public void startBoard() throws FileNotFoundException {
        new Game();
        players = Game.getPlayerList();

        initializePlayers(); //Adds player info to the scene
        initializeHexagons(); //Adds hexagons to the scene
    }

    @FXML
    void escape(KeyEvent event) {
        if (event.getCode() == KeyCode.P) {
            if (menuScreen.isVisible())
                menuScreen.setVisible(false);
            else
                menuScreen.setVisible(true);
        }
    }

    @FXML
    void exitGame(MouseEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("StartScreen-view.fxml"));

        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.getScene().setRoot(root);
        stage.setFullScreen(true);
        stage.setTitle("Start Screen");
        stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        stage.show();
    }

    private void initializePlayerItemsMap() {

        for (int i = 0; i < players.size(); i++) {
            ArrayList<Node> playerNodes = new ArrayList<Node>();
            switch (i) {
                case 0 -> {
                    playerNodes.add(nameLabel1);
                    playerNodes.add(playerBoard1);
                    playerNodes.add(materials1);
                }
                case 1 -> {
                    playerNodes.add(nameLabel2);
                    playerNodes.add(playerBoard2);
                    playerNodes.add(materials2);
                }
                case 2 -> {
                    playerNodes.add(nameLabel3);
                    playerNodes.add(playerBoard3);
                    playerNodes.add(materials3);
                }
                case 3 -> {
                    playerNodes.add(nameLabel4);
                    playerNodes.add(playerBoard4);
                    playerNodes.add(materials4);
                }
            }
            playersItems.put(players.get(i), playerNodes);
        }

    }
    

    private void initializeHexagons() throws FileNotFoundException {
        for (Hexagon hexagon : Board.getHexagonList()) {
            initializeBoardImages(hexagon, new ImageView(hexagon.getImage()), 0, 0, 195.0, 178.0);
            if( !hexagon.getBiome().equals("Desert") ) {
                String path = "src/main/resources/assets/hexagons/hexagonNumbers/no." + hexagon.getNumber() + ".png";
                ImageView hexagonNumber = new ImageView(new Image(new FileInputStream(path)));
                initializeBoardImages(hexagon, hexagonNumber, 48, 52, 80.0, 80.0);
            } else {
                ImageView pirate = new ImageView(hexagon.getHasPirate().getImage());
                initializeBoardImages(hexagon, pirate, 55, 23, 119.0, 93.0);
            }
        }
    }
    
    private void initializeBoardImages(Hexagon hexagon, ImageView boardItems, Integer x, Integer y, Double width, Double height) {
        boardItems.setLayoutX(hexagon.getCoords().getX()+x);
        boardItems.setLayoutY(hexagon.getCoords().getY()+y);
        boardItems.setFitHeight(width);
        boardItems.setFitWidth(height);
        boardItems.toFront();
        boardPane.getChildren().add(boardItems);
        
    }

    private void initializePlayers() {

        initializePlayerItemsMap();

        initializePlayerInfo();

        initializeMaterialList();
    }

    private void initializePlayerInfo() {
        for(int i = 0; i < players.size(); i++) {
            Label tempLabel = (Label) playersItems.get(players.get(i)).get(0);
            ImageView tempImageview = (ImageView) playersItems.get(players.get(i)).get(1);
            tempLabel.getParent().setVisible(true);
            tempLabel.setText(players.get(i).getName());
            tempImageview.setImage(players.get(i).getImage());
        }
    }
    private void initializeMaterialList() {
        for(Player player : players) {
            HashMap<String, Integer> materials = player.getMaterials();
            for (String material : materials.keySet()) {
                if(!material.equals("Sum")) {
                    ListView<Integer> tempList = (ListView<Integer>) playersItems.get(player).get(2);
                    tempList.getItems().add(materials.get(material));
                }
            }
        }
    }

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
