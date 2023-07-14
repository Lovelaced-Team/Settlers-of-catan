package com.game;

import com.board.Hexagon;
import com.board.Pirate;

import java.util.ArrayList;

public class SpecialCard extends Card {
	public SpecialCard(String name, int points) {
		super(name, points);
	}

	public void usePirateCard(Player perpetrator, Player victim, ArrayList<Hexagon> boardHexagons) {
		Pirate pirate;
		for(Hexagon h : boardHexagons) {
			if( h.getHasPirate() != null ) {
				pirate = h.getHasPirate();
				pirate.stealForPlayer(victim, perpetrator);
				break;
			}
		}
	}

	public void takeMaterialFromPlayers(Player player, String material) {
		for(Player p:Game.getPlayerList()) {
			if( p.getMaterialAmount(material) > 0 && !p.equals(player) ) {
				player.addMaterial(material, p.getMaterialAmount(material));
				p.subtractMaterial(material, p.getMaterialAmount(material));
			}
		}
		player.removeCard(this);
	}

	public void takeMaterialFromBank(Player player, String firstMaterial, String secondMaterial) {
		player.addMaterial(firstMaterial,  1);
		player.addMaterial(secondMaterial, 1);
		player.removeCard(this);
	}

	public void giveRoad(Player player) {
		player.addMaterial("Wood", 2);
		player.addMaterial("Clay", 2);
		player.removeCard(this);
	}

}
