package com.game;

import com.board.Structure;
import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.HashMap;

//CalculateContinuousRoads method wasn't implemented. Required for the longest Road quest.

//Class representing the player. Each Player has a name, an avatar, a color and points.
public class Player implements Comparable {
	private String name;
	private Image avatar;
	private String color;
	private int points;

	private ArrayList<Card> cards = new ArrayList<>(); //ArrayList with all the player's cards.
	private HashMap<String, ArrayList<Structure>> structures = new HashMap<>(); //HashMap storing the amount of structures that the player has built.
	private HashMap<String, Integer> materials = new HashMap<>(); //HashMap storing the amount of every material that the player owns.
	private HashMap<String, Integer> materialTradingCost = new HashMap<>(); //HashMap storing the trading cost for every material.
	
	public Player(String name, Image avatar, String color, int points) {
		this.name = name;
		this.avatar = avatar;
		this.color = color;
		this.points = points;
		this.initializeMaterialAmounts();
		this.initializeMaterialTradingCost();
		this.initializeStructures();
	}

	public String getName () {
		return name;
	}

	public Image getImage () {
		return avatar;
	}

	public void initializeMaterialAmounts() {
		this.materials.put("Rock",  0);
		this.materials.put("Wheat", 2);
		this.materials.put("Wood",  4);
		this.materials.put("Wool",  2);
		this.materials.put("Clay",  4);
		this.materials.put("Sum",   12);
	}

	public void initializeMaterialTradingCost() {
		this.materialTradingCost.put("Rock",  4);
		this.materialTradingCost.put("Wheat", 4);
		this.materialTradingCost.put("Wood",  4);
		this.materialTradingCost.put("Wool",  4);
		this.materialTradingCost.put("Clay",  4);
	}

	public void initializeStructures() {
		structures.put("Road", new ArrayList<>());
		structures.put("Village", new ArrayList<>());
		structures.put("City", new ArrayList<>());
	}

	public HashMap<String, Integer> getMaterials(){
		return this.materials;
	}

	public ArrayList<Card> getCardsList(){
		return this.cards;
	}
	public int getCardListSize(){
		return this.cards.size();
	}

	public int getMaterialAmount(String material){
		return this.materials.get(material);
	}

	public void addMaterial(String material, int amount) {
		if(material.equals("Forest")) material = "Wood";
		else if(material.equals("Sheep")) material = "Wool";

		this.materials.put(material, this.materials.get(material) + amount);
		this.materials.put("Sum", this.materials.get("Sum") + amount);
	}

	public void subtractMaterial(String material, int amount) {
		if(material.equals("Forest")) material = "Wood";
		else if(material.equals("Sheep")) material = "Wool";

		this.materials.put(material, this.materials.get(material) - amount);
		this.materials.put("Sum", this.materials.get("Sum") - amount);
	}

	public HashMap<String, ArrayList<Structure>> getStructureMap(){
		return this.structures;
	}

	public int getStructureAmount(String structure) {
		return this.structures.get(structure).size();
	}

	public void addStructure(String structure, Structure addedStructure) {
		this.structures.get(structure).add(addedStructure);
	}

	public void removeStructure(String structure, Structure addedStructure) {
		this.structures.get(structure).remove(addedStructure);
	}

	public String getColor() { return this.color; }

	public int getPoints(){
		return this.points;
	}

	public void addPoints(int amount){
		this.points += amount;
	}

	public void subtractPoints(int amount){
		if( this.points > 0) this.points -= amount;
	}

	public void addCard(Card c) {
		if( c.getPoints() > 0 ) this.addPoints(c.getPoints());
		this.cards.add(c);
	}

	public void removeCard(Card c) {
		if( c.getPoints() > 0 ) this.subtractPoints(c.getPoints());
		this.cards.remove(c);
	}

	public void getSpecialCard() {
		if( Game.getCardList().size() > 0) {
			if( this.materials.get("Wool")  > 1 &&
				this.materials.get("Rock")  > 1 &&
				this.materials.get("Wheat") > 1)
			{
				subtractMaterial("Wool",  1);
				subtractMaterial("Rock",  1);
				subtractMaterial("Wheat", 1);

				ArrayList<Card> cards = new ArrayList<>(Game.getCardList());
				int randomCardIndex =  (int )(Math.random() * cards.size());

				Card card = cards.get(randomCardIndex);
				this.addCard(card);

				cards.remove(card);
			}
		}
	}

	public Card getSelectedCard(String cardToFind) {
		for(Card card : this.cards) {
			if( card.getName().equals(cardToFind) )
				return card;
		}
		return null;
	}

	//Method that takes specific materials from a player and gives him the one he chose
	public void tradeWithBank(String material, String suppliedMaterial, Integer amount) {
		if(this.getMaterialAmount(suppliedMaterial) >= amount) {
			this.subtractMaterial(suppliedMaterial, amount);
			this.addMaterial(material, 1);
		}
	}

	//Method that takes materials from one player and adds them to another one
	public void tradeWithPlayers(Player player, HashMap<String, Integer> demandedMaterials, HashMap<String, Integer> suppliedMaterials) {
		boolean canTrade = true;
		for(String s : demandedMaterials.keySet()){
			if( player.getMaterialAmount(s) < demandedMaterials.get(s) ) canTrade = false;
		}

		for(String s : suppliedMaterials.keySet()){
			if( this.getMaterialAmount(s) < suppliedMaterials.get(s) ) canTrade = false;
		}

		if( canTrade ) {
			for(String s : demandedMaterials.keySet()) {
				this.addMaterial(s, demandedMaterials.get(s));
				player.subtractMaterial(s, demandedMaterials.get(s));
			}

			for(String s : suppliedMaterials.keySet()) {
				player.addMaterial(s, suppliedMaterials.get(s));
				this.subtractMaterial(s, suppliedMaterials.get(s));
			}
		}
	}

	//Method that returns two random numbers and their sum
	public int[] rollDice() {
		int	firstDice  = (int )((Math.random() * 6) + 1);
		int secondDice = (int )((Math.random() * 6) + 1);

		return new int[]{firstDice, secondDice, firstDice + secondDice };
	}

	public void changeTradingCost(String material, int amount) {
		if( this.materialTradingCost.get(material) - amount >= 0 ) {
			this.materialTradingCost.put(material, amount);
		}
	}

	public int getTradingCost(String material) {
		return this.materialTradingCost.get(material);
	}
	public HashMap<String, Integer> getTradingCostList() {
		return materialTradingCost;
	}

	public boolean equals(Object object) {
		Player p = (Player)object;
		return this.name.equals(p.name) &&
				this.avatar.equals(p.avatar) &&
				this.color.equals(p.color) &&
				this.points == p.points;
	}

	@Override
	public int compareTo( Object player) {
		return Integer.compare(((Player) player).getPoints(), this.points);
	}
}
