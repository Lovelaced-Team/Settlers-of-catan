package com.game;

import com.board.Board;
import com.board.Hexagon;
import com.board.Pirate;

public class SpecialCard extends Card{
	public SpecialCard(String name, String image, int points) {
		super(name, image, points);
	}

	public void usePirateCard(Player perpatrator, Player victim, Hexagon hexagon){
		Pirate pirate;
		for(Hexagon h : Board.getHexagonList()){
			if( h.getHasPirate() != null ){
				pirate = h.getHasPirate();
				pirate.moveHexagon(hexagon);
				pirate.stealForPlayer(victim, perpatrator);
				break;
			}
		}
		Quest.addArmyAmount(perpatrator, 1);
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
