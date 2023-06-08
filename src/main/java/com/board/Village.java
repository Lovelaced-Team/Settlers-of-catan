package com.board;

import com.game.Player;
import javafx.scene.image.Image;

import java.util.HashMap;

public class Village extends Structure {
	protected int points;

	public Village(String image, Coordinates coords, Player player) {
		super(image, coords, player);
		this.points = 1;
	}

	public int getPoints() {
		return points;
	}

	public static boolean canBeBuilt(Player player) {
		return  player.getMaterialAmount("Wood")  >= 1 &&
				player.getMaterialAmount("Clay")  >= 1 &&
				player.getMaterialAmount("Wool")  >= 1 &&
				player.getMaterialAmount("Wheat") >= 1;
	}

	public void build(HashMap<Integer, Hexagon> hexagonCorners) {

		boolean flag = false, tradingCost = false;
		Port port = null;

		for (int position : hexagonCorners.keySet()) {
			Structure[] structures = hexagonCorners.get(position).getStructures();

			if ( canBeBuilt(this.owner) && position % 2 == 0 ) {
				if ( structures[position] instanceof Road ) {
					port = hexagonCorners.get(position).getPort();
					tradingCost = true;
				}
				structures[position] = this;
				flag = true;
			}
		}

		if( flag ) {
			this.owner.addStructure("Village", this);
			this.owner.subtractMaterial("Wood",  1);
			this.owner.subtractMaterial("Clay",  1);
			this.owner.subtractMaterial("Wool",  1);
			this.owner.subtractMaterial("Wheat", 1);

			this.owner.addPoints(this.points);
		}

		if( tradingCost ) {
			port.changePlayerTradingValue(this.owner);
		}
	}

	public static City upgradeToCity(HashMap<Integer, Hexagon> hexagonCorners, Player owner, Coordinates coords) {

		boolean flag = false;
		int villagePoints = 0;
		City city = null;

		for(int corner : hexagonCorners.keySet()) {
			if( City.canBeBuilt(owner) )
			{
				Structure[] structures = hexagonCorners.get(corner).getStructures();
				city = new City("/assets/gameScreen/Build/City/city_"+owner.getColor()+".png", coords, owner);

				villagePoints = structures[corner].getPoints();

				owner.removeStructure("Village", structures[corner]);
				structures[corner] = city;
				owner.addStructure("City", city);
				flag = true;
			}
		}
		if( flag ){
			owner.subtractMaterial("Rock", 3);
			owner.subtractMaterial("Wheat", 2);
			owner.subtractPoints(villagePoints);
			owner.addPoints(city.points);
		}

		return city;
	}

	public boolean equals(Object object) {
		Village v = (Village)object;
		return  this.image.equals(v.image)   &&
				this.coords.equals(v.coords) &&
				this.points == v.points;
	}

	public Image getImage() {
		return super.getImage();
	}
}
