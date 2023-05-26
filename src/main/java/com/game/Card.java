package com.game;

import javafx.scene.image.Image;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

abstract public class Card {
	protected String name;
	protected int points;
	public Card(String name, int points) {
		this.name = name;
		this.points = points;
	}

	public String getName(){
		return this.name;
	}
	public int getPoints(){
		return this.points;
	}
}
