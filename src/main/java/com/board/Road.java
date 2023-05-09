package com.board;

import com.game.Player;

public class Road extends Structure {
	
	public Road(String image, Coordinates coords, Player player) {
		super(image, coords, player);
	}
	
	public static boolean canBeBuilt(Player player) {
		return player.getMaterialAmount("Wood") >= 1 &&
				player.getMaterialAmount("Clay") >= 1;
	}

	public void build(Hexagon hexagon, int position){
		Structure[] structures = hexagon.getStructures();
		if( canBeBuilt(this.owner) && position % 2 != 0){
			if( structures[position] == null ){
				structures[position] = this;
				this.owner.subtractMaterial("Wood", 1);
				this.owner.subtractMaterial("Clay", 1);
			}
		}
	}
	public int getPoints() {
		return 0;
	}
}
