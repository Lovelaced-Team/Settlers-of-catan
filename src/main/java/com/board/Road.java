package com.board;

import com.game.Player;
import javafx.scene.image.Image;

import java.util.HashMap;

public class Road extends Structure {
	
	public Road(String image, Coordinates coords, Player player) {
		super(image, coords, player);
	}
	
	public static boolean canBeBuilt(Player player) {
		return player.getMaterialAmount("Wood") >= 1 &&
				player.getMaterialAmount("Clay") >= 1;
	}

	public void build(HashMap<Integer, Hexagon> hexagonEdges) {
		boolean flag = false;
		for(int position:hexagonEdges.keySet()) {
			Structure[] structures = hexagonEdges.get(position).getStructures();
			if( canBeBuilt(this.owner) && position % 2 != 0 ) {
				structures[position] = this;
				flag = true;
			}
		}
		if( flag ) {
			this.owner.addStructure("Road", this);
			this.owner.subtractMaterial("Wood", 1);
			this.owner.subtractMaterial("Clay", 1);
		}
	}
	public int getPoints() {
		return 0;
	}

	public Image getImage() {
		return super.getImage();
	}
}
