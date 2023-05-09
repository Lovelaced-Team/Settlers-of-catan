package com.board;

import com.game.Player;

public class Village extends Structure{
	protected int points;
	
	public Village(String image, Coordinates coords, Player player) {
		super(image, coords, player);
		this.points = 1;
	}
	
	public int getPoints() {
		return points;
	}

	public static boolean canBeBuilt(Player player) {
		return player.getMaterialAmount("Wood") >= 1 &&
				player.getMaterialAmount("Clay") >= 1 &&
				player.getMaterialAmount("Sheep") >= 1 &&
				player.getMaterialAmount("Wheat") >= 1;
	}

	public void build(Hexagon hexagon, int position){
		Structure[] structures = hexagon.getStructures();
		if( canBeBuilt(this.owner) && position % 2 == 0){
			if( structures[position] instanceof Road ){
				hexagon.getPort().changePlayerTradingValue(this.owner);
			}
			if( structures[position] == null || structures[position] instanceof Road ){
				structures[position] = this;
				this.owner.subtractMaterial("Wood", 1);
				this.owner.subtractMaterial("Clay", 1);
				this.owner.subtractMaterial("Sheep", 1);
				this.owner.subtractMaterial("Wheat", 1);

				this.owner.addPoints(this.points);
			}
		}
	}

	public void upgradeToCity(Player player, Hexagon hexagon) {
		if( City.canBeBuilt(player) && this.owner.equals(player) )
		{
			player.subtractMaterial("Rock", 3);
			player.subtractMaterial("Wheat", 2);

			City city;
			Structure[] structures = hexagon.getStructures();

			for(int i=0; i<12; i+=2){
				if( structures[i].equals(this) ) {
					city = new City("", this.coords, this.owner);
					structures[i] = city;

					this.owner.subtractPoints(this.points);
					this.owner.addPoints(city.getPoints());

					break;
				}
			}
		}
	}

	public boolean equals(Object object) {
		Village v = (Village)object;
		return this.image.equals(v.image) &&
				this.coords.equals(v.coords) &&
				this.points == v.points;
	}
}
