package com.game;

import javafx.scene.image.Image;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;


//Class representing the player. Each Player has a name, an avatar, a color and points.
public class Player {
	private String name;
	private Image avatar;
	private String color;
	private int points;

	private ArrayList<Card> cards = new ArrayList<>(); //ArrayList with all of the player's cards.
	private HashMap<String, Integer> structures = new HashMap<>(); //HashMap storing the amount of structures that the player has build.
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

	public void initializeMaterialAmounts(){
		this.materials.put("Rock", 0);
		this.materials.put("Wheat", 0);
		this.materials.put("Forest", 0);
		this.materials.put("Sheep", 0);
		this.materials.put("Clay", 0);
		this.materials.put("Sum", 0);
	}

	public void initializeMaterialTradingCost(){
		this.materialTradingCost.put("Rock", 4);
		this.materialTradingCost.put("Wheat", 4);
		this.materialTradingCost.put("Forest", 4);
		this.materialTradingCost.put("Sheep", 4);
		this.materialTradingCost.put("Clay", 4);
	}

	public void initializeStructures(){
		structures.put("Road", 0);
		structures.put("Village", 0);
		structures.put("City", 0);
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

	public void addMaterial(String material, int amount){
		this.materials.put(material, this.materials.get(material) + amount);
		this.materials.put("Sum", this.materials.get("Sum") + amount);
	}

	public void subtractMaterial(String material, int amount){
		this.materials.put(material, this.materials.get(material) - amount);
		this.materials.put("Sum", this.materials.get("Sum") - amount);
	}

	public void addStructure(String structure, int amount){
		this.materials.put(structure, this.materials.get(structure) + amount);
	}

	public void subtractStructure(String structure, int amount){
		this.materials.put(structure, this.materials.get(structure) - amount);
	}

	public int getPoints(){
		return this.points;
	}

	public void addPoints(int amount){
		this.points += amount;
	}

	public void subtractPoints(int amount){
		if( this.points > 0) this.points -= amount;
	}

	public void addCard(Card c){
		if( c.getPoints() > 0 ){
			this.addPoints(c.getPoints());
		}
		this.cards.add(c);
	}

	public void removeCard(Card c) {
		if( c.getPoints() > 0 ) {
			this.subtractPoints(c.getPoints());
		}
		this.cards.remove(c);
	}

	public boolean getSpecialCard(){
		if( Game.getCardList().size() > 0) {
			if( this.materials.get("Sheep") > 1 &&
				this.materials.get("Rock") > 1 &&
				this.materials.get("Wheat") > 1)
			{
				subtractMaterial("Sheep", 1);
				subtractMaterial("Rock", 1);
				subtractMaterial("Wheat", 1);

				ArrayList<Card> cards = new ArrayList<>(Game.getCardList());
				Random rand = new Random();
				int randomCardIndex = rand.nextInt(cards.size());

				Card card = cards.get(randomCardIndex);
				this.addCard(card);

				cards.remove(card);
				return true;
			}
		}
		return false;
	}

	//Method that takes specific materials from a player and gives gives him the one he choose
	public void tradeWithBank(String material, HashMap<String, Integer> suppliedMaterial){
		for(String s : suppliedMaterial.keySet()){
			this.subtractMaterial(s, suppliedMaterial.get(s));
		}

		this.addMaterial(material, 1);
	}

	//Method that takes materials from one player and adds them to another one
	public void tradeWithPlayers(Player player, HashMap<String, Integer> demandedMaterials, HashMap<String, Integer> suppliedMaterials){
		for(String s : demandedMaterials.keySet()){
			this.addMaterial(s, demandedMaterials.get(s));
			player.subtractMaterial(s, demandedMaterials.get(s));
		}

		for(String s : suppliedMaterials.keySet()){
			player.addMaterial(s, suppliedMaterials.get(s));
			this.subtractMaterial(s, suppliedMaterials.get(s));
		}
	}

	//Method that returns two random numbers and their sum
	public int[] rollDice(){
		Random rand = new Random();
		int	firstDice = rand.nextInt(6)+1;
		int secondDice = rand.nextInt(6)+1;

		return new int[]{firstDice, secondDice, firstDice + secondDice };
	}

	public void changeTradingCost(String material, int amount){
		if( this.materialTradingCost.get(material) - amount >= 0 ) {
			this.materialTradingCost.put(material, this.materialTradingCost.get(material) - amount);
		}
	}

	public int getTradingCost(String material){
		return this.materialTradingCost.get(material);
	}

	public boolean equals(Object object) {
		Player p = (Player)object;
		return this.name.equals(p.name) &&
				this.avatar.equals(p.avatar) &&
				this.color.equals(p.color) &&
				this.points == p.points;
	}
}
