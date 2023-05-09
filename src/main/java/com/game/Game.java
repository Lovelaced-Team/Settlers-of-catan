package com.game;

import com.board.Board;
import com.board.Hexagon;
import javafx.scene.paint.Color;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
public class Game {
	private static ArrayList<Player> players = new ArrayList<>();
	private static ArrayList<Card> cards = new ArrayList<>();
	private static int currentPlayer;
	private static Board board;
	private static Quest quest;

	public Game(){
		board = new Board();
		quest = new Quest(players);
		Game.currentPlayer = 0;
        Game.initializeCards();
	}
	public static void main(String[] args) {
		Game game = new Game();
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
	public static void initializeCards(){
        for(int i=0; i<29; i++){
			Game.cards.add(new SpecialCard("PirateCard",
					"",
					0));
		}
        for(int i=0; i<5; i++) {
			Game.cards.add(new SpecialCard("PlusOnePointCard",
                    "",
                    1));

            if (i < 2) {
				Game.cards.add(new SpecialCard("RoadCard",
                        "",
                        0));

				Game.cards.add(new SpecialCard("MonopolyCard",
                        "",
                        0));

				Game.cards.add(new SpecialCard("YearOfPlentyCard",
                        "",
                        0));
            }
        }
		Collections.shuffle(Game.cards);
	}

	//Updates the materials for all the players that have build a structure on the hexagon that got rolled.
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
}
