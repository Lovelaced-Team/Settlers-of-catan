package com.game;

import com.board.Hexagon;
import com.board.Pirate;

import java.util.ArrayList;

public class SpecialCard extends Card{
	public SpecialCard(String name, int points) {
		super(name, points);
	}

	public void usePirateCard(Player perpetrator, Player victim, Hexagon hexagon, ArrayList<Hexagon> boardHexagons){
		Pirate pirate;
		for(Hexagon h : boardHexagons){
			if( h.getHasPirate() != null ){
				pirate = h.getHasPirate();
				pirate.moveHexagon(hexagon);
				pirate.stealForPlayer(victim, perpetrator);
				break;
			}
		}
		Quest.addArmyAmount(perpetrator, 1);
	}

	public void takeMaterialFromPlayers(Player player, String material){
		for(Player p:Game.getPlayerList()){
			if( p.getMaterialAmount(material) > 0){
				player.addMaterial(material, p.getMaterialAmount(material));
				p.subtractMaterial(material, p.getMaterialAmount(material));
			}
		}
	}

	public void takeMaterialFromBank(Player player, String firstMaterial, String secondMaterial){
		player.addMaterial(firstMaterial, 1);
		player.addMaterial(secondMaterial, 1);
	}

}
