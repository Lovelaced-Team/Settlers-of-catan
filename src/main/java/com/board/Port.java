package com.board;

import com.game.Player;
import com.screens.StartScreen;
import javafx.scene.image.Image;
import java.util.Objects;

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
        switch (edge) {
            case 1 -> {
                coords.setX(coords.getX() - 29);
                coords.setY(coords.getY() - 88);
            }
            case 3 -> {
                coords.setX(coords.getX() + 98);
                coords.setY(coords.getY() - 78);
            }
            case 5 -> {
                coords.setX(coords.getX() + 155);
                coords.setY(coords.getY() + 36);
            }
            case 7 -> {
                coords.setX(coords.getX() + 81);
                coords.setY(coords.getY() + 140);
            }
            case 9 -> {
                coords.setX(coords.getX() - 48);
                coords.setY(coords.getY() + 131);
            }
            case 11 -> {
                coords.setX(coords.getX() - 103);
                coords.setY(coords.getY() + 13);
            }
        }
        this.coords = coords;

        if( material.equals("Sheep") ) material = "Wool";
        else if( material.equals("Forest") ) material = "Wood";
        this.material = material;

        this.image = new Image(Objects.requireNonNull(StartScreen.class.getResourceAsStream("/assets/port/Ship_" + this.material + ".png")));

        this.edge = edge;

        this.cost = 3;
    }

    public int getEdge(){
        return this.edge;
    }

    public Image getImage(){
        return this.image;
    }

    public Coordinates getCoords(){
        return this.coords;
    }

    public void changePlayerTradingValue(Player player) {
        String material = this.material;

        if( material.equals("All") ) {

            for (String tempMaterial : player.getTradingCostList().keySet()) {
                if( player.getTradingCost(tempMaterial) > this.cost ) {
                    player.changeTradingCost(tempMaterial, this.cost);
                }
            }
        } else if( player.getTradingCost(this.material) > this.cost ) {
            player.changeTradingCost(this.material, this.cost);
        }
    }
}


