package com.game;

import java.util.ArrayList;
import java.util.HashMap;

public class Quest {
    private static Player hasLongestRoad;
    private static int longestRoadAmount;
    private static Player hasLongestArmy;
    private static int longestArmyAmount;
    private static HashMap<Player, Integer> armyAmounts = new HashMap<>();

    public Quest(ArrayList<Player> players) {
        Quest.longestRoadAmount = 4;
        Quest.longestArmyAmount = 2;

        armyAmounts.clear();
        for(Player p: players){
            armyAmounts.put(p, 0);
        }
    }
    public static void setHasLongestRoad(Player player, int amount) {
        SpecialCard card = new SpecialCard("LongestRoad", 2);
        if( Quest.hasLongestRoad != null ) {
            card = (SpecialCard) Quest.hasLongestRoad.getSelectedCard("LongestRoad");
            Quest.hasLongestRoad.removeCard(card);
        }

        Quest.longestRoadAmount = amount;
        Quest.hasLongestRoad = player;
        if( card!=null ) Quest.hasLongestRoad.addCard(card);
    }

    public static Player getHasLongestRoad() { return Quest.hasLongestRoad; }

    public void setLongestRoadAmount(int amount){
        Quest.longestRoadAmount = amount;
    }

    public static int getLongestRoadAmount(){
        return Quest.longestRoadAmount;
    }

    public static void setHasLongestArmy(Player player) {
        SpecialCard card = new SpecialCard("LongestArmy", 2);
        if( Quest.hasLongestArmy!=null ) {
            card = (SpecialCard) Quest.hasLongestArmy.getSelectedCard("LongestArmy");
            Quest.hasLongestArmy.removeCard(card);
        }
        Quest.hasLongestArmy = player;
        if( card!=null ) Quest.hasLongestArmy.addCard(card);
    }

    public Player getHasLongestArmy(){
        return Quest.hasLongestArmy;
    }

    public static void addArmyAmount(Player player, int amount) {
        Quest.armyAmounts.put(player, Quest.armyAmounts.get(player)+amount);
        if( Quest.armyAmounts.get(player) > Quest.longestArmyAmount ){
            Quest.longestArmyAmount = Quest.armyAmounts.get(player);
            Quest.setHasLongestArmy(player);
        }
    }

    public HashMap<Player, Integer> getArmyAmounts(){
        return Quest.armyAmounts;
    }

    public static int getPlayerArmyAmount(Player player){
        return Quest.armyAmounts.get(player);
    }
}