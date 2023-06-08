package com.board;

import com.game.Player;
import com.screens.StartScreen;
import javafx.scene.image.Image;
import java.util.HashMap;
import java.util.Objects;

abstract public class Structure {
	
	protected Image image;
	protected Coordinates coords;
	protected Player owner;
	
	public Structure (String image, Coordinates coords, Player player) {
		this.image = new Image(Objects.requireNonNull(StartScreen.class.getResourceAsStream(image)));
		this.coords = coords;
		this.owner = player;
	}
	
    public abstract int getPoints() ;

	public abstract void build(HashMap<Integer, Hexagon> hexagonsAndPositions);

	public Coordinates getCoords() {
		return coords;
	}
	
	public Image getImage() {
		return image;
	}

	public Player getOwner(){
		return this.owner;
	}
}
