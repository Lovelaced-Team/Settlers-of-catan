package com.board;

import java.util.Objects;

//Class used to track the Coordinates of structures, ports and hexagons
public class Coordinates {
	private double x;
	private double y;

	public Coordinates(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public double getX() {
		return this.x;
	}

	public double getY() {
		return this.y;
	}

	public void setX(double x) {
		this.x = x;
	}

	public void setY(double y) {
		this.y = y;
	}

	public String toString() {
		return "(" + this.x + ", " + this.y + ")";
	}

	public boolean equals(Object object) {
		Coordinates c = (Coordinates) object;
		return this.x == c.x && this.y == c.y;
	}

	public int hashCode() {
		return Objects.hash(x, y);
	}


}
