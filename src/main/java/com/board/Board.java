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
	private static HashMap<Coordinates, HashMap<Integer, Hexagon>> hexagonCorners = new HashMap<>();

	private static HashMap<Coordinates, HashMap<Integer, Hexagon>> hexagonEdges = new HashMap<>();
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
					if (Character.getNumericValue(s.charAt(0)) != 0 && Character.getNumericValue(s.charAt(1)) != 0) {
						port = new Port(coords, biomes.get(Character.getNumericValue(s.charAt(1)) + 1), (Character.getNumericValue(s.charAt(0)-1)*2+1));
					}


					//Creates the hexagons
					String biome = biomes.get(Integer.parseInt(String.valueOf(s.charAt(2))));
					if ( biome != null ){
						Hexagon hexagon = new Hexagon(coords, biome, port);
						hexagons.add(hexagon);
						addHexagonCorners(hexagon);
						addHexagonEdges(hexagon);
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

	public static HashMap <Coordinates, HashMap<Integer, Hexagon>> getHexagonCorners() {
		return Board.hexagonCorners;
	}

	public static HashMap<Coordinates, HashMap<Integer, Hexagon>> getHexagonEdges() {
		return Board.hexagonEdges;
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

	/*
	    This method utilizes a hashmap to store hexagonal data in a structured manner.
	    The hashmap's keys are pairs of coordinates that correspond to the corners of each hexagon.
	    The values associated with these keys are inner hashmaps, where the key represents the number of the corner,
	    and the value represents the hexagon itself.
	*/
	public void addHexagonCorners(Hexagon hexagon){
		HashMap<Coordinates, Integer> coordsToCorners = new HashMap<>();

		coordsToCorners.put(new Coordinates(Math.ceil(hexagon.getCoords().getX())-7, hexagon.getCoords().getY()+36), 0);
		coordsToCorners.put(new Coordinates(Math.floor(hexagon.getCoords().getX())+74, hexagon.getCoords().getY()-7), 2);
		coordsToCorners.put(new Coordinates(Math.ceil(hexagon.getCoords().getX())+154, hexagon.getCoords().getY()+36), 4);
		coordsToCorners.put(new Coordinates(Math.ceil(hexagon.getCoords().getX())+154, hexagon.getCoords().getY()+133), 6);
		coordsToCorners.put(new Coordinates(Math.floor(hexagon.getCoords().getX())+74, hexagon.getCoords().getY()+176), 8);
		coordsToCorners.put(new Coordinates(Math.ceil(hexagon.getCoords().getX())-7, hexagon.getCoords().getY()+133), 10);

		for(Coordinates coords : coordsToCorners.keySet()) {
			if( !Board.hexagonCorners.containsKey(coords) ){
				Board.hexagonCorners.put(coords, new HashMap<Integer, Hexagon>());
			}
			if ( !Board.hexagonCorners.get(coords).containsKey(coordsToCorners.get(coords)) ){
				Board.hexagonCorners.get(coords).put(coordsToCorners.get(coords), hexagon);
			}
		}
	}

	/*
    	This method utilizes a hashmap to store hexagonal data in a structured manner.
    	The hashmap's keys are pairs of coordinates that correspond to the edges of each hexagon.
    	The values associated with these keys are inner hashmaps, where the key represents the number of the edge,
    	and the value represents the hexagon itself.
	*/
	public void addHexagonEdges(Hexagon hexagon){
		HashMap<Coordinates, Integer> coordsToEdges = new HashMap<>();

		coordsToEdges.put(new Coordinates(Math.ceil(hexagon.getCoords().getX())+32, hexagon.getCoords().getY()+13), 1);
		coordsToEdges.put(new Coordinates(Math.floor(hexagon.getCoords().getX())+113, hexagon.getCoords().getY()+13), 3);
		coordsToEdges.put(new Coordinates(Math.ceil(hexagon.getCoords().getX())+153, hexagon.getCoords().getY()+82), 5);
		coordsToEdges.put(new Coordinates(Math.floor(hexagon.getCoords().getX())+113, hexagon.getCoords().getY()+153), 7);
		coordsToEdges.put(new Coordinates(Math.ceil(hexagon.getCoords().getX())+32, hexagon.getCoords().getY()+153), 9);
		coordsToEdges.put(new Coordinates(Math.ceil(hexagon.getCoords().getX())-8, hexagon.getCoords().getY()+82), 11);

		for(Coordinates coords : coordsToEdges.keySet()) {
			if( !Board.hexagonEdges.containsKey(coords) ){
				Board.hexagonEdges.put(coords, new HashMap<Integer, Hexagon>());
			}
			if ( !Board.hexagonEdges.get(coords).containsKey(coordsToEdges.get(coords)) ){
				Board.hexagonEdges.get(coords).put(coordsToEdges.get(coords), hexagon);
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
