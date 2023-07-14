package com.screens;

import com.board.*;
import com.game.*;

import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;


public class GameScreenController {
    private Parent root;

    @FXML
    private AnchorPane boardPane, menuScreen, buildPane, tradePane, tradeOptionsPane, confirmPlayerTradePane, dicePane, piratePane, cardPane;

    private final ArrayList<ChoiceBox<Integer>> trades = new ArrayList<>();
    private final ArrayList<ChoiceBox<Integer>> giveToPirate = new ArrayList<>();

    @FXML
    private ChoiceBox<Integer> tradeClay, tradeRock, tradeWheat, tradeWool, tradeWood, pirateClay, pirateRock, pirateWheat, pirateWool, pirateWood;

    @FXML
    private ChoiceBox<String> bankTradeMaterial, tradePlayerName, selectVictim, firstMaterial, secondMaterial;

    @FXML
    private ListView<String> cards1, cards2, cards3, cards4;

    @FXML
    private ListView<Integer> stats1, stats2, stats3, stats4, materials1, materials2, materials3, materials4;

    @FXML
    private Label nameLabel1, nameLabel2, nameLabel3, nameLabel4;

    @FXML
    private ImageView playerBoard1, playerBoard2, playerBoard3, playerBoard4, playerTrading, settingsButton, settingsButton2, exitButton;

    @FXML
    private ImageView sound, buildButton, tradeButton, buildRoadButton, buildVillageButton, upgradeToCityButton, pirate, diceButton, firstDie, secondDie, giveToPirateButton, takeMaterials;
    private final Group roadGroup = new Group();
    private final Group buildingsGroup = new Group();
    private final Group circleGroup = new Group();

    // Data structures to store player and UI elements
    private ArrayList<Player> players = new ArrayList<>();
    private final ArrayList<Player> victims = new ArrayList<>();
    private final HashMap<String, Integer> demandedMaterials = new HashMap<>();
    private final HashMap<Player, ArrayList<Node>> playersItems = new HashMap<>();
    private final ArrayList<String> materials = new ArrayList<>();
    private Game game;

    boolean upgradeButtonMode; // This boolean flag toggles the ability for a player to upgrade his villages into cities.
    boolean hasRolled; // This boolean prevents the player from clicking on other buttons until he has rolled the dice.
    boolean isStealing; // This boolean is used when the player is stealing materials from another player

    // Start the Game board
    public void startBoard() throws FileNotFoundException {
        game = new Game();

        players = Game.getPlayerList();
        initializePlayers(); // Adds player info to the scene
        hasRolled = false;

        initializeHexagons();

        initializeButtons(calculateAvailableVillages(), "Village");
    }

    // Initialize player information on the UI
    private void initializePlayers() throws FileNotFoundException {
        Music.changeSong(Music.getGameScreenSong());
        nameLabel1.getParent().setStyle("-fx-effect: dropShadow(three-pass-box, " + game.getCurrentPlayer().getColor() + ", 50, 0, 0, 0)");
        initializePlayerItemsMap();
        initializeCardListeners();
        initializePlayerInfo();
        initializeMaterialList();
        initializeCardList();
        initializeStatsList();
        updateBuildPane();
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

    private void initializeCardListeners(){
        for(Player player : players){
            ListView<String> list = (ListView<String>)playersItems.get(player).get(3);
            list.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && event.getButton() == MouseButton.PRIMARY) {
                    Music.playPopSound();
                    String selectedItem = list.getSelectionModel().getSelectedItem();

                    if( list == playersItems.get(game.getCurrentPlayer()).get(3) && circleGroup.getChildren().isEmpty() ){
                        switch(selectedItem){
                            case "Pirate":
                                isStealing = true;
                                game.getCurrentPlayer().removeCard(game.getCurrentPlayer().getSelectedCard("Pirate"));
                                Quest.addArmyAmount(game.getCurrentPlayer(), 1);
                                updatePlayerCards(game.getCurrentPlayer());

                                HashSet<Coordinates> hexagonCoordinatesList = new HashSet<>();
                                for(Hexagon hexagon : game.getBoard().getHexagonList()){
                                    if( !hexagon.getBiome().equals("Desert") && hexagon.getHasPirate() == null)
                                        hexagonCoordinatesList.add(new Coordinates(hexagon.getCoords().getX()+74, hexagon.getCoords().getY()+80));
                                }
                                try {
                                    initializeButtons(hexagonCoordinatesList, "Hexagon");
                                } catch (FileNotFoundException e) {
                                    throw new RuntimeException(e);
                                }
                                tradePane.setVisible(false);
                                buildPane.setVisible(false);
                                break;
                            case "RoadCard":
                                SpecialCard roadCard = (SpecialCard) game.getCurrentPlayer().getSelectedCard("RoadCard");
                                roadCard.giveRoad(game.getCurrentPlayer());
                                try {
                                    updatePlayerMaterials(game.getCurrentPlayer());
                                    updatePlayerCards(game.getCurrentPlayer());
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                                break;
                            case "Monopoly":
                                takeMaterials.setImage(new Image(Objects.requireNonNull(StartScreen.class.getResourceAsStream("/assets/gameScreen/tradeWithPlayer.png"))));
                                tradePane.setVisible(false);
                                buildPane.setVisible(false);
                                cardPane.setVisible(true);
                                firstMaterial.setVisible(true);
                                break;
                            case "YearOfPlenty":
                                takeMaterials.setImage(new Image(Objects.requireNonNull(StartScreen.class.getResourceAsStream("/assets/gameScreen/tradeWithBank.png"))));
                                tradePane.setVisible(false);
                                buildPane.setVisible(false);
                                cardPane.setVisible(true);
                                firstMaterial.setVisible(true);
                                secondMaterial.setVisible(true);
                                break;
                        }
                    }
                }
            });
        }
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

    private void updateBuildPane() throws FileNotFoundException {
        upgradeToCityButton.setImage(new Image(Objects.requireNonNull(StartScreen.class.getResourceAsStream("/assets/gameScreen/Build/City/city_blank.png"))));
        buildVillageButton.setImage(new Image(Objects.requireNonNull(StartScreen.class.getResourceAsStream("/assets/gameScreen/Build/Village/village_blank.png"))));
        buildRoadButton.setImage(new Image(Objects.requireNonNull(StartScreen.class.getResourceAsStream("/assets/gameScreen/Build/Road/road_blank.png"))));

        if( City.canBeBuilt(game.getCurrentPlayer()) ) upgradeToCityButton.setImage(new Image(Objects.requireNonNull(StartScreen.class.getResourceAsStream("/assets/gameScreen/Build/City/city_" + game.getCurrentPlayer().getColor() + ".png"))));
        if( Village.canBeBuilt(game.getCurrentPlayer()) ) buildVillageButton.setImage(new Image(Objects.requireNonNull(StartScreen.class.getResourceAsStream("/assets/gameScreen/Build/Village/village_" + game.getCurrentPlayer().getColor() + ".png"))));
        if( Road.canBeBuilt(game.getCurrentPlayer()) ) buildRoadButton.setImage(new Image(Objects.requireNonNull(StartScreen.class.getResourceAsStream("/assets/gameScreen/Build/Road/road_" + game.getCurrentPlayer().getColor() + ".png"))));
    }


    @FXML
    void animationPop(MouseEvent event) {
        if(event.getSource() == settingsButton || event.getSource() == settingsButton2 ||
                event.getSource() == exitButton|| event.getSource() == sound)
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
        if(event.getSource() == settingsButton || event.getSource() == settingsButton2 ||
                event.getSource() == exitButton|| event.getSource() == sound)
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

    @FXML
    void buildMenu(MouseEvent event){
        AnchorPane pane = null;
        if(hasRolled && !piratePane.isVisible() && !cardPane.isVisible()) {
            Music.playButtonSound();
            if (tradePane.isVisible()) tradePane.setVisible(false);
            if (event.getSource() == buildButton) {
                //Player should not be able to open the build menu in his first two rounds
                if (game.getRound() > Game.getPlayerList().size() * 2) {
                    pane = buildPane;
                    circleGroup.getChildren().clear();
                    upgradeButtonMode = false;
                }
            }
        }
        if( pane!= null) pane.setVisible(!pane.isVisible());
    }

    @FXML
    void tradeMenu(MouseEvent event) {
        if(hasRolled && !piratePane.isVisible() && !cardPane.isVisible()){
            Music.playButtonSound();
            if (buildPane.isVisible()) buildPane.setVisible(false);

            bankTradeMaterial.getSelectionModel().clearSelection();
            tradePlayerName.getItems().clear();
            for(Player player : players){
                if( !player.equals(game.getCurrentPlayer()) )
                    tradePlayerName.getItems().add(player.getName());
            }

            for(ChoiceBox<Integer> temp : trades) {
                temp.getSelectionModel().selectFirst();
            }

            AnchorPane pane = null;
            if(event.getSource() == tradeButton) {
                //Player should not be able to open the trade menu in his first two rounds
                if (game.getRound() > Game.getPlayerList().size() * 2) {
                    pane = tradePane;
                    circleGroup.getChildren().clear();
                    upgradeButtonMode = false;
                }
            }
            if( pane!= null){
                pane.setVisible(!pane.isVisible());
                tradeOptionsPane.setVisible(true);
            }
        }
    }

    @FXML
    void tradeWithBank (MouseEvent event) throws IOException {
        Music.playButtonSound();

        if(bankTradeMaterial.getValue()!=null && bankTradeMaterial.getValue().equals("Special Card")) {
            if(trades.get(1).getValue()!=0 && trades.get(3).getValue()!=0 && trades.get(4).getValue()!=0) {
                game.getCurrentPlayer().getSpecialCard();
                updatePlayerMaterials(game.getCurrentPlayer());
                updatePlayerCards(game.getCurrentPlayer());
                updatePlayerPoints(game.getCurrentPlayer());
            }
        } else {
            int materialIndex = 0;
            int tradingCost;
            int amount;

            for (ChoiceBox<Integer> trade: trades) {
                amount = trade.getValue();
                tradingCost = game.getCurrentPlayer().getTradingCost(materials.get(materialIndex));

                while((amount % tradingCost >= 0) && amount >= tradingCost) {
                    game.getCurrentPlayer().tradeWithBank(bankTradeMaterial.getValue(), materials.get(materialIndex), tradingCost);
                    updatePlayerMaterials(game.getCurrentPlayer());
                    amount -= tradingCost;

                }
                materialIndex++;
            }
        }
        updateBuildPane();
    }
    @FXML
    void tradeWithPlayer (MouseEvent event) {
        Music.playButtonSound();
        int amount;
        int materialIndex = 0;

        Player playerToTradeWith = null;
        for (Player player : players) {
            if (player.getName().equals(tradePlayerName.getValue())) {
                playerToTradeWith = player;
            }
        }

        if(playerToTradeWith!=null){
            playerTrading.setImage(playerToTradeWith.getImage());
            demandedMaterials.clear();
            for (ChoiceBox<Integer> trade: trades) {
                amount = trade.getValue();
                demandedMaterials.put(materials.get(materialIndex), amount);
                materialIndex++;
            }
            tradeOptionsPane.setVisible(false);
            confirmPlayerTradePane.setVisible(true);
            for (ChoiceBox<Integer> trade: trades) {
                trade.getSelectionModel().selectFirst();
            }
        }
    }

    @FXML
    void tradeConfirmed (MouseEvent event) throws IOException {
        Music.playButtonSound();

        int materialIndex = 0, amount;
        HashMap<String, Integer> suppliedMaterials = new HashMap<>();

        Player currentPlayer = null;
        for (Player player : players) {
            if (player.getName().equals(tradePlayerName.getValue()))
                currentPlayer = player;
        }

        for (ChoiceBox<Integer> trade: trades) {
            amount = trade.getValue();
            suppliedMaterials.put(materials.get(materialIndex), amount);
            materialIndex++;
        }

        game.getCurrentPlayer().tradeWithPlayers(currentPlayer, suppliedMaterials, demandedMaterials);

        updatePlayerMaterials(game.getCurrentPlayer());
        updatePlayerMaterials(currentPlayer);

        confirmPlayerTradePane.setVisible(false);
        tradePane.setVisible(false);
        updateBuildPane();
    }

    @FXML
    void getCard(MouseEvent event) throws IOException {
        if( secondMaterial.isVisible() ) {
            String firstItem = firstMaterial.getValue();
            String secondItem = secondMaterial.getValue();

            ((SpecialCard) game.getCurrentPlayer().getSelectedCard("YearOfPlenty")).takeMaterialFromBank(game.getCurrentPlayer(), firstItem, secondItem);
        } else if( selectVictim.isVisible() ){
            String victim = selectVictim.getSelectionModel().getSelectedItem();
            for(Player player : players){
                if(victim != null && victim.equals(player.getName())){
                    SpecialCard card = new SpecialCard("Pirate", 0);
                    card.usePirateCard(game.getCurrentPlayer(), player, game.getBoard().getHexagonList());
                    break;
                }
            }
        } else {
            String firstItem = firstMaterial.getValue();
            ((SpecialCard)game.getCurrentPlayer().getSelectedCard("Monopoly")).takeMaterialFromPlayers(game.getCurrentPlayer(), firstItem);
        }

        cardPane.setVisible(false);

        firstMaterial.setVisible(false);
        firstMaterial.getSelectionModel().clearSelection();

        secondMaterial.setVisible(false);
        secondMaterial.getSelectionModel().clearSelection();

        selectVictim.setVisible(false);
        selectVictim.getItems().clear();
        victims.clear();

        for(Player player : players) {
            updatePlayerPoints(player);
            updatePlayerMaterials(player);
            updatePlayerCards(player);
        }

    }
    @FXML
    void takeMaterial(MouseEvent event) throws IOException {
        Music.playButtonSound();
        int materialIndex=0, amount, totalAmount=0;
        boolean flag = false;

        Player player = victims.get(0);

        for (ChoiceBox<Integer> temp : giveToPirate) {
            amount = temp.getValue();
            if(player.getMaterialAmount(materials.get(materialIndex)) < amount) break;
            demandedMaterials.put(materials.get(materialIndex), amount);
            totalAmount += amount;
            if(totalAmount == Math.ceil((double) player.getMaterialAmount("Sum") /2)) flag = true;
            materialIndex++;
        }

        if(flag){
            Pirate.stealFromPlayer(player, demandedMaterials);
            updatePlayerMaterials(player);

            victims.remove(player);
            if (victims.size() == 0) {
                piratePane.setVisible(false);
            } else {
                giveToPirateButton.setImage(victims.get(0).getImage());
            }
            demandedMaterials.clear();
            for (ChoiceBox<Integer> temp : giveToPirate) {
                temp.getSelectionModel().selectFirst();
            }
        }
    }

    @FXML
    void settings(MouseEvent event) {
        Music.playButtonSound();
        menuScreen.setVisible(!menuScreen.isVisible());
    }

    // Handle escape key event to toggle the menu screen visibility
    @FXML
    void escape(KeyEvent event) {
        if (event.getCode() == KeyCode.P) {
            Music.playButtonSound();
            menuScreen.setVisible(!menuScreen.isVisible());
        }
    }

    @FXML
    void mute(MouseEvent event) {
        String isMuted = (!Music.isMute())? "ON" : "OFF";
        ((ImageView) event.getSource()).setImage(new Image(Objects.requireNonNull(StartScreen.class.getResourceAsStream("/assets/settings/sound_" + isMuted + ".png"))));
    }

    @FXML
    void endTurn(MouseEvent event) throws FileNotFoundException {
        if((hasRolled || game.getRound() <= Game.getPlayerList().size() * 2) && !piratePane.isVisible() && !cardPane.isVisible()){
            Music.playButtonSound();
            if (tradePane.isVisible()) tradePane.setVisible(false);
            else if (buildPane.isVisible()) buildPane.setVisible(false);

            if(circleGroup.getChildren().isEmpty()){
                playersItems.get(game.getCurrentPlayer()).get(1).getParent().setStyle(null);

                game.endTurn();
                hasRolled = false;

                dicePane.setVisible(false);
                diceButton.setVisible(true);

                updateBuildPane();
                upgradeButtonMode = false;

                playersItems.get(game.getCurrentPlayer()).get(1).getParent().setStyle("-fx-effect: dropShadow(three-pass-box, " + game.getCurrentPlayer().getColor() + ", 50, 0, 0, 0)");


                if( game.getRound() <= Game.getPlayerList().size() * 2 ) {
                    initializeButtons(calculateAvailableVillages(), "Village");
                }
            }
        }
    }

    @FXML
    void rollDice(MouseEvent event) throws IOException {
        if( game.getRound() > Game.getPlayerList().size() * 2 ) {
            Music.playButtonSound();
            dicePane.setVisible(true);
            diceButton.setVisible(false);
            int dice = updateDice();
            if( dice!=7 ) {
                game.updateMaterials(dice);

                for (Player player : Game.getPlayerList()) {
                    updatePlayerMaterials(player);
                }

                updateBuildPane();

                hasRolled = true;
            } else {
                HashSet<Coordinates> hexagonCoordinatesList = new HashSet<>();
                for(Hexagon hexagon : game.getBoard().getHexagonList()){
                    if( !hexagon.getBiome().equals("Desert") && hexagon.getHasPirate() == null)
                        hexagonCoordinatesList.add(new Coordinates(hexagon.getCoords().getX()+74, hexagon.getCoords().getY()+80));
                }
                initializeButtons(hexagonCoordinatesList, "Hexagon");
            }
        }
    }

    private int updateDice() {
        int[] dice = game.getCurrentPlayer().rollDice();
        firstDie.setImage(new Image(Objects.requireNonNull(StartScreen.class.getResourceAsStream("/assets/gameScreen/Dice/" + dice[0] + ".png"))));
        secondDie.setImage(new Image(Objects.requireNonNull(StartScreen.class.getResourceAsStream("/assets/gameScreen/Dice/" + dice[1] + ".png"))));
        return dice[2];
    }

    // Exit the game and go back to the start screen
    @FXML
    void exitGame(MouseEvent event) throws IOException {
        Music.playButtonSound();
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("StartScreen-view.fxml")));

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

    // Initialize the hexagons on the game board
    private void initializeHexagons() {
        for(Hexagon hexagon : game.getBoard().getHexagonList()) {
            ImageView boardItems = new ImageView(hexagon.getImage());
            initializeBoardImages(hexagon.getCoords(), boardItems, 0, 0, 178.0, 195.0);
            boardPane.getChildren().add(boardItems);

            if (!hexagon.getBiome().equals("Desert")) {
                String path = "/assets/hexagons/hexagonNumbers/no." + hexagon.getNumber() + ".png";
                boardItems = new ImageView(new Image(Objects.requireNonNull(StartScreen.class.getResourceAsStream(path))));
                initializeBoardImages(hexagon.getCoords(), boardItems, 48, 52, 80.0, 80.0);
                boardPane.getChildren().add(boardItems);
            } else {
                pirate = new ImageView(hexagon.getHasPirate().getImage());
                initializeBoardImages(hexagon.getCoords(), pirate, 60, 15, 83.0, 109.0);
                boardPane.getChildren().add(pirate);
            }

            if(hexagon.getPort()!=null){
                Port port = hexagon.getPort();
                boardItems = new ImageView(port.getImage());
                initializeBoardImages(port.getCoords(), boardItems, 0, 0, 128,140);
                switch (port.getEdge()) {
                    case 3 -> boardItems.setRotate(57);
                    case 5 -> boardItems.setRotate(118);
                    case 7 -> boardItems.setRotate(180);
                    case 9 -> boardItems.setRotate(-123);
                    case 11 -> boardItems.setRotate(-57);
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
            Image buttonImage = new Image(Objects.requireNonNull(StartScreen.class.getResourceAsStream("/assets/gameScreen/Build/selectionCircle.png")));
            ImageView button = new ImageView(buttonImage);
            button.setLayoutX(coords.getX());
            button.setLayoutY(coords.getY());
            button.toFront();

            switch (structure) {
                case "Village" -> button.setOnMouseClicked(buildVillageListener(coords, button));
                case "Road" -> button.setOnMouseClicked(buildRoadListener(coords, button));
                case "Hexagon" -> button.setOnMouseClicked(movePirateListener(coords, button));
            }

            circleGroup.getChildren().add(button);
        }
    }

    private EventHandler<MouseEvent> buildVillageListener(Coordinates coords, ImageView button) {

        return event -> {
                String villageImagePath = "/assets/gameScreen/Build/Village/village_" + game.getCurrentPlayer().getColor() + ".png";
                Village village = new Village(villageImagePath, new Coordinates(coords.getX(), coords.getY()), game.getCurrentPlayer());
                village.build(game.getBoard().getHexagonCorners().get(coords));

                initializeBoardImages(coords, button, -10, -18, 48, 41.8);
                button.setImage(village.getImage());
                button.setOnMouseClicked(upgradeToCityListener(coords, button));
                buildingsGroup.getChildren().add(button);
                circleGroup.getChildren().clear();
                try {
                    Music.playPopSound();
                    updatePlayerPoints(game.getCurrentPlayer());
                    updatePlayerMaterials(game.getCurrentPlayer());
                    updateBuildPane();
                    if( game.getRound() <= Game.getPlayerList().size()*2 ){
                        initializeButtons(calculateAvailableRoads(), "Road");
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
        };
    }

    private EventHandler<MouseEvent> buildRoadListener(Coordinates coords, ImageView button) {
        return event -> {
            String roadImagePath = "/assets/gameScreen/Build/Road/road_" + game.getCurrentPlayer().getColor() + ".png";
            Road road = new Road(roadImagePath, new Coordinates(coords.getX(), coords.getY()), game.getCurrentPlayer());
            HashMap<Integer, Hexagon> edgeCoords = game.getBoard().getHexagonEdges().get(coords);
            road.build(edgeCoords);

            initializeBoardImages(coords, button, 5, -41, 20, 110);
            button.setImage(road.getImage());
            button.setOnMouseClicked(null);
            roadGroup.getChildren().add(button);

            if( edgeCoords.containsKey(1) || edgeCoords.containsKey(7) ){
                button.setRotate(61);
            } else if( edgeCoords.containsKey(3) ||edgeCoords.containsKey(9) ) {
                button.setRotate(-61);
            }
            circleGroup.getChildren().clear();

            if(game.getCurrentPlayer().getStructureAmount("Road") > Quest.getLongestRoadAmount()
            && Quest.getHasLongestRoad() != game.getCurrentPlayer()) {
                Player hadLongestRoad = Quest.getHasLongestRoad();
                Quest.setHasLongestRoad(game.getCurrentPlayer(), game.getCurrentPlayer().getStructureAmount("Road"));

                updatePlayerCards(game.getCurrentPlayer());

                if(hadLongestRoad!=null){
                    updatePlayerCards(hadLongestRoad);
                    try {
                        updatePlayerPoints(hadLongestRoad);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            try {
                Music.playPopSound();
                updatePlayerPoints(game.getCurrentPlayer());
                updatePlayerMaterials(game.getCurrentPlayer());
                updateBuildPane();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        };
    }

    private EventHandler<MouseEvent> movePirateListener(Coordinates coords, ImageView button) {
        return event -> {
            Hexagon pirateHexagon = game.getBoard().getHexagonContainingPirate();

            Coordinates hexagonCoordinates = new Coordinates(coords.getX()-74, coords.getY()-80);
            Hexagon hexagonToMoveTo = game.getBoard().coordinatesToHexagon(hexagonCoordinates);
            pirateHexagon.getHasPirate().moveHexagon(hexagonToMoveTo);

            pirate.toFront();
            pirate.setLayoutX(hexagonCoordinates.getX()+60);
            pirate.setLayoutY(hexagonCoordinates.getY()+15);

            if(!isStealing){
                for(Player player : players){
                    if( player.getMaterialAmount("Sum") > 8 ){
                        victims.add(player);
                        giveToPirateButton.setImage(victims.get(0).getImage());
                        piratePane.setVisible(true);
                    }
                }
            } else {
                for(Structure playerStructure : hexagonToMoveTo.getStructures()){
                    if( playerStructure != null && playerStructure.getOwner() != null && !playerStructure.getOwner().equals(game.getCurrentPlayer()) ) {
                        if( !victims.contains(playerStructure.getOwner())){
                            victims.add(playerStructure.getOwner());
                        }
                    }
                }

                for(Player victim : victims){
                    selectVictim.getItems().add(victim.getName());
                }
                takeMaterials.setImage(new Image(Objects.requireNonNull(StartScreen.class.getResourceAsStream("/assets/gameScreen/tradeWithPlayer.png"))));

                if( victims.size()>0 ){
                    tradePane.setVisible(false);
                    buildPane.setVisible(false);
                    cardPane.setVisible(true);
                    selectVictim.setVisible(true);
                }
                isStealing = false;
            }

            hasRolled = true;
            circleGroup.getChildren().clear();
            try {
                Music.playPopSound();
                updatePlayerPoints(game.getCurrentPlayer());
                updatePlayerMaterials(game.getCurrentPlayer());
                updateBuildPane();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        };
    }

    private EventHandler<MouseEvent> upgradeToCityListener(Coordinates coords, ImageView button) {
        return event ->  {
            if( upgradeButtonMode ){
                upgradeButtonMode = false;
                boolean isCity = false;
                Player owner = null;
                for(int corner : game.getBoard().getHexagonCorners().get(coords).keySet()) {
                    owner = game.getBoard().getHexagonCorners().get(coords).get(corner).getStructure(corner).getOwner();
                    if(game.getBoard().getHexagonCorners().get(coords).get(corner).getStructure(corner) instanceof City) isCity = true;
                    break;
                }
                if( owner.equals(game.getCurrentPlayer()) && !isCity ){
                    City city = Village.upgradeToCity(game.getBoard().getHexagonCorners().get(coords), owner, coords);
                    button.setImage(city.getImage());
                    button.setOnMouseClicked(null);
                    try {
                        Music.playPopSound();
                        updatePlayerPoints(game.getCurrentPlayer());
                        updatePlayerMaterials(game.getCurrentPlayer());
                        updateBuildPane();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        };
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

    //Calculates every possible position where a player can build a road.
    //Needs to change.
    private HashSet<Coordinates> calculateAvailableRoads() {
        HashSet<Coordinates> availableRoadPositions = new HashSet<>();
        ArrayList<Structure> buildings = new ArrayList<>();
        if (game.getRound() <= Game.getPlayerList().size() || game.getRound() > Game.getPlayerList().size() * 2){
            buildings.addAll(game.getCurrentPlayer().getStructureMap().get("Village"));
            buildings.addAll(game.getCurrentPlayer().getStructureMap().get("City"));
            buildings.addAll(game.getCurrentPlayer().getStructureMap().get("Road"));
        } else {
            buildings.add(game.getCurrentPlayer().getStructureMap().get("Village").get(1));
        }

        for(Structure tempBuilding : buildings){
            double tempX = tempBuilding.getCoords().getX();
            double tempY = tempBuilding.getCoords().getY();
            Hexagon hexagon;

            for(Coordinates coordsForEveryEdge : game.getBoard().getHexagonEdges().keySet()) {
                if ( Math.sqrt(Math.pow(coordsForEveryEdge.getX() - tempX, 2) + Math.pow(coordsForEveryEdge.getY() - tempY, 2)) <= 100 ) {
                    for(int index :  game.getBoard().getHexagonEdges().get(coordsForEveryEdge).keySet()){
                        hexagon = game.getBoard().getHexagonEdges().get(coordsForEveryEdge).get(index);
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

        if(game.getRound() > Game.getPlayerList().size() * 2){
            ArrayList<Structure> roads = new ArrayList<>(game.getCurrentPlayer().getStructureMap().get("Road"));

            for(Structure tempRoad : roads){
                tempX = tempRoad.getCoords().getX();
                tempY = tempRoad.getCoords().getY();
                Hexagon hexagon;

                for(Coordinates coordsForEveryCorner : game.getBoard().getHexagonCorners().keySet()) {
                    if( !possibleVillagePositions.contains(coordsForEveryCorner) ){
                        if ( Math.sqrt(Math.pow(coordsForEveryCorner.getX() - tempX, 2) + Math.pow(coordsForEveryCorner.getY() - tempY, 2)) < 55 ) {
                            for(int index :  game.getBoard().getHexagonCorners().get(coordsForEveryCorner).keySet()){
                                hexagon = game.getBoard().getHexagonCorners().get(coordsForEveryCorner).get(index);
                                if(hexagon.getStructure(index) == null || hexagon.getStructure(index) instanceof Road) possibleVillagePositions.add(coordsForEveryCorner);
                            }
                        }
                    }
                }
            }
        } else {
            possibleVillagePositions.addAll(game.getBoard().getHexagonCorners().keySet());
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

        if( game.hasWon() ){
            root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("EndScreen-view.fxml")));

            Stage stage = (Stage) menuScreen.getScene().getWindow();
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

    private void initializetradeChoiceBoxes(){
        trades.add(tradeClay); trades.add(tradeRock);
        trades.add(tradeWood);  trades.add(tradeWool);
        trades.add(tradeWheat);

        for(ChoiceBox<Integer> temp : trades) {
            for (int i = 0; i <= 10; i++){
                temp.getItems().add(i);
            }
        }

        bankTradeMaterial.getItems().addAll(materials);
        bankTradeMaterial.getItems().add("Special Card");

        firstMaterial.getItems().addAll(materials);
        secondMaterial.getItems().addAll(materials);
    }

    private void initializePirateChoiceBoxes(){
        giveToPirate.add(pirateClay); giveToPirate.add(pirateRock);
        giveToPirate.add(pirateWood); giveToPirate.add(pirateWool);
        giveToPirate.add(pirateWheat);

        for(ChoiceBox<Integer> temp : giveToPirate) {
            for (int i = 0; i <= 10; i++) {
                temp.getItems().add(i);
            }
            temp.getSelectionModel().selectFirst();
        }
    }

    // FXML initialization method
    @FXML
    void initialize() throws FileNotFoundException {
        startBoard();

        materials.add("Clay"); materials.add("Rock");
        materials.add("Wood"); materials.add("Wool");
        materials.add("Wheat");

        initializetradeChoiceBoxes();
        initializePirateChoiceBoxes();

        buildVillageButton.setOnMouseClicked(event -> {
            if( Village.canBeBuilt(game.getCurrentPlayer()) ){
                Music.playButtonSound();
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
            if( Road.canBeBuilt(game.getCurrentPlayer()) ){
                Music.playButtonSound();
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
            if( City.canBeBuilt(game.getCurrentPlayer()) ){
                Music.playButtonSound();
                buildPane.setVisible(false);
                try {
                    updateBuildPane();
                    upgradeButtonMode = true;
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        assert bankTradeMaterial != null : "fx:id=\"bankTradeMaterial\" was not injected: check your FXML file 'GameScreen-view.fxml'.";
        assert boardPane != null : "fx:id=\"boardPane\" was not injected: check your FXML file 'GameScreen-view.fxml'.";
        assert buildButton != null : "fx:id=\"buildButton\" was not injected: check your FXML file 'GameScreen-view.fxml'.";
        assert buildPane != null : "fx:id=\"buildPane\" was not injected: check your FXML file 'GameScreen-view.fxml'.";
        assert buildRoadButton != null : "fx:id=\"buildRoadButton\" was not injected: check your FXML file 'GameScreen-view.fxml'.";
        assert buildVillageButton != null : "fx:id=\"buildVillageButton\" was not injected: check your FXML file 'GameScreen-view.fxml'.";
        assert cardPane != null : "fx:id=\"cardPane\" was not injected: check your FXML file 'GameScreen-view.fxml'.";
        assert cards1 != null : "fx:id=\"cards1\" was not injected: check your FXML file 'GameScreen-view.fxml'.";
        assert cards2 != null : "fx:id=\"cards2\" was not injected: check your FXML file 'GameScreen-view.fxml'.";
        assert cards3 != null : "fx:id=\"cards3\" was not injected: check your FXML file 'GameScreen-view.fxml'.";
        assert cards4 != null : "fx:id=\"cards4\" was not injected: check your FXML file 'GameScreen-view.fxml'.";
        assert confirmPlayerTradePane != null : "fx:id=\"confirmPlayerTradePane\" was not injected: check your FXML file 'GameScreen-view.fxml'.";
        assert diceButton != null : "fx:id=\"diceButton\" was not injected: check your FXML file 'GameScreen-view.fxml'.";
        assert dicePane != null : "fx:id=\"dicePane\" was not injected: check your FXML file 'GameScreen-view.fxml'.";
        assert exitButton != null : "fx:id=\"exitButton\" was not injected: check your FXML file 'GameScreen-view.fxml'.";
        assert firstDie != null : "fx:id=\"firstDie\" was not injected: check your FXML file 'GameScreen-view.fxml'.";
        assert firstMaterial != null : "fx:id=\"firstMaterial\" was not injected: check your FXML file 'GameScreen-view.fxml'.";
        assert giveToPirateButton != null : "fx:id=\"giveToPirateButton\" was not injected: check your FXML file 'GameScreen-view.fxml'.";
        assert materials1 != null : "fx:id=\"materials1\" was not injected: check your FXML file 'GameScreen-view.fxml'.";
        assert materials2 != null : "fx:id=\"materials2\" was not injected: check your FXML file 'GameScreen-view.fxml'.";
        assert materials3 != null : "fx:id=\"materials3\" was not injected: check your FXML file 'GameScreen-view.fxml'.";
        assert materials4 != null : "fx:id=\"materials4\" was not injected: check your FXML file 'GameScreen-view.fxml'.";
        assert menuScreen != null : "fx:id=\"menuScreen\" was not injected: check your FXML file 'GameScreen-view.fxml'.";
        assert nameLabel1 != null : "fx:id=\"nameLabel1\" was not injected: check your FXML file 'GameScreen-view.fxml'.";
        assert nameLabel2 != null : "fx:id=\"nameLabel2\" was not injected: check your FXML file 'GameScreen-view.fxml'.";
        assert nameLabel3 != null : "fx:id=\"nameLabel3\" was not injected: check your FXML file 'GameScreen-view.fxml'.";
        assert nameLabel4 != null : "fx:id=\"nameLabel4\" was not injected: check your FXML file 'GameScreen-view.fxml'.";
        assert pirateClay != null : "fx:id=\"pirateClay\" was not injected: check your FXML file 'GameScreen-view.fxml'.";
        assert piratePane != null : "fx:id=\"piratePane\" was not injected: check your FXML file 'GameScreen-view.fxml'.";
        assert pirateRock != null : "fx:id=\"pirateRock\" was not injected: check your FXML file 'GameScreen-view.fxml'.";
        assert pirateWheat != null : "fx:id=\"pirateWheat\" was not injected: check your FXML file 'GameScreen-view.fxml'.";
        assert pirateWood != null : "fx:id=\"pirateWood\" was not injected: check your FXML file 'GameScreen-view.fxml'.";
        assert pirateWool != null : "fx:id=\"pirateWool\" was not injected: check your FXML file 'GameScreen-view.fxml'.";
        assert playerBoard1 != null : "fx:id=\"playerBoard1\" was not injected: check your FXML file 'GameScreen-view.fxml'.";
        assert playerBoard2 != null : "fx:id=\"playerBoard2\" was not injected: check your FXML file 'GameScreen-view.fxml'.";
        assert playerBoard3 != null : "fx:id=\"playerBoard3\" was not injected: check your FXML file 'GameScreen-view.fxml'.";
        assert playerBoard4 != null : "fx:id=\"playerBoard4\" was not injected: check your FXML file 'GameScreen-view.fxml'.";
        assert playerTrading != null : "fx:id=\"playerTrading\" was not injected: check your FXML file 'GameScreen-view.fxml'.";
        assert secondDie != null : "fx:id=\"secondDie\" was not injected: check your FXML file 'GameScreen-view.fxml'.";
        assert secondMaterial != null : "fx:id=\"secondMaterial\" was not injected: check your FXML file 'GameScreen-view.fxml'.";
        assert selectVictim != null : "fx:id=\"selectVictim\" was not injected: check your FXML file 'GameScreen-view.fxml'.";
        assert settingsButton != null : "fx:id=\"settingsButton\" was not injected: check your FXML file 'GameScreen-view.fxml'.";
        assert settingsButton2 != null : "fx:id=\"settingsButton2\" was not injected: check your FXML file 'GameScreen-view.fxml'.";
        assert sound != null : "fx:id=\"sound\" was not injected: check your FXML file 'GameScreen-view.fxml'.";
        assert stats1 != null : "fx:id=\"stats1\" was not injected: check your FXML file 'GameScreen-view.fxml'.";
        assert stats2 != null : "fx:id=\"stats2\" was not injected: check your FXML file 'GameScreen-view.fxml'.";
        assert stats3 != null : "fx:id=\"stats3\" was not injected: check your FXML file 'GameScreen-view.fxml'.";
        assert stats4 != null : "fx:id=\"stats4\" was not injected: check your FXML file 'GameScreen-view.fxml'.";
        assert takeMaterials != null : "fx:id=\"takeMaterials\" was not injected: check your FXML file 'GameScreen-view.fxml'.";
        assert tradeButton != null : "fx:id=\"tradeButton\" was not injected: check your FXML file 'GameScreen-view.fxml'.";
        assert tradeClay != null : "fx:id=\"tradeClay\" was not injected: check your FXML file 'GameScreen-view.fxml'.";
        assert tradeOptionsPane != null : "fx:id=\"tradeOptionsPane\" was not injected: check your FXML file 'GameScreen-view.fxml'.";
        assert tradePane != null : "fx:id=\"tradePane\" was not injected: check your FXML file 'GameScreen-view.fxml'.";
        assert tradePlayerName != null : "fx:id=\"tradePlayerName\" was not injected: check your FXML file 'GameScreen-view.fxml'.";
        assert tradeRock != null : "fx:id=\"tradeRock\" was not injected: check your FXML file 'GameScreen-view.fxml'.";
        assert tradeWheat != null : "fx:id=\"tradeWheat\" was not injected: check your FXML file 'GameScreen-view.fxml'.";
        assert tradeWood != null : "fx:id=\"tradeWood\" was not injected: check your FXML file 'GameScreen-view.fxml'.";
        assert tradeWool != null : "fx:id=\"tradeWool\" was not injected: check your FXML file 'GameScreen-view.fxml'.";
        assert upgradeToCityButton != null : "fx:id=\"upgradeToCityButton\" was not injected: check your FXML file 'GameScreen-view.fxml'.";

    }
}

