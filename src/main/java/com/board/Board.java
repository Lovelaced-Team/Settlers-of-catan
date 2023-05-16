package com.board;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

//Class representing the board of the game. Creates and stores a list of all the hexagons.
public class Board {
	private final double XLOCATION = 0;
	private final double YLOCATION = 0;
	private final double XOFFSET = 161;
	private final double YOFFSET = 140;
	private static ArrayList<Hexagon> hexagons = new ArrayList<>(); //List of hexagons in the board
	private HashMap<Integer, String> biomes = new HashMap<>();

	public Board() {
		initializeBiomes();
		try {
			//Reads from a file that has 3digit numbers
			//The first digit represents the edge you need to reach to get to the port
			//The second digit represents the type of port
			//The third digit represents the hexagon's biome
			FileReader fileReader = new FileReader("src/main/resources/map/StartingMap.txt");
			BufferedReader reader = new BufferedReader(fileReader);

			double currentX = XLOCATION+XOFFSET/2;
			double currentY = YLOCATION;
			int row = 1;

			Coordinates coords;

			String line = reader.readLine();

			while (line != null) {
				String hexagonLocations[] = line.split(" "); //Stores all the 3 digit numbers that a line contain seperately

				for(String s : hexagonLocations) {
					coords = new Coordinates(currentX, currentY);

					//Creates the port
					Port port = null;
					if( Character.getNumericValue(s.charAt(0)) != 0 && Character.getNumericValue(s.charAt(1)) != 0){
						port = new Port(coords, biomes.get(Character.getNumericValue(s.charAt(0))+1), Character.getNumericValue(s.charAt(1)));
					}

					//Creates the hexagons
					switch (Integer.parseInt(String.valueOf(s.charAt(2)))) {
						case 1 -> hexagons.add(new Hexagon(coords, biomes.get(1), port));
						case 2 -> hexagons.add(new Hexagon(coords, biomes.get(2), port));
						case 3 -> hexagons.add(new Hexagon(coords, biomes.get(3), port));
						case 4 -> hexagons.add(new Hexagon(coords, biomes.get(4), port));
						case 5 -> hexagons.add(new Hexagon(coords, biomes.get(5), port));
						case 6 -> hexagons.add(new Hexagon(coords, biomes.get(6), port));
					}
					currentX += XOFFSET;
				}
				row++;

				//Offsets the x and y cordinates for the next row of hexagons
				currentY += YOFFSET;
				currentX = XLOCATION;
				if( row % 2 != 0) {
					currentX += XOFFSET/2;
				}

				line = reader.readLine();
			}

			reader.close();
			fileReader.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
		initializeHexagonsNumbers();
	}

	public void addHexagon(Hexagon e) {
		Board.hexagons.add(e);
	}

	public static ArrayList<Hexagon> getHexagonList() {
		return Board.hexagons;
	}

	//Sets a random number for every hexagon.
	//Can only assign the number 2 and 12 once.
	//Can't assign the number 7.
	//Desert hexagons get the number 0.
	public void initializeHexagonsNumbers() {
		int[] toPickDuplicates = {3, 4, 5, 6, 8, 9, 10, 11}; //numbers from 3 to 11 without 7
		ArrayList<Integer> numbers = new ArrayList<>(); //final number list for hexagons

		numbers.add(2);
		numbers.add(12);

		for(int i=0; i<hexagons.size()-3; i++){
			numbers.add(toPickDuplicates[i % 8]);
		}

		Random rand = new Random();
		int randomNumber;
		for(Hexagon hex : hexagons){
			if( !hex.getBiome().equals("Desert") ){
				randomNumber = rand.nextInt(numbers.size());
				hex.setNumber(numbers.get(randomNumber));
				numbers.remove(randomNumber);
			} else {
				hex.setNumber(7);
			}
		}
	}

	//Returns a HashMap with a number corresponding to a biome.
	public void initializeBiomes(){
		this.biomes.put(1, "Desert");
		this.biomes.put(2, "Rock");
		this.biomes.put(3, "Wheat");
		this.biomes.put(4, "Forest");
		this.biomes.put(5, "Sheep");
		this.biomes.put(6, "Clay");
		this.biomes.put(7, "All");
	}
}
