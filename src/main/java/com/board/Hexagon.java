package com.board;

import com.game.Player;
import com.screens.StartScreen;
import javafx.scene.image.Image;

import java.io.*;
import java.util.HashMap;
import java.util.Objects;

//Class representing the hexagons of the game.
//Structures and a pirate can be placed on hexagons.
//Hexagons can produce materials.
public class Hexagon implements Serializable{

	private String biome;
	private Image image;
	private int number;
	private Coordinates coords;
	private Pirate hasPirate;
	private Port port;
	private Structure[] structures = new Structure[12];
	private static HashMap<String, String> biomeImages = new HashMap<>(initializeBiomeImages()); //All the biome image sources.

	public Hexagon(String biome, String image, int number, Coordinates coords, Pirate hasPirate, Port port) {
		this.biome = biome;
		this.image = new Image(image);
		this.number = number;
		this.coords = coords;
		this.hasPirate = hasPirate;
		this.port = port;

		if( port != null ) {
			int leftCorner=port.getEdge()-1, rightCorner=port.getEdge()+1;
			if( leftCorner == -1 ) leftCorner = 10;
			else if( rightCorner == 12 ) rightCorner = 0;

			addStructure(leftCorner,  new Road("assets/gameScreen/Build/Road/road_blank.png", null, null));
			addStructure(rightCorner, new Road("assets/gameScreen/Build/Road/road_blank.png", null, null));
		}

	}

	public Hexagon(Coordinates coords, String biome, Port port) {
		this.biome = biome;
		this.image = new Image(Objects.requireNonNull(StartScreen.class.getResourceAsStream(biomeToImage(biome))));
		this.coords = coords;
		this.port = port;

		if( port != null ) {
			int leftCorner=port.getEdge()-1, rightCorner=port.getEdge()+1;
			if( leftCorner == -1 ) leftCorner = 10;
			else if( rightCorner == 12 ) rightCorner = 0;

			addStructure(leftCorner,  new Road("/assets/gameScreen/Build/Road/road_blank.png", null, null));
			addStructure(rightCorner, new Road("/assets/gameScreen/Build/Road/road_blank.png", null, null));
		}

		if( biome.equals("Desert") ) {
			this.hasPirate = new Pirate(this);
		}
	}

	public String toString() {
		return this.biome + ", " + this.image + ", " + this.number + ", " + this.coords + ", " + this.hasPirate + ", " + this.port;
	}

	public void addStructure(int position, Structure struct) {
		this.structures[position] = struct;
	}

	public Coordinates getCoords() {
		return this.coords;
	}

	public Pirate getHasPirate() {
		return hasPirate;
	}

	public String getBiome() {
		return biome;
	}

	public Image getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = new Image(image);
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public void setHasPirate(Pirate pirate) {
		this.hasPirate = pirate;
	}

	public Structure[] getStructures() {
		return structures;
	}

	public Structure getStructure(int index) {
		return structures[index];
	}

	public Port getPort(){
		return this.port;
	}

	//Returns a hashMap that contains an image source for every biome
	public static HashMap<String, String> initializeBiomeImages() {
		HashMap<String, String> biomeImageSources = new HashMap<>();

		try {
			InputStreamReader fileIn = new InputStreamReader(Objects.requireNonNull(StartScreen.class.getResourceAsStream("/map/biomeImages.txt")));
			BufferedReader in = new BufferedReader(fileIn);

			String line;

			while( (line = in.readLine()) != null) {
				String[] parts = line.split(",");
				biomeImageSources.put(parts[0],parts[1]);
			}

			in.close();
			fileIn.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return biomeImageSources;
	}

	//Given a biome it returns the corresponding Image.
	public static String biomeToImage(String biome) {
		return biomeImages.get(biome);
	}

	//Produces materials and adds them to a player.
	public void produceMaterials(Player player) {
		if( getHasPirate() == null ) {
			for(int i = 0; i<12; i += 2) {
				if( structures[i] instanceof Village && structures[i].getOwner().equals(player) ) {
					player.addMaterial(this.biome, structures[i].getPoints());
					break;
				}
			}
		}
	}
}
