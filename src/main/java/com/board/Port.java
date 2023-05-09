package com.board;

import com.game.Player;
import javafx.scene.image.Image;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Random;

//Class representing the harbors.
//Harbors are stored on hexagons.
//They can change a player's trading cost.
public class Port{
    private Image image;
    private Coordinates coords;
    private String material;
    private int edge;
    private int cost;
    public Port(Coordinates hexagonCoords, String material, int edge) {
        Coordinates coords = new Coordinates(hexagonCoords.getX(), hexagonCoords.getY());

        //Generates the coordinates for the port based on the value of the edge attribute.
        //This value is stored on the StartingMap.txt file.
        //Offsets will probably change
        switch(edge){
            case 1:
                coords.setX(coords.getX()-352);
                coords.setY(coords.getY()+508);
                break;
            case 2:
                coords.setX(coords.getX()+352);
                coords.setY(coords.getY()+508);
                break;
            case 3:
                coords.setX(coords.getX()+666);
                break;
            case 4:
                coords.setX(coords.getX()+352);
                coords.setY(coords.getY()-508);
                break;
            case 5:
                coords.setX(coords.getX()-352);
                coords.setY(coords.getY()-508);
                break;
            case 6:
                coords.setX(coords.getX()-666);
                break;
        }
        try {
            this.image = new Image(new FileInputStream("src/main/resources/assets/port/Ship.png"));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        this.coords = coords;
        this.material = material;
        this.edge = edge;
        Random rand = new Random();
        this.cost = rand.nextInt(2) + 2;
    }

    public int getEdge(){
        return this.edge;
    }

    public void changePlayerTradingValue(Player player){
        if( player.getTradingCost(this.material) < this.cost ) {
            player.changeTradingCost(this.material, this.cost);
        }
    }
}

