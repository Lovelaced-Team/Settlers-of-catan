package com.lovelaced;

import com.board.Board;
import com.board.Hexagon;
import com.game.Game;
import com.game.Music;
import com.game.Player;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;


public class GameScreenController {

    @FXML
    private AnchorPane boardPane;

    @FXML
    private Label nameLabel1, nameLabel2, nameLabel3, nameLabel4;

    @FXML
    private ImageView playerBoard1, playerBoard2, playerBoard3, playerBoard4;
    private ArrayList<Player> players = new ArrayList<>();
    public void startBoard(){
        new Game();
        players = Game.getPlayerList();

        initializePlayers(); //Adds player info to the scene
        initializeHexagons(); //Adds hexagons to the scene
    }

    private void initializeHexagons() {
        for(Hexagon hex : Board.getHexagonList()){
            ImageView hexagon = new ImageView(hex.getImage());
            hexagon.setLayoutX(hex.getCoords().getX());
            hexagon.setLayoutY(hex.getCoords().getY());
            hexagon.setFitHeight(195);
            hexagon.setFitWidth(178);
            hexagon.toFront();
            boardPane.getChildren().add(hexagon);

            try {
                if( !hex.getBiome().equals("Desert") ) {
                    ImageView hexagonNumber = new ImageView(new Image(new FileInputStream("src/main/resources/assets/hexagons/hexagonNumbers/no."+hex.getNumber()+".png")));
                    hexagonNumber.setLayoutX(hex.getCoords().getX()+48);
                    hexagonNumber.setLayoutY(hex.getCoords().getY()+52);
                    hexagonNumber.setFitHeight(80);
                    hexagonNumber.setFitWidth(80);
                    hexagonNumber.toFront();
                    boardPane.getChildren().add(hexagonNumber);
                } else {
                    ImageView pirate = new ImageView(hex.getHasPirate().getImage());
                    pirate.setLayoutX(hex.getCoords().getX()+55);
                    pirate.setLayoutY(hex.getCoords().getY()+23);
                    pirate.setFitHeight(119);
                    pirate.setFitWidth(93);
                    pirate.toFront();
                    boardPane.getChildren().add(pirate);
                }
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void initializePlayers() {

        Music.changeSong(Music.gameScreenSong);

        nameLabel1.setText(players.get(0).getName());
        playerBoard1.setImage(players.get(0).getImage());

        nameLabel2.setText(players.get(1).getName());
        playerBoard2.setImage(players.get(1).getImage());

        if( players.size() > 2 ) {
            nameLabel3.getParent().setVisible(true);
            nameLabel3.setText(players.get(2).getName());
            playerBoard3.setImage(players.get(2).getImage());
        }

        if( players.size() > 3 ) {
            nameLabel4.getParent().setVisible(true);
            nameLabel4.setText(players.get(3).getName());
            playerBoard4.setImage(players.get(3).getImage());
        }
    }

    @FXML
    void initialize() {

        assert boardPane != null : "fx:id=\"boardPane\" was not injected: check your FXML file 'GameScreen-view.fxml'.";

    }
}
