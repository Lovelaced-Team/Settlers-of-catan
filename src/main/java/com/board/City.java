package com.board;

import com.game.Player;

public class City extends Village {
	public City(String image, Coordinates coords, Player player) {
		super(image, coords, player);
		this.points = 2;
	}

	public static boolean canBeBuilt(Player player) {
		return player.getMaterialAmount("Rock") >= 3 &&
				player.getMaterialAmount("Wheat") >= 2;
	}
}
