package com.lovelaced;

import com.board.*;
import com.game.*;

import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;


public class GameScreenController {
    private Parent root;
    private Stage stage;
    // FXML elements
    @FXML
    private AnchorPane boardPane, menuScreen, buildPane, buildSelectionButtonsPane, tradePane, tradePlayer;

    private ArrayList<ChoiceBox<Integer>> trades = new ArrayList<>();
    private ArrayList<ChoiceBox<Integer>> tradings = new ArrayList<>();

    @FXML
    private ChoiceBox<Integer> tradeClay, tradeRock, tradeWheat, tradeWool, tradeWood, tradingClay, tradingRock, tradingWheat, tradingWool, tradingWood ;

    @FXML
    private TextField bankTradeMaterial, tradePlayerName;

    @FXML
    private ListView<String> cards1, cards2, cards3, cards4;

    @FXML
    private ListView<Integer> stats1, stats2, stats3, stats4;

    @FXML
    private Label nameLabel1, nameLabel2, nameLabel3, nameLabel4;

    @FXML
    private ImageView playerBoard1, playerBoard2, playerBoard3, playerBoard4, playerTrading, settingsButton, settingsButton2, exitButton;


    @FXML
    private ListView<Integer> materials1, materials2, materials3, materials4;

    @FXML
    private ImageView sound, buildButton, tradeButton, buildRoadButton, buildVillageButton, upgradeToCityButton, pirate, tradingButton;

    @FXML
    private Slider volumeSlider;

    boolean upgradeButtonMode;

    private Group roadGroup = new Group();
    private Group buildingsGroup = new Group();
    private Group circleGroup = new Group();

    // Data structures to store player and UI elements
    private ArrayList<Player> players = new ArrayList<>();

    private ArrayList<ImageView> playerBorders = new ArrayList<ImageView>();

    private HashMap<String, Integer> demandedMaterials = new HashMap<>();
    private HashMap<Player, ArrayList<Node>> playersItems = new HashMap<>();

    private ArrayList<String> materials = new ArrayList<>();

    // This boolean flag is used for two rounds per player to determine if they have successfully built a village
    // in order to connect a road.
    private boolean hasBuiltVillage;
    private int round;


    @FXML
    void animationPop(MouseEvent event) {
        if(((ImageView)event.getSource()) == settingsButton || ((ImageView)event.getSource()) == settingsButton2 ||
                ((ImageView)event.getSource()) == exitButton|| ((ImageView)event.getSource()) == sound)
            ((ImageView)event.getSource()).setStyle("-fx-effect: dropShadow(gaussian, " + "#1DCC04" + ", 28, 0, 0, 0)");
        else {
            TranslateTransition translate = new TranslateTransition();
            ScaleTransition scale = new ScaleTransition();

            translate.setToY(-12);
            scale.setToX(1.07);
            scale.setToY(1.07);
            scale.setDuration(Duration.millis(700));
            translate.setDuration(Duration.millis(500));
            scale.setAutoReverse(true);
            translate.setAutoReverse(true);
            scale.setNode((ImageView)event.getSource());
            translate.setNode((ImageView)event.getSource());
            scale.play();
            translate.play();
        }
    }
    @FXML
    void animationPopUp(MouseEvent event) {
        if(((ImageView)event.getSource()) == settingsButton || ((ImageView)event.getSource()) == settingsButton2 ||
                ((ImageView)event.getSource()) == exitButton|| ((ImageView)event.getSource()) == sound)
            ((ImageView)event.getSource()).setStyle(null);
        else {
            TranslateTransition translate = new TranslateTransition();
            ScaleTransition scale = new ScaleTransition();

            translate.setToY(0);
            scale.setToX(1);
            scale.setToY(1);
            scale.setDuration(Duration.millis(700));
            translate.setDuration(Duration.millis(500));
            scale.setAutoReverse(true);
            translate.setAutoReverse(true);
            scale.setNode((ImageView) event.getSource());
            translate.setNode((ImageView) event.getSource());
            scale.play();
            translate.play();
        }
    }

    // Start the game board
    public void startBoard() throws FileNotFoundException {
        new Game();
        round = 1;

        players = Game.getPlayerList();
        initializePlayers(); // Adds player info to the scene

        initializeHexagons();

        initializeButtons(calculateAvailableVillages(), "Village");
        hasBuiltVillage = true;
    }

    @FXML
    void buildMenu(MouseEvent event){
        if (tradePane.isVisible()) tradePane.setVisible(false);
        AnchorPane pane = null;
        if(event.getSource() == buildButton) {
            //Player should not be able to open the build menu in his first two rounds
            if (round > Game.getPlayerList().size() * 2) {
                pane = buildPane;
                circleGroup.getChildren().clear();
                upgradeButtonMode = false;
            }
        }
        if( pane!= null) pane.setVisible(!pane.isVisible());
    }

    @FXML
    void tradeMenu(MouseEvent event) {

        if (buildPane.isVisible()) buildPane.setVisible(false);

        tradePlayerName.clear();
        bankTradeMaterial.clear();

        for(ChoiceBox<Integer> temp : trades) {
            for (int i = 0; i <= 10; i++){
                temp.getSelectionModel().selectFirst();
            }
        }

        for(ChoiceBox<Integer> temp : tradings) {
            for (int i = 0; i <= 10; i++){
                temp.getSelectionModel().selectFirst();
            }
        }

        AnchorPane pane = null;
        if(event.getSource() == tradeButton) {
            //Player should not be able to open the trade menu in his first two rounds
            if (round > Game.getPlayerList().size() * 2) {
                pane = tradePane;
                circleGroup.getChildren().clear();
                upgradeButtonMode = false;
            }
        }
        if( pane!= null) pane.setVisible(!pane.isVisible());
    }
    @FXML
    void settings(MouseEvent event) {
        menuScreen.setVisible(!menuScreen.isVisible());
    }

    @FXML
    void tradeWithBank (MouseEvent event) throws IOException {

        int i = 0;
        int tradingCost;
        int amount;

        if(bankTradeMaterial.getText().toLowerCase().equals("special card")) {
            Game.getCurrentPlayer().getSpecialCard();
            updatePlayerMaterials(Game.getCurrentPlayer());
            updatePlayerCards(Game.getCurrentPlayer());
        } else {
            switch (bankTradeMaterial.getText().toLowerCase()) {
                case "clay":
                case "rock":
                case "wood":
                case "wool":
                case "wheat":

                    String material = bankTradeMaterial.getText().toLowerCase();
                    material = material.substring(0, 1).toUpperCase() + material.substring(1);

                    for (ChoiceBox<Integer> temp: trades) {


                        amount = temp.getValue();
                        tradingCost = Game.getCurrentPlayer().getTradingCost(materials.get(i));

                        while((amount % tradingCost >= 0) && amount >= tradingCost) {

                            Game.getCurrentPlayer().tradeWithBank(material, materials.get(i), tradingCost);
                            updatePlayerMaterials(Game.getCurrentPlayer());
                            amount -= tradingCost;

                        }

                        i++;
                    }
                    break;
                default: bankTradeMaterial.setStyle("-fx-text-fill: red;"); break;

            }
        }
    }
    @FXML
    void tradeWithPlayer (MouseEvent event) {

        int amount;
        Boolean flag = false;
        int i = 0, j = 0;

        Player currentPlayer = null;
        for (i = 0; i < players.size(); i++) {
            if(players.get(i).getName().toLowerCase().equals(tradePlayerName.getText().toLowerCase()) && !tradePlayerName.getText().toLowerCase().equals(Game.getCurrentPlayer().getName().toLowerCase())) {
                currentPlayer = players.get(i);
                flag = true;
            }
        }
        if(flag) {
            playerTrading.setImage(currentPlayer.getImage());
            for (ChoiceBox<Integer> temp: trades) {
                amount = temp.getValue();
                demandedMaterials.put(materials.get(j), amount);
                j++;
            }
            if( tradePane!= null) tradePane.setVisible(!tradePane.isVisible());
            if( tradePlayer!= null) tradePlayer.setVisible(!tradePlayer.isVisible());
        } else
            tradePlayerName.setStyle("-fx-text-fill: red;");


    }
    @FXML
    void tradeConfirmed (MouseEvent event) throws IOException {

        int i = 0, j = 0, amount = 0;
        HashMap<String, Integer> suppliedMaterials = new HashMap<>();

        Player currentPlayer = null;
        for (i = 0; i < players.size(); i++) {
            if(players.get(i).getName().toLowerCase().equals(tradePlayerName.getText().toLowerCase()))
                currentPlayer = players.get(i);
        }

        for (ChoiceBox<Integer> temp: tradings) {
            amount = temp.getValue();
            suppliedMaterials.put(materials.get(j), amount);
            j++;
        }

        Game.getCurrentPlayer().tradeWithPlayers(currentPlayer, suppliedMaterials, demandedMaterials);

        updatePlayerMaterials(Game.getCurrentPlayer());
        updatePlayerMaterials(currentPlayer);

        if( tradePlayer!= null) tradePlayer.setVisible(!tradePlayer.isVisible());
    }

    // Handle escape key event to toggle the menu screen visibility
    @FXML
    void escape(KeyEvent event) {
        if (event.getCode() == KeyCode.P) {
            menuScreen.setVisible(!menuScreen.isVisible());
        }
    }

    @FXML
    void mute(MouseEvent event) throws FileNotFoundException {
        String isMuted = (!Music.isMute())? "ON" : "OFF";
        ((ImageView) event.getSource()).setImage(new Image(new FileInputStream("src/main/resources/assets/settings/sound_" + isMuted + ".png")));
    }

    @FXML
    void endTurn(MouseEvent event) throws FileNotFoundException {
        if (tradePane.isVisible()) tradePane.setVisible(false);
        if (buildPane.isVisible()) buildPane.setVisible(false);
        if(!hasBuiltVillage){
            playersItems.get(Game.getCurrentPlayer()).get(1).setStyle(null);

            Game.endTurn();

            updateBuildPane();
            circleGroup.getChildren().clear();
            upgradeButtonMode = false;
            round++;

            playersItems.get(Game.getCurrentPlayer()).get(1).setStyle("-fx-effect: dropShadow(three-pass-box, " + Game.getCurrentPlayer().getColor() + ", 50, 0, 0, 0)");


            if( round <= Game.getPlayerList().size() * 2 ) {
                initializeButtons(calculateAvailableVillages(), "Village");
                hasBuiltVillage = true;
            }
        }
    }

    private void updateBuildPane() throws FileNotFoundException {
        upgradeToCityButton.setImage(new Image(new FileInputStream("src/main/resources/assets/gameScreen/Build/City/city_blank.png")));
        buildVillageButton.setImage(new Image(new FileInputStream("src/main/resources/assets/gameScreen/Build/Village/village_blank.png")));
        buildRoadButton.setImage(new Image(new FileInputStream("src/main/resources/assets/gameScreen/Build/Road/road_blank.png")));

        if( City.canBeBuilt(Game.getCurrentPlayer()) ) upgradeToCityButton.setImage(new Image(new FileInputStream("src/main/resources/assets/gameScreen/Build/City/city_" + Game.getCurrentPlayer().getColor() + ".png")));
        if( Village.canBeBuilt(Game.getCurrentPlayer()) ) buildVillageButton.setImage(new Image(new FileInputStream("src/main/resources/assets/gameScreen/Build/Village/village_"+ Game.getCurrentPlayer().getColor() + ".png")));
        if( Road.canBeBuilt(Game.getCurrentPlayer()) ) buildRoadButton.setImage(new Image(new FileInputStream("src/main/resources/assets/gameScreen/Build/Road/road_"+ Game.getCurrentPlayer().getColor() + ".png")));
    }

    // Exit the game and go back to the start screen
    @FXML
    void exitGame(MouseEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("StartScreen-view.fxml"));

        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        configureStage(stage, "Start");
        Music.changeSong(Music.getStartScreenSong());
    }

    private void configureStage(Stage stage, String stageTitle) {
        stage.getScene().setRoot(root);
        stage.setFullScreen(true);
        stage.setTitle(stageTitle + " " + "Screen");
        stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        stage.show();
    }


    // Initialize the mapping between players and UI elements
    private void initializePlayerItemsMap() {
        ArrayList<Node> playerNodes;
        for (int i = 0; i < players.size(); i++) {
            playerNodes = new ArrayList<>();
            Player player = players.get(i);
            switch (i) {
                case 0 -> {
                    playerNodes.add(nameLabel1);
                    playerNodes.add(playerBoard1);
                    playerNodes.add(materials1);
                    playerNodes.add(cards1);
                    playerNodes.add(stats1);
                }
                case 1 -> {
                    playerNodes.add(nameLabel2);
                    playerNodes.add(playerBoard2);
                    playerNodes.add(materials2);
                    playerNodes.add(cards2);
                    playerNodes.add(stats2);
                }
                case 2 -> {
                    playerNodes.add(nameLabel3);
                    playerNodes.add(playerBoard3);
                    playerNodes.add(materials3);
                    playerNodes.add(cards3);
                    playerNodes.add(stats3);
                }
                case 3 -> {
                    playerNodes.add(nameLabel4);
                    playerNodes.add(playerBoard4);
                    playerNodes.add(materials4);
                    playerNodes.add(cards4);
                    playerNodes.add(stats4);
                }
                default -> throw new IllegalStateException("Unexpected value: " + i);
            }
            playersItems.put(player, playerNodes);
        }
    }


    // Initialize the hexagons on the game board
    private void initializeHexagons() throws FileNotFoundException {
        for(Hexagon hexagon : Board.getHexagonList()) {
            ImageView boardItems = new ImageView(hexagon.getImage());
            initializeBoardImages(hexagon.getCoords(), boardItems, 0, 0, 178.0, 195.0);
            boardPane.getChildren().add(boardItems);

            if (!hexagon.getBiome().equals("Desert")) {
                String path = "src/main/resources/assets/hexagons/hexagonNumbers/no." + hexagon.getNumber() + ".png";
                boardItems = new ImageView(new Image(new FileInputStream(path)));
                initializeBoardImages(hexagon.getCoords(), boardItems, 48, 52, 80.0, 80.0);
                boardPane.getChildren().add(boardItems);
            } else {
                pirate = new ImageView(hexagon.getHasPirate().getImage());
                initializeBoardImages(hexagon.getCoords(), pirate, 55, 23, 93.0, 119.0);
                boardPane.getChildren().add(pirate);
            }

            if(hexagon.getPort()!=null){
                Port port = hexagon.getPort();
                boardItems = new ImageView(port.getImage());
                initializeBoardImages(port.getCoords(), boardItems, 0, 0, 128,140);
                switch(port.getEdge()){
                    case 3:
                        boardItems.setRotate(57);
                        break;
                    case 5:
                        boardItems.setRotate(118);
                        break;
                    case 7:
                        boardItems.setRotate(180);
                        break;
                    case 9:
                        boardItems.setRotate(-123);
                        break;
                    case 11:
                        boardItems.setRotate(-57);
                        break;
                }
                roadGroup.getChildren().add(boardItems);
            }
        }
        boardPane.getChildren().add(roadGroup);
        boardPane.getChildren().add(circleGroup);
        boardPane.getChildren().add(buildingsGroup);
    }

    //This method is used to create the areas where a player can click to build
    private void initializeButtons(HashSet<Coordinates> buttons, String structure) throws FileNotFoundException {
        for(Coordinates coords : buttons){
            Image buttonImage = new Image(new FileInputStream("src/main/resources/assets/gameScreen/Build/selectionCircle.png"));
            ImageView button = new ImageView(buttonImage);
            button.setLayoutX(coords.getX());
            button.setLayoutY(coords.getY());
            button.toFront();

            //this event listener probably needs to change.
            //handles the building part
            button.setOnMouseClicked(event -> {
                if( button.getImage() == buttonImage ) {
                    if( structure.equals("Village") ){
                        String villageImagePath = "src/main/resources/assets/gameScreen/Build/Village/village_" + Game.getCurrentPlayer().getColor() + ".png";
                        Village village = new Village(villageImagePath, new Coordinates(coords.getX(), coords.getY()), Game.getCurrentPlayer());
                        village.build(Board.getHexagonCorners().get(coords));

                        initializeBoardImages(coords, button, -10, -18, 48, 41.8);
                        button.setImage(village.getImage());
                        buildingsGroup.getChildren().add(button);
                    } else if( structure.equals("Road") ){
                        String roadImagePath = "src/main/resources/assets/gameScreen/Build/Road/road_" + Game.getCurrentPlayer().getColor() + ".png";
                        Road road = new Road(roadImagePath, new Coordinates(coords.getX(), coords.getY()), Game.getCurrentPlayer());
                        HashMap<Integer, Hexagon> edgeCoords = Board.getHexagonEdges().get(coords);
                        road.build(edgeCoords);

                        initializeBoardImages(coords, button, 5, -41, 20, 110);
                        button.setImage(road.getImage());
                        roadGroup.getChildren().add(button);

                        if( edgeCoords.containsKey(1) || edgeCoords.containsKey(7) ){
                            button.setRotate(61);
                        } else if( edgeCoords.containsKey(3) ||edgeCoords.containsKey(9) ) {
                            button.setRotate(-61);
                        }

                        hasBuiltVillage = false;
                    }
                    circleGroup.getChildren().clear();
                } else if(upgradeButtonMode && Board.getHexagonCorners().containsKey(new Coordinates(button.getLayoutX()+10, button.getLayoutY()+18)) ){
                    upgradeButtonMode = false;
                    boolean isCity = false;
                    Player owner = null;
                    for(int corner : Board.getHexagonCorners().get(coords).keySet()) {
                        owner = Board.getHexagonCorners().get(coords).get(corner).getStructure(corner).getOwner();
                        if(Board.getHexagonCorners().get(coords).get(corner).getStructure(corner) instanceof City) isCity = true;
                        break;
                    }
                    if( owner.equals(Game.getCurrentPlayer()) && !isCity ){
                        City city = Village.upgradeToCity(Board.getHexagonCorners().get(coords), owner, coords);
                        button.setImage(city.getImage());
                    }
                }
                try {
                    updatePlayerPoints(Game.getCurrentPlayer());
                    updatePlayerMaterials(Game.getCurrentPlayer());
                    updateBuildPane();
                    if( round <= Game.getPlayerList().size()*2 && hasBuiltVillage){
                        initializeButtons(calculateAvailableRoads(), "Road");
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            circleGroup.getChildren().add(button);
        }
    }


    // Initialize the layout and properties of the board items
    private void initializeBoardImages(Coordinates coords, ImageView boardItems, int x, int y, double width, double height) {
        double layoutX = coords.getX() + x;
        double layoutY = coords.getY() + y;
        boardItems.setLayoutX(layoutX);
        boardItems.setLayoutY(layoutY);
        boardItems.setFitWidth(width);
        boardItems.setFitHeight(height);
        boardItems.toFront();
    }


    // Initialize player information on the UI
    private void initializePlayers() throws FileNotFoundException {
        Music.changeSong(Music.gameScreenSong);
        playerBoard1.setStyle("-fx-effect: dropShadow(three-pass-box, " + Game.getCurrentPlayer().getColor() + ", 50, 0, 0, 0)");
        initializePlayerItemsMap();
        initializePlayerInfo();
        initializeMaterialList();
        initializeCardList();
        initializeStatsList();
        updateBuildPane();
    }

    // Initialize player names and images on the UI
    private void initializePlayerInfo() {
        for (Player player : players) {
            ArrayList<Node> playerNodes = playersItems.get(player);
            Label tempLabel = (Label) playerNodes.get(0);
            ImageView tempImageView = (ImageView) playerNodes.get(1);
            tempLabel.getParent().setVisible(true);
            tempLabel.setText(player.getName());
            tempImageView.setImage(player.getImage());
        }

    }


    // Initialize material lists for each player
    private void initializeMaterialList() {
        for (Player player : players) {
            HashMap<String, Integer> materials = player.getMaterials();
            for (String material : materials.keySet()) {
                if (!material.equals("Sum")) {
                    ListView<Integer> tempList = (ListView<Integer>) playersItems.get(player).get(2);
                    tempList.getItems().add(materials.get(material));
                    tempList.setMouseTransparent(true);
                    tempList.setFocusTraversable(false);
                }
            }
        }
    }

    private void initializeCardList() {
        for (Player player : players) {
            ArrayList<Card> cards = player.getCardsList();
            for (Card card : cards) {
                ListView<String> tempList = (ListView<String>) playersItems.get(player).get(3);
                tempList.getItems().add(card.getName());
            }
        }
    }

    private void initializeStatsList() {
        for (Player player : players) {
            ListView<Integer> tempList = (ListView<Integer>) playersItems.get(player).get(4);
            tempList.getItems().add(player.getPoints());
            tempList.getItems().add(player.getCardListSize());
            tempList.getItems().add(Quest.getPlayerArmyAmount(player));
        }
    }

    //Calculates every possible position where a player can build a road.
    //Needs to change.
    private HashSet<Coordinates> calculateAvailableRoads() {
        HashSet<Coordinates> availableRoadPositions = new HashSet<>();
        ArrayList<Structure> buildings = new ArrayList<>();
        if (round <= Game.getPlayerList().size() || round >= Game.getPlayerList().size() * 2){
            buildings.addAll(Game.getCurrentPlayer().getStructureMap().get("Village"));
            buildings.addAll(Game.getCurrentPlayer().getStructureMap().get("City"));
            buildings.addAll(Game.getCurrentPlayer().getStructureMap().get("Road"));
        } else {
            buildings.add(Game.getCurrentPlayer().getStructureMap().get("Village").get(1));
        }

        for(Structure tempBuilding : buildings){
            double tempX = tempBuilding.getCoords().getX();
            double tempY = tempBuilding.getCoords().getY();
            Hexagon hexagon;

            for(Coordinates coordsForEveryEdge : Board.getHexagonEdges().keySet()) {
                if ( Math.sqrt(Math.pow(coordsForEveryEdge.getX() - tempX, 2) + Math.pow(coordsForEveryEdge.getY() - tempY, 2)) <= 100 ) {
                    for(int index :  Board.getHexagonEdges().get(coordsForEveryEdge).keySet()){
                        hexagon = Board.getHexagonEdges().get(coordsForEveryEdge).get(index);
                        if( hexagon.getStructure(index) == null ) {
                            availableRoadPositions.add(coordsForEveryEdge);
                        }
                    }
                }
            }
        }

        return availableRoadPositions;
    }

    //Calculates every possible position where a player can build a village.
    //Needs to change.
    private HashSet<Coordinates> calculateAvailableVillages(){
        HashSet<Coordinates> possibleVillagePositions = new HashSet<>();

        ArrayList<Structure> buildings = new ArrayList<>();

        double tempX, tempY;

        if(round > Game.getPlayerList().size() * 2){
            ArrayList<Structure> roads = new ArrayList<>(Game.getCurrentPlayer().getStructureMap().get("Road"));

            for(Structure tempRoad : roads){
                tempX = tempRoad.getCoords().getX();
                tempY = tempRoad.getCoords().getY();
                Hexagon hexagon;

                for(Coordinates coordsForEveryCorner : Board.getHexagonCorners().keySet()) {
                    if( !possibleVillagePositions.contains(coordsForEveryCorner) ){
                        if ( Math.sqrt(Math.pow(coordsForEveryCorner.getX() - tempX, 2) + Math.pow(coordsForEveryCorner.getY() - tempY, 2)) < 55 ) {
                            for(int index :  Board.getHexagonCorners().get(coordsForEveryCorner).keySet()){
                                hexagon = Board.getHexagonCorners().get(coordsForEveryCorner).get(index);
                                if(hexagon.getStructure(index) == null || hexagon.getStructure(index) instanceof Road) possibleVillagePositions.add(coordsForEveryCorner);
                            }
                        }
                    }
                }
            }
        } else {
            possibleVillagePositions.addAll(Board.getHexagonCorners().keySet());
        }

        for(Player player : Game.getPlayerList()) {
            buildings.addAll(player.getStructureMap().get("Village"));
            buildings.addAll(player.getStructureMap().get("City"));
        }
        HashSet<Coordinates> availableVillagePositions = new HashSet<>(possibleVillagePositions);

        for(Structure tempBuilding : buildings){
            tempX = tempBuilding.getCoords().getX();
            tempY = tempBuilding.getCoords().getY();
            for(Coordinates availableVillageCoords : possibleVillagePositions){
                if ( Math.sqrt(Math.pow(availableVillageCoords.getX() - tempX, 2) + Math.pow(availableVillageCoords.getY() - tempY, 2)) < 110 )
                    availableVillagePositions.remove(availableVillageCoords);
            }
        }

        return availableVillagePositions;
    }

    private void updatePlayerPoints(Player player) throws IOException {
        ListView<Integer> tempList = (ListView<Integer>) playersItems.get(player).get(4);
        tempList.getItems().set(0, player.getPoints());

        if( Game.hasWon() ){
            root = FXMLLoader.load(getClass().getResource("EndScreen-view.fxml"));

            stage = (Stage) menuScreen.getScene().getWindow();
            stage.getScene().setRoot(root);
            stage.setFullScreen(true);
            stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
            stage.setTitle("End Screen");
            stage.show();
        }
    }

    private void updatePlayerMaterials(Player player) throws IOException {
        ListView<Integer> tempList = (ListView<Integer>) playersItems.get(player).get(2);

        int materialIndex = 0;
        for( String material : player.getMaterials().keySet()){
            if( !material.equals("Sum")) {
                tempList.getItems().set(materialIndex, player.getMaterialAmount(material));
                materialIndex++;
            }
        }
    }

    private void updatePlayerCards(Player player) {

        ListView<String> tempList = (ListView<String>) playersItems.get(player).get(3);
        tempList.getItems().clear();
        for( Card card : player.getCardsList()) {

            tempList.getItems().add(card.getName());

        }
    }

    private void errorEvent(MouseEvent event) {
        ((TextField)event.getSource()).setStyle("-fx-text-fill: black;");
    }

    // FXML initialization method
    @FXML
    void initialize() throws FileNotFoundException {
        startBoard();
        playerBorders.add(playerBoard1);
        playerBorders.add(playerBoard2);
        playerBorders.add(playerBoard3);
        playerBorders.add(playerBoard4);


        trades.add(tradeClay); trades.add(tradeRock);
        trades.add(tradeWood);  trades.add(tradeWool);
        trades.add(tradeWheat);

        for(ChoiceBox<Integer> temp : trades) {
            for (int i = 0; i <= 10; i++){
                temp.getItems().add(i);
            }
        }

        tradings.add(tradingClay); tradings.add(tradingRock);
        tradings.add(tradingWood);  tradings.add(tradingWool);
        tradings.add(tradingWheat);

        for(ChoiceBox<Integer> temp : tradings) {
            for (int i = 0; i <= 10; i++){
                temp.getItems().add(i);
            }
        }

        materials.add("Clay");materials.add("Rock");
        materials.add("Wood");materials.add("Wool");
        materials.add("Wheat");

        assert bankTradeMaterial != null : "fx:id=\"bankTradeMaterial\" was not injected: check your FXML file 'GameScreen-view.fxml'.";
        assert boardPane != null : "fx:id=\"boardPane\" was not injected: check your FXML file 'GameScreen-view.fxml'.";
        assert buildButton != null : "fx:id=\"buildButton\" was not injected: check your FXML file 'GameScreen-view.fxml'.";
        assert buildPane != null : "fx:id=\"buildPane\" was not injected: check your FXML file 'GameScreen-view.fxml'.";
        assert buildRoadButton != null : "fx:id=\"buildRoadButton\" was not injected: check your FXML file 'GameScreen-view.fxml'.";
        assert buildSelectionButtonsPane != null : "fx:id=\"buildSelectionButtonsPane\" was not injected: check your FXML file 'GameScreen-view.fxml'.";
        assert buildVillageButton != null : "fx:id=\"buildVillageButton\" was not injected: check your FXML file 'GameScreen-view.fxml'.";
        assert cards1 != null : "fx:id=\"cards1\" was not injected: check your FXML file 'GameScreen-view.fxml'.";
        assert cards2 != null : "fx:id=\"cards2\" was not injected: check your FXML file 'GameScreen-view.fxml'.";
        assert cards3 != null : "fx:id=\"cards3\" was not injected: check your FXML file 'GameScreen-view.fxml'.";
        assert cards4 != null : "fx:id=\"cards4\" was not injected: check your FXML file 'GameScreen-view.fxml'.";
        assert exitButton != null : "fx:id=\"exitButton\" was not injected: check your FXML file 'GameScreen-view.fxml'.";
        assert materials1 != null : "fx:id=\"materials1\" was not injected: check your FXML file 'GameScreen-view.fxml'.";
        assert materials2 != null : "fx:id=\"materials2\" was not injected: check your FXML file 'GameScreen-view.fxml'.";
        assert materials3 != null : "fx:id=\"materials3\" was not injected: check your FXML file 'GameScreen-view.fxml'.";
        assert materials4 != null : "fx:id=\"materials4\" was not injected: check your FXML file 'GameScreen-view.fxml'.";
        assert menuScreen != null : "fx:id=\"menuScreen\" was not injected: check your FXML file 'GameScreen-view.fxml'.";
        assert nameLabel1 != null : "fx:id=\"nameLabel1\" was not injected: check your FXML file 'GameScreen-view.fxml'.";
        assert nameLabel2 != null : "fx:id=\"nameLabel2\" was not injected: check your FXML file 'GameScreen-view.fxml'.";
        assert nameLabel3 != null : "fx:id=\"nameLabel3\" was not injected: check your FXML file 'GameScreen-view.fxml'.";
        assert nameLabel4 != null : "fx:id=\"nameLabel4\" was not injected: check your FXML file 'GameScreen-view.fxml'.";
        assert playerBoard1 != null : "fx:id=\"playerBoard1\" was not injected: check your FXML file 'GameScreen-view.fxml'.";
        assert playerBoard2 != null : "fx:id=\"playerBoard2\" was not injected: check your FXML file 'GameScreen-view.fxml'.";
        assert playerBoard3 != null : "fx:id=\"playerBoard3\" was not injected: check your FXML file 'GameScreen-view.fxml'.";
        assert playerBoard4 != null : "fx:id=\"playerBoard4\" was not injected: check your FXML file 'GameScreen-view.fxml'.";
        assert playerTrading != null : "fx:id=\"playerTrading\" was not injected: check your FXML file 'GameScreen-view.fxml'.";
        assert settingsButton != null : "fx:id=\"settingsButton\" was not injected: check your FXML file 'GameScreen-view.fxml'.";
        assert settingsButton2 != null : "fx:id=\"settingsButton2\" was not injected: check your FXML file 'GameScreen-view.fxml'.";
        assert sound != null : "fx:id=\"sound\" was not injected: check your FXML file 'GameScreen-view.fxml'.";
        assert stats1 != null : "fx:id=\"stats1\" was not injected: check your FXML file 'GameScreen-view.fxml'.";
        assert stats2 != null : "fx:id=\"stats2\" was not injected: check your FXML file 'GameScreen-view.fxml'.";
        assert stats3 != null : "fx:id=\"stats3\" was not injected: check your FXML file 'GameScreen-view.fxml'.";
        assert stats4 != null : "fx:id=\"stats4\" was not injected: check your FXML file 'GameScreen-view.fxml'.";
        assert tradeButton != null : "fx:id=\"tradeButton\" was not injected: check your FXML file 'GameScreen-view.fxml'.";
        assert tradeClay != null : "fx:id=\"tradeClay\" was not injected: check your FXML file 'GameScreen-view.fxml'.";
        assert tradePane != null : "fx:id=\"tradePane\" was not injected: check your FXML file 'GameScreen-view.fxml'.";
        assert tradePlayer != null : "fx:id=\"tradePlayer\" was not injected: check your FXML file 'GameScreen-view.fxml'.";
        assert tradePlayerName != null : "fx:id=\"tradePlayerName\" was not injected: check your FXML file 'GameScreen-view.fxml'.";
        assert tradeRock != null : "fx:id=\"tradeRock\" was not injected: check your FXML file 'GameScreen-view.fxml'.";
        assert tradeWheat != null : "fx:id=\"tradeWheat\" was not injected: check your FXML file 'GameScreen-view.fxml'.";
        assert tradeWood != null : "fx:id=\"tradeWood\" was not injected: check your FXML file 'GameScreen-view.fxml'.";
        assert tradeWool != null : "fx:id=\"tradeWool\" was not injected: check your FXML file 'GameScreen-view.fxml'.";
        assert tradingButton != null : "fx:id=\"tradingButton\" was not injected: check your FXML file 'GameScreen-view.fxml'.";
        assert tradingClay != null : "fx:id=\"tradingClay\" was not injected: check your FXML file 'GameScreen-view.fxml'.";
        assert tradingRock != null : "fx:id=\"tradingRock\" was not injected: check your FXML file 'GameScreen-view.fxml'.";
        assert tradingWheat != null : "fx:id=\"tradingWheat\" was not injected: check your FXML file 'GameScreen-view.fxml'.";
        assert tradingWood != null : "fx:id=\"tradingWood\" was not injected: check your FXML file 'GameScreen-view.fxml'.";
        assert tradingWool != null : "fx:id=\"tradingWool\" was not injected: check your FXML file 'GameScreen-view.fxml'.";
        assert upgradeToCityButton != null : "fx:id=\"upgradeToCityButton\" was not injected: check your FXML file 'GameScreen-view.fxml'.";
        assert volumeSlider != null : "fx:id=\"volumeSlider\" was not injected: check your FXML file 'GameScreen-view.fxml'.";

        EventHandler<MouseEvent> temp = this::errorEvent;
        bankTradeMaterial.setOnMouseClicked(temp);
        tradePlayerName.setOnMouseClicked(temp);
        
        volumeSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            Music.setVolume(newValue.doubleValue());
            String isMuted = (newValue.doubleValue() > 0)? "ON" : "OFF";
            try {
                sound.setImage(new Image(new FileInputStream("src/main/resources/assets/settings/sound_" + isMuted + ".png")));
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
        
        buildVillageButton.setOnMouseClicked(event -> {
            if( Village.canBeBuilt(Game.getCurrentPlayer()) ){
                buildPane.setVisible(false);
                try {
                    updateBuildPane();
                    initializeButtons(calculateAvailableVillages(), "Village");
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        buildRoadButton.setOnMouseClicked(event -> {
            if( Road.canBeBuilt(Game.getCurrentPlayer()) ){
                buildPane.setVisible(false);
                try {
                    updateBuildPane();
                    HashSet<Coordinates> availableRoads = calculateAvailableRoads();
                    if( availableRoads.size()!=0 )
                        initializeButtons(availableRoads, "Road");
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        upgradeToCityButton.setOnMouseClicked(event -> {
            if( City.canBeBuilt(Game.getCurrentPlayer()) ){
                buildPane.setVisible(false);
                try {
                    updateBuildPane();
                    upgradeButtonMode = true;
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
}

