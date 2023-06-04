package com.game;

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
