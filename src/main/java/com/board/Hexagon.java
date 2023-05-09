package com.board;

import com.game.Player;
import javafx.scene.image.Image;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

//Class representing the hexagons of the game.
//Structures and a pirate can be placed on hexagons.
//Hexagons can produce materials.
public class Hexagon implements Serializable{

	private String biome;
	private Image image;
	private int number;
	private boolean canProduce;
	private Coordinates coords;
	private Pirate hasPirate;
	private Port port;
	private Structure[] structures = new Structure[12];
	private Coordinates[] structureCoords;
	private ArrayList<Coordinates> coordinatesList = new ArrayList<>();
	private static HashMap<String, String> biomeImages = new HashMap<String, String>(initializeBiomeImages()); //All the biome image sources.

	public Hexagon(String biome, String image, int number, Coordinates coords, Pirate hasPirate, boolean canProduce, Port port) {
		this.biome = biome;
		this.image = new Image(image);
		this.number = number;
		this.coords = coords;
		this.hasPirate = hasPirate;
		this.canProduce = canProduce;
		this.port = port;
		//
		//if( port != null ){
			//addStructure(port.getEdge()+1, new Road("", null, null));
			//addStructure(port.getEdge()-1, new Road("", null, null));
		//}

	}

	public Hexagon(Coordinates coords, String biome, Port port) {
		this.biome = biome;
		try {
			this.image = new Image(new FileInputStream(biomeToImage(biome)));
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
		this.coords = coords;
		this.port = port;
		//if( port != null ){
			//addStructure(port.getEdge()+1, new Road("", null, null));
			//addStructure(port.getEdge()-1, new Road("", null, null));
		//}

		this.canProduce = true;
		if( biome.equals("Desert") ){
			this.hasPirate = new Pirate(this);
			this.canProduce = false;
		}
	}

	public String toString() {
		return this.biome + ", " + this.image + ", " + this.number + ", " + this.coords + ", " + this.hasPirate + ", " + this.canProduce + ", " + this.port;
	}

	public void addStructure(int position, Structure struct) {
		this.structures[position] = struct;
	}

	public Coordinates getCoords() {
		return this.coords;
	}

	public void setCoords(Coordinates coords) {
		this.coords = coords;
	}

	public Pirate getHasPirate() {
		return hasPirate;
	}

	public String getBiome() {
		return biome;
	}

	public void setBiome(String biome) {
		this.biome = biome;
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

	public boolean getCanProduce() {
		return canProduce;
	}

	public void setCanProduce(boolean canProduce) {
		this.canProduce = canProduce;
	}

	public Structure[] getStructures() {
		return structures;
	}

	public Port getPort(){
		return this.port;
	}

	public void setPort(Port port){
		this.port = port;
	}
	//Returns an ArrayList with the players that have placed structures in this hexagon
	public ArrayList<Player> hexagonToPlayer(){
		ArrayList<Player> players = new ArrayList<>();

		for(Structure s : structures){
			if( s instanceof Village ){
				players.add(s.getOwner());
			}
		}

		return players;
	}

	//Returns a hashMap that contains an image source for every biome
	public static HashMap<String, String> initializeBiomeImages(){
		HashMap<String, String> biomeImageSources = new HashMap<String, String>();

		try {
			FileReader fileIn = new FileReader("src/main/resources/map/biomeImages.txt");
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
	public void produceMaterials(Player player){
		if( this.canProduce ){
			for(int i = 0; i<12; i += 2){
				if( structures[i].getOwner().equals(player) && structures[i] instanceof Village ){
					player.addMaterial(this.biome, structures[i].getPoints());
					break;
				}
			}
		}
	}
}
