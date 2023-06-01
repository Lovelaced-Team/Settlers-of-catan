package com.board;

import com.game.Player;
import javafx.scene.image.Image;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;

abstract public class Structure {
	
	protected Image image;
	protected Coordinates coords;
	protected Player owner;
	
	public Structure (String image, Coordinates coords, Player player) {
		try {
			this.image = new Image(new FileInputStream(image));
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
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
