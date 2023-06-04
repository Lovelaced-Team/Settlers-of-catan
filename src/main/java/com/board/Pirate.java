package com.board;

import com.game.Card;
import com.game.Player;
import javafx.scene.image.Image;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

//Class representing the pirate of the game.
//The pirate can be placed on a hexagon to stop it from producing material.
//A player can move the pirate and steal materials from other players.
public class Pirate {
	private Hexagon belongsTo;
	private Image image;

	public Pirate(Hexagon hexagon) {
		this.belongsTo = hexagon;

		try {
			this.image = new Image(new FileInputStream("src/main/resources/assets/hexagons/pirate/Pirate.png"));
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	public void moveHexagon(Hexagon hexagon) {
		belongsTo.setHasPirate(null);

		this.belongsTo = hexagon;

		hexagon.setHasPirate(this);
	}

	//If a player has more than 8 cards the pirate steals half of them.
	public static void stealFromPlayer(Player player, HashMap<String, Integer> suppliedMaterials){
		for(String s : suppliedMaterials.keySet()){
			player.subtractMaterial(s, suppliedMaterials.get(s));
		}
	}

	//Steals a random material or card from a victim and gives it to the perpetrator.
	public void stealForPlayer(Player victim, Player perpetrator){
		Random rand = new Random();
		HashMap<String, Integer> victimMaterials = victim.getMaterials(); //HashMap containing quantities for every possible material that the victim can have

		if(victim.getMaterialAmount("Sum") >= 1 ){
			ArrayList<String> materials = new ArrayList<>(); //Array that contains only the names of the materials that the victim possesses

			for(String material : victimMaterials.keySet()){
				if( victimMaterials.get(material) > 0 ){
					materials.add(material);
				}
			}

			int materialToBeRemovedRandomIndex = rand.nextInt(materials.size());
			String materialToBeRemoved = materials.get(materialToBeRemovedRandomIndex);

			victim.subtractMaterial(materialToBeRemoved, 1);
			perpetrator.addMaterial(materialToBeRemoved, 1);
		} else if(victim.getCardListSize() >= 1){
			ArrayList<Card> cards = new ArrayList<>(victim.getCardsList());
			int cardToBeRemovedRandomIndex = rand.nextInt(cards.size());
			Card cardToBeRemoved = cards.get(cardToBeRemovedRandomIndex);

			victim.removeCard(cardToBeRemoved);
			perpetrator.addCard(cardToBeRemoved);
		}
	}

	public Image getImage(){
		return this.image;
	}
}
