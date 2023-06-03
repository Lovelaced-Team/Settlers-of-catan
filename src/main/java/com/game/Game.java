package com.game;

import com.board.Board;
import com.board.Hexagon;
import java.util.ArrayList;
import java.util.Collections;
public class Game {
	private static ArrayList<Player> players = new ArrayList<>();
	private static ArrayList<Card> cards = new ArrayList<>();
	private static int currentPlayer;
	private static Board board;
	private static Quest quest;

	public Game() {
		board = new Board();
		quest = new Quest(players);
		Game.currentPlayer = 0;
        Game.initializeCards();
	}
	public static Player getCurrentPlayer(){
		return Game.players.get(currentPlayer);
	}

	public static void endTurn(){
		Game.currentPlayer++;
		if( Game.currentPlayer == Game.players.size() ){
			Game.currentPlayer = 0;
		}
	}

	//Checks if the current player has reached 10 points
	public static boolean hasWon(){
		return Game.getCurrentPlayer().getPoints() == 10;
	}

	//Creates the special cards, adds them to a cards list and shuffles it.
	//Need to add image paths
	public static void initializeCards() {
        for(int i=0; i<29; i++)
			Game.cards.add(new SpecialCard("Pirate",0));
		
        for(int i=0; i<5; i++) {
			Game.cards.add(new SpecialCard("PlusOne",1));

            if (i < 2) {
				Game.cards.add(new SpecialCard("RoadCard", 0));

				Game.cards.add(new SpecialCard("Monopoly", 0));

				Game.cards.add(new SpecialCard("YearOfPlenty", 0));
            }
        }
		Collections.shuffle(Game.cards);
	}

	//Updates the materials for all the players that have built a structure on the hexagon that got rolled.
	public void updateMaterials(int rolledNumber){
		for(Hexagon h : Board.getHexagonList()){
			if( h.getNumber() == rolledNumber ){
				for(Player p : Game.players){
					h.produceMaterials(p);
				}
			}
		}
	}

	public static ArrayList<Card> getCardList(){
		return Game.cards;
	}

	public static ArrayList<Player> getPlayerList(){
		return Game.players;
	}

	public static void addPlayer(Player player){
		players.add(player);
	}
}
