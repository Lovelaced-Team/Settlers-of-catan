package com.game;

import javafx.scene.image.Image;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

abstract public class Card {
	protected String name;
	protected Image image;
	protected int points;
	public Card(String name, String image, int points) {
		this.name = name;
		this.points = points;
		try {
			this.image = new Image(new FileInputStream(image));
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	public String getName(){
		return this.name;
	}
	public int getPoints(){
		return this.points;
	}
}
